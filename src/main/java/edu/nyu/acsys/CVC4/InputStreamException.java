/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package edu.nyu.acsys.CVC4;

public class InputStreamException extends Exception {
  private transient long swigCPtr;

  protected InputStreamException(long cPtr, boolean cMemoryOwn) {
    super(CVC4JNI.InputStreamException_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(InputStreamException obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  /* protected void finalize() {
    delete();
  } */

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        CVC4JNI.delete_InputStreamException(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public InputStreamException(String msg) {
    this(CVC4JNI.new_InputStreamException(msg), true);
  }

}