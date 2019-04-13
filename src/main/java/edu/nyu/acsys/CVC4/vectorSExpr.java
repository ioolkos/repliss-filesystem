/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package edu.nyu.acsys.CVC4;

public class vectorSExpr {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected vectorSExpr(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(vectorSExpr obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  /* protected void finalize() {
    delete();
  } */

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        CVC4JNI.delete_vectorSExpr(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public vectorSExpr() {
    this(CVC4JNI.new_vectorSExpr__SWIG_0(), true);
  }

  public vectorSExpr(long n) {
    this(CVC4JNI.new_vectorSExpr__SWIG_1(n), true);
  }

  public long size() {
    return CVC4JNI.vectorSExpr_size(swigCPtr, this);
  }

  public long capacity() {
    return CVC4JNI.vectorSExpr_capacity(swigCPtr, this);
  }

  public void reserve(long n) {
    CVC4JNI.vectorSExpr_reserve(swigCPtr, this, n);
  }

  public boolean isEmpty() {
    return CVC4JNI.vectorSExpr_isEmpty(swigCPtr, this);
  }

  public void clear() {
    CVC4JNI.vectorSExpr_clear(swigCPtr, this);
  }

  public void add(SExpr x) {
    CVC4JNI.vectorSExpr_add(swigCPtr, this, SExpr.getCPtr(x), x);
  }

  public SExpr get(int i) {
    return new SExpr(CVC4JNI.vectorSExpr_get(swigCPtr, this, i), false);
  }

  public void set(int i, SExpr val) {
    CVC4JNI.vectorSExpr_set(swigCPtr, this, i, SExpr.getCPtr(val), val);
  }

}
