package com.htlc.jrjz_employee.common.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public abstract class CallBack<T> implements Handler.Callback {
	private Handler mHandler;
	public static final int SUCCESS = 0x10;
	public static final int FAIL = 0x11;

	public CallBack() {
		if (Looper.myLooper() != null) {
			mHandler = new Handler(this);
		}
		else{
			mHandler = new Handler(Looper.getMainLooper());
		}
	}

	public void sendMsg(int what, T result) {

		Message message = null;
		if (mHandler != null) {
			message = mHandler.obtainMessage();
		} else {
			message = Message.obtain();
		}
		message.what = what;
		message.obj = result;
		if (mHandler != null) {
			mHandler.sendMessage(message);
		} else {
			handleMessage(message);
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case SUCCESS:
			onSuccess((T) msg.obj);
			break;
		case FAIL:
			onFail(msg.obj.toString());
			break;
		}
		return false;
	}

	public  void onFail(String msg){};

	public abstract void onSuccess(T result);

}
