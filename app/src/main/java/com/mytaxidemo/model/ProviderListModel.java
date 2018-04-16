package com.mytaxidemo.model;

import java.util.List;

public class ProviderListModel{
	private List<PoiListItem> poiList;

	public void setPoiList(List<PoiListItem> poiList){
		this.poiList = poiList;
	}

	public List<PoiListItem> getPoiList(){
		return poiList;
	}

	@Override
 	public String toString(){
		return 
			"ProviderListModel{" + 
			"poiList = '" + poiList + '\'' + 
			"}";
		}
}