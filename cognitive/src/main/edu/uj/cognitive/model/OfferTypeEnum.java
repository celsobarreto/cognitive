package edu.uj.cognitive.model;

public enum OfferTypeEnum {
	JOB("job"),
	SERVICE("service");
	
	private String offerType;
	
	OfferTypeEnum(String offerType){
		this.offerType = offerType;
	}
	
	public String getOfferType(){
		return offerType;
	}
	
	public void setOfferType(String offerType){
		this.offerType = offerType;
	}
}
