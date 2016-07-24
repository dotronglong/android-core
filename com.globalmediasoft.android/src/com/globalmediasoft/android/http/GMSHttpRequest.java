package com.globalmediasoft.android.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.globalmediasoft.android.cache.GMSCacheFile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class GMSHttpRequest {
	protected URL url;
	protected GMSHttpResponse response;
	protected GMSHttpRequest.Listener listener;
	protected List<NameValuePair> params;
	protected Vector<GMSHttpHeader> headers;
	protected String lastRequestURL;
	protected String method = "GET";
	protected String charset = "UTF-8";
	
	public static String METHOD_GET  		= "GET";
	public static String METHOD_POST 		= "POST";
	public static String METHOD_OPTIONS 	= "OPTIONS";
	public static String METHOD_HEAD 		= "HEAD";
	public static String METHOD_PUT 		= "PUT";
	public static String METHOD_DELETE 		= "DELETE";
	public static String METHOD_TRACE 		= "TRACE";
	
	protected String error = "";
	
	public GMSHttpRequest() {
		this.params  = new LinkedList<NameValuePair>();
		this.headers = new Vector<GMSHttpHeader>(); 
	}
	
	public GMSHttpRequest(String address) {
		setAddress(address);
		this.params  = new LinkedList<NameValuePair>();
		this.headers = new Vector<GMSHttpHeader>(); 
	}
	
	public GMSHttpRequest setMethod(String method) {
		this.method = method;
		return this;
	}
	
	public GMSHttpRequest setCharset(String charset) {
		this.charset = charset;
		return this;
	}
	
	public GMSHttpRequest addParam(String key, String value) {
		params.add(new BasicNameValuePair(key, value));
		return this;
	}
	
	public GMSHttpRequest addParam(String key, int value) {
		params.add(new BasicNameValuePair(key, String.valueOf(value)));
		return this;
	}
	
	public GMSHttpRequest addParam(String key, long value) {
		params.add(new BasicNameValuePair(key, String.valueOf(value)));
		return this;
	}
	
	public GMSHttpRequest addParam(String key, float value) {
		params.add(new BasicNameValuePair(key, String.valueOf(value)));
		return this;
	}
	
	public GMSHttpRequest addParam(String key, double value) {
		params.add(new BasicNameValuePair(key, String.valueOf(value)));
		return this;
	}
	
	public class GMSHttpHeader {
		public String name;
		public String value;
		
		public GMSHttpHeader(String name, String value) {
			this.name  = name;
			this.value = value;
		}
	}
	
	public GMSHttpRequest addHeader(String name, String value) {
		headers.add(new GMSHttpHeader(name, value));
		return this;
	}
	
	public GMSHttpRequest removeHeader(String name) {
		if (headers.size() > 0) {
			for (int i = 0; i < headers.size(); i++) {
				if (headers.get(i).name.equals(name)) {
					headers.remove(i);
					break;
				}
			}
		}
		return this;
	}
	
	protected HttpURLConnection getConnection() throws IOException {
		int i;
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		if (headers.size() > 0) {
			GMSHttpHeader header;
			for (i = 0; i < headers.size(); i++) {
				header = headers.get(i);
				urlConnection.setRequestProperty(header.name, header.value);
			}
		}
		
		if (method.equals(METHOD_POST)) {
			urlConnection.setDoOutput(true);
			urlConnection.setChunkedStreamingMode(0);
			String data = "";
			if (params.size() > 0) {
				for (i = 0; i < params.size(); i++) {
					NameValuePair param = params.get(i);
					if (i > 0) {
						data += "&";
					}
					data += URLEncoder.encode(param.getName(), charset) + "=" + URLEncoder.encode(param.getValue(), charset);
				}
			}
			if (!data.isEmpty()) {
				OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
				out.write(data);
				out.flush();
			}
		} else if (method.equals(METHOD_GET)) {
			// do nothing as default
		} else {
			urlConnection.setRequestMethod(method);
		}
		
		return urlConnection;
	}
	
	private void _execute(GMSHttpRequest.Listener listener) throws NullPointerException {
		if (url == null) {
			throw new NullPointerException("Request URL must be set before calling execute (try setURL first)");
		}

		if (!params.isEmpty() && !method.equals(METHOD_POST)) {
			setAddress(url.toString() + (url.toString().contains("?") ? "" : "?") + URLEncodedUtils.format(params, "utf-8"));
			lastRequestURL = url.toString();
		}
		this.response = new GMSHttpResponse(url);
		this.listener = listener;
	}
	
	public void execute(GMSHttpRequest.Listener listener) {
		_execute(listener);
		new RequestTask().execute();
	}
	
	public void executeImage(GMSHttpRequest.Listener listener) {
		_execute(listener);
		new RequestImageTask().execute();
	}
	
	public GMSHttpRequest setAddress(String address) {
		try {
			this.url = new URL(address);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return this;
	}
	
	public String getLastRequestURL() {
		return lastRequestURL;
	}
	
	public List<NameValuePair> params() {
		return params;
	}
	
	private class RequestTask extends AsyncTask<Void, Void, String> {
	     protected String doInBackground(Void... v) {
	    	 StringBuilder response = new StringBuilder();
	    	 
	    	 try {
	    		 HttpURLConnection urlConnection = getConnection();
	    		 InputStream responseStream = new BufferedInputStream(urlConnection.getInputStream());
	    		 BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
	    		 String line = "";
	    		 while ((line = responseStreamReader.readLine()) != null) {
	    		 	response.append(line);
	    		 } 
	    		 responseStreamReader.close();
	    		 urlConnection.disconnect();
	    	 } catch (MalformedURLException e) {
	    		 error = e.getMessage();
	    		 e.printStackTrace();
	 		 } catch (IOException e) {
	 			 error = e.getMessage();
	 			 e.printStackTrace();
	 		 }

	         return response.toString();
	     }

	     protected void onPostExecute(String response) {
	    	 if (!error.isEmpty()) {
	    		 GMSHttpRequest.this.listener.onRequestFailed(error);
	    	 } else {
	    		 GMSHttpRequest.this.response.setContent(response);
	    		 GMSHttpRequest.this.listener.onRequestCompleted(GMSHttpRequest.this.response);
	    	 }
	     }
	}
	
	private class RequestImageTask extends AsyncTask<Void, Void, Bitmap> {
	     protected Bitmap doInBackground(Void... v) {
	    	 try {
	    		 HttpURLConnection urlConnection = getConnection();
	    		 InputStream responseStream = urlConnection.getInputStream();
	    		 Bitmap bm = BitmapFactory.decodeStream(responseStream);
	    		 urlConnection.disconnect();
	    		 return bm;
	    	 } catch (MalformedURLException e) {
	    		 error = e.getMessage();
	    		 e.printStackTrace();
	 		 } catch (IOException e) {
	 			 error = e.getMessage();
	 			 e.printStackTrace();
	 		 }
	    	 
	         return null;
	     }

	     protected void onPostExecute(Bitmap response) {
	    	 if (!error.isEmpty()) {
	    		 GMSHttpRequest.this.listener.onRequestFailed(error);
	    	 } else {
	    		 GMSHttpRequest.this.response.setContent(response);
	    		 GMSHttpRequest.this.listener.onRequestCompleted(GMSHttpRequest.this.response);
	    	 }
	     }
	}
	
	public static void loadImage(String address, final ImageView img) {
		loadImage(address, img, null);
	}
	
	public static void loadImage(String address, final ImageView img, final GMSCacheFile cache) {
		if (!address.isEmpty()) {
			new GMSHttpRequest(address).executeImage(new Listener() {
				
				@Override
				public void onRequestFailed(String error) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onRequestCompleted(GMSHttpResponse response) {
					// TODO Auto-generated method stub
					Bitmap bm = response.getBitmapContent();
					if (bm != null) {
						img.setImageBitmap(bm);
						if (cache != null) {
							cache.save(bm);
						}
					}
				}
			});
		}
	}
	
	public GMSHttpResponse getResponse() {
		return response;
	}
	
	public String getError() {
		return error;
	}
	
	public static interface Listener {
		/**
		 * This method will be called when completed receiving response from source
		 * @param response
		 */
		public void onRequestCompleted(GMSHttpResponse response);
		
		/**
		 * This method will be called if there is an error occurred
		 * @param error
		 */
		public void onRequestFailed(String error);
	}
}