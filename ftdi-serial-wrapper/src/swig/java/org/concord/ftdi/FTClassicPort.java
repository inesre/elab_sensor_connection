/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 1.3.24
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.concord.ftdi;

public class FTClassicPort {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected FTClassicPort(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(FTClassicPort obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public void delete() {
    if(swigCPtr != 0 && swigCMemOwn) {
      swigCMemOwn = false;
      FtdiSerialWrapperJNI.delete_FTClassicPort(swigCPtr);
    }
    swigCPtr = 0;
  }

  public void setHandle(SWIGTYPE_p_void handle) {
    FtdiSerialWrapperJNI.set_FTClassicPort_handle(swigCPtr, SWIGTYPE_p_void.getCPtr(handle));
  }

  public SWIGTYPE_p_void getHandle() {
    long cPtr = FtdiSerialWrapperJNI.get_FTClassicPort_handle(swigCPtr);
    return (cPtr == 0) ? null : new SWIGTYPE_p_void(cPtr, false);
  }

  public void setStatus(long status) {
    FtdiSerialWrapperJNI.set_FTClassicPort_status(swigCPtr, status);
  }

  public long getStatus() {
    return FtdiSerialWrapperJNI.get_FTClassicPort_status(swigCPtr);
  }

  public void open(int deviceNumber) throws java.io.IOException {
    FtdiSerialWrapperJNI.FTClassicPort_open(swigCPtr, deviceNumber);
  }

  public void close() throws java.io.IOException {
    FtdiSerialWrapperJNI.FTClassicPort_close(swigCPtr);
  }

  public long read(byte[] lpBuffer, long nBufferSize) throws java.io.IOException {
    return FtdiSerialWrapperJNI.FTClassicPort_read(swigCPtr, lpBuffer, nBufferSize);
  }

  public long write(byte[] lpBuffer, long nBufferSize) throws java.io.IOException {
    return FtdiSerialWrapperJNI.FTClassicPort_write(swigCPtr, lpBuffer, nBufferSize);
  }

  public void setBaudRate(long BaudRate) throws java.io.IOException {
    FtdiSerialWrapperJNI.FTClassicPort_setBaudRate(swigCPtr, BaudRate);
  }

  public void setDataCharacteristics(short WordLength, short StopBits, short Parity) throws java.io.IOException {
    FtdiSerialWrapperJNI.FTClassicPort_setDataCharacteristics(swigCPtr, WordLength, StopBits, Parity);
  }

  public void setFlowControl(int FlowControl, short XonChar, short XoffChar) throws java.io.IOException {
    FtdiSerialWrapperJNI.FTClassicPort_setFlowControl(swigCPtr, FlowControl, XonChar, XoffChar);
  }

  public void resetDevice() throws java.io.IOException {
    FtdiSerialWrapperJNI.FTClassicPort_resetDevice(swigCPtr);
  }

  public void setDtr() throws java.io.IOException {
    FtdiSerialWrapperJNI.FTClassicPort_setDtr(swigCPtr);
  }

  public void clrDtr() throws java.io.IOException {
    FtdiSerialWrapperJNI.FTClassicPort_clrDtr(swigCPtr);
  }

  public void setRts() throws java.io.IOException {
    FtdiSerialWrapperJNI.FTClassicPort_setRts(swigCPtr);
  }

  public void crRts() throws java.io.IOException {
    FtdiSerialWrapperJNI.FTClassicPort_crRts(swigCPtr);
  }

  public void setTimeouts(long ReadTimeout, long WriteTimeout) throws java.io.IOException {
    FtdiSerialWrapperJNI.FTClassicPort_setTimeouts(swigCPtr, ReadTimeout, WriteTimeout);
  }

  public void resetPort() throws java.io.IOException {
    FtdiSerialWrapperJNI.FTClassicPort_resetPort(swigCPtr);
  }

  public FTClassicPort() {
    this(FtdiSerialWrapperJNI.new_FTClassicPort(), true);
  }

}