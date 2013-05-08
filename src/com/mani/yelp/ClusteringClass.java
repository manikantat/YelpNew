package com.mani.yelp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class ClusteringClass {

	public void generateCluster(String fileName) throws Exception
	{
		
		SimpleKMeans kmeans = new SimpleKMeans();
		
		kmeans.setSeed(10);
		kmeans.setPreserveInstancesOrder(true);
		kmeans.setNumClusters(5);
		
		
		ArffLoader fileLoader = new ArffLoader();
		Instances instances;
		
		
		File usrFile = new File(fileName);
		fileLoader.setFile(usrFile);
		instances = fileLoader.getDataSet();
		kmeans.buildClusterer(instances);
		
		int[] assignments = kmeans.getAssignments();
		
		Instances centroids = kmeans.getClusterCentroids();
			
		writeFile(assignments, fileName, centroids);
	}

	private void writeFile(int[] assignments, String fileName, Instances centroids) throws IOException 
	{
			File oldFile = new File(fileName);
			File clusteredFile = new File(fileName.substring(0, fileName.length()-5)+"Clusterred.arff");
			FileReader fr = new FileReader(oldFile);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(clusteredFile);
			String line = br.readLine();
			while(line != null)
			{
				fw.write(line+"\n");
				line = br.readLine();
				if(!line.equals("") && line.substring(1, line.length()).trim().equalsIgnoreCase("DATA"))
					break;
			}
			String extraAttrib  = "@ATTRIBUTE cluesterClass {A,B,C,D,E}\n\n" ;
			fw.write(extraAttrib);
			fw.write(line+"\n");
			int instanceNumber =0;
			while((line = br.readLine()) !=null)
			{
				char clusterClass;
				switch(assignments[instanceNumber])
				{
				case 0:
					clusterClass = 'E';
					break;
				case 1:
					clusterClass = 'D';
					break;
				case 2:
					clusterClass = 'C';
					break;
				case 3:
					clusterClass = 'B';
					break;
				case 4:
					clusterClass = 'A';
					break;
				default: 
					 clusterClass ='E';
				}
				line += ","+clusterClass+"\n";
				fw.write(line);
				instanceNumber++;
			}
			br.close();
			fw.close();
	}
}
