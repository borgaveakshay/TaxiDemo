package com.mytaxidemo.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mytaxidemo.R;
import com.mytaxidemo.databinding.ActivityMainBinding;
import com.mytaxidemo.viewmodel.MapViewHolder;
import com.mytaxidemo.viewmodel.RecyclerViewModel;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    ActivityMainBinding mActivityMainBinding;
    SupportMapFragment mMapFragment;
    MapViewHolder mMapViewHolder;
    GoogleMap mGoogleMap;
    List<Marker> mMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mMapViewHolder = new MapViewHolder();
        mMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mMapFragment.onCreate(savedInstanceState);
        mMapFragment.getMapAsync(this);
        mActivityMainBinding.setMapViewHolder(mMapViewHolder);

        setUpNearByTaxiDataListener();
        setUpGoogleMapMarkerListener();
    }

    private void setUpNearByTaxiDataListener() {

        mMapViewHolder.getNearByTaxiList().observe(this, this::onTaxiDataAvailable);

    }

    private void setUpGoogleMapMarkerListener() {

        mMapViewHolder.getMapLatLng().observe(this, this::onMapMarkersAvailable);

    }

    public void onTaxiDataAvailable(List<RecyclerViewModel> nearByTaxis) {
        setAdapter(nearByTaxis);
    }

    public void onMapMarkersAvailable(List<LatLng> mMarkerList) {

        if (mGoogleMap != null) {
            mGoogleMap.clear();
            mMarkers = new ArrayList<>();
            for (LatLng latLng : mMarkerList) {
                mMarkers.add(mGoogleMap.addMarker(new MarkerOptions().position(latLng)));
            }
            zoomMapView();
        }
    }

    private void zoomMapView() {

        if (mGoogleMap != null && mMarkers != null && !mMarkers.isEmpty()) {

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : mMarkers) {
                builder.include(marker.getPosition());
            }
            LatLngBounds bounds = builder.build();
            int padding = 300; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mGoogleMap.animateCamera(cu);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        mMapViewHolder.onSwipeRefreshListener();
    }

    private void setAdapter(List<RecyclerViewModel> nearByTaxis) {

        NearByAdapter nearByAdapter = new NearByAdapter(nearByTaxis, R.layout.recycler_view_item);
        mActivityMainBinding.recycleView.setAdapter(nearByAdapter);
    }
}
