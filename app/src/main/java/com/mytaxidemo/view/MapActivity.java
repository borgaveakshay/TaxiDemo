package com.mytaxidemo.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mytaxidemo.R;
import com.mytaxidemo.databinding.ActivityMainBinding;
import com.mytaxidemo.viewmodel.RecyclerViewModel;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity {

    ActivityMainBinding mActivityMainBinding;
    List<RecyclerViewModel> mNearByTaxis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setNearByTaxiList();
        setAdapter();



    }


    private void setNearByTaxiList() {

        mNearByTaxis = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            RecyclerViewModel poiListItem = new RecyclerViewModel();
            if (i == 4 || i == 5 || i == 7) {
                poiListItem.setFleetType("POOLING");
            }
            poiListItem.setHeading((i + 1) * 25.66);

            mNearByTaxis.add(poiListItem);
        }

    }

    private void setAdapter() {


        NearByAdapter nearByAdapter = new NearByAdapter(mNearByTaxis, R.layout.recycler_view_item);

        mActivityMainBinding.recycleView.setAdapter(nearByAdapter);
    }
}
