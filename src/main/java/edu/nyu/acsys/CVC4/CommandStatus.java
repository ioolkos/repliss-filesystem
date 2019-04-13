/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package edu.nyu.acsys.CVC4;

public class CommandStatus {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CommandStatus(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CommandStatus obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  /* protected void finalize() {
    delete();
  } */

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        CVC4JNI.delete_CommandStatus(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void toStream(java.io.OutputStream out, OutputLanguage language) {
    edu.nyu.acsys.CVC4.JavaOutputStreamAdapter tempout = new edu.nyu.acsys.CVC4.JavaOutputStreamAdapter();
    try {
      CVC4JNI.CommandStatus_toStream__SWIG_0(swigCPtr, this, edu.nyu.acsys.CVC4.JavaOutputStreamAdapter.getCPtr(tempout), language.swigValue());
    } finally {
    new java.io.PrintStream(out).print(tempout.toString());
    }
  }

  public void toStream(java.io.OutputStream out) {
    edu.nyu.acsys.CVC4.JavaOutputStreamAdapter tempout = new edu.nyu.acsys.CVC4.JavaOutputStreamAdapter();
    try {
      CVC4JNI.CommandStatus_toStream__SWIG_1(swigCPtr, this, edu.nyu.acsys.CVC4.JavaOutputStreamAdapter.getCPtr(tempout));
    } finally {
    new java.io.PrintStream(out).print(tempout.toString());
    }
  }

  public CommandStatus clone() {
    return new CommandStatus(CVC4JNI.CommandStatus_clone(swigCPtr, this), false);
  }

}
