package com.mytaxidemo.model;

import java.util.List;



@SuppressWarnings("unused")
public class PoiListModel{


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
			"PoiListModel{" + 
			"poiList = '" + poiList + '\'' + 
			"}";
		}
}