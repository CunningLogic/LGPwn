package com.cunninglogic.lgpwn;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.Toast;

public class Worker {

	public static void setup(Context context){

	File filesDir = new File("/data/data/com.cunninglogic.lgpwn/files/");
	
	if (!filesDir.exists())
		filesDir.delete();
	filesDir.mkdir();
	
	File backupDir = new File("/sdcard/Backup");
	
	if(!backupDir.exists())
		backupDir.mkdir(); 
	
	copyAssets(context);    
	
	exec("chmod 777 /data/data/com.cunninglogic.lgpwn/files");
	exec("rm /data/data/com.cunninglogic.lgpwn/files/*");
	Intent backupApp = new Intent();
	backupApp.setComponent(new ComponentName("com.spritemobile.backup.lg","com.spritemobile.backup.layout.SpriteBackup"));
	context.startActivity(backupApp);
	Intent i = new Intent(context, RootService.class);
	context.startService(i);
	}
	
	private static void copyAssets(Context context) {
	    AssetManager assetManager = context.getAssets();
	    String[] files = null;
	    try {
	        files = assetManager.list("");
	    } catch (IOException e) {
	        Log.e("tag", "Failed to get asset file list.", e);
	    }
	    for(String filename : files) {
	        InputStream in = null;
	        OutputStream out = null;
	        try {
	          in = assetManager.open(filename);
	          out = new FileOutputStream("/sdcard/Backup/" + filename);
	          copyFile(in, out);
	          in.close();
	          in = null;
	          out.flush();
	          out.close();
	          out = null;
	        } catch(IOException e) {
	            Log.e("tag", "Failed to copy asset file: " + filename, e);
	        }       
	    }
	}
	
	private static void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}
	
	public static void exec(String cmd){

		try {
			Process process = Runtime.getRuntime().exec("sh");
			DataOutputStream os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(cmd + "\n");
			os.writeBytes("exit\n");
			os.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
