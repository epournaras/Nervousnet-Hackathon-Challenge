package challenge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Analyzer {
	
	//private HashMap<Integer, HashMap<Integer, HashMap<Integer, Double>>> map;	
	
	private static final String[] paths = {"ECBT/File1.txt", "ECBT/File2.txt", "ECBT/File3.txt", 
										   "ECBT/File4.txt", "ECBT/File5.txt", "ECBT/File6.txt"};
	
	public Analyzer() {
		//this.map = new HashMap<Integer, HashMap<Integer, HashMap<Integer, Double>>>();
	}
	
	public static void main(String[] args) {
		Analyzer analyser = new Analyzer();
		analyser.analyze();
	}
	
	public void analyze() {
		this.load();
	}
	
	private void load() {
		Counter counter = new Counter();
		for(String path : paths) {
			RawReader reader = new RawReader();
			System.out.println("Reading file " + path);
			reader.setPath(path);
			reader.setSkipFirstLine(false);
			reader.openFile();
			reader.readFile();
			reader.closeFile();
			HashMap<Integer, HashMap<Integer, HashMap<Integer, Double>>> rmap = reader.exportMap();			
			System.out.println("Counting..");
			counter.action(rmap);
			//this.merge(rmap);
		}
		System.out.println("Loading files finished.");
		counter.availableUsers();
		counter.print();
	}
	
//	private void merge(HashMap<Integer, HashMap<Integer, HashMap<Integer, Double>>> rmap) {
//		for(Integer user : this.getSortedUsers(rmap)) {
//			for(Integer day : this.getSortedDays(rmap, user)) {
//				for(Integer time : this.getSortedTimes(rmap, user, day)) {
//					Double raw = this.get(rmap, user, day, time);
//					this.add(user, day, time, raw);
//				}
//			}
//		}
//	}
	
//	private boolean add(Integer user, Integer day, Integer time, Double raw) {
//		if(!this.map.containsKey(user)) {
//			this.map.put(user, new HashMap<Integer, HashMap<Integer, Double>>());
//		}
//		if(!this.map.get(user).containsKey(day)) {
//			this.map.get(user).put(day, new HashMap<Integer, Double>());
//		}
//		if(this.map.get(user).get(day).containsKey(time)) {
//			System.err.println("Hash map already contains value for user " + user + " day " + day + " time " + time + ".");
//			return false;
//		}
//		this.map.get(user).get(day).put(time, raw);
//		return true;
//	}
	
	private Double get(HashMap<Integer, HashMap<Integer, HashMap<Integer, Double>>> map, Integer user, Integer day, Integer time) {
		if(!map.containsKey(user)) {
			return null;
		}
		if(!map.get(user).containsKey(day)) {
			return null;
		}
		if(!map.get(user).get(day).containsKey(time)) {
			return null;
		}
		return map.get(user).get(day).get(time);
	}
	
	private ArrayList<Integer> getSortedUsers(HashMap<Integer, HashMap<Integer, HashMap<Integer, Double>>> map) {
		ArrayList<Integer> list = new ArrayList<Integer>(map.keySet());
		Collections.sort(list);
		return list;
	}
	
	private ArrayList<Integer> getSortedDays(HashMap<Integer, HashMap<Integer, HashMap<Integer, Double>>> map, Integer user) {
		if(!map.containsKey(user)) {
			return new ArrayList<Integer>();
		}
		ArrayList<Integer> list = new ArrayList<Integer>(map.get(user).keySet());
		Collections.sort(list);
		return list;
	}
	
	private ArrayList<Integer> getSortedTimes(HashMap<Integer, HashMap<Integer, HashMap<Integer, Double>>> map, Integer user, Integer day) {
		if(!map.containsKey(user)) {
			return new ArrayList<Integer>();
		}
		if(!map.get(user).containsKey(day)) {
			return new ArrayList<Integer>();
		}
		ArrayList<Integer> list = new ArrayList<Integer>(map.get(user).get(day).keySet());
		Collections.sort(list);
		return list;
	}

}
