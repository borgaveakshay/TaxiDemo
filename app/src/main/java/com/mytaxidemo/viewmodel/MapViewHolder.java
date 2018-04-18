package com.mytaxidemo.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mytaxidemo.model.PoiListItem;
import com.mytaxidemo.model.PoiListModel;
import com.mytaxidemo.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MapViewHolder {

    private static final String TAG = "MapViewModel";
    public MutableLiveData<List<LatLng>> mMapLatLng = new MutableLiveData<>();
    public ObservableBoolean isLoading = new ObservableBoolean();
    public Observable<PoiListModel> mNearByTaxis;
    public MutableLiveData<List<RecyclerViewModel>> mNearByTaxiList = new MutableLiveData<>();

    public MutableLiveData<List<RecyclerViewModel>> getNearByTaxiList() {
        return mNearByTaxiList;
    }

    public MutableLiveData<List<LatLng>> getMapLatLng() {
        return mMapLatLng;
    }

    @BindingAdapter("app:init")
    public static void initMap(MapView mapView, final List<LatLng> latLng) {

        if (mapView != null) {
            mapView.onCreate(new Bundle());
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {

                    if (latLng != null && !latLng.isEmpty()) {
                        for (LatLng latLong : latLng) {
                            googleMap.addMarker(new MarkerOptions().position(latLong));
                        }
//                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng1));
                    }
                }
            });

        }

    }

    public void onSwipeRefreshListener() {

        if (mMapLatLng.getValue() != null && !mMapLatLng.getValue().isEmpty()) {
            mMapLatLng.setValue(new ArrayList<>());
        }
        if (mNearByTaxiList.getValue() != null && !mNearByTaxiList.getValue().isEmpty()) {
            mNearByTaxiList.setValue(new ArrayList<>());
        }
        isLoading.set(true);
        mNearByTaxis = RetrofitClient.getAPIService().getNearByTaxis(53.694865,
                9.757589, 53.394655, 10.099891);

        mNearByTaxis.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PoiListModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "subscription successful: " + d.isDisposed());
                    }

                    @Override
                    public void onNext(PoiListModel poiListModel) {
                        isLoading.set(false);
                        if (poiListModel != null
                                && poiListModel.getPoiList() != null
                                && !poiListModel.getPoiList().isEmpty()) {
                            List<LatLng> latLngList = new ArrayList<>();
                            List<RecyclerViewModel> recyclerViewModelList = new ArrayList<>();
                            for (PoiListItem poiListItem : poiListModel.getPoiList()) {

                                if (poiListItem.getCoordinate() != null) {
                                    double latitude = poiListItem.getCoordinate().getLatitude();
                                    double longitude = poiListItem.getCoordinate().getLongitude();
                                    LatLng latLng = new LatLng(latitude, longitude);
                                    latLngList.add(latLng);
                                }

                                RecyclerViewModel recyclerViewModel = new RecyclerViewModel();
                                recyclerViewModel.setFleetType(poiListItem.getFleetType());
                                recyclerViewModel.setHeading(poiListItem.getHeading());
                                recyclerViewModelList.add(recyclerViewModel);

                            }
                            mNearByTaxiList.postValue(recyclerViewModelList);
                            mMapLatLng.postValue(latLngList);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "On error: " + e.getMessage());


                    }

                    @Override
                    public void onComplete() {

                        Log.i(TAG, "Api execution completed: ");

                    }
                });
    }
}
