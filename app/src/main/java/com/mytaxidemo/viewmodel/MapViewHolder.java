package com.mytaxidemo.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
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

@SuppressWarnings("ALL")
public class MapViewHolder {

    private static final String TAG = "MapViewModel";
    public ObservableBoolean isLoading = new ObservableBoolean();
    public ObservableBoolean isSwipeToRefreshEnabled = new ObservableBoolean();
    private Observable<PoiListModel> mNearByTaxis;
    private MutableLiveData<List<RecyclerViewModel>> mNearByTaxiList = new MutableLiveData<>();

    public MutableLiveData<List<RecyclerViewModel>> getNearByTaxiList() {
        return mNearByTaxiList;
    }


    public void setIsSwipeToRefreshEnabled(boolean isSwipeToRefreshEnabled) {
        this.isSwipeToRefreshEnabled.set(isSwipeToRefreshEnabled);
    }


    public void onSwipeRefreshListener() {

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
                                recyclerViewModel.setCoordinate(poiListItem.getCoordinate());
                                recyclerViewModelList.add(recyclerViewModel);

                            }
                            mNearByTaxiList.postValue(recyclerViewModelList);
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
