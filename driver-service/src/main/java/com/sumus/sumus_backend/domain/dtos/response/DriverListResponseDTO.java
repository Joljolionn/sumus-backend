package com.sumus.sumus_backend.domain.dtos.response;

import java.util.List;

import com.sumus.sumus_backend.domain.entities.driver.DriverDocument;

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
