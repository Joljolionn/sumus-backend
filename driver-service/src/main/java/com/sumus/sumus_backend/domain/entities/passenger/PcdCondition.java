package com.sumus.sumus_backend.domain.entities.passenger;

import org.bson.types.ObjectId;

public class PcdCondition {

    private String necessity;

    private ObjectId document;
    
    private ValidationStatus validationStatus;

    public enum ValidationStatus {
        PENDENTE, APROVADO, REJEITADO
    }

    public PcdCondition(){}

    public PcdCondition(String necessity){
        this.necessity = necessity;
        this.validationStatus = ValidationStatus.PENDENTE;
    }

	public String getNecessity() {
		return necessity;
	}

	public void setNecessity(String necessity) {
		this.necessity = necessity;
	}

	public ObjectId getDocument() {
		return document;
	}

	public void setDocument(ObjectId document) {
		this.document = document;
	}

	public ValidationStatus getValidationStatus() {
		return validationStatus;
	}

	public void setValidationStatus(ValidationStatus validationStatus) {
		this.validationStatus = validationStatus;
	}
}
