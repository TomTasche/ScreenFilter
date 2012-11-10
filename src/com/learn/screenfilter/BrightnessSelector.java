package com.learn.screenfilter;

import java.util.Formatter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;

import android.text.InputType;
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
	private static final String TAG = "ScreenFilter:Selector";   
	private SeekBar brightnessAdjust;
	private CheckBox softKeysEnabled;
	private int mColorRGB = Color.RED; //< Holding the color, only use the RGB components

	public BrightnessSelector(final Context paramContext, AttributeSet paramAttributeSet)
	{
		super(paramContext, paramAttributeSet);
		View localView = ((LayoutInflater)paramContext.getSystemService("layout_inflater")).inflate(R.layout.brightness_selector, null);
		this.brightnessAdjust = ((SeekBar)localView.findViewById(R.id.BrightnessSeekBar));
		this.brightnessAdjust.setOnSeekBarChangeListener(new ProgressChangedHandler());
		this.softKeysEnabled = ((CheckBox)localView.findViewById(R.id.SoftKeysEnabled));
		((Button)localView.findViewById(R.id.BrightnessEdit)).setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View paramAnonymousView)
			{
				final EditText localEditText = new EditText(paramContext);
				localEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
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
		
		((Button)localView.findViewById(R.id.ColorEdit)).setOnClickListener(new ColorChangedHandler());
		
		addView(localView);
		setProgress(82);
	}

	private void setExampleBrightness(int progress)
	{
		((TextView)findViewById(R.id.ExampleText))
		  			.setTextColor(Color.argb(FilterActivity.computeAlpha(progress), 
		  							Color.red(mColorRGB), 
		  							Color.green(mColorRGB),
		  							Color.blue(mColorRGB)));
		TextView localTextView = (TextView)findViewById(R.id.BrightnessPercentage);
		Formatter formatter = new Formatter();
		String str = formatter.format("%.1f%%", Double.valueOf(FilterActivity.computePercentage(progress))).toString();
		if (progress == 100)
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
	  
	private void setExampleColor(int color)
	{
		mColorRGB = color;
		setExampleBrightness(getProgress());
	}
	  
	public int getColor()
	{
		return mColorRGB;
	}
	
	public void setColor(int color) 
	{
		mColorRGB = color;
		setExampleBrightness(getProgress());
	}
	  
	private class ColorChangedHandler
	    implements ColorPickerDialog.OnColorChangedListener,
	    			Button.OnClickListener {
		public void colorChanged(int color) {
			  // mPaint.setColor(color);
			  Log.d(TAG, "Color set " + Color.red(color) + " " + Color.green(color) + " " + Color.blue(color) );
			  BrightnessSelector.this.setExampleColor(color);
		}
		  
		public void onClick(View paramView)
		{
			new ColorPickerDialog(paramView.getContext(), this, BrightnessSelector.this.getColor()).show();
		}		  
	}	  
}
