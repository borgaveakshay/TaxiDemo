package com.mytaxidemo.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

@SuppressWarnings("ALL")
public class Utils {

    public static boolean isFleetTypePooling(String fleetType) {

        return "POOLING".equals(fleetType);
    }

    public static String getStringValue(double value) {

        return String.valueOf(value);
    }

   public static Bitmap getSmallIcon(Context context, int resourceId, int height, int width){

       BitmapDrawable bitmapdraw=(BitmapDrawable)context.getResources().getDrawable(resourceId);
       Bitmap b = bitmapdraw.getBitmap();
       return Bitmap.createScaledBitmap(b, width, height, false);
   }

}
