package com.globalmediasoft.android.cache;

import android.graphics.Bitmap;

public interface GMSCache {
	/**
	 * Check whether cache is expired or not
	 * @return
	 */
	public boolean isExpired();
	
	/**
	 * Set cache expiration, 0 to cache forever
	 * @param seconds
	 */
	public void setExpire(int seconds);
	
	/**
	 * Disable caching
	 */
	public void disable();
	
	public void setCacheId(String id);
	
	public String getCacheId();
	
	/**
	 * Save a Bitmap image into cache
	 * 
	 * @param name
	 * @param img
	 */
	public void save(Bitmap img);
	
	/**
	 * Get Bitmap image from cache
	 * @param name
	 * @return {@link Bitmap}
	 */
	public Bitmap getBitmap();
}