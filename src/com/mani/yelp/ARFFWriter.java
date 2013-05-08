package com.mani.yelp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;


public class ARFFWriter {
	
	 ArrayListMultimap<String, User> uMap;
	 ArrayListMultimap<String, Review> rMap;
	
	
	public ARFFWriter(ArrayListMultimap<String, User> user, ArrayListMultimap<String, Review> review)
	{
		this.uMap = user;
		this.rMap = review;
		
	}
	
	public void write() throws IOException
	{
		System.out.println("Writing ARFF files.. :)");
		File fileName = new File("trainAttributes.arff");
		if(fileName.exists())
		{
			fileName = new File("testAttributes.arff");
		}
		FileWriter file = new FileWriter(fileName);
						
		String line = "% Title: User database. \n"+
				  "% Source: Yelp.com Data. \n" +
				  "% Authors: Manikanta Talanki & Kaushik Sirineni. \n\n"+
				  "@RELATION userdb \n";
		file.write(line);
	
		// reviewUsefullness class Attribute.
		
		line =     "@ATTRIBUTE userStarRating NUMERIC \n" +
				   "@ATTRIBUTE userReviewCount NUMERIC\n"+
		    	   "@ATTRIBUTE reviewStarRating {1,2,3,4,5}\n"+
			  // Add text attribute...
				   "@ATTRIBUTE reviewUsefulness  {A,B,C,D,E} \n"+
				   "@DATA \n";
		
		file.write(line);
		line="";
		
		Set<String> uSet = uMap.keySet();
		
		Iterator<String> uItr = uSet.iterator();
		
		while(uItr.hasNext())
		{
			String userId = uItr.next();
			
			List<User> uList = uMap.get(userId);
			List<Review> rList = rMap.get(userId);
			
			for(int i=0; i<uList.size(); i++)
			{
				User u = uList.get(i);
				for(int j=0; j<rList.size(); j++)
				{
					Review r = rList.get(j);
					double userStarRating = u.starRating;
					int userReviewCount = u.reviewCount;
					double reviewStarRatin = r.starRating;
					char reviewUsefulness = r.usefulnessClass;
					line +=userStarRating +","+userReviewCount+","+reviewStarRatin+","+reviewUsefulness+"\n";
					file.write(line);
					line="";
					
				}
			}
		}
		file.close();
	}
}
