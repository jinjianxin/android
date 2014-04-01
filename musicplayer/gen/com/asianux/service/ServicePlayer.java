/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/jjx/workspace/musicplayer/src/com/asianux/service/ServicePlayer.aidl
 */
package com.asianux.service;
public interface ServicePlayer extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.asianux.service.ServicePlayer
{
private static final java.lang.String DESCRIPTOR = "com.asianux.service.ServicePlayer";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.asianux.service.ServicePlayer interface,
 * generating a proxy if needed.
 */
public static com.asianux.service.ServicePlayer asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.asianux.service.ServicePlayer))) {
return ((com.asianux.service.ServicePlayer)iin);
}
return new com.asianux.service.ServicePlayer.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_play:
{
data.enforceInterface(DESCRIPTOR);
this.play();
reply.writeNoException();
return true;
}
case TRANSACTION_pause:
{
data.enforceInterface(DESCRIPTOR);
this.pause();
reply.writeNoException();
return true;
}
case TRANSACTION_stop:
{
data.enforceInterface(DESCRIPTOR);
this.stop();
reply.writeNoException();
return true;
}
case TRANSACTION_previous:
{
data.enforceInterface(DESCRIPTOR);
this.previous();
reply.writeNoException();
return true;
}
case TRANSACTION_next:
{
data.enforceInterface(DESCRIPTOR);
this.next();
reply.writeNoException();
return true;
}
case TRANSACTION_playMode:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.playMode(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getDuration:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getDuration();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getCurrentTime:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getCurrentTime();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getPosition:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getPosition();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getAudioSessionId:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getAudioSessionId();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setCurrent:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.setCurrent(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_isPlaying:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isPlaying();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getPlayingUrl:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getPlayingUrl();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getPlayingInfo:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getPlayingInfo();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getMp3info:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<java.lang.String> _result = this.getMp3info();
reply.writeNoException();
reply.writeStringList(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.asianux.service.ServicePlayer
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void play() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_play, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void pause() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_pause, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void stop() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stop, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void previous() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_previous, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void next() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_next, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void playMode(int mode) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(mode);
mRemote.transact(Stub.TRANSACTION_playMode, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int getDuration() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getDuration, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getCurrentTime() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCurrentTime, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getPosition() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPosition, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getAudioSessionId() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getAudioSessionId, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void setCurrent(int cur) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(cur);
mRemote.transact(Stub.TRANSACTION_setCurrent, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean isPlaying() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isPlaying, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getPlayingUrl() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPlayingUrl, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getPlayingInfo() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPlayingInfo, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.util.List<java.lang.String> getMp3info() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<java.lang.String> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getMp3info, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArrayList();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_play = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_pause = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_stop = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_previous = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_next = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_playMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_getDuration = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_getCurrentTime = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_getPosition = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_getAudioSessionId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_setCurrent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_isPlaying = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_getPlayingUrl = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_getPlayingInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_getMp3info = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
}
public void play() throws android.os.RemoteException;
public void pause() throws android.os.RemoteException;
public void stop() throws android.os.RemoteException;
public void previous() throws android.os.RemoteException;
public void next() throws android.os.RemoteException;
public void playMode(int mode) throws android.os.RemoteException;
public int getDuration() throws android.os.RemoteException;
public int getCurrentTime() throws android.os.RemoteException;
public int getPosition() throws android.os.RemoteException;
public int getAudioSessionId() throws android.os.RemoteException;
public void setCurrent(int cur) throws android.os.RemoteException;
public boolean isPlaying() throws android.os.RemoteException;
public java.lang.String getPlayingUrl() throws android.os.RemoteException;
public java.lang.String getPlayingInfo() throws android.os.RemoteException;
public java.util.List<java.lang.String> getMp3info() throws android.os.RemoteException;
}
