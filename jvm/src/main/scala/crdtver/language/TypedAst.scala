package crdtver.language

import crdtver.language.InputAst.BuiltInFunc._
import crdtver.language.InputAst.{AstElem, Identifier, NoSource, SourceTrace}
import crdtver.language.TypedAst.InTypeExpr
import crdtver.language.crdts.{ACrdtInstance, StructCrdt}
import crdtver.parser.LangParser._
import crdtver.symbolic.SymbolicContext
import crdtver.testing.Interpreter
import crdtver.testing.Interpreter.AnyValue
import crdtver.utils.PrettyPrintDoc._
import crdtver.utils.{PrettyPrintDoc, myMemo}

import scala.language.implicitConversions

/**
 * This defines the abstract syntax of the Repliss input language in terms of case classes.
 */
object TypedAst {

  sealed abstract class AstElem(source: SourceTrace) {

    def getSource: SourceTrace = source

    override def toString: String = customToString.prettyStr(120)

    def customToString: Doc

    def printAst: Doc = TypedAstPrinter.print(this)
  }

  case class InProgram(
    name: String,
    source: ProgramContext,
    procedures: List[InProcedure],
    types: List[InTypeDecl],
    axioms: List[InAxiomDecl],
    invariants: List[InInvariantDecl],
    programCrdt: ACrdtInstance = new StructCrdt("root", fields = Map()).instantiate()
  ) extends AstElem(source) {
    def idTypes: List[InTypeDecl] =
      types.filter(_.isIdType)


    private val queryCache = new myMemo[String, Option[InQueryDecl]]({ name: String =>
      programCrdt.queryDefinitions().find(_.name.name == name)
    })

    def findQuery(name: String): Option[InQueryDecl] =
      queryCache(name)

    def hasQuery(name: String): Boolean =
      queryCache(name).isDefined

    override def customToString: Doc = "program"


    def findProcedure(procname: String): InProcedure =
      tryFindProcedure(procname)
        .getOrElse(throw new RuntimeException(s"Procedure $procname not found."))

    def tryFindProcedure(procname: String): Option[InProcedure] = {
      procedures.find(p => p.name.name == procname)
    }

    def findType(name: String): Option[InTypeDecl] =
      types.find(t => t.name.name == name)

    def findDatatype(name: String): Option[InTypeDecl] =
      findType(name).find(t => t.dataTypeCases.nonEmpty)

  }

  sealed abstract class InDeclaration(source: SourceTrace) extends AstElem(source: SourceTrace) {

  }

  case class InProcedure(
    source: SourceTrace,
    name: Identifier,
    params: List[InVariable],
    locals: List[InVariable],
    returnType: InTypeExpr,
    body: InStatement
  ) extends InDeclaration(source) {
    override def customToString: Doc = s"procedure $name"
  }

  case class InTypeDecl(
    source: SourceTrace,
    isIdType: Boolean,
    name: Identifier,
    typeParameters: List[TypeParameter],
    dataTypeCases: List[DataTypeCase]
  ) extends InDeclaration(source) {

    def toTypeExpr: TypedAst.InTypeExpr = {
      if (isIdType)
        IdType(name.name)()
      else
        SimpleType(name.name)()
    }

    def instantiate(typeArgs: List[InTypeExpr]): InTypeDecl = {
      require(typeArgs.length == typeParameters.length)
      if (typeArgs.isEmpty)
        return this
      val subst = Subst.fromLists(typeParameters, typeArgs)
      copy(
        typeParameters = List(),
        dataTypeCases = for (c <- dataTypeCases) yield {
          c.subst(subst)
        }
      )
    }

    override def customToString: Doc = s"type $name"

  }

  case class TypeParameter(
    source: SourceTrace,
    name: Identifier
  ) extends AstElem(source: SourceTrace) {
    override def customToString: Doc = name.name
  }

  case class DataTypeCase(
    source: SourceTrace,
    name: Identifier,
    params: List[InVariable]
  ) extends AstElem(source) {
    def subst(subst: Subst): DataTypeCase = copy(
      params = params.map(_.subst(subst))
    )

    override def customToString: Doc = s"datatype case $name"
  }


  case class InOperationDecl(
    source: SourceTrace,
    name: Identifier,
    params: List[InVariable]
  ) extends InDeclaration(source) {
    override def customToString: Doc = s"operation $name"
  }

  case class InQueryDecl(
    source: SourceTrace,
    name: Identifier,
    params: List[InVariable],
    returnType: InTypeExpr,
    implementation: Option[InExpr],
    ensures: Option[InExpr],
    annotations: Set[InAnnotation]
  ) extends InDeclaration(source) {
    def rewrite(qryName: String, r: InExpr => InExpr): InQueryDecl =
      copy(
        name = name.copy(name = qryName),
        implementation = implementation.map(r),
        ensures = ensures.map(r)
      )

    override def customToString: Doc = s"query $name"
  }

  type InAnnotation = InputAst.InAnnotation


  case class InAxiomDecl(
    source: SourceTrace,
    expr: InExpr
  ) extends InDeclaration(source) {
    override def customToString: Doc = s"axiom $expr"
  }

  case class InInvariantDecl(
    source: SourceTrace,
    name: String,
    isFree: Boolean,
    expr: InExpr,
    priority: Int = 1
  ) extends InDeclaration(source) {


    override def customToString: Doc = nested(4, s"invariant" <+> name <> ":" </> expr.printAst)
  }

  case class InCrdtDecl(
    source: SourceTrace,
    keyDecl: InKeyDecl
  ) extends InDeclaration(source) {
    override def customToString: Doc = s"crdt $keyDecl"
  }

  case class InKeyDecl(
    source: SourceTrace,
    name: Identifier,
    crdttype: InCrdtType)
    extends AstElem(source) {
    override def customToString: Doc = s"crdttype $crdttype"
  }

  sealed abstract class InCrdtType(source: SourceTrace)
    extends AstElem(source: SourceTrace) {
  }

  case class InCrdt(
    source: SourceTrace,
    name: Identifier,
    typ: List[InCrdtType]
  ) extends InCrdtType(source) {
    override def customToString: Doc = s"typ $name $typ"
  }

  case class InStructCrdt(
    source: SourceTrace,
    keyDecl: List[InKeyDecl]
  ) extends InCrdtType(source) {
    override def customToString: Doc = s"key $keyDecl"
  }

  type SourceRange = InputAst.SourceRange

  type SourcePosition = InputAst.SourcePosition

  type SourceTrace = InputAst.SourceTrace
  type Identifier = InputAst.Identifier

  case class InVariable(
    source: SourceTrace,
    name: Identifier,
    typ: InTypeExpr)
    extends AstElem(source) {

    def subst(subst: Subst): InVariable =
      copy(typ = typ.subst(subst))

    override def customToString: Doc = s"var $name: $typ"
  }

  sealed abstract class InExpr(source: SourceTrace, typ: InTypeExpr)
    extends AstElem(source: SourceTrace) {

    final override def customToString: Doc = this.printAst

    /** calculates the free variables in an expression */
    def freeVars(names: Set[String] = Set()): Set[TypedAst.VarUse] = this match {
      case v: VarUse => Set(v)
      case _: BoolConst => Set()
      case _: IntConst => Set()
      case expr: CallExpr =>
        expr.args.view.flatMap(_.freeVars(names)).toSet
      case QuantifierExpr(_, _, vars, expr) =>
        expr.freeVars(names ++ vars.map(_.name.name))
      case InAllValidSnapshots(_, expr) =>
        expr.freeVars(names)
      case q: CrdtQuery =>
        q.qryOp.freeVars(names)
      case q: AggregateExpr =>
        val newBound = names ++ q.vars.map(_.name.name)
        q.filter.freeVars(newBound) ++ q.elem.freeVars(newBound)
    }


    /**
     * Rewrites the expression in a post-order traversal
     */
    def rewrite(f: PartialFunction[InExpr, InExpr]): InExpr = {
      val e2: InExpr = this match {
        case v: VarUse => v
        case c: BoolConst => c
        case c: IntConst => c
        case expr: CallExpr =>
          expr match {
            case fc: FunctionCall =>
              fc.copy(args = fc.args.map(_.rewrite(f)))
            case fc: ApplyBuiltin =>
              fc.copy(args = fc.args.map(_.rewrite(f)))
          }
        case q: CrdtQuery =>
          q.copy(qryOp = q.qryOp.rewrite(f).asInstanceOf[FunctionCall])
        case q: QuantifierExpr =>
          q.copy(expr = q.expr.rewrite(f))
        case q: InAllValidSnapshots =>
          q.copy(expr = q.expr.rewrite(f))
        case q: AggregateExpr =>
          q.copy(filter = q.filter.rewrite(f), elem = q.elem.rewrite(f))
      }
      f.applyOrElse(e2, (_: InExpr) => e2)
    }

    def getTyp: InTypeExpr = typ
  }

  case class VarUse(
    source: SourceTrace,
    typ: InTypeExpr,
    name: String
  ) extends InExpr(source, typ) {
  }

  case class BoolConst(
    source: SourceTrace,
    typ: InTypeExpr,
    value: Boolean
  ) extends InExpr(source, typ) {
  }

  case class IntConst(
    source: SourceTrace,
    typ: InTypeExpr,
    value: BigInt
  ) extends InExpr(source, typ) {
  }


  //  case class FieldAccess(
  //    source: SourceTrace,
  //    typ: InTypeExpr,
  //    receiver: InExpr,
  //    fieldName: Identifier
  //  ) extends InExpr(source, typ)


  sealed abstract class CallExpr(
    source: SourceTrace,
    typ: InTypeExpr
  ) extends InExpr(source, typ) {
    def args: List[InExpr]
  }

  case class FunctionCall(
    source: SourceTrace,
    typ: InTypeExpr,
    functionName: Identifier,
    typeArgs: List[InTypeExpr],
    args: List[InExpr],
    kind: FunctionKind
  ) extends CallExpr(source, typ) {
//    require(!functionName.name.endsWith("_MessageId"))
  }

  case class CrdtQuery(
    source: SourceTrace,
    typ: InTypeExpr,
    qryOp: FunctionCall,
//    queryName: String,
//    /** arguments for the query */
//    args: List[InExpr],
  ) extends InExpr(source, typ) {
  }

  case class ApplyBuiltin(
    source: SourceTrace,
    typ: InTypeExpr,
    function: BuiltInFunc,
    args: List[InExpr]
  ) extends CallExpr(source, typ) {

    // preconditions:
    function match {
      case BF_happensBefore(HappensBeforeOn.Unknown()) =>
        throw new IllegalArgumentException("Happens before must not be unknown")
      case _ =>
    }

  }

  case class QuantifierExpr(
    source: SourceTrace,
    quantifier: Quantifier,
    vars: List[InVariable],
    expr: InExpr
  ) extends InExpr(source, BoolType()) {
  }


  case class AggregateExpr(
    source: SourceTrace = NoSource(),
    op: AggregateOp,
    vars: List[InVariable],
    filter: InExpr,
    elem: InExpr
  ) extends InExpr(source, BoolType()) {
  }

  sealed abstract class AggregateOp

  case class Sum() extends AggregateOp


  case class InAllValidSnapshots(source: SourceTrace, expr: InExpr) extends InExpr(source, expr.getTyp) {
  }


  type Quantifier = InputAst.Quantifier

  type BuiltInFunc = InputAst.BuiltInFunc


  sealed abstract class InStatement(source: SourceTrace)
    extends AstElem(source: SourceTrace) {

  }

  case class BlockStmt(
    source: SourceTrace,
    stmts: List[InStatement]
  ) extends InStatement(source) {
    override def customToString: Doc = s"{${stmts.mkString(";")}}"
  }

  def makeBlock(
    source: SourceTrace,
    stmts: List[InStatement]
  ): BlockStmt = BlockStmt(
    source,
    stmts.flatMap(flatten)
  )

  private def flatten(s: InStatement): List[InStatement] = s match {
    case BlockStmt(source, stmts) => stmts.flatMap(flatten)
    case _ => List(s)
  }

  case class Atomic(
    source: SourceTrace,
    body: InStatement
  ) extends InStatement(source) {
    override def customToString: Doc = s"atomic $body"
  }


  case class LocalVar(
    source: SourceTrace,
    variable: InVariable
  ) extends InStatement(source) {
    override def customToString: Doc = s"var $variable"
  }


  case class IfStmt(
    source: SourceTrace,
    cond: InExpr,
    thenStmt: InStatement,
    elseStmt: InStatement
  ) extends InStatement(source) {
    override def customToString: Doc = s"if ($cond) $thenStmt else $elseStmt"
  }

  case class MatchStmt(
    source: SourceTrace,
    expr: InExpr,
    cases: List[MatchCase]
  ) extends InStatement(source) {
    override def customToString: Doc = s"$expr match { ${cases.mkString(";")} }"
  }

  case class MatchCase(
    source: SourceTrace,
    pattern: InExpr,
    statement: InStatement
  ) extends AstElem(source) {

    override def customToString: Doc = s"case $pattern => $statement"
  }


  case class CrdtCall(
    source: SourceTrace,
    call: FunctionCall
  ) extends InStatement(source) {
    require(call.typ == CallInfoType())

    override def customToString: Doc = s"call $call"
  }


  case class Assignment(
    source: SourceTrace,
    varname: Identifier,
    expr: InExpr
  ) extends InStatement(source) {
    override def customToString: Doc = s"$varname := $expr"
  }


  case class NewIdStmt(
    source: SourceTrace,
    varname: Identifier,
    typename: IdType
  ) extends InStatement(source) {
    override def customToString: Doc = s"$varname := new $typename"
  }


  case class ReturnStmt(
    source: SourceTrace,
    expr: InExpr,
    assertions: List[AssertStmt]
  ) extends InStatement(source) {
    override def customToString: Doc = s"return $expr"
  }


  case class AssertStmt(
    source: SourceTrace,
    expr: InExpr
  ) extends InStatement(source) {
    override def customToString: Doc = s"assert $expr"
  }

  case class Subst(
    s: Map[TypeVarUse, InTypeExpr] = Map()
  ) {
    def get(v: TypeVarUse): Option[InTypeExpr] = s.get(v)

    def +(t: (TypeVarUse, InTypeExpr)): Subst = {
      val newS = s.view.mapValues(x => x.subst(Subst(Map(t)))).toMap + t
      Subst(newS)
    }

  }

  object Subst {
    def fromLists(typeParameters: List[TypeParameter], typeArgs: List[InTypeExpr]): Subst = {
      require(typeParameters.length == typeArgs.length)
      val pairs: List[(TypeVarUse, InTypeExpr)] = for ((p, a) <- typeParameters.zip(typeArgs)) yield {
        TypeVarUse(p.name.name)() -> a
      }
      Subst(pairs.toMap)
    }
  }

  sealed abstract class InTypeExpr(source: SourceTrace = NoSource())
    extends AstElem(source: SourceTrace) {


    def extractTypeArgs: List[InTypeExpr] = this match {
      case SimpleType(name, typeArgs) =>typeArgs
      case _ => List()
    }


    def freeVars: Set[TypeVarUse] = this match {
      case AnyType() => Set()
      case BoolType() => Set()
      case IntType() => Set()
      case CallIdType() => Set()
      case InvocationIdType() => Set()
      case TransactionIdType() => Set()
      case InvocationInfoType() => Set()
      case InvocationResultType() => Set()
      case SomeOperationType() => Set()
      case OperationType(name) => Set()
      case TypedAst.FunctionType(argTypes, returnType, functionKind) =>
        (argTypes.view.flatMap(_.freeVars) ++ returnType.freeVars).toSet
      case SimpleType(name, typeArgs) =>
        typeArgs.view.flatMap(_.freeVars).toSet
      case v: TypeVarUse => Set(v)
      case IdType(name) => Set()
      case CallInfoType() => Set()
      case UnitType() => Set()
    }

    def subst(s: Subst): InTypeExpr = this match {
      case TypedAst.FunctionType(argTypes, returnType, functionKind) =>
        TypedAst.FunctionType(argTypes.map(_.subst(s)), returnType.subst(s), functionKind)()
      case SimpleType(name, typeArgs) =>
        SimpleType(name, typeArgs.map(_.subst(s)))()
      case v: TypeVarUse =>
        s.get(v) match {
          case Some(t) => t
          case None => this
        }
      case _ => this
    }


  }

  case class AnyType() extends InTypeExpr {

    override def customToString: Doc = "any"
  }

  case class UnitType() extends InTypeExpr {

    override def customToString: Doc = "Unit"
  }

  case class BoolType() extends InTypeExpr {

    override def customToString: Doc = "Bool"
  }

  case class IntType() extends InTypeExpr {

    override def customToString: Doc = "Int"
  }

  case class CallIdType() extends InTypeExpr {

    override def customToString: Doc = "CallId"
  }

  case class CallInfoType() extends InTypeExpr {

    override def customToString: Doc = "CallInfo"
  }

  case class InvocationIdType() extends InTypeExpr {

    override def customToString: Doc = "InvocationId"
  }


  case class TransactionIdType() extends InTypeExpr {

    override def customToString: Doc = "TransactionId"
  }

  case class InvocationInfoType() extends InTypeExpr {

    override def customToString: Doc = "InvocationInfo"
  }

  case class InvocationResultType() extends InTypeExpr {

    override def customToString: Doc = "InvocationResult"
  }

  case class SomeOperationType() extends InTypeExpr {

    override def customToString: Doc = "Operation"
  }

  case class OperationType(name: String)(source: SourceTrace = NoSource())
    extends InTypeExpr(source) {


    override def customToString: Doc = s"Operation<$name>"
  }

  sealed abstract class FunctionKind

  object FunctionKind {

    case class FunctionKindDatatypeConstructor() extends FunctionKind

    case class FunctionKindCrdtQuery() extends FunctionKind

  }

  /**
   * Polymorphic type for functions
   */
  case class PrincipleType(
    typeParams: List[TypeVarUse],
    typ: InTypeExpr
  ) {

  }

  case class FunctionType(argTypes: List[InTypeExpr], returnType: InTypeExpr, functionKind: FunctionKind)(source: SourceTrace = NoSource())
    extends InTypeExpr(source) {


    override def customToString: Doc = s"(${argTypes.mkString(", ")}) => $returnType"
  }

  case class SimpleType(name: String, typeArgs: List[InTypeExpr] = List())(source: SourceTrace = NoSource()) extends InTypeExpr(source) {

    override def customToString: Doc = name <> (if (typeArgs.isEmpty) "" else "[" <> sep(", ", typeArgs.map(_.customToString)) <> "]")
  }

  case class TypeVarUse(name: String)(source: SourceTrace = NoSource()) extends InTypeExpr(source) {

    override def customToString: Doc = name
  }

  case class IdType(name: String)(source: SourceTrace = NoSource()) extends InTypeExpr(source) {
    override def customToString: Doc = name
  }


  def extractIds(result: AnyValue, returnType: Option[InTypeExpr]): Map[IdType, Set[AnyValue]] = returnType match {
    case Some(t) =>
      t match {
        case idt@IdType(name) =>
          Map(idt -> Set(result))
        case _ =>
          // TODO handle datatypes with nested ids
          Map()
      }
    case None =>
      Map()
  }

}
