package com.globalmediasoft.android.device;

import android.app.Activity;
import android.util.DisplayMetrics;

public class GMSDeviceDisplay {
	public static String getDisplayDrawableName(Activity activity) {
		String name = "";		
		switch (getDisplayDpi(activity)) {
			case 640:
				name = "xxxhdpi";
				break;
			case 480:
				name = "xxhdpi";
				break;
	
			case 320:
				name = "xhdpi";
				break;
			case 240:
				name = "hdpi";
				break;
			case 160:
			default:
				name = "mdpi";
				break;
		}
		
		return name;
	}
	
	public static int getDisplayDpi(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.densityDpi;
	}
}
