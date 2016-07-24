package com.globalmediasoft.android.http;

import java.net.URL;

import org.json.JSONException;

import com.globalmediasoft.android.parser.GMSParserJSON;

import android.graphics.Bitmap;

public class GMSHttpResponse {
	protected String content;
	protected Bitmap bm;
	protected URL url;
	
	public GMSHttpResponse() {
		
	}
	
	public GMSHttpResponse(String content) {
		this.content = content;
	}
	
	public GMSHttpResponse(URL url) {
		this.url = url;
	}
	
	public String getContent() {
		return content.trim();
	}
	
	public Bitmap getBitmapContent() {
		return this.bm;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public void setContent(Bitmap bm) {
		this.bm = bm;
	}
	
	public GMSParserJSON getJSONContent() {
		try {
			return new GMSParserJSON(getContent());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getRequestedUrl() {
		return url.toString();
	}
}
