/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package edu.nyu.acsys.CVC4;

public class ParserException extends Exception {
  private transient long swigCPtr;

  protected ParserException(long cPtr, boolean cMemoryOwn) {
    super(CVC4JNI.ParserException_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(ParserException obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  /* protected void finalize() {
    delete();
  } */

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        CVC4JNI.delete_ParserException(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public ParserException() {
    this(CVC4JNI.new_ParserException__SWIG_0(), true);
  }

  public ParserException(String msg) {
    this(CVC4JNI.new_ParserException__SWIG_1(msg), true);
  }

  public ParserException(String msg, String filename, long line, long column) {
    this(CVC4JNI.new_ParserException__SWIG_2(msg, filename, line, column), true);
  }

  public void toStream(java.io.OutputStream os) {
    edu.nyu.acsys.CVC4.JavaOutputStreamAdapter tempos = new edu.nyu.acsys.CVC4.JavaOutputStreamAdapter();
    try {
      CVC4JNI.ParserException_toStream(swigCPtr, this, edu.nyu.acsys.CVC4.JavaOutputStreamAdapter.getCPtr(tempos));
    } finally {
    new java.io.PrintStream(os).print(tempos.toString());
    }
  }

  public String getFilename() {
    return CVC4JNI.ParserException_getFilename(swigCPtr, this);
  }

  public int getLine() {
    return CVC4JNI.ParserException_getLine(swigCPtr, this);
  }

  public int getColumn() {
    return CVC4JNI.ParserException_getColumn(swigCPtr, this);
  }

}
