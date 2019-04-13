/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package edu.nyu.acsys.CVC4;

public class ParserBuilder {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected ParserBuilder(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(ParserBuilder obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  /* protected void finalize() {
    delete();
  } */

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        CVC4JNI.delete_ParserBuilder(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public ParserBuilder(SWIGTYPE_p_CVC4__api__Solver solver, String filename) {
    this(CVC4JNI.new_ParserBuilder__SWIG_0(SWIGTYPE_p_CVC4__api__Solver.getCPtr(solver), filename), true);
  }

  public ParserBuilder(SWIGTYPE_p_CVC4__api__Solver solver, String filename, Options options) {
    this(CVC4JNI.new_ParserBuilder__SWIG_1(SWIGTYPE_p_CVC4__api__Solver.getCPtr(solver), filename, Options.getCPtr(options), options), true);
  }

  public Parser build() {
    long cPtr = CVC4JNI.ParserBuilder_build(swigCPtr, this);
    return (cPtr == 0) ? null : new Parser(cPtr, false);
  }

  public ParserBuilder withChecks(boolean flag) {
    return new ParserBuilder(CVC4JNI.ParserBuilder_withChecks__SWIG_0(swigCPtr, this, flag), false);
  }

  public ParserBuilder withChecks() {
    return new ParserBuilder(CVC4JNI.ParserBuilder_withChecks__SWIG_1(swigCPtr, this), false);
  }

  public ParserBuilder withSolver(SWIGTYPE_p_CVC4__api__Solver solver) {
    return new ParserBuilder(CVC4JNI.ParserBuilder_withSolver(swigCPtr, this, SWIGTYPE_p_CVC4__api__Solver.getCPtr(solver)), false);
  }

  public ParserBuilder withFileInput() {
    return new ParserBuilder(CVC4JNI.ParserBuilder_withFileInput(swigCPtr, this), false);
  }

  public ParserBuilder withFilename(String filename) {
    return new ParserBuilder(CVC4JNI.ParserBuilder_withFilename(swigCPtr, this, filename), false);
  }

  public ParserBuilder withInputLanguage(InputLanguage lang) {
    return new ParserBuilder(CVC4JNI.ParserBuilder_withInputLanguage(swigCPtr, this, lang.swigValue()), false);
  }

  public ParserBuilder withMmap(boolean flag) {
    return new ParserBuilder(CVC4JNI.ParserBuilder_withMmap__SWIG_0(swigCPtr, this, flag), false);
  }

  public ParserBuilder withMmap() {
    return new ParserBuilder(CVC4JNI.ParserBuilder_withMmap__SWIG_1(swigCPtr, this), false);
  }

  public ParserBuilder withParseOnly(boolean flag) {
    return new ParserBuilder(CVC4JNI.ParserBuilder_withParseOnly__SWIG_0(swigCPtr, this, flag), false);
  }

  public ParserBuilder withParseOnly() {
    return new ParserBuilder(CVC4JNI.ParserBuilder_withParseOnly__SWIG_1(swigCPtr, this), false);
  }

  public ParserBuilder withOptions(Options options) {
    return new ParserBuilder(CVC4JNI.ParserBuilder_withOptions(swigCPtr, this, Options.getCPtr(options), options), false);
  }

  public ParserBuilder withStrictMode(boolean flag) {
    return new ParserBuilder(CVC4JNI.ParserBuilder_withStrictMode__SWIG_0(swigCPtr, this, flag), false);
  }

  public ParserBuilder withStrictMode() {
    return new ParserBuilder(CVC4JNI.ParserBuilder_withStrictMode__SWIG_1(swigCPtr, this), false);
  }

  public ParserBuilder withIncludeFile(boolean flag) {
    return new ParserBuilder(CVC4JNI.ParserBuilder_withIncludeFile__SWIG_0(swigCPtr, this, flag), false);
  }

  public ParserBuilder withIncludeFile() {
    return new ParserBuilder(CVC4JNI.ParserBuilder_withIncludeFile__SWIG_1(swigCPtr, this), false);
  }

  public ParserBuilder withStreamInput(java.io.InputStream input) {
    edu.nyu.acsys.CVC4.JavaInputStreamAdapter tempinput = edu.nyu.acsys.CVC4.JavaInputStreamAdapter.get(input);
    {
      return new ParserBuilder(CVC4JNI.ParserBuilder_withStreamInput(swigCPtr, this, edu.nyu.acsys.CVC4.JavaInputStreamAdapter.getCPtr(tempinput)), false);
    }
  }

  public ParserBuilder withLineBufferedStreamInput(java.io.InputStream input) {
    edu.nyu.acsys.CVC4.JavaInputStreamAdapter tempinput = edu.nyu.acsys.CVC4.JavaInputStreamAdapter.get(input);
    {
      return new ParserBuilder(CVC4JNI.ParserBuilder_withLineBufferedStreamInput(swigCPtr, this, edu.nyu.acsys.CVC4.JavaInputStreamAdapter.getCPtr(tempinput)), false);
    }
  }

  public ParserBuilder withStringInput(String input) {
    return new ParserBuilder(CVC4JNI.ParserBuilder_withStringInput(swigCPtr, this, input), false);
  }

  public ParserBuilder withForcedLogic(String logic) {
    return new ParserBuilder(CVC4JNI.ParserBuilder_withForcedLogic(swigCPtr, this, logic), false);
  }

}
