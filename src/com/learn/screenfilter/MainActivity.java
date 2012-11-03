package com.learn.screenfilter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Window window = getWindow();

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
        
        // Let touches go through to apps/activities underneath.
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE); 
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        window.addFlags(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);
        
        
        */
        startService(new Intent(this, ScreenFilterService.class));
        
        // Now set up content view
        setContentView(R.layout.activity_main);
    }
    
    public void onDestroy()
    {
    	super.onDestroy();
    }    
}
