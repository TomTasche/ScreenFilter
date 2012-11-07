package com.learn.screenfilter;

import java.util.Formatter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Button;

public class BrightnessSelector extends RelativeLayout {
	  private SeekBar brightnessAdjust;
	  private CheckBox softKeysEnabled;

	  public BrightnessSelector(final Context paramContext, AttributeSet paramAttributeSet)
	  {
	    super(paramContext, paramAttributeSet);
	    View localView = ((LayoutInflater)paramContext.getSystemService("layout_inflater")).inflate(R.layout.brightness_selector, null);
	    this.brightnessAdjust = ((SeekBar)localView.findViewById(R.id.BrightnessSeekBar));
	    this.brightnessAdjust.setOnSeekBarChangeListener(new ProgressChangedHandler());
	    this.softKeysEnabled = ((CheckBox)localView.findViewById(R.id.SoftKeysEnabled));
	    ((Button)localView.findViewById(R.id.BrightnessEdit)).setOnClickListener(new View.OnClickListener()
	    {
	      public void onClick(View paramAnonymousView)
	      {
	        final EditText localEditText = new EditText(paramContext);
	        localEditText.setInputType(3);
	        new AlertDialog.Builder(paramContext)
	        		.setTitle(paramContext.getString(R.string.edit_brightness))
	        		.setMessage(paramContext.getString(R.string.edit_brightness_message))
	        		.setView(localEditText)
	        		.setPositiveButton("Ok", new DialogInterface.OnClickListener()
								{
								  public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int)
								  {
								    String str = localEditText.getText().toString();
								    int i = -1;
								    try
								    {
								      int j = Integer.parseInt(str);
								      i = j;
								      if ((i >= 0) && (i <= 100))
								      {
								        BrightnessSelector.this.setProgress(FilterActivity.invertAlpha((int)Math.round(255.0D * i / 100.0D)));
								        paramAnonymous2DialogInterface.dismiss();	                
								      }
								      else
								    	  Log.e("BrightnessSelector", "User input out of range: " + str);
								  return;
								}
								catch (NumberFormatException localNumberFormatException)
								{
								    Log.e("BrightnessSelector", "Bad user input: " + str);	            
								    }
								  }
								})
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
						        {
						          public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int)
						          {
						            paramAnonymous2DialogInterface.dismiss();
						          }
						        })
	        .create()
	        .show();
	      }
	    });
	    addView(localView);
	    setProgress(82);
	  }

	  private void setExampleBrightness(int paramInt)
	  {
	    ((TextView)findViewById(R.id.ExampleText)).setTextColor(Color.argb(FilterActivity.computeAlpha(paramInt), 255, 255, 255));
	    TextView localTextView = (TextView)findViewById(R.id.BrightnessPercentage);
	    Formatter formatter = new Formatter();
	    String str = formatter.format("%.1f%%", Double.valueOf(FilterActivity.computePercentage(paramInt))).toString();
	    if (paramInt == 100)
	      str = "100%";
	    localTextView.setText(str);
	  }

	  public int getProgress()
	  {
	    return this.brightnessAdjust.getProgress();
	  }

	  public boolean isSoftKeysEnabled()
	  {
	    return this.softKeysEnabled.isChecked();
	  }

	  public void setProgress(int paramInt)
	  {
	    this.brightnessAdjust.setProgress(paramInt);
	    setExampleBrightness(paramInt);
	  }

	  public void setSoftKeysEnabled(boolean paramBoolean)
	  {
	    this.softKeysEnabled.setChecked(paramBoolean);
	  }

	  private class ProgressChangedHandler
	    implements SeekBar.OnSeekBarChangeListener
	  {
	    private ProgressChangedHandler()
	    {
	    }

	    public void onProgressChanged(SeekBar paramSeekBar, int paramInt, boolean paramBoolean)
	    {
	      BrightnessSelector.this.setExampleBrightness(paramInt);
	    }

	    public void onStartTrackingTouch(SeekBar paramSeekBar)
	    {
	    }

	    public void onStopTrackingTouch(SeekBar paramSeekBar)
	    {
	    }
	  }
}
