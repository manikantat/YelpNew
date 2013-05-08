package com.mani.yelp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

public class CreateDictionary {
	
	int maxUsefulnessCount = Integer.MIN_VALUE;
	int minUsefulnessCount = Integer.MAX_VALUE;
	
	public class DualInteger{
		int sumUsefulVotes;
		int wordRepitationCount;
		//int usefulReviewCount;
		int sumUsefulnessPercent;
		DualInteger(int s,int c, int u){
			sumUsefulVotes= s;
			wordRepitationCount =c;
			//usefulReviewCount = u;
			sumUsefulnessPercent = u;
		}
		public String toString(){
			String str = sumUsefulVotes+"\t"+wordRepitationCount;
			return str;
		}
	}
	
	ArrayList<attributeType> trainingObjects = new ArrayList<attributeType>();
	
	HashSet<String> dictionary = new HashSet<String>();
	static HashMap<String, DualInteger> dictionary2 =  new HashMap<>();
	
	public CreateDictionary(ArrayList<attributeType> traindata)
	{
		this.trainingObjects = traindata;
	}
	
	public void populateDictionary()
	{
		Iterator<attributeType> itr = this.trainingObjects.iterator();
		
		while(itr.hasNext())
		{
			Review rw = (Review) itr.next();
			String text = rw.reviewText;
			int usefulvotes = rw.usefulCount;
			String[] splitText = text.split(" ");
			int i=0;
			while(i<splitText.length)
			{
				String word = splitText[i].toLowerCase().trim().replaceAll("\\W+", "");
				word = word.replaceAll("[0-9]*", "");
				
				if(word.length() >3 && word.length() <15)
					if(dictionary2.containsKey(word)){
						DualInteger di = dictionary2.get(word);
						di.sumUsefulVotes += usefulvotes;
						di.wordRepitationCount ++;
						di.sumUsefulnessPercent += rw.usefulnessPercentage;
						/*if(rw.usefulOrNot)
							di.usefulReviewCount++;
						else
							di.usefulReviewCount--;
						if(di.usefulReviewCount > maxUsefulnessCount)
							maxUsefulnessCount = di.usefulReviewCount;
						if(di.usefulReviewCount < minUsefulnessCount)
							minUsefulnessCount = di.usefulReviewCount;*/
					}else{
						//if(rw.usefulOrNot)						
							dictionary2.put(word, new DualInteger(usefulvotes,1,rw.usefulnessPercentage));
						//else
							//dictionary2.put(word, new DualInteger(usefulvotes,1,-1));
					}
				i++;
			}
		}
		System.out.println("maxc "+maxUsefulnessCount+" min cnt "+minUsefulnessCount);
	}
	public void makeDictionaryFile() throws IOException
	{
		this.populateDictionary();
		
		File dictFile = new File("dictionary.arff");
		File dictFile1 = new File("dictionary1.arff");
		File originalFile = new File("original.arff");
		FileWriter writeDict = new FileWriter(dictFile);
		FileWriter writeDict1 = new FileWriter(dictFile1);
		FileWriter OriginalDict = new FileWriter(originalFile);
		//Iterator<String> dictItr = dictionary.iterator();
		//while(dictItr.hasNext())
			//writeDict.write(dictItr.next()+"\n");
		
		String line = "% Title: Dictionary database. \n"+
				  "% Source: Yelp.com Data. \n" +
				  "% Authors: Manikanta Talanki & Kaushik Sirineni. \n\n"+
				  "@RELATION dictionary1 \n";
		writeDict.write(line);
		
		line =    // "@ATTRIBUTE word String \n" +
				   "@ATTRIBUTE usefulVotesPercent NUMERIC\n\n"+
				   "@DATA \n";
		
		writeDict.write(line);
		
		line = "% Title: Dictionary database. \n"+
				  "% Source: Yelp.com Data. \n" +
				  "% Authors: Manikanta Talanki & Kaushik Sirineni. \n\n"+
				  "@RELATION dictionary2 \n";
		writeDict1.write(line);
		
		line =     //"@ATTRIBUTE word String \n" +
				   "@ATTRIBUTE usefulnessPercent NUMERIC\n\n"+
				   "@DATA \n";
		
		writeDict1.write(line);
		line = "% Title: Dictionary database. \n"+
				  "% Source: Yelp.com Data. \n" +
				  "% Authors: Manikanta Talanki & Kaushik Sirineni. \n\n"+
				  "@RELATION dictionary original \n";
		OriginalDict.write(line);
		
		line =     "@ATTRIBUTE word String \n" +
				   "@ATTRIBUTE usefulVotesPercent NUMERIC\n"+
				   "@ATTRIBUTE usefulnessPercent NUMERIC\n\n"+
				   "@DATA \n";
		OriginalDict.write(line);
		
		
		for (Entry<String, DualInteger> entry : dictionary2.entrySet()) {
			if(entry.getValue().wordRepitationCount >30)
			{
				OriginalDict.write(entry.getKey()+"\n");
				writeDict.write(entry.getValue().sumUsefulVotes/(float)entry.getValue().wordRepitationCount+"\n" );
				writeDict1.write(entry.getValue().sumUsefulnessPercent/(float)entry.getValue().wordRepitationCount+"\n" );
			}
		}
		OriginalDict.close();
		writeDict.close();
		writeDict1.close();
	}
	public static HashMap<String, Integer> reviewTextWords(String text)
	{
		HashMap<String, Integer> reviewWords = new HashMap<String, Integer>();
		
		String[] splitText = text.split(" ");
		int i=0;
		while(i<splitText.length)
		{
		String word = splitText[i].toLowerCase().trim().replaceAll("\\W+", "");
		word = word.replaceAll("[0-9]*", "");
		int count=0;
		if(dictionary2.containsKey(word))
			count = dictionary2.get(word).wordRepitationCount;
		if(count!=0)
			reviewWords.put(word, new Integer(count));
		i++;
		}
		
		return reviewWords;
	}
	public static HashMap<String, DualInteger> getDictionary()
	{
		return dictionary2;
	}
	
	public int normalize(int max,int min,int value,int scaleRange){
		return (int)scaleRange*(value-min)/(max-min);
	}
	
}
