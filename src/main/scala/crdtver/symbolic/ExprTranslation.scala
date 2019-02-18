package crdtver.symbolic

import crdtver.language.InputAst
import crdtver.language.InputAst.{AnyType, ApplyBuiltin, BoolType, CallIdType, FunctionKind, FunctionType, HappensBeforeOn, IdType, InExpr, IntType, InvocationInfoType, InvocationResultType, OperationType, SimpleType, SomeOperationType, TransactionIdType, UnknownType, UnresolvedType}

object ExprTranslation {

  def translateType(typ: InputAst.InTypeExpr)(implicit ctxt: SymbolicContext): SymbolicSort =
    typ match {
      case CallIdType() => SortCallId()
      case BoolType() => SortBoolean()
      case IntType() => SortInt()
      case InvocationResultType() =>
        SortInvocationRes()
      case FunctionType(argTypes, returnType, kind) => ???
      case UnresolvedType(name) => ???
      case TransactionIdType() => SortTxId()
      case InvocationInfoType() => SortInvocationInfo()
      case AnyType() => ???
      case UnknownType() => ???
      case st: IdType =>
        SortCustomUninterpreted(st.name)
      case st: SimpleType =>
        ctxt.getCustomType(st)
      case SomeOperationType() => ???
      case OperationType(name) => ???
      case InputAst.InvocationIdType() => SortInvocationId()
    }

  /** determines the invocation of a call*/
  def callInvocation(cId: SVal[SortCallId])(implicit ctxt: SymbolicContext, state: SymbolicState): SVal[SortOption[SortInvocationId]] = {
    val tx = ctxt.makeVariable[SortTxId]("tx")
    val i = ctxt.makeVariable[SortInvocationId]("i")
    SOptionMatch(
      state.callOrigin.get(cId),
      tx,
      state.transactionOrigin.get(tx),
      SNone()
    )
  }

  def translateBuiltin(expr: ApplyBuiltin)(implicit ctxt: SymbolicContext, state: SymbolicState): SVal[_ <: SymbolicSort] = {
    val args: List[SVal[_]] = expr.args.map(translateUntyped)
    expr.function match {
      case InputAst.BF_isVisible() =>
        state.visibleCalls.contains(cast(args(0)))
      case InputAst.BF_happensBefore(on) =>
        on match {
          case HappensBeforeOn.Unknown() =>
            ???
          case HappensBeforeOn.Call() =>
            val c1 = cast[SortCallId](args(0))
            val c2 = cast[SortCallId](args(1))
            callHappensBefore(c1, c2)
          case HappensBeforeOn.Invoc() =>
            val i1: SVal[SortInvocationId] = cast(args(0))
            val i2: SVal[SortInvocationId] = cast(args(1))
            // invocation
            val c1 = ctxt.makeVariable[SortCallId]("c1")
            val c2 = ctxt.makeVariable[SortCallId]("c1")
            // exists c1, c2 :: c1.origin == i1 && c2.origin == i2 && c1 happened before c2
            val existsHb =
              QuantifierExpr(QExists(), c1,
                QuantifierExpr(QExists(), c2,
                  SAnd(
                  SAnd(
                    SEq(callInvocation(c1), SSome(i1)),
                    SEq(callInvocation(c2), SSome(i2))),
                    callHappensBefore(c1, c2))))
            // forall c1, c2 :: (c1.origin == i1 && c2.origin == i2) ==> c1 happened before c2
            val allHb =
              QuantifierExpr(QForall(), c1,
                              QuantifierExpr(QForall(), c2,
                                SImplies(
                                SAnd(
                                  SEq(callInvocation(c1), SSome(i1)),
                                  SEq(callInvocation(c2), SSome(i2))),
                                  callHappensBefore(c1, c2))))
            SAnd(existsHb, allHb)
        }
      case InputAst.BF_sameTransaction() =>
        SEq(state.callOrigin.get(cast(args(0))), state.callOrigin.get(cast(args(1))))
      case InputAst.BF_less() =>
        SLessThan(cast(args(0)), cast(args(1)))
      case InputAst.BF_lessEq() =>
        SLessThanOrEqual(cast(args(0)), cast(args(1)))
      case InputAst.BF_greater() =>
        SLessThan(cast(args(1)), cast(args(0)))
      case InputAst.BF_greaterEq() =>
        SLessThanOrEqual(cast(args(1)), cast(args(0)))
      case InputAst.BF_equals() =>
        SEq(castSymbolicSort(args(0)), castSymbolicSort(args(1)))
      case InputAst.BF_notEquals() =>
        SNotEq(castSymbolicSort(args(0)), castSymbolicSort(args(1)))
      case InputAst.BF_and() =>
        SAnd(cast(args(0)), cast(args(1)))
      case InputAst.BF_or() =>
        SOr(cast(args(0)), cast(args(1)))
      case InputAst.BF_implies() =>
        SImplies(cast(args(0)), cast(args(1)))
      case InputAst.BF_not() =>
        SNot(cast(args(0)))
      case InputAst.BF_plus() =>
        ???
      case InputAst.BF_minus() =>
        ???
      case InputAst.BF_mult() =>
        ???
      case InputAst.BF_div() =>
        ???
      case InputAst.BF_mod() =>
        ???
      case InputAst.BF_getOperation() =>
        state.calls.get(cast(args(0)))
      case InputAst.BF_getInfo() =>
        state.invocationOp.get(cast(args(0)))
      case InputAst.BF_getResult() =>
        state.invocationRes.get(cast(args(0)))
      case InputAst.BF_getOrigin() =>
        state.transactionOrigin.get(cast(args(0)))
      case InputAst.BF_getTransaction() =>
        state.callOrigin.get(cast(args(0)))
      case InputAst.BF_inCurrentInvoc() =>
        //        SEq(state.currentInvocation, state.transactionOrigin.get(state.callOrigin.get(cast(args(0)))))
        ???
    }
  }

  /** checks that c1 happened before c2 */
  private def callHappensBefore(c1: SVal[SortCallId], c2: SVal[SortCallId])(implicit ctxt: SymbolicContext, state: SymbolicState) = {
    SSetContains[SortCallId](state.happensBefore.get(c2), c1)
  }

  def translate[T <: SymbolicSort](expr: InExpr)(implicit sort: T, ctxt: SymbolicContext, state: SymbolicState): SVal[T] = {
    val res: SVal[_] = translateUntyped(expr)
    cast(res)
  }


  def translateUntyped(expr: InExpr)(implicit ctxt: SymbolicContext, state: SymbolicState): SVal[SymbolicSort] = {
    expr match {
      case InputAst.VarUse(source, typ, name) =>
        state.lookupLocal(name).upcast()
      case InputAst.BoolConst(source, typ, value) =>
        SBool(value).upcast()
      case InputAst.IntConst(source, typ, value) =>
        ConcreteVal(value)(SortInt())
      case expr: InputAst.CallExpr => expr match {
        case InputAst.FunctionCall(source, typ, functionName, args, kind) =>
          val translatedArgs = args.map(translateUntyped(_))
          kind match {
            case FunctionKind.FunctionKindUnknown() =>
              throw new RuntimeException(s"Cannot translate $expr")
            case FunctionKind.FunctionKindDatatypeConstructor() =>
              SDatatypeValue(ctxt.datypeImpl(ctxt.translateSortDatatype(typ)), functionName.name, translatedArgs).upcast()
            case FunctionKind.FunctionKindCrdtQuery() =>
              ctxt.findQuery(functionName.name) match {
                case None =>
                  throw new RuntimeException(s"Could not find function $functionName")
                case Some(query) =>
                  // bind the parameter values:
                  var state2 = state
                  for ((p,a) <- query.params.zip(translatedArgs)) {
                    state2 = state2.withLocal(ProgramVariable(p.name.name), a)
                  }

                  query.implementation match {
                    case Some(impl) =>
                      // inline the implementation:
                      translateUntyped(impl)(ctxt, state2)
                    case None =>
                      query.ensures match {
                        case Some(postCondition) =>
                          // create a new symbolic variable and assume the postcondition:
                          val result = ctxt.makeVariable(query.name.name)(translateType(query.returnType))
                          state2 = state2.withLocal(ProgramVariable("result"), result)
                          ctxt.addConstraint(translate(postCondition)(SortBoolean(), ctxt, state2))
                          result
                        case None =>
                          throw new RuntimeException(s"Query $functionName does not have a specification.")
                      }
                  }
              }
          }

        case bi: ApplyBuiltin =>
          translateBuiltin(bi).upcast()
      }
      case InputAst.QuantifierExpr(source, typ, quantifier, vars, e) =>

        val q = quantifier match {
          case InputAst.Forall() => QForall()
          case InputAst.Exists() => QExists()
        }
        var state2 = state
        var res: SVal[SortBoolean] => SVal[SortBoolean] = x => x
        for (v <- vars) {
          val v2 = ctxt.makeVariable(v.name.name)(translateType(v.typ))
          state2 = state2.withLocal(ProgramVariable(v.name.name), v2)
          res = e => QuantifierExpr(q, v2, e)
        }
        res(translate(e)(implicitly, implicitly, state2)).upcast()
    }
  }

  def cast[T <: SymbolicSort](e: SVal[_])(implicit sort: T, state: SymbolicState): SVal[T] = {
    if (e.typ != sort) {
      throw new RuntimeException(s"Expected expression of type $sort, but got $e of type ${e.typ}")
    }
    e.asInstanceOf[SVal[T]]
  }

  def castList[T <: SymbolicSort](es: List[SVal[_]])(implicit sort: T, state: SymbolicState): List[SVal[T]] =
    es.map(cast(_)(sort, state))

  def castSymbolicSort(e: SVal[_]): SVal[SymbolicSort] = {
    e.asInstanceOf[SVal[SymbolicSort]]
  }


}
