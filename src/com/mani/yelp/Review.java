package com.mani.yelp;

import java.util.HashMap;


public class Review extends attributeType{

	String reviewText;
	String reviewID;
	char usefulnessClass;
	int usefulnessPercentage;
	
	HashMap<String, Integer> reviewTextWords = new HashMap<String, Integer>();
	
	public void setValues(String[] populatedObject, String testTrain) 
	{	
		this.userID = populatedObject[0];
		this.starRating = Double.parseDouble(populatedObject[1]);
		this.reviewText = populatedObject[2];
		
		if(testTrain.equalsIgnoreCase("train"))
		{
			this.usefulCount = Integer.parseInt(populatedObject[3]);
			this.funnyCount = Integer.parseInt(populatedObject[4]);
			this.coolCount = Integer.parseInt(populatedObject[5]);
			this.totalNumberofVotes = this.coolCount +  this.funnyCount + this.usefulCount;
			usefulnessPercentage = (int) (this.usefulCount/(float)(this.coolCount+this.funnyCount))*100;
			
		}
		
		this.reviewID = populatedObject[6];
		
		setUsefulnessClass(testTrain);
		setRatingClass();
		
		
	}

	private void setUsefulnessClass(String testTrain) {
		
		if(testTrain.equals("train"))
		{
			double percent =0;
			if(this.totalNumberofVotes !=0)
		         percent = (this.usefulCount / this.totalNumberofVotes)*100;
			
		
		if(percent >= 90)
			this.usefulnessClass = 'A';
		else if (percent >=80 && percent <90)
			this.usefulnessClass = 'B';
		else if (percent >=65 && percent< 80)
			this.usefulnessClass = 'C';
		else if(percent >=45 && percent<65)
			this.usefulnessClass = 'D';
		else 
			this.usefulnessClass = 'E';
		}
		else
			this.usefulnessClass = 'A';		
	}
}
