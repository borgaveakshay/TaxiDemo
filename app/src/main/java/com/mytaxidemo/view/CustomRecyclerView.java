package com.mytaxidemo.view;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class CustomRecyclerView<T, E extends ViewDataBinding> extends RecyclerView.Adapter<CustomRecyclerView.CustomView> {

    private List<T> mDataList;
    private LayoutInflater mLayoutInflater;
    private int mLayoutId;
    private OnItemClickListener<T> mOnItemClickListener;

    public OnItemClickListener<T> getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener<T> mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public List<T> getDataList() {
        return mDataList;
    }


    public CustomRecyclerView(List<T> dataList, int layoutId) {

        mDataList = dataList;
        mLayoutId = layoutId;
    }

    @Override
    public CustomView onCreateViewHolder(ViewGroup parent, int viewType) {

        if (mLayoutInflater == null) {

            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }

        E mDataBindingItem = DataBindingUtil.inflate(mLayoutInflater, mLayoutId, parent, false);


        return new CustomView(mDataBindingItem);
    }

    @Override
    public void onBindViewHolder(CustomRecyclerView.CustomView holder, int position) {

        onBindView((E) holder.getDataBinding(), position);
        holder.itemView.setOnClickListener(view ->  {
           if (getOnItemClickListener() != null){
               getOnItemClickListener().onItemClicked(mDataList.get(position));
           }
        });
    }

    @Override
    public int getItemCount() {
        return (mDataList != null && !mDataList.isEmpty()) ? mDataList.size() : 0;
    }

    public class CustomView extends RecyclerView.ViewHolder {

        E mDataBinding;

        private CustomView(E viewBinding) {
            super(viewBinding.getRoot());
            mDataBinding = viewBinding;
        }

        public E getDataBinding() {
            return mDataBinding;
        }

    }


    abstract void onBindView(E stateBinding, int position);

}
