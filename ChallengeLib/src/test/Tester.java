package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


import nervousnet.challenge.Dumper;
import nervousnet.challenge.Loader;
import nervousnet.challenge.exceptions.IllegalHashMapArgumentException;
import nervousnet.challenge.exceptions.MissingDataException;
import nervousnet.challenge.exceptions.MissingFileException;
import nervousnet.challenge.exceptions.NullArgumentException;
import nervousnet.challenge.exceptions.UnsupportedRawFileFormatException;
import nervousnet.challenge.tags.Tags;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class Tester {

	private Loader loader = new Loader();
	private Dumper dumper = new Dumper();
	
	public static final int numOfClusters = 3;

	public Tester() {

	}

	private ArrayList<Double> kMeans(ArrayList<Double> raws, int numOfClusters) throws Exception {
		FastVector atts = new FastVector();
		atts.addElement(new Attribute("NN"));

		Instances kmeans = new Instances("kmeans", atts, 0);		
		for(Double raw : raws) {
			double vals[] = new double[kmeans.numAttributes()];
			vals[0] = raw; 
			kmeans.add(new Instance(1.0, vals));
		}

		SimpleKMeans kMeansAlgo = new SimpleKMeans();
		kMeansAlgo.setSeed(10);		
		kMeansAlgo.setPreserveInstancesOrder(true);
		kMeansAlgo.setNumClusters(numOfClusters);
		kMeansAlgo.buildClusterer(kmeans);
		
		int[] assignments = kMeansAlgo.getAssignments();		
		Instances centroids = kMeansAlgo.getClusterCentroids();
		
		ArrayList<Double> centroidList = new ArrayList<Double>();
		for(int i = 0; i < assignments.length; i++) {
			double rawValue = kmeans.instance(i).value(0);
			double centroidValue = centroids.instance(assignments[i]).value(0);
			centroidList.add(centroidValue);
		}
		
		return centroidList;
	}


	public void analyze() {
		try {
			HashMap<Integer, LinkedHashMap<Integer, LinkedHashMap<Integer, Double>>> rawMap = loader.exportRawValues();
			
			HashMap<Integer, LinkedHashMap<Integer, LinkedHashMap<Integer, Double>>> outputMap = Dumper.initOutputMap();
			System.out.println("Processing data..");
			for(Integer user : loader.getSortedUsers()) {
				for(Integer day : loader.getSortedDays(user)) {
					try {
						ArrayList<Double> rawValues = loader.getSortedRawValues(user, day);
						ArrayList<Double> summarizedValues = this.kMeans(rawValues, numOfClusters);
						
						for(int time = Tags.minTime, i = 0; time <= Tags.maxTime; time++, i++) {
							outputMap.get(user).get(day).put(time, summarizedValues.get(i));
						}
						
					} catch(Exception e) {
						e.printStackTrace();
					}					
				}
			}

			dumper.dump(outputMap);	

		} /*catch (UnsupportedRawFileFormatException e) {
			e.printStackTrace();
		} */catch (IllegalHashMapArgumentException e) {
			e.printStackTrace();
		} catch (NullArgumentException e) {
			e.printStackTrace();
		} catch (MissingDataException e) {
			e.printStackTrace();
		} catch (MissingFileException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Tester tester = new Tester();
		tester.analyze();
	}

}
