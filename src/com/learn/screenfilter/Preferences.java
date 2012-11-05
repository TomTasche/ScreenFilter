package com.learn.screenfilter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Preferences extends Activity {
	private static final String LOGNAME = "ScreenFilter:Preferences"; 
	private SharedPreferences prefs;
	  //private BrightnessSelector selector;

	  protected void onCreate(Bundle paramBundle)
	  {
	    super.onCreate(paramBundle);
	    Log.d(LOGNAME, "onCreate");
	    onNewIntent(getIntent());
	  }

	  public void onNewIntent(Intent paramIntent)
	  {
	    Log.d(LOGNAME, "onNewIntent");
	    this.prefs = getSharedPreferences("preferences", 0);
	    FilterActivity.hideFilter(this, false);
	    /*
	    setContentView(2130903043);
	    ((Button)findViewById(2131230731)).setOnClickListener(new ReenableListener(null));
	    int i = ScreenFilter.getBrightness(this);
	    this.selector = ((BrightnessSelector)findViewById(2131230728));
	    this.selector.setProgress(i);
	    this.selector.setSoftKeysEnabled(this.prefs.getBoolean("SOFT_KEYS_ENABLED", true));
	    */
	    setContentView(R.layout.preferences);
	  }

	  protected void onPause()
	  {
	    super.onPause();
	    Log.d(LOGNAME, "onPause");
	    // this.prefs.edit().putInt("NEW_BRIGHTNESS", this.selector.getProgress()).putBoolean("SOFT_KEYS_ENABLED", this.selector.isSoftKeysEnabled()).remove("TOO_LOW_CONFIRMED").commit();
	    this.prefs.edit()
	    			.putInt("NEW_BRIGHTNESS", 60)
	    			.putBoolean("SOFT_KEYS_ENABLED", false)
	    			.remove("TOO_LOW_CONFIRMED").commit();
	    finish();
	  }

	  private class ReenableListener
	    implements View.OnClickListener
	  {
	    private ReenableListener()
	    {
	    }

	    public void onClick(View paramView)
	    {
	      Preferences.this.startActivity(new Intent(Preferences.this, FilterActivity.class));
	      Preferences.this.finish();
	    }
	  }
}
