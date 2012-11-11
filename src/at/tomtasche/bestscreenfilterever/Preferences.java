package at.tomtasche.bestscreenfilterever;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Preferences extends Activity {

	public static final String PREFERENCE_COLOR = "color";
	public static final String PREFERENCE_TRANSPARENCY = "transparency";

	public static SharedPreferences getSharedPreferences(Context context) {
		return context
				.getSharedPreferences("preferences", Context.MODE_PRIVATE);
	}

	public static int getTransparency(Context context) {
		return getSharedPreferences(context).getInt(
				Preferences.PREFERENCE_TRANSPARENCY, 90);
	}

	public static int getColor(Context context) {
		return Color.BLACK;
	}

	public static void setTransparency(Context context, int transparency) {
		getSharedPreferences(context).edit()
				.putInt(PREFERENCE_TRANSPARENCY, transparency).commit();
	}

	private BrightnessSelector brightnessSelector;

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);

		stop();

		setContentView(R.layout.preferences);
		((Button) findViewById(R.id.ReenableButton))
				.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						save();
						stop();
						start();
					}
				});

		brightnessSelector = ((BrightnessSelector) findViewById(R.id.PreferencesBrightnessSelector));
		brightnessSelector.setBrightness(Preferences.getTransparency(this));
	}

	private void save() {
		Preferences
				.getSharedPreferences(this)
				.edit()
				.putInt(PREFERENCE_TRANSPARENCY,
						this.brightnessSelector.getBrightness())
				.putInt(PREFERENCE_COLOR, getColor(this)).commit();
	}

	private void stop() {
		stopService(new Intent(Preferences.this, FilterService.class));
	}

	private void start() {
		startService(new Intent(Preferences.this, FilterService.class));
	}

	protected void onPause() {
		super.onPause();

		save();
		stop();
		start();
	}
}
