package challenge;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Separatista {
	
	private int start1 = 213;
	private int end1 = 240;
	private int start2 = 388;
	private int end2 = 415;
	
	private static final String[] paths = {"ECBT/File1.txt", "ECBT/File2.txt", "ECBT/File3.txt", 
			   							   "ECBT/File4.txt", "ECBT/File5.txt", "ECBT/File6.txt"};
	
	private HashMap<Integer, HashMap<Integer, HashMap<Integer, Double>>> map;			//<User, <Day, <Time, raw value>>>
	
	private ArrayList<Integer> chosenUsers;
	
	public Separatista() {
		this.map = new HashMap<Integer, HashMap<Integer, HashMap<Integer, Double>>>();
	}
	
	private void makeDirectories() {
		File folder = new File("raw/");
		folder.mkdir();
	}
	
	public static void main(String[] args) {
		Separatista sep = new Separatista();
		sep.analyze();
	}
	
	public void analyze() {
		this.makeDirectories();
		this.load();
		this.printOut();
	}
	
	private void load() {
		RandomUserReader rur = new RandomUserReader();
		rur.setPath("randomlyChosenUsers.txt");
		rur.setSkipFirstLine(false);
		rur.openFile();
		rur.readFile();
		rur.closeFile();
		chosenUsers = rur.getChosenUsers();		
		
		for(String path : paths) {
			RawReader reader = new RawReader();
			System.out.println("Reading file " + path);
			reader.setPath(path);
			reader.setSkipFirstLine(false);
			reader.openFile();
			reader.readFile();
			reader.closeFile();
			HashMap<Integer, HashMap<Integer, HashMap<Integer, Double>>> rmap = reader.exportMap();			
			this.filter(rmap);
		}
		System.out.println("Loading files finished.");
	}
	
	private void filter(HashMap<Integer, HashMap<Integer, HashMap<Integer, Double>>> rmap) {
		for(Integer user : chosenUsers) {
			for(int day1 = start1; day1 <= end1; day1++) {
				for(int time = 1; time <= 48; time++) {
					Double raw = this.get(rmap, user, day1, time);
					if(raw != null) {
						this.add(user, day1, time, raw);
					}
				}
			}
			for(int day2 = start2; day2 <= end2; day2++) {
				for(int time = 1; time <= 48; time++) {
					Double raw = this.get(rmap, user, day2, time);
					if(raw != null) {
						this.add(user, day2, time, raw);
					}
				}
			}
		}		
	}
	
	private boolean add(Integer user, Integer day, Integer time, Double raw) {
		if(!this.map.containsKey(user)) {
			this.map.put(user, new HashMap<Integer, HashMap<Integer, Double>>());
		}
		if(!this.map.get(user).containsKey(day)) {
			this.map.get(user).put(day, new HashMap<Integer, Double>());
		}
		if(this.map.get(user).get(day).containsKey(time)) {
			System.err.println("Hash map already contains value for user " + user + " day " + day + " time " + time + ".");
			return false;
		}
		this.map.get(user).get(day).put(time, raw);
		return true;
	}
	
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
	
	private void printOut() {
		for(Integer user : this.getSortedUsers(map)) {
			StringBuilder sb = new StringBuilder();
			for(Integer day : this.getSortedDays(map, user)) {
				for(int time = 1; time <= 48; time++) {
					double raw = this.get(map, user, day, time);
					sb.append(day + "," + time + "," + raw + "\n");
				}
			}
			
			PrintWriter out = null;
			String path = "raw/user_" + user + ".txt";
			try {
				out = new PrintWriter(new BufferedWriter(new FileWriter(path, false)));
				out.print(sb.toString());
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				if(out != null) {
					out.flush();
					out.close();
				}
			}
		}
	}
	
}
