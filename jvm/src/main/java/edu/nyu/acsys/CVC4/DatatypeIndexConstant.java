/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package edu.nyu.acsys.CVC4;

public class DatatypeIndexConstant {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected DatatypeIndexConstant(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(DatatypeIndexConstant obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  /* protected void finalize() {
    delete();
  } */

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        CVC4JNI.delete_DatatypeIndexConstant(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public DatatypeIndexConstant(long index) {
    this(CVC4JNI.new_DatatypeIndexConstant(index), true);
  }

  public long getIndex() {
    return CVC4JNI.DatatypeIndexConstant_getIndex(swigCPtr, this);
  }

  public boolean equals(DatatypeIndexConstant uc) {
    return CVC4JNI.DatatypeIndexConstant_equals(swigCPtr, this, DatatypeIndexConstant.getCPtr(uc), uc);
  }

  public boolean less(DatatypeIndexConstant uc) {
    return CVC4JNI.DatatypeIndexConstant_less(swigCPtr, this, DatatypeIndexConstant.getCPtr(uc), uc);
  }

  public boolean lessEqual(DatatypeIndexConstant uc) {
    return CVC4JNI.DatatypeIndexConstant_lessEqual(swigCPtr, this, DatatypeIndexConstant.getCPtr(uc), uc);
  }

  public boolean greater(DatatypeIndexConstant uc) {
    return CVC4JNI.DatatypeIndexConstant_greater(swigCPtr, this, DatatypeIndexConstant.getCPtr(uc), uc);
  }

  public boolean greaterEqual(DatatypeIndexConstant uc) {
    return CVC4JNI.DatatypeIndexConstant_greaterEqual(swigCPtr, this, DatatypeIndexConstant.getCPtr(uc), uc);
  }

}
