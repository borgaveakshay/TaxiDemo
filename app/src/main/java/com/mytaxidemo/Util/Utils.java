package com.mytaxidemo.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

@SuppressWarnings("ALL")
public class Utils {

    /**
     *
     * @param fleetType
     * @return
     * Method verifies fleet type
     */
    public static boolean isFleetTypePooling(String fleetType) {

        return "POOLING".equals(fleetType);
    }

    public static String getStringValue(double value) {

        return String.valueOf(value);
    }

    /**
     *
      * @param context
     * @param resourceId
     * @param height
     * @param width
     * @return
     * Method returns bitmap with given dimmenssion from PNG image
     */
   public static Bitmap getSmallIcon(Context context, int resourceId, int height, int width){

       BitmapDrawable bitmapdraw=(BitmapDrawable)context.getResources().getDrawable(resourceId);
       Bitmap b = bitmapdraw.getBitmap();
       return Bitmap.createScaledBitmap(b, width, height, false);
   }

}
