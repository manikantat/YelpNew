package com.mani.yelp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.common.collect.ArrayListMultimap;

public class ControllerClass {

	/**
	 * @param args
	 */
	static String trainingPath;
	static String testPath;
	static ArrayListMultimap<String, User> userMap = ArrayListMultimap.create();
	static ArrayListMultimap<String, Review> reviewMap = ArrayListMultimap.create();
	
	public static void main(String[] args) {
			
		try
		{
		 trainingPath = args[0];
		 testPath = args[1];
		
		 
		 // Setting up the Paths
		 
		 String reviewPath = trainingPath + "\\yelp_training_set_review.json";
		 String userPath = trainingPath + "\\yelp_training_set_user.json";
		 String reviewPathTest = testPath + "\\yelp_test_set_review.json";
		 String userPathTest = testPath + "\\yelp_test_set_user.json";
		 
		 // Grabbing from JSONs
		 
		 JsonGrabber jgrabber = new JsonGrabber(reviewPath, "review");
		 JsonGrabber userGrabber = new JsonGrabber(userPath, "reviewer");
		 JsonGrabber jgrabberTest = new JsonGrabber(reviewPathTest, "review");
		 JsonGrabber userGrabberTest = new JsonGrabber(userPathTest, "reviewer");
		
		 ArrayList<attributeType> trainingReviewInstances = new ArrayList<attributeType>();
		 ArrayList<attributeType> trainingReviewerInstances = new ArrayList<attributeType>();
		 
		 // Populating the TRAINING review and User objects with the data from grabber.
		 System.out.println("Preparing Training Review objects...");
		 trainingReviewInstances = jgrabber.populateJson("train");
		
		 System.out.println("Preparing Training Reviewer objects...");
 	 	 trainingReviewerInstances = userGrabber.populateJson("train");
 	 	 
 	 	 // Populating the multimaps for TRAINING user and review
 	 	 System.out.println("populating reviewer instances..");
		 populateMap(trainingReviewerInstances);
		 System.out.println("populating review instances..");
		 populateMap(trainingReviewInstances);
 	 	 
		 System.out.println("Creating the Dictionary file...");
		 CreateDictionary dict = new CreateDictionary(trainingReviewInstances);
		 dict.makeDictionaryFile();		 
		 
		 // Joining User and review with user_id as join attribute, and writing ARFF files.
 	 	 System.out.println("writing train Arff files..");
		 ARFFWriter writer = new ARFFWriter(userMap, reviewMap);
		 writer.write();
		 userMap.clear();
		 reviewMap.clear();
		 // Creating TEST objects.
 	 	 ArrayList<attributeType> testReviewInstances = new ArrayList<attributeType>();
		 ArrayList<attributeType> testReviewerInstances = new ArrayList<attributeType>();
		 
		 // Grabbing Test attributes from the TRAINING Json Grabbers
		 System.out.println("Preparing Test Review objects...");
		 testReviewInstances = jgrabberTest.populateJson("test");
		
		 System.out.println("Preparing Test Reviewer objects...");
	 	 testReviewerInstances = userGrabberTest.populateJson("test");
	 	 
	 	 // Populatinght Multimaps from the TEST objects.
	 	 System.out.println("populating reviewer instances..");
		 populateMap(testReviewerInstances);
		 System.out.println("populating review instances..");
		 populateMap(testReviewInstances);
	 	 
		 //Joining the User and Review with user_id as the joining attribute and writting it into ARFF files.
		 
	 	 System.out.println("writing train Arff files..");
		 writer = new ARFFWriter(userMap, reviewMap);
		 writer.write();
		 
		 
		 ClusteringClass cc = new ClusteringClass();
		 cc.generateCluster("dictionary.arff");
		 cc.generateCluster("Dictionary1.arff");
		 
		 
		 
	
		 System.out.println("Files Created.");

		
		}
		catch(FileNotFoundException fne)
		{
			fne.printStackTrace();			
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }
		
		

	}
	private static void populateMap(
			ArrayList<attributeType> trainingInstances) {
		
		Iterator<attributeType> itr = trainingInstances.iterator();
		
		while(itr.hasNext())
		{
			attributeType att = itr.next();
			if(att instanceof User)
			{
				User user = (User) att;
				userMap.put(user.userID, user);
			}
			else
			{
				Review rw = (Review) att;
				reviewMap.put(rw.userID, rw);
			}
		}
		
		
	}

}
