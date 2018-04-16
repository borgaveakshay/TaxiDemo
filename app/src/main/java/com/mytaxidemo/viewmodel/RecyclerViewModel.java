package com.mytaxidemo.viewmodel;

import android.annotation.SuppressLint;
import android.databinding.BindingAdapter;
import android.support.annotation.IdRes;
import android.widget.ImageView;

public class RecyclerViewModel {
    private String fleetType;
    private double heading;

    public String getFleetType() {
        return fleetType;
    }

    public void setFleetType(String fleetType) {
        this.fleetType = fleetType;
    }

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    @SuppressLint("ResourceType")
    @BindingAdapter("android:src")
    public static void setImage(ImageView view, @IdRes int resourceId){
        view.setImageResource(resourceId);
    }


}
