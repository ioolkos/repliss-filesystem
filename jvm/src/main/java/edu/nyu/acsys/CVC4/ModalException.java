/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package edu.nyu.acsys.CVC4;

public class ModalException extends Exception {
  private transient long swigCPtr;

  protected ModalException(long cPtr, boolean cMemoryOwn) {
    super(CVC4JNI.ModalException_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(ModalException obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  /* protected void finalize() {
    delete();
  } */

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        CVC4JNI.delete_ModalException(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public ModalException() {
    this(CVC4JNI.new_ModalException__SWIG_0(), true);
  }

  public ModalException(String msg) {
    this(CVC4JNI.new_ModalException__SWIG_1(msg), true);
  }

}
