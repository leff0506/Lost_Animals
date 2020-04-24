package com.example.lost_animals.custom_views;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import androidx.annotation.NonNull;

public class BitmapTransformation {
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int color, int borderDips) {


        Bitmap output = Bitmap.createBitmap(bitmap.getWidth()+2*borderDips,
                bitmap.getHeight()+2*borderDips,
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        canvas.drawColor(Color.TRANSPARENT);

        final RectF rectF = new RectF(0, 0, output.getWidth(), output.getHeight());
        final Paint paint = new Paint();
        // prepare canvas for transfer
        paint.setAntiAlias(true);
        paint.setStrokeWidth((float) borderDips);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawRoundRect(rectF, bitmap.getWidth() + borderDips, bitmap.getWidth()+borderDips, paint);

        canvas.drawBitmap(bitmap, borderDips, borderDips, null);
        bitmap.recycle();
        return output;
    }
    public static Bitmap createRoundedRectBitmap(@NonNull Bitmap bitmap,
                                                  float topLeftCorner, float topRightCorner,
                                                  float bottomRightCorner, float bottomLeftCorner) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = Color.MAGENTA;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        Path path = new Path();
        float[] radii = new float[]{
                topLeftCorner, bottomLeftCorner,
                topRightCorner, topRightCorner,
                bottomRightCorner, bottomRightCorner,
                bottomLeftCorner, bottomLeftCorner
        };

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(color);
        path.addRoundRect(rectF, radii, Path.Direction.CW);
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
