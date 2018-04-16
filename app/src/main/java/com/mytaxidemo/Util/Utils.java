package com.mytaxidemo.Util;

public class Utils {

    public static boolean isFleetTypePooling(String fleetType){

        return "POOLING".equals(fleetType);
    }

    public static String getStringValue(double value){

        return String.valueOf(value);
    }
}
