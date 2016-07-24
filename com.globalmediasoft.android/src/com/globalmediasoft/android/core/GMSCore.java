package com.globalmediasoft.android.core;

import com.globalmediasoft.android.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.util.Log;

public class GMSCore {

	public static void alert(Activity activity, String message) {
		alert(activity, message, activity.getString(R.string.gmscore_alert_title));
	}
	
	public static void alert(Activity activity, String message, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title)
		.setMessage(message);
		builder.create().show();
	}
	
	public static void alert(Activity activity, int resId) {
		alert(activity, activity.getString(resId));
	}
	
	public static void alert(Activity activity, int resId, String title) {
		alert(activity, activity.getString(resId), title);
	}

	public static SharedPreferences getSharedPreferences(Activity activity, String name) {
		return getSharedPreferences(activity, name, 0);
	}
	
	public static SharedPreferences getSharedPreferences(Activity activity, String name, int mode) {
		return activity.getSharedPreferences(name, mode);
	}
	
	public static void log(String tag, int[] items) {
		if (items.length > 0) {
			String log = "";
			for (int i = 0; i < items.length; i++) {
				log += (i == 0 ? "" : ", ") + String.valueOf(items[i]);
			}
			Log.d(tag, log);
		}
	}
}