package com.gzplanet.xposed.xperianavbarbuttons;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class Utils {

	public static int getScreenWidth(Context context) {
		final Display defaultDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		final Point point = new Point();
		defaultDisplay.getSize(point);
		return point.x;

	}

	public static Bitmap decodeImageFromResource(Resources resources, int resId, int reqWidth, int reqHeight) {
		Bitmap bitmap = null;

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(resources, resId, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight, false);
		options.inJustDecodeBounds = false;
		// options.inMutable = true;

		// decode image file to estimated dimension
		bitmap = BitmapFactory.decodeResource(resources, resId, options);

		// scale to requried dimension
		bitmap = Bitmap.createScaledBitmap(bitmap, reqWidth, reqHeight, true);

		return bitmap;
	}

	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight, boolean cropToFit) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (cropToFit)
				inSampleSize = Math.min((int) Math.floor((double) height / (double) reqHeight), (int) Math.floor((double) width / (double) reqWidth));
			else
				inSampleSize = Math.max((int) Math.floor((double) height / (double) reqHeight), (int) Math.floor((double) width / (double) reqWidth));

			// adjust if final image will be smaller than required image
			if (inSampleSize > 0)
				if (width / inSampleSize < reqWidth || height / inSampleSize < reqHeight)
					inSampleSize--;

			// adjust to avoid out of memory error
			final float totalPixels = width * height;
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;
			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap)
				inSampleSize++;
		}
		return inSampleSize;
	}
}