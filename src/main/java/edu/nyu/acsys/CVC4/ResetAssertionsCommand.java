/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package edu.nyu.acsys.CVC4;

public class ResetAssertionsCommand extends Command {
  private transient long swigCPtr;

  protected ResetAssertionsCommand(long cPtr, boolean cMemoryOwn) {
    super(CVC4JNI.ResetAssertionsCommand_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(ResetAssertionsCommand obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  /* protected void finalize() {
    delete();
  } */

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        CVC4JNI.delete_ResetAssertionsCommand(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public ResetAssertionsCommand() {
    this(CVC4JNI.new_ResetAssertionsCommand(), true);
  }

  public void invoke(SmtEngine smtEngine) {
    CVC4JNI.ResetAssertionsCommand_invoke(swigCPtr, this, SmtEngine.getCPtr(smtEngine), smtEngine);
  }

  public Command exportTo(ExprManager exprManager, ExprManagerMapCollection variableMap) {
    long cPtr = CVC4JNI.ResetAssertionsCommand_exportTo(swigCPtr, this, ExprManager.getCPtr(exprManager), exprManager, ExprManagerMapCollection.getCPtr(variableMap), variableMap);
    return (cPtr == 0) ? null : new Command(cPtr, false);
  }

  public Command clone() {
    long cPtr = CVC4JNI.ResetAssertionsCommand_clone(swigCPtr, this);
    return (cPtr == 0) ? null : new Command(cPtr, false);
  }

  public String getCommandName() {
    return CVC4JNI.ResetAssertionsCommand_getCommandName(swigCPtr, this);
  }

}
