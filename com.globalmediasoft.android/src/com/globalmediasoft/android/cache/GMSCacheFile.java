package com.globalmediasoft.android.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.globalmediasoft.android.string.GMSString;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

public class GMSCacheFile implements GMSCache {
	protected int cacheExpire;
	protected String cacheDirectory;
	protected String cacheId;
	protected String cacheName;
	protected Context context;
	protected Bundle options = new Bundle();
	
	public final static String DATA_FILE = "data";
	public final static String INFO_FILE = "info";
	
	public GMSCacheFile(Context context, String cacheDirectory, String cacheName) {
		this.context = context;
		this.cacheDirectory = cacheDirectory;
		setCacheId(cacheName);
	}
	
	protected File getDataFile() {
		File file = new File(getFile(cacheDirectory + "/" + cacheId), DATA_FILE);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return file;
	}
	
	protected File getInfoFile() {
		File file = new File(getFile(cacheDirectory + "/" + cacheId), INFO_FILE);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return file;
	}
	
	protected File getFile(String path) {
		String[] folders = path.split("/");

		File file = null;
		if (folders.length == 1) {
			file = new File(path);
		} else if (folders.length > 1) {
			for (String folder : folders) {
				if (file == null) {
					file = context.getDir(folder, Context.MODE_PRIVATE);
				} else {
					file = new File(file, folder);
				}
			}
		}
		
		if (!file.exists() || !file.isDirectory()) {
			file.mkdirs();
		}
		
		return file;
	}
	
	protected FileOutputStream getWriter() {
		FileOutputStream writer = null;
		try {
			File file = getDataFile();
			if (file != null) {
				writer = new FileOutputStream(file);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return writer;
	}
	
	protected FileInputStream getReader() {
		FileInputStream reader = null;
		try {
			File file = getDataFile();
			if (file != null) {
				reader = new FileInputStream(file);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return reader;
	}
	
	public GMSCacheFile setOptions(Bundle options) {
		this.options = options;
		return this;
	}
	
	public Bundle getOptions() {
		return options;
	}

	@Override
	public boolean isExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setExpire(int seconds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCacheId(String id) {
		// TODO Auto-generated method stub
		cacheName = id;
		cacheId   = GMSString.md5(id);
	}

	@Override
	public String getCacheId() {
		// TODO Auto-generated method stub
		return cacheId;
	}

	@Override
	public void save(Bitmap bm) {
		// TODO Auto-generated method stub
		FileOutputStream writer = getWriter();
		if (writer != null) {
			bm.compress(Bitmap.CompressFormat.PNG, 0, writer);
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public Bitmap getBitmap() {
		// TODO Auto-generated method stub
		FileInputStream reader = getReader();
		Bitmap bm = null;
		
		if (reader != null) {
			bm = BitmapFactory.decodeStream(reader);
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return bm;
	}

}
