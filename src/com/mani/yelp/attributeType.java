package com.mani.yelp;

public class attributeType {
	
	String userID;
	double starRating;
	int usefulCount =0;
	int funnyCount=0;
	int coolCount=0;
	int totalNumberofVotes;
	
	public void setRatingClass()
	{
		this.starRating = Math.ceil(this.starRating);	
	}

}
