package com.learn.screenfilter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Preferences extends Activity {
	private static final String TAG = "ScreenFilter:Preferences"; 
	private SharedPreferences prefs;
	  private BrightnessSelector selector;

	  protected void onCreate(Bundle paramBundle)
	  {
	    super.onCreate(paramBundle);
	    Log.d(TAG, "onCreate");
	    onNewIntent(getIntent());
	  }

	  public void onNewIntent(Intent paramIntent)
	  {
	    Log.d(TAG, "onNewIntent");
	    this.prefs = getSharedPreferences("preferences", 0);
	    FilterActivity.hideFilter(this, false);
	    
	    setContentView(R.layout.preferences);
	    ((Button)findViewById(R.id.ReenableButton)).setOnClickListener(new ReenableListener());	    
	    
	    int i = FilterActivity.getBrightness(this);
	    this.selector = ((BrightnessSelector)findViewById(R.id.PreferencesBrightnessSelector));
	    this.selector.setProgress(i);
	    this.selector.setSoftKeysEnabled(this.prefs.getBoolean("SOFT_KEYS_ENABLED", true));
	    
	    setContentView(R.layout.preferences);
	  }

	  protected void onPause()
	  {
	    super.onPause();
	    Log.d(TAG, "onPause");
	    //this.prefs.edit().putInt("NEW_BRIGHTNESS", this.selector.getProgress()).putBoolean("SOFT_KEYS_ENABLED", this.selector.isSoftKeysEnabled()).remove("TOO_LOW_CONFIRMED").commit();
	    this.prefs.edit()
	    			.putInt("NEW_BRIGHTNESS", this.selector.getProgress())
	    			.putBoolean("SOFT_KEYS_ENABLED", false)
	    			.remove("TOO_LOW_CONFIRMED").commit();
	    finish();
	  }

	  private class ReenableListener
	    implements View.OnClickListener
	  {
	    public void onClick(View paramView)
	    {
	    	Log.d(TAG, "Reenabling with new setting");
	    	Preferences.this.startActivity(new Intent(Preferences.this, FilterActivity.class));
	    	Preferences.this.finish();
	    }
	  }
}
