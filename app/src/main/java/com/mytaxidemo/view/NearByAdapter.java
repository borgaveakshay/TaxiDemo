package com.mytaxidemo.view;


import com.mytaxidemo.databinding.RecyclerViewModelBinding;
import com.mytaxidemo.viewmodel.RecyclerViewModel;

import java.util.List;

public class NearByAdapter extends CustomRecyclerView<RecyclerViewModel, RecyclerViewModelBinding> {


    public NearByAdapter(List<RecyclerViewModel> listItems, int layoutId){
        super(listItems, layoutId);
    }

    @Override
    void onBindView(RecyclerViewModelBinding stateBinding, int position) {
        stateBinding.setListItem(getDataList().get(position));
    }

}
