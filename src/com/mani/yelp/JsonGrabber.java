package com.mani.yelp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException; 

public class JsonGrabber {
	
	String jsonPath;
	String jsonType;
	
	public JsonGrabber(String path, String type)
	{
		this.jsonPath = path;
		this.jsonType = type.toLowerCase();
	}
	
	
	public ArrayList<attributeType> populateJson(String testTrain) throws FileNotFoundException, IOException, ParseException
	{
		ArrayList<attributeType> objects = new ArrayList<attributeType>();
			
		FileInputStream fstream = new FileInputStream(this.jsonPath);    
		DataInputStream in = new DataInputStream(fstream);  
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		String line;
		while((line = br.readLine()) != null)
		{
			JSONObject json = (JSONObject)new JSONParser().parse(line);
			
			String[] populatedObject = new String[7];
			populatedObject[0] =   json.get("user_id").toString();
			
			if(testTrain.equals("train"))
			{
				JSONObject votes = (JSONObject) json.get("votes");
				populatedObject[3] = votes.get("useful").toString();
				populatedObject[4]=  votes.get("funny").toString();
				populatedObject[5] =  votes.get("cool").toString();
			}
											
			if(json != null && json.get("user_id")!=null)
			{
				if(this.jsonType.equalsIgnoreCase("reviewer"))
				
					{
						populatedObject[1] = json.get("average_stars").toString();
						populatedObject[2] = json.get("review_count").toString();
						User tmpUserObject = new User();

						tmpUserObject.setValues(populatedObject, testTrain);
						
						objects.add(tmpUserObject);
					}
				else if(this.jsonType.equalsIgnoreCase("review"))
					{	
						populatedObject[1] = json.get("stars").toString();
						populatedObject[2] = json.get("text").toString();
						populatedObject[6] = json.get("review_id").toString();
						Review tmpReviewObject = new Review();
						tmpReviewObject.setValues(populatedObject, testTrain);
						objects.add(tmpReviewObject);
					}
				else 
					System.err.println("Object type not found");
		
				}
		}
		br.close();
		return objects;
	}	
}
