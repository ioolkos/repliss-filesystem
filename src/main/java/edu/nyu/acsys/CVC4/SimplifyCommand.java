/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package edu.nyu.acsys.CVC4;

public class SimplifyCommand extends Command {
  private transient long swigCPtr;

  protected SimplifyCommand(long cPtr, boolean cMemoryOwn) {
    super(CVC4JNI.SimplifyCommand_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(SimplifyCommand obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  /* protected void finalize() {
    delete();
  } */

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        CVC4JNI.delete_SimplifyCommand(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public SimplifyCommand(Expr term) {
    this(CVC4JNI.new_SimplifyCommand(Expr.getCPtr(term), term), true);
  }

  public Expr getTerm() {
    return new Expr(CVC4JNI.SimplifyCommand_getTerm(swigCPtr, this), true);
  }

  public Expr getResult() {
    return new Expr(CVC4JNI.SimplifyCommand_getResult(swigCPtr, this), true);
  }

  public void invoke(SmtEngine smtEngine) {
    CVC4JNI.SimplifyCommand_invoke(swigCPtr, this, SmtEngine.getCPtr(smtEngine), smtEngine);
  }

  public void printResult(java.io.OutputStream out, long verbosity) {
    edu.nyu.acsys.CVC4.JavaOutputStreamAdapter tempout = new edu.nyu.acsys.CVC4.JavaOutputStreamAdapter();
    try {
      CVC4JNI.SimplifyCommand_printResult__SWIG_0(swigCPtr, this, edu.nyu.acsys.CVC4.JavaOutputStreamAdapter.getCPtr(tempout), verbosity);
    } finally {
    new java.io.PrintStream(out).print(tempout.toString());
    }
  }

  public void printResult(java.io.OutputStream out) {
    edu.nyu.acsys.CVC4.JavaOutputStreamAdapter tempout = new edu.nyu.acsys.CVC4.JavaOutputStreamAdapter();
    try {
      CVC4JNI.SimplifyCommand_printResult__SWIG_1(swigCPtr, this, edu.nyu.acsys.CVC4.JavaOutputStreamAdapter.getCPtr(tempout));
    } finally {
    new java.io.PrintStream(out).print(tempout.toString());
    }
  }

  public Command exportTo(ExprManager exprManager, ExprManagerMapCollection variableMap) {
    long cPtr = CVC4JNI.SimplifyCommand_exportTo(swigCPtr, this, ExprManager.getCPtr(exprManager), exprManager, ExprManagerMapCollection.getCPtr(variableMap), variableMap);
    return (cPtr == 0) ? null : new Command(cPtr, false);
  }

  public Command clone() {
    long cPtr = CVC4JNI.SimplifyCommand_clone(swigCPtr, this);
    return (cPtr == 0) ? null : new Command(cPtr, false);
  }

  public String getCommandName() {
    return CVC4JNI.SimplifyCommand_getCommandName(swigCPtr, this);
  }

}
