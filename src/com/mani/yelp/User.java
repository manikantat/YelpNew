package com.mani.yelp;

public class User extends attributeType {

	int reviewCount;
	
	public void setValues(String[] populatedObject, String testTrain)
	{
		this.userID = populatedObject[0];
		this.starRating = Double.parseDouble(populatedObject[1]);
		this.reviewCount = Integer.parseInt(populatedObject[2]);
		
		if(testTrain.equals("train"))
		{
			this.usefulCount = Integer.parseInt(populatedObject[3]);
			this.funnyCount = Integer.parseInt(populatedObject[4]);
			this.coolCount = Integer.parseInt(populatedObject[5]);
			this.totalNumberofVotes = this.coolCount +  this.funnyCount + this.usefulCount;
		}
		
		setRatingClass();		
	}
	
}
