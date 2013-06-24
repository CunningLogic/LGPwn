package com.cunninglogic.lgpwn;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Guide");
		dialog.setMessage("Press RootMe to run the backup app, then select restore, then internal storage," +
				" then restore the Lgpwn backup. Shortly after the restore is done, the phone will reboot." +
				" once the phone has rebooted, please install Supersu from the market.");
	
		dialog.setCancelable(false);
		dialog.setPositiveButton("RootMe",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {

				Worker.setup(MainActivity.this);
			}
		  });
		dialog.setNegativeButton("Exit",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				MainActivity.this.finish();
			}
		});
		AlertDialog alertDialog = dialog.create();
		 
		alertDialog.show();
		
		
	}


	
}
