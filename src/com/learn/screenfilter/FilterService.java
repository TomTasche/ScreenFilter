package com.learn.screenfilter;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class FilterService extends Service {
	private static final String LOGNAME = "ScreenFilter:FilterService"; 
	private final IBinder mBinder = new LocalBinder();
	private final int mID = 1234;
	private NotificationManager mNM;
	
	@Override
	public IBinder onBind(Intent intent) {
		return this.mBinder;
	}

	@TargetApi(5)
	@Override
	public void onCreate() {		
	    super.onCreate();
	    Toast.makeText(this, "Filter Service Created", Toast.LENGTH_SHORT).show();		    
	    Notification localNotification = FilterActivity.getNotification(getBaseContext());
	    try
	    {
	    	startForeground(mID, localNotification);
	    	return;
	    }
	    catch (Exception localException)
	    {
	    	// Fall back to the old API    		
			Log.w(LOGNAME, "Error calling startForeground");    		
    		//setForeground(true); // For API level < 5
    		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    		mNM.notify(mID, localNotification);
	    }
	}	

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Filter Service Stopped", Toast.LENGTH_SHORT).show();		
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		Toast.makeText(this, "Filter Service Started", Toast.LENGTH_SHORT).show();
	}	
	
	public class LocalBinder extends Binder
	{
		public LocalBinder() {
		}

		FilterService getService() {
			return FilterService.this;
		}
	}	
}
