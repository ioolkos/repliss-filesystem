/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package edu.nyu.acsys.CVC4;

class JavaIteratorAdapter_Datatype implements java.util.Iterator<DatatypeConstructor> {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected JavaIteratorAdapter_Datatype(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(JavaIteratorAdapter_Datatype obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  /* protected void finalize() {
    delete();
  } */

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        CVC4JNI.delete_JavaIteratorAdapter_Datatype(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void remove() {
    throw new java.lang.UnsupportedOperationException();
  }

  public DatatypeConstructor next() {
    if(hasNext()) {
      return getNext();
    } else {
      throw new java.util.NoSuchElementException();
    }
  }

  public JavaIteratorAdapter_Datatype(Datatype t) {
    this(CVC4JNI.new_JavaIteratorAdapter_Datatype(Datatype.getCPtr(t), t), true);
  }

  public boolean hasNext() {
    return CVC4JNI.JavaIteratorAdapter_Datatype_hasNext(swigCPtr, this);
  }

  private DatatypeConstructor getNext() {
    return new DatatypeConstructor(CVC4JNI.JavaIteratorAdapter_Datatype_getNext(swigCPtr, this), false);
  }

}
