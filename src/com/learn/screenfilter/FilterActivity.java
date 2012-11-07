package com.learn.screenfilter;

import java.lang.reflect.Method;
import java.util.Formatter;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class FilterActivity extends Activity {
    private static final String TAG = "ScreenFilter:FilterActivity"; 
    private static int currentProgress = -1;
    //! This flag indicates whether the activity is activated by widget (true). false if activated by launcher
    private static boolean directlyInitiated = false;
    //! This is the view that simulate the filter glass
    private static View toastView;
    private static WindowManager windowManager = null;
	  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        Log.d(TAG, "onCreate");
        onNewIntent(getIntent());        
    }
    
    public void onDestroy() {
    	super.onDestroy();
        Log.d(TAG, "onDestroy; isFinishing=" + isFinishing());
    }
    
    protected void onPause() {
      super.onPause();
      Log.d(TAG, "onPause");
    }    
    
    public void onNewIntent(Intent paramIntent) {
    	Log.d(TAG, "onNewIntent");
    	int progress = paramIntent.getIntExtra("NEW_BRIGHTNESS", -1);

        boolean enabling = false;
        boolean skipWarning = false;
        
        if (progress == -1) {
        	progress = getBrightness(this);
        	Log.d(TAG, "User-initiated filter; progress=" + progress);

            if (toastView == null) 
                enabling = true;
            else 
                enabling = false;            
        } else {
            Log.d(TAG, "Directly-initiated filter; progress=" + progress);
            if (progress < 100) 
                enabling = true;          
            else 
                enabling = false;
            
            skipWarning = true;            
        }
        
        boolean softKeysEnabled = false;
        if (paramIntent.hasExtra("SOFT_KEYS_ENABLED") != false) {
            softKeysEnabled = paramIntent.getBooleanExtra("SOFT_KEYS_ENABLED", true);
        }
        else {
            SharedPreferences prefs = getSharedPreferences("preferences", 0);
            softKeysEnabled = prefs.getBoolean("SOFT_KEYS_ENABLED", false);
        }
        
        if (windowManager == null) {
            try {	
            	windowManager = (WindowManager)Class.forName("android.view.WindowManagerImpl").getMethod("getDefault", new Class[0]).invoke(null, new Object[0]);
            } catch (Exception ex) {
                Log.e(TAG, "Error getting WindowManager", ex);
            }        
        } 
        
        Log.d(TAG, "Check toastView");
        if (toastView != null)  {
            hideFilter(this, enabling);
        } else {   
        	if (enabling != false) {
        		Log.d(TAG, "Enabling filter service");
                currentProgress = progress;
                directlyInitiated = skipWarning;
                showFilter(progress, softKeysEnabled);
                startService(new Intent(this, FilterService.class));
            } 
        }
              
        if (skipWarning == false && shouldShowWarning(progress) != false) {
            showWarning(progress);
        }
        else {	
            Log.d(TAG, "No warning required; finishing activity");
            finish();
        }
           
        return;
    }    
    
    public static Notification getNotification(Context paramContext)
    {
    	Log.d(TAG, "getNotification"); 
        double percentage = 100.0D * computeAlpha(currentProgress) / 255.0D;
        Formatter formatter1 = new Formatter();
        String str1 = paramContext.getString(R.string.filter_enabled);
        String str2;
        String str3;
        Formatter formatter2 = new Formatter();
        if (directlyInitiated)
        	str2 = paramContext.getString(R.string.click_to_disable);
        else
        	str2 = paramContext.getString(R.string.click_to_adjust);
        
        str3 = formatter2.format(str2, Double.valueOf(percentage)).toString();
        Log.d(TAG, "Notification message: " + str3);        
        
        Intent localIntent;
        if (!directlyInitiated) {
        	// Invoke the setting dialog
        	Log.d(TAG, "Click will invoke preference");   
        	localIntent = new Intent(paramContext, Preferences.class);
        } else {
        	Log.d(TAG, "Click will invoke Filter with 100% brightness"); 
        	// Wake the main activity with a new brightness setting
        	localIntent = new Intent(paramContext, FilterActivity.class);
        	localIntent.putExtra("NEW_BRIGHTNESS", 100);
        }
        
        Notification noti = new NotificationCompat.Builder(paramContext)        
									        .setContentTitle(formatter1.format(str1, Double.valueOf(percentage)).toString())
									        .setContentText(str3)
									        .setSmallIcon(R.drawable.ic_launcher)
									        .setOngoing(true)
									        .setContentIntent(PendingIntent.getActivity(paramContext, 0, localIntent, 0))
									        .build();

        return noti;
    }
    
    public static void hideNotification(Context context) {
    	((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(1);
    }    

	public static int computeAlpha(int paramInt)
	{
	  return (int)Math.round(255.0D * Math.exp(4.0D * (paramInt / 100.0D) - 4.0D));
	}
	
	public static int getBrightness(Context paramContext)
	{
	    SharedPreferences localSharedPreferences = paramContext.getSharedPreferences("preferences", 0);
	    int i = localSharedPreferences.getInt("BRIGHTNESS", -1);
	    int j = localSharedPreferences.getInt("NEW_BRIGHTNESS", -1);
	    if ((i != -1) && (j == -1))
	    {
	      j = invertAlpha(Math.round(255.0F * (i / 100.0F)));
	      Log.d("ScreenFilter", "Migrating preferences.");
	      SharedPreferences.Editor localEditor = localSharedPreferences.edit();
	      localEditor.putInt("NEW_BRIGHTNESS", j);
	      localEditor.remove("BRIGHTNESS");
	      localEditor.commit();
	    }
	    else if (j == -1)
	    	j = 82;
	    
	    return j;	    
	}
	
	private boolean shouldShowWarning(int paramInt)
	{
		// TODO
		return false;
	}
	
	private void showWarning(int paramInt)
	{
		return;
	}
		
	private void showFilter(int paramInt, boolean paramBoolean)
	{
		Log.d(TAG, "showFilter");
		showToast(paramInt, paramBoolean);
	}	
	
	public static void hideFilter(Context context, boolean enabling)
	{
		hideNotification(context);
	    if (toastView != null)
	    {
	    	windowManager.removeView(toastView);
	    	toastView = null;
	    	if (!enabling)
	    	{
	    		context.stopService(new Intent(context, FilterService.class));
	    		Toast.makeText(context, "Filter stopping service", Toast.LENGTH_SHORT).show();
	    	}
	    }
	}
	
	private void showToast(int progress, boolean softKeysEnabled)
	{ 
		Log.d(TAG, "showToast");
		toastView = ((LayoutInflater)getSystemService("layout_inflater"))
				.inflate(R.layout.transient_notification, null);
		toastView.setFocusable(false);
		toastView.setFocusable(false);
		toastView.setClickable(false);
		toastView.setKeepScreenOn(false);
		toastView.setLongClickable(false);
		toastView.setFocusableInTouchMode(false);
		toastView.setBackgroundDrawable(getBackgroundDrawable(progress));
	    	
		try {
		      Method localMethod = View.class.getMethod("setFilterTouchesWhenObscured", Boolean.TYPE);
		      View localView = toastView;
		      localMethod.invoke(localView, Boolean.valueOf(false));
		} catch (Exception ex) {
			Log.w(TAG, "Cannot setFilterTouchesWhenObscured");
		}	

		WindowManager.LayoutParams localLayoutParams;
		localLayoutParams = new WindowManager.LayoutParams();
		localLayoutParams.height = -1;
		localLayoutParams.width = -1;
		localLayoutParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN 
					| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE 
					| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		localLayoutParams.format = PixelFormat.TRANSLUCENT;
		localLayoutParams.windowAnimations = android.R.style.Animation_Toast;
		localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
		localLayoutParams.setTitle("Toast");
		localLayoutParams.gravity = Gravity.FILL;
		localLayoutParams.x = 0;
		localLayoutParams.y = 0;
		localLayoutParams.verticalWeight = 1.0F;
		localLayoutParams.horizontalWeight = 1.0F;
		localLayoutParams.verticalMargin = 0.0F;
		localLayoutParams.horizontalMargin = 0.0F;
		
		if (softKeysEnabled)  {    
			try  {
				WindowManager.LayoutParams.class.getField("buttonBrightness").set(localLayoutParams, Integer.valueOf(0));
			} catch (Exception ex) {
				Log.w(TAG, "Cannot set button brightness");
			}
		} 
		
		windowManager.addView(toastView, localLayoutParams);
		return;
	}   
	
	private Drawable getBackgroundDrawable(int paramInt) {
		int i = 255 - computeAlpha(paramInt);
		Log.i(TAG, "Background alpha=" + i);
		// TODO: Make the color of the filter configurable
		return new ColorDrawable(Color.argb(i, 100, 0, 0));
	}
	
	public static double computePercentage(int paramInt) {
		return 100.0D * computeAlpha(paramInt) / 255.0D;
	}	
	
	public static int invertAlpha(int paramInt) {
		long l = Math.max(0L, Math.min(100L, Math.round(100.0D * (1.0D + Math.log(paramInt / 255.0D) / 4.0D))));
	    Log.d(TAG, "Inverted alpha=" + paramInt + " to progress=" + l);
	    return (int)l;
	}	
	
}
