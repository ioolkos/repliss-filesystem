package crdtver.language

import crdtver.language.InputAst.{Identifier, NoSource}
import crdtver.language.TypedAst._
import crdtver.testing.Interpreter
import crdtver.testing.Interpreter.{AbstractAnyValue, AnyValue, CallId, CallInfo, DataTypeValue, State}
import crdtver.language.InputAstHelper._
import crdtver.language.TypedAst.FunctionKind.FunctionKindDatatypeConstructor
import crdtver.language.crdts.CrdtTypeDefinition.Param
import crdtver.language.crdts.{CrdtInstance, CrdtTypeDefinition, NameContext, UniqueName}

import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag


case class StructInstance(
  scope: String,
  fields: Map[UniqueName, CrdtInstance],
  context: NameContext
) extends CrdtInstance()(context) {

  private val nestedUpdate = nameContext.newName("nestedUpdate")
  private val nestedQuery = nameContext.newName("nestedQuery")

  /** Prefixes structinstance name to the operation name.
    *
    * name of structinstance : a
    * operations : add,remove,assign
    * updated operations : a_add, a_remove, a_assign
    *
    */


  override def operations: List[CrdtTypeDefinition.Operation] = {
    fields.toList.flatMap {
      case (fieldName, nestedInstance) =>
        val ops = nestedInstance.operations

        List(
          CrdtTypeDefinition.Operation(
            this,
            fieldName,
            List(Param(nestedUpdate, nestedInstance.updateDatatype)),
            TypeUnit()
          ),
          CrdtTypeDefinition.Operation(
            this,
            fieldName,
            List(Param(nestedQuery, nestedInstance.queryDatatype)),
            DependentReturnType(ops)
          )
        )
    }
  }


  override def evaluateQuery(name: UniqueName, args: List[AbstractAnyValue], state: State): AnyValue = {
    val filteredCalls = state.calls.filter { case (c, ci) =>
      ci.operation.operationName == name
    }.view.mapValues(ci => ci.copy(operation = ci.operation.args.head.asInstanceOf[DataTypeValue])).toMap
    val filteredState = state.copy(calls = filteredCalls)
    val nestedOp = args.head.asInstanceOf[DataTypeValue]
    fields(name).evaluateQuery(nestedOp.operationName, nestedOp.args, filteredState)
  }


  private def rewriteQuery(x: InExpr, fName: UniqueName): InExpr = {
    x match {
      case v: VarUse =>
        v
      case b: BoolConst =>
        b
      case i: IntConst =>
        i
      case a: ApplyBuiltin => // Logical operators, Ex: a && b
        val updatedArgs = a.args.map(arg => rewriteQuery(arg, fName)) // call updateExpr on each expr. (updateExpr(a), updateExpr(b))
        a.copy(args = updatedArgs)
      case f: FunctionCall =>
        // nest
        FunctionCall(f.source, SimpleType(fName)(), fName,
          List(f), FunctionKindDatatypeConstructor())
      case qe: QuantifierExpr =>
        val newExpr = rewriteQuery(qe.expr, fName)
        qe.copy(expr = newExpr)
      case qe: InAllValidSnapshots =>
        val newExpr = rewriteQuery(qe.expr, fName)
        qe.copy(expr = newExpr)
      case dc: DatabaseCall =>
        dc.copy(operation = rewriteQuery(dc.operation, fName).asInstanceOf[FunctionCall])
    }
  }

  override def querySpecification(name: UniqueName, args: List[InExpr]): CrdtInstance.QuerySpecification = {
    args match {
      case List(FunctionCall(_, _, nestedName, nestedArgs, _)) =>
        fields(name).querySpecification(nestedName, nestedArgs)
    }
  }
}


