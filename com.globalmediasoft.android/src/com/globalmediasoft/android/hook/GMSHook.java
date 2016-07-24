package com.globalmediasoft.android.hook;

import java.util.HashMap;
import java.util.Vector;

import android.os.Handler;

public class GMSHook {
	private static HashMap<String, Vector<Runnable>> hooks = new HashMap<String, Vector<Runnable>>();
	
	/**
	 * Call a hook
	 * 
	 * @param name
	 * @return void
	 */
	public static void call(String name) {
		Vector<Runnable> funcs;
		if (hooks.containsKey(name)) {
			funcs = hooks.get(name);
			if (funcs.size() > 0) {
				for (Runnable func : funcs) {
					func.run();
				}
			}
			
			clear(name);
		}
	}
	
	/**
	 * Call a hook with a delayed time (in milliseconds)
	 * 
	 * @param name
	 * @param delayMillis
	 * @return void
	 */
	public static void call(final String name, long delayMillis) {
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				call(name);
			}
		}, delayMillis);
	}
	
	/**
	 * Add a hook by name
	 * 
	 * @param name
	 * @param func
	 * @return void
	 */
	public static void add(String name, Runnable func) {
		Vector<Runnable> funcs;
		if (hooks.containsKey(name)) {
			funcs = hooks.get(name);
		} else {
			funcs = new Vector<Runnable>();
		}
		
		funcs.add(func);
		hooks.put(name, funcs);
	}
	
	/**
	 * Remove hook specified by name
	 * 
	 * @param name
	 * @return void
	 */
	public static void clear(String name) {
		hooks.put(name, new Vector<Runnable>());
	}
	
	/**
	 * Remove all hooks
	 * 
	 * @return void
	 */
	public static void clear() {
		hooks.clear();
	}
}