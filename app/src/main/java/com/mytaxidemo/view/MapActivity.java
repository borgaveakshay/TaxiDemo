package com.mytaxidemo.view;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mytaxidemo.R;
import com.mytaxidemo.Util.Utils;
import com.mytaxidemo.databinding.ActivityMainBinding;
import com.mytaxidemo.viewmodel.MapViewHolder;
import com.mytaxidemo.viewmodel.RecyclerViewModel;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, OnItemClickListener<RecyclerViewModel> {

    private ActivityMainBinding mActivityMainBinding;
    private SupportMapFragment mMapFragment;
    private MapViewHolder mMapViewHolder;
    private GoogleMap mGoogleMap;
    private List<Marker> mMarkers;
    private List<RecyclerViewModel> mRecyclerViewModelList;
    private NearByAdapter mNearByAdapter;

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
        mMapViewHolder.setIsSwipeToRefreshEnabled(false);
        mMapViewHolder.setIsPoolingSelected(false);
        setUpNearByTaxiDataListener();
        mRecyclerViewModelList = new ArrayList<>();
        setAdapter();
        setCheckChangeListener();

    }

    /**
     * Intialize obsercer for switch change listener
     */
    private void setCheckChangeListener() {

        mMapViewHolder.getIsPoolingSelected().observe(this, this::onCheckChanged);
    }

    /**
     * Intialize observer for Successful API list callback
     */
    private void setUpNearByTaxiDataListener() {

        mMapViewHolder.getNearByTaxiList().observe(this, this::onTaxiDataAvailable);

    }

    /**
     *
     * @param nearByTaxis
     * Observer callback for API callback
     */

    private void onTaxiDataAvailable(List<RecyclerViewModel> nearByTaxis) {
        mRecyclerViewModelList = new ArrayList<>(nearByTaxis);
        addMarkers();
        if (mNearByAdapter != null) {
            mNearByAdapter.updateItems(mRecyclerViewModelList);
        }

    }

    /**
     *
     * @param isPoolingSelected
     * Observer callback for switch widget
     */
    private void onCheckChanged(boolean isPoolingSelected) {

        if (isPoolingSelected) {
            mMapViewHolder.loadPoolingList();
        } else {
            mMapViewHolder.loadTaxiList();
        }

    }

    /**
     * Method adds markes to the map post for API list
     */
    private void addMarkers() {

        if (mGoogleMap != null
                && mRecyclerViewModelList != null
                && !mRecyclerViewModelList.isEmpty()) {

            mGoogleMap.clear();
            mMarkers = new ArrayList<>();

            for (RecyclerViewModel recyclerViewModel : mRecyclerViewModelList) {
                String fleetType = recyclerViewModel.getFleetType();
                Bitmap bitmap;
                if (Utils.isFleetTypePooling(fleetType)) {
                    bitmap = Utils.getSmallIcon(this, R.drawable.ic_taxi_pool, 70, 70);
                } else {
                    bitmap = Utils.getSmallIcon(this, R.drawable.ic_taxi, 70, 70);
                }
                if (recyclerViewModel.getCoordinate() != null) {
                    mMarkers.add(mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(recyclerViewModel.getCoordinate().getLatitude(), recyclerViewModel.getCoordinate().getLongitude()))
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                            .rotation((float) recyclerViewModel.getHeading())

                    ));
                }
            }

        }
        zoomMapView();
    }

    /**
     * Method zooms map to accomodate all added markers
     */
    private void zoomMapView() {

        if (mGoogleMap != null && mMarkers != null && !mMarkers.isEmpty()) {

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : mMarkers) {
                builder.include(marker.getPosition());
            }
            LatLngBounds bounds = builder.build();
            int padding = 200; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mGoogleMap.animateCamera(cu);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        mMapViewHolder.onSwipeRefreshListener();
    }

    /**
     * method sets rececler view adapter
     */
    private void setAdapter() {
        mNearByAdapter = new NearByAdapter(mRecyclerViewModelList, R.layout.recycler_view_item);
        mNearByAdapter.setOnItemClickListener(this);
        mActivityMainBinding.recycleView.setAdapter(mNearByAdapter);
    }

    /**
     *
     * @param latLng
     * Method zooms perticular marker
     */
    private void zoomToParticularMarker(LatLng latLng) {
        if (mGoogleMap != null) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }

    /**
     * Method gets called when user clicks on one of the recycler item
     * @param item
     */
    @Override
    public void onItemClicked(RecyclerViewModel item) {

        if (item != null && item.getCoordinate() != null) {
            zoomToParticularMarker(new LatLng(item.getCoordinate().getLatitude(), item.getCoordinate().getLongitude()));

        } else {
            Toast.makeText(this, "Please refresh list", Toast.LENGTH_LONG).show();
        }
    }
}
