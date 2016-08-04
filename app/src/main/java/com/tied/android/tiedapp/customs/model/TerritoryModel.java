package com.tied.android.tiedapp.customs.model;

public class TerritoryModel {
	
	String territory_name;
	boolean iNew;

	public boolean isiNew() {
		return iNew;
	}
	public void setiNew(boolean iNew) {
		this.iNew = iNew;
	}

	public String getTerritory_name() {
		return territory_name;
	}

	public void setTerritory_name(String territory_name) {
		this.territory_name = territory_name;
	}

	@Override
	public String toString() {
		return "TerritoryModel{" +
				"territory_name='" + territory_name + '\'' +
				", iNew=" + iNew +
				'}';
	}
}
