package com.sumus.driver.domain.dtos.response;

import java.util.List;

import com.sumus.driver.domain.entities.DriverDocument;


public class DriverListResponseDTO {

    private List<DriverDocument> drivers;
    
    public DriverListResponseDTO(List<DriverDocument> drivers){
        this.drivers = drivers;
    }

	public List<DriverDocument> getDrivers() {
		return drivers;
	}

	public void setDrivers(List<DriverDocument> drivers) {
		this.drivers = drivers;
	}


}
