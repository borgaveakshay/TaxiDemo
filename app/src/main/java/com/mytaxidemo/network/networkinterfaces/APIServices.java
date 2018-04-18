package com.mytaxidemo.network.networkinterfaces;

import com.mytaxidemo.model.PoiListModel;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIServices {

    @GET("/")
    Observable<PoiListModel> getNearByTaxis(@Query("p1Lat") double lat1, @Query("p1Lon") double lon1, @Query("p2Lat") double lat2, @Query("p2Lon") double lon2);
}
