package at.tomtasche.bestscreenfilterever;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

public class FilterUtil {

	public static int computeAlpha(int transparency) {
		return (int) Math.round(255.0D * Math
				.exp(4.0D * (transparency / 100.0D) - 4.0D));
	}

	public static double computePercentage(int transparency) {
		return 100.0D * computeAlpha(transparency) / 255.0D;
	}

	public static int invertAlpha(int paramInt) {
		return (int) Math.max(0L, Math.min(100L, Math
				.round(100.0D * (1.0D + Math.log(paramInt / 255.0D) / 4.0D))));
	}

	public static int getBrightness(Context context) {
		int transparency = Preferences.getTransparency(context);
		return FilterUtil.invertAlpha(Math
				.round(255.0F * (transparency / 100.0F)));
	}

	public static Drawable getBackgroundDrawable(Context context,
			int transparency) {
		int color = Preferences.getColor(context);
		int i = 255 - FilterUtil.computeAlpha(transparency);
		return new ColorDrawable(Color.argb(i, Color.red(color),
				Color.green(color), Color.blue(color)));
	}

	public static Drawable getBackgroundDrawable(Context context) {
		return getBackgroundDrawable(context,
				Preferences.getTransparency(context));
	}
}
