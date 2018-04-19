package com.mytaxidemo.viewmodel;

import android.annotation.SuppressLint;
import android.databinding.BindingAdapter;
import android.support.annotation.IdRes;
import android.widget.ImageView;

import com.mytaxidemo.model.Coordinate;

public class RecyclerViewModel {
    private int id;
    private String fleetType;
    private double heading;
    private Coordinate coordinate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

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

    /**
     *
     * @param view
     * @param resourceId
     * This method binds image to view.
     */
    @SuppressLint("ResourceType")
    @BindingAdapter("android:src")
    public static void setImage(ImageView view, @IdRes int resourceId) {
        view.setImageResource(resourceId);
    }


}
