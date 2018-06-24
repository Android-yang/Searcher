package com.android.yangke.http;

import android.os.Environment;

import com.android.yangke.base.BaseApplication;

import java.io.File;

public class CacheUtils {
    private static File cacheDir;
	
	static{
        cacheDir = new File(Environment.getExternalStorageDirectory(), "dazongwuliu");
		if(!cacheDir.exists()){
			cacheDir.mkdirs();
		}
	}
	
	public static boolean hasSdCard(){
		File externalStorageDirectory = Environment.getExternalStorageDirectory();
		return externalStorageDirectory.exists()&&externalStorageDirectory.isDirectory()&&externalStorageDirectory.canRead()&&externalStorageDirectory.canWrite();
	}
	
	public static File getCacheFile(String name){
		return new File(cacheDir,name);
	}
	
	public static File getFile(String name){
		return new File(BaseApplication.instance().getFilesDir(),name);
	}
	
}
