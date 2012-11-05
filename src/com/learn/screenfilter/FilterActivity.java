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
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class FilterActivity extends Activity {
    private static final String LOGNAME = "ScreenFilter:FilterActivity"; 
    private static int currentProgress = -1;
    private static boolean directlyInitiated = false;
    private static View toastView;
    private static WindowManager windowManager = null;
	  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        Log.d(LOGNAME, "onCreate");
        onNewIntent(getIntent());        
    }
    
    public void onDestroy() {
    	super.onDestroy();
        Log.d(LOGNAME, "onDestroy; isFinishing=" + isFinishing());
    }
    
    protected void onPause()
    {
      super.onPause();
      Log.d(LOGNAME, "onPause");
    }    
    
    public void onNewIntent(Intent paramIntent) {
        //const/4 v9, -0x1
        //const/4 v12, 0x1
        //const/4 v10, 0x0
        //const/4 v6, 0x0

    	Log.d(LOGNAME, "onNewIntent");
    	int progress = paramIntent.getIntExtra("NEW_BRIGHTNESS", -1);

        boolean enabling = false;
        boolean skipWarning = false;
        
        if (progress == -1) // :cond_4
        {
        	progress = getBrightness(this);
        	Log.d(LOGNAME, "User-initiated filter; progress=" + progress);

            if (toastView == null) //:cond_3
                enabling = true;
            else // :cond_3
                enabling = false;            
        }
        else    // progress != -1 :cond_4
        {
            Log.d(LOGNAME, "Directly-initiated filter; progress=" + progress);
            if (progress < 100)  // if-ge v5, 100,  :cond_5
            {
                enabling = true;
            }
            else // :cond_5
            {
                enabling = false;
            }
            skipWarning = true;            
        }
        
        boolean softKeysEnabled = false;
        if (paramIntent.hasExtra("SOFT_KEYS_ENABLED") != false) // :cond_6
        {
            softKeysEnabled = paramIntent.getBooleanExtra("SOFT_KEYS_ENABLED", true);
        }
        else //  :cond_6
        {
            SharedPreferences prefs = getSharedPreferences("preferences", 0);
            softKeysEnabled = prefs.getBoolean("SOFT_KEYS_ENABLED", false);
        }
        
        if (windowManager == null) //  if-nez v8, :cond_0
        {
            try //:try_start_0
            {	
            	windowManager = (WindowManager)Class.forName("android.view.WindowManagerImpl").getMethod("getDefault", new Class[0]).invoke(null, new Object[0]);
            } // :try_end_0
            catch (Exception localException) // :catch_0
            {
                Log.e(LOGNAME, "Error getting WindowManager", localException);
            }        
        } 
        
        Log.d(LOGNAME, "Check toastView");
        if (toastView != null)  // if-eqz v8, :cond_1
        {
            hideFilter(this, enabling);
        } 
        else // :cond_1
        {   
        	if (enabling != false) // if-eqz v2, :cond_2
            {
        		Log.d(LOGNAME, "Enabling filter service");
                currentProgress = progress;
                directlyInitiated = skipWarning;
                showFilter(progress, softKeysEnabled);
                startService(new Intent(this, FilterService.class));
            } // :cond_2
        }
        
        // windowManager != null // :cond_0
        if (skipWarning == false && shouldShowWarning(progress) != false) //  if-nez v6, :cond_7, if-eqz v8, :cond_7
        {
            showWarning(progress);
        }
        else //   :cond_7
        {	
            Log.d(LOGNAME, "No warning required; finishing activity");
            finish();
        }
           
        return;
    }    
    
    public static Notification getNotification(Context paramContext)
    {
    	Log.d(LOGNAME, "getNotification"); 
        double percentage = 100.0D * computeAlpha(currentProgress) / 255.0D;
        Formatter localFormatter1 = new Formatter();
        String str1 = paramContext.getString(R.string.filter_enabled);
        Notification localNotification = new Notification(R.string.app_name, localFormatter1.format(str1, Double.valueOf(percentage)).toString(), System.currentTimeMillis());
        localNotification.flags = (Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT | localNotification.flags);

        String str2;
        String str3;
        Formatter localFormatter2 = new Formatter();
        if (directlyInitiated)
        	str2 = paramContext.getString(R.string.click_to_disable);
        else
        	str2 = paramContext.getString(R.string.click_to_adjust);
        
        str3 = localFormatter2.format(str2, Double.valueOf(percentage)).toString();
        Log.d(LOGNAME, "Notification message: " + str3);        
        
        Intent localIntent;
        if (directlyInitiated)
        {
        	// Invoke the setting dialog
        	localIntent = new Intent(paramContext, Preferences.class);
        }
        else
        {
        	// Wake the main activity with a new brightness setting
        	localIntent = new Intent(paramContext, FilterActivity.class);
        	localIntent.putExtra("NEW_BRIGHTNESS", 100);
        }
        localNotification.setLatestEventInfo(paramContext, paramContext.getString(2131099655), str3, PendingIntent.getActivity(paramContext, 0, localIntent, 0));
        return localNotification;
    }
    
    public static void hideNotification(Context paramContext)
    {
    	((NotificationManager)paramContext.getSystemService("notification")).cancel(1);
    }    

	public static int computeAlpha(int paramInt)
	{
	  return (int)Math.round(255.0D * Math.exp(4.0D * (paramInt / 100.0D) - 4.0D));
	}
	
	public static int getBrightness(Context paramContext)
	{
		// TODO: get brightness from preference/setting
		return 70;
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
		Log.d(LOGNAME, "showFilter");
		showToast(paramInt, paramBoolean);
		Intent localIntent = new Intent("com.haxor.ScreenFilter.ACTION_FILTER_TOGGLED");
		localIntent.putExtra("com.haxor.ScreenFilter.ACTION_FILTER_TOGGLED_EXTRA", true);
		sendBroadcast(localIntent);
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
	    	Intent localIntent = new Intent("com.haxor.ScreenFilter.ACTION_FILTER_TOGGLED");
	    	localIntent.putExtra("com.haxor.ScreenFilter.ACTION_FILTER_TOGGLED_EXTRA", false);
	    	context.sendBroadcast(localIntent);
	    }
	}
	
	private void showToast(int progress, boolean softKeysEnabled)
	{ 
		Log.d(LOGNAME, "showToast");
		toastView = ((LayoutInflater)getSystemService("layout_inflater"))
				.inflate(R.layout.transient_notification, null);
		toastView.setFocusable(false);
		toastView.setFocusable(false);
		toastView.setClickable(false);
		toastView.setKeepScreenOn(false);
		toastView.setLongClickable(false);
		toastView.setFocusableInTouchMode(false);
		toastView.setBackgroundDrawable(getBackgroundDrawable(progress));
	    	
		try // :try_start_0
		{
		      Class[] arrayOfClass = new Class[1];
		      arrayOfClass[0] = Boolean.TYPE;
		      Method localMethod = View.class.getMethod("setFilterTouchesWhenObscured", arrayOfClass);
		      View localView = toastView;
		      Object[] arrayOfObject = new Object[1];
		      arrayOfObject[0] = Boolean.valueOf(false);
		      localMethod.invoke(localView, arrayOfObject);
		}	// :try_end_0
		// .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0
		catch (Exception ex) // :catch_0
		{
			Log.w(LOGNAME, "Cannot setFilterTouchesWhenObscured");
		}	

		WindowManager.LayoutParams localLayoutParams;
		localLayoutParams = new WindowManager.LayoutParams();
		localLayoutParams.height = -1;
		localLayoutParams.width = -1;
		localLayoutParams.flags = 280;
		localLayoutParams.format = -3;
		localLayoutParams.windowAnimations = 16973828;
		localLayoutParams.type = 2006;
		localLayoutParams.setTitle("Toast");
		localLayoutParams.gravity = 119;
		localLayoutParams.x = 0;
		localLayoutParams.y = 0;
		localLayoutParams.verticalWeight = 1.0F;
		localLayoutParams.horizontalWeight = 1.0F;
		localLayoutParams.verticalMargin = 0.0F;
		localLayoutParams.horizontalMargin = 0.0F;
		
		if (softKeysEnabled)  // if-nez p2, :cond_0
		{    
			try  // :try_start_1
			{
				WindowManager.LayoutParams.class.getField("buttonBrightness").set(localLayoutParams, Integer.valueOf(0));
			} // :try_end_1
			// .catch Ljava/lang/Exception; {:try_start_1 .. :try_end_1} :catch_1
			catch (Exception ex) // :catch_1
			{
				Log.w(LOGNAME, "Cannot set button brightness");
			}
		} // :cond_0
		
		windowManager.addView(toastView, localLayoutParams);
		return;
	}   
	
	private Drawable getBackgroundDrawable(int paramInt)
	{
		int i = 255 - computeAlpha(paramInt);
		Log.i(LOGNAME, "Background alpha=" + i);
		// TODO: Make the color of the filter configurable
		return new ColorDrawable(Color.argb(i, 100, 0, 0));
  }	
	
}
