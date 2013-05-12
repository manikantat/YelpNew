package com.mani.yelp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class DictionaryReader {

	String filename;
	class TwoInt{
		int a=0;
		int b=0;
		public TwoInt(int x,int y) {
			a = x;
			b = y;
		}
	}
	BufferedReader br = null;
	static HashMap<String, TwoInt> dict =  new HashMap<String, TwoInt>();
	
	public DictionaryReader(String fname) {
		filename = fname;
		try {
			 System.out.println("understanding dictionary....");
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader(filename));
 
			int countoflines = 0;
			//while ((sCurrentLine = br.readLine()) != null && countoflines < 10){	
			//}
			while ((sCurrentLine = br.readLine()) != null) {
				String wordscores[] = sCurrentLine.split(",");
				if(wordscores.length != 3) continue;
				int p = convertLetter(wordscores[1]);
				int q = convertLetter(wordscores[2]);
				if(p >5 || p<1) p = 0;
				if(q >5 || q<1) q = 0;
				dict.put(wordscores[0], new TwoInt(p, q));
			}
			System.out.println("Number of words in dictionary : "+dict.size()+"\nSample data:");
			Set<String> set = dict.keySet();
			Iterator<String> it = set.iterator();
			for(int i=0;i< dict.size()&&i<5;i++){
				String s = (String) it.next();
				System.out.print(i+":"+s+","+dict.get(s).a+"|  ");
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if (br != null)br.close();
				System.out.println("\nDictionary Successful....");
				
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
	}
	
	int getScore1(String text){
		String textWords[] = text.split("\\s");
		int score = 0,count=1;
		for (String word : textWords) {
			TwoInt ti = dict.get(word);
			if(ti == null) continue;
			score += ti.a;
			count++;
		}
		return score/count;
	}
	
	int getScore2(String text){
		String textWords[] = text.split("\\s");
		int score = 0;
		int count = 1;
		for (String word : textWords) {
			TwoInt ti = dict.get(word);
			if(ti == null) continue;
			score += ti.b;
			count++;
		}
		return score/count;
	}
	
	private int convertLetter(String s){
		switch (s) {
		case "A":
			return 5;
		case "B":
			return 4;
		case "C":
			return 3;
		case "D":
			return 2;
		case "E":
			return 1;
		default:
			return 0;
		}
	}
	
}
