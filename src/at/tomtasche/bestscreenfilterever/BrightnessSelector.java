package at.tomtasche.bestscreenfilterever;

import java.util.Formatter;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class BrightnessSelector extends RelativeLayout {

	private SeekBar brightnessSeeker;

	public BrightnessSelector(final Context context, AttributeSet attributes) {
		super(context, attributes);

		View localView = ((LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.brightness_selector, null);
		brightnessSeeker = ((SeekBar) localView
				.findViewById(R.id.brightnessSeeker));
		brightnessSeeker
				.setOnSeekBarChangeListener(new ProgressChangedHandler());

		addView(localView);
	}

	private void setSampleBrightness(int progress) {
		((TextView) findViewById(R.id.sampleText)).setTextColor(Color.argb(
				FilterUtil.computeAlpha(progress), Color.red(Color.WHITE),
				Color.green(Color.WHITE), Color.blue(Color.WHITE)));

		TextView localTextView = (TextView) findViewById(R.id.brightnessText);

		Formatter formatter = new Formatter();
		String str = formatter.format("%.1f%%",
				Double.valueOf(FilterUtil.computePercentage(progress)))
				.toString();
		formatter.close();

		if (progress == 100)
			str = "100%";

		localTextView.setText(str);
	}

	public int getBrightness() {
		return brightnessSeeker.getProgress();
	}

	public void setBrightness(int brightness) {
		brightnessSeeker.setProgress(brightness);

		setSampleBrightness(brightness);
	}

	private class ProgressChangedHandler implements
			SeekBar.OnSeekBarChangeListener {
		private ProgressChangedHandler() {
		}

		public void onProgressChanged(SeekBar paramSeekBar, int paramInt,
				boolean paramBoolean) {
			setSampleBrightness(paramInt);
		}

		public void onStartTrackingTouch(SeekBar paramSeekBar) {
		}

		public void onStopTrackingTouch(SeekBar paramSeekBar) {
		}
	}
}
