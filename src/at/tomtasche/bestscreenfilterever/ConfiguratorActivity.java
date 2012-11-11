package at.tomtasche.bestscreenfilterever;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;

public class ConfiguratorActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_configurator);

		((RadioButton) findViewById(R.id.lowRadio))
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							changeTransparency(10);

							changeBrightness(0.1f);
						}
					}
				});
		((RadioButton) findViewById(R.id.middleRadio))
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							changeTransparency(100);

							changeBrightness(0.1f);
						}
					}
				});
		((RadioButton) findViewById(R.id.highRadio))
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							changeTransparency(100);

							changeBrightness(1);
						}
					}
				});
		((RadioButton) findViewById(R.id.festRadio))
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							changeTransparency(-1);
						}
					}
				});
		((RadioButton) findViewById(R.id.birthdayRadio))
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							changeTransparency(-2);
						}
					}
				});
		((RadioButton) findViewById(R.id.androidRadio))
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							changeTransparency(-3);
						}
					}
				});
	}

	private void changeTransparency(int transparency) {
		Preferences.setTransparency(ConfiguratorActivity.this, transparency);

		stop();
		start();
	}

	private void stop() {
		stopService(new Intent(this, FilterService.class));
	}

	private void start() {
		startService(new Intent(this, FilterService.class));
	}

	private void changeBrightness(float percentage) {
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.screenBrightness = percentage;
		getWindow().setAttributes(params);
	}
}
