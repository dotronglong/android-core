package com.globalmediasoft.android.activity;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;


public class GMSActivity extends Activity {
	public static String fontOpenSans = "OpenSans-Light";
	public boolean setFont(TextView input, String font) {
		try {
			input.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/" + font + ".ttf"));
		} catch (NoSuchMethodError ex) {
			return false;
		}
		
		return true;
	}
	
	public static void addFragment(FragmentActivity activity, int containerId, Fragment fragment, String tag) {
		FragmentManager fm = activity.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(containerId, fragment, tag)
		.addToBackStack(null)
		.commit();
	}
	
	public static void addFragment(FragmentActivity activity, int containerId, Fragment fragment) {
		addFragment(activity, containerId, fragment, null);
	}
	
	public static void replaceFragment(FragmentActivity activity, int containerId, Fragment fragment, String tag) {
		FragmentManager fm = activity.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(containerId, fragment, tag)
		  .addToBackStack(null)
		  .commit();
	}
	
	public static void replaceFragment(FragmentActivity activity, int containerId, Fragment fragment) {
		addFragment(activity, containerId, fragment, null);
	}
}