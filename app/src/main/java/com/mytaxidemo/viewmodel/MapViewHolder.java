package com.mytaxidemo.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.mytaxidemo.Util.Utils;
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
    public MutableLiveData<Boolean> isPoolingSelected = new MutableLiveData<>();
    private Observable<PoiListModel> mNearByTaxis;
    private MutableLiveData<List<RecyclerViewModel>> mNearByTaxiList = new MutableLiveData<>();
    private List<RecyclerViewModel> mTaxiLists = new ArrayList<>();
    private List<RecyclerViewModel> mPoolingList = new ArrayList<>();

    public void setIsPoolingSelected(boolean isPoolingSelected) {
        this.isPoolingSelected.postValue(isPoolingSelected);
    }

    public MutableLiveData<Boolean> getIsPoolingSelected() {
        return isPoolingSelected;
    }

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

                            for (PoiListItem poiListItem : poiListModel.getPoiList()) {
                                RecyclerViewModel recyclerViewModel = new RecyclerViewModel();
                                recyclerViewModel.setFleetType(poiListItem.getFleetType());
                                recyclerViewModel.setHeading(poiListItem.getHeading());
                                recyclerViewModel.setCoordinate(poiListItem.getCoordinate());
                                recyclerViewModel.setId(poiListItem.getId());
                                formatList(recyclerViewModel);

                            }
                            mNearByTaxiList.postValue(mTaxiLists);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "On error: " + e.getMessage());


                    }

                    @Override
                    public void onComplete() {

                        isLoading.set(false);
                        Log.i(TAG, "Api execution completed: ");

                    }
                });
    }

    private void formatList(RecyclerViewModel recyclerViewModel) {

        if (recyclerViewModel != null
                && !TextUtils.isEmpty(recyclerViewModel.getFleetType())) {

            if (Utils.isFleetTypePooling(recyclerViewModel.getFleetType()))
                mPoolingList.add(recyclerViewModel);
            else
                mTaxiLists.add(recyclerViewModel);

        }

    }

    public void loadTaxiList() {
        mNearByTaxiList.postValue(mTaxiLists);
    }

    public void loadPoolingList() {
        mNearByTaxiList.postValue(mPoolingList);
    }


    public void onStateChanged() {

        if (isPoolingSelected.getValue() == true) {
            isPoolingSelected.postValue(false);
        } else {
            isPoolingSelected.postValue(true);
        }

        Log.i(TAG, "isPoolingSelected: " + isPoolingSelected.getValue());

    }
}
