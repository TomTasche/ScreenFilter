package at.tomtasche.bestscreenfilterever;

import java.lang.reflect.Method;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class FilterService extends Service {

	private final int mID = 1993;

	private WindowManager windowManager;
	private RelativeLayout hackLayout;

	@Override
	public void onCreate() {
		super.onCreate();

		Notification notification = new NotificationCompat.Builder(this)
				.setContentTitle("hello").setContentText("world!")
				// .setSmallIcon(R.drawable.ic_launcher)
				.setOngoing(true).build();

		try {
			startForeground(mID, notification);
		} catch (Exception localException) {
			((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
					.notify(mID, notification);
		}

		hackLayout = (RelativeLayout) ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.hack, null);

		if (Preferences.getTransparency(this) < 0) {
			hackLayout.setBackgroundDrawable(FilterUtil.getBackgroundDrawable(
					this, 100));
			if (Preferences.getTransparency(this) == -1) {
				((ImageView) hackLayout.findViewById(R.id.hackView))
						.setImageDrawable(getResources().getDrawable(
								R.drawable.devfest));
			} else if (Preferences.getTransparency(this) == -2) {
				((ImageView) hackLayout.findViewById(R.id.hackView))
						.setImageDrawable(getResources().getDrawable(
								R.drawable.birthday));
			} else if (Preferences.getTransparency(this) == -3) {
				((ImageView) hackLayout.findViewById(R.id.hackView))
				.setImageDrawable(getResources().getDrawable(
						R.drawable.android));
				
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, R.id.hackView);
				params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
						R.id.hackView);

				hackLayout.findViewById(R.id.hackView).setLayoutParams(params);
			}
		} else {
			hackLayout.setBackgroundDrawable(FilterUtil
					.getBackgroundDrawable(this));
		}
		hackLayout.setFocusable(false);
		hackLayout.setFocusable(false);
		hackLayout.setClickable(false);
		hackLayout.setKeepScreenOn(false);
		hackLayout.setLongClickable(false);
		hackLayout.setFocusableInTouchMode(false);

		windowManager = null;
		try {
			windowManager = (WindowManager) Class
					.forName("android.view.WindowManagerImpl")
					.getMethod("getDefault", new Class[0])
					.invoke(null, new Object[0]);
		} catch (Exception ex) {
			Log.e("bla", "Error getting WindowManager", ex);
		}

		try {
			Method localMethod = View.class.getMethod(
					"setFilterTouchesWhenObscured", Boolean.TYPE);
			View localView = hackLayout;
			localMethod.invoke(localView, Boolean.valueOf(false));
		} catch (Exception ex) {
			Log.w("bla", "Cannot setFilterTouchesWhenObscured");
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

		try {
			WindowManager.LayoutParams.class.getField("buttonBrightness").set(
					localLayoutParams, Integer.valueOf(0));
		} catch (Exception ex) {
			Log.w("bla", "Cannot set button brightness");
		}

		windowManager.addView(hackLayout, localLayoutParams);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		windowManager.removeView(hackLayout);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
