package com.mytaxidemo.model;

@SuppressWarnings("unused")
public class PoiListItem{
	private Coordinate coordinate;
	private String fleetType;
	private double heading;
	private int id;

	@SuppressWarnings("unused")
	public void setCoordinate(Coordinate coordinate){
		this.coordinate = coordinate;
	}

	public Coordinate getCoordinate(){
		return coordinate;
	}

	public void setFleetType(String fleetType){
		this.fleetType = fleetType;
	}

	public String getFleetType(){
		return fleetType;
	}

	public void setHeading(double heading){
		this.heading = heading;
	}

	public double getHeading(){
		return heading;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}


	@Override
 	public String toString(){
		return 
			"PoiListItem{" + 
			"coordinate = '" + coordinate + '\'' + 
			",fleetType = '" + fleetType + '\'' + 
			",heading = '" + heading + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}

}
