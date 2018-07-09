package com.superc.bannerdairy.jiguang;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/********************************************************************
  @version: 1.0.0
  @description: 激光推送的别名控制--设置别名setAlias() 以及清除别名cancleAlias()
  @author: user
  @time: 2017/11/9 9:59
  @变更历史:
********************************************************************/
public class SetJPushAlias {
	private final String TAG = "JPush";
	private String alias;
	private Context context;

	/**
	 *
	 * @param alias 极光别名
	 * @param context
	 */
	public SetJPushAlias(String alias, Context context) {
		super();
		this.alias = alias;
		this.context = context;
	}

	/**
	 * 极光推送 设置alias
	 */
	public void setAlias() {
		if (!JPushUtil.isValidTagAndAlias(alias)) {
			Toast.makeText(context, "推送别名出错", Toast.LENGTH_SHORT).show();
			return;
		}

		// 调用JPush API设置Alias
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
	}



	/**
	 * 极光推送 alias的callack
	 */
	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
				case 0:
					logs = "Set alias success:" + alias;
					Log.i(TAG, logs);
					break;

				case 6002:
					logs = "Failed to set alias due to timeout. Try again after 2s.";
					Log.i(TAG, logs);
					if (JPushUtil.isConnected(context)) {
						mHandler.sendMessageDelayed(
								mHandler.obtainMessage(MSG_SET_ALIAS, alias),
								1000 * 2);
					} else {
						Log.i(TAG, "No network");
					}
					break;

				default:
					logs = "Failed with errorCode = " + code;
					Log.e(TAG, logs);
			}


		}

	};


	private static final int MSG_SET_ALIAS = 1001;

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case MSG_SET_ALIAS:
					Log.d(TAG, "Set alias in handler.");
					JPushInterface.setAlias(context, (String) msg.obj,
							mAliasCallback);
					break;

				default:
					Log.i(TAG, "Unhandled msg - " + msg.what);
			}
		}
	};

	// 解除设备和极光的绑定
	private static final int MSG_CANCLE_ALIAS = 2001;

	/**
	 * 极光推送 取消alias
	 */
	public void cancleAlias() {

		// 调用JPush API设置Alias
		mCancleHandler.sendMessage(mCancleHandler.obtainMessage(
				MSG_CANCLE_ALIAS, alias));
	}


	private final Handler mCancleHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case MSG_CANCLE_ALIAS:
					Log.d(TAG, "cancle alias in handler.");
					JPushInterface.setAlias(context, (String) msg.obj,
							mCancleAliasCallback);
					break;

				default:
					Log.i(TAG, "Unhandled msg - " + msg.what);
			}
		}
	};

	/**
	 * 取消极光推送 alias的callack
	 */
	private final TagAliasCallback mCancleAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
				case 0:
					logs = "Cancle alias success";
					Log.i(TAG, logs);
					break;

				case 6002:
					logs = "Failed to set alias and tags due to timeout. Try again after 2s.";
					Log.i(TAG, logs);
					if (JPushUtil.isConnected(context)) {
						mCancleHandler.sendMessageDelayed(mCancleHandler
								.obtainMessage(MSG_CANCLE_ALIAS, alias), 1000 * 2);
					} else {
						Log.i(TAG, "No network");
					}
					break;

				default:
					logs = "Failed with errorCode = " + code;
					Log.e(TAG, logs);
			}

		}

	};

}
