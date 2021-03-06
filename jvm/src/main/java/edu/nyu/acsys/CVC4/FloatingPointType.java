/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package edu.nyu.acsys.CVC4;

public class FloatingPointType extends Type {
  private transient long swigCPtr;

  protected FloatingPointType(long cPtr, boolean cMemoryOwn) {
    super(CVC4JNI.FloatingPointType_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(FloatingPointType obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  /* protected void finalize() {
    delete();
  } */

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        CVC4JNI.delete_FloatingPointType(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public FloatingPointType(Type type) {
    this(CVC4JNI.new_FloatingPointType__SWIG_0(Type.getCPtr(type), type), true);
  }

  public FloatingPointType() {
    this(CVC4JNI.new_FloatingPointType__SWIG_1(), true);
  }

  public long getExponentSize() {
    return CVC4JNI.FloatingPointType_getExponentSize(swigCPtr, this);
  }

  public long getSignificandSize() {
    return CVC4JNI.FloatingPointType_getSignificandSize(swigCPtr, this);
  }

}
