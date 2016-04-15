package challenge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;

public class RawReader extends Reader{
	
	private HashMap<Integer, HashMap<Integer, HashMap<Integer, Double>>> map;			//<User, <Day, <Time, raw value>>>
	
	public RawReader() {
		this.map = new HashMap<Integer, HashMap<Integer, HashMap<Integer, Double>>>();
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
			return true;
		}
		this.map.get(user).get(day).put(time, raw);
		return true;
	}
	
	private Double get(Integer user, Integer day, Integer time) {
		if(!this.map.containsKey(user)) {
			return null;
		}
		if(!this.map.get(user).containsKey(day)) {
			return null;
		}
		if(!this.map.get(user).get(day).containsKey(time)) {
			return null;
		}
		return this.map.get(user).get(day).get(time);
	}
	
	private ArrayList<Integer> getSortedUsers() {
		ArrayList<Integer> list = new ArrayList<Integer>(this.map.keySet());
		Collections.sort(list);
		return list;
	}
	
	private ArrayList<Integer> getSortedDays(Integer user) {
		if(!this.map.containsKey(user)) {
			return new ArrayList<Integer>();
		}
		ArrayList<Integer> list = new ArrayList<Integer>(this.map.get(user).keySet());
		Collections.sort(list);
		return list;
	}
	
	private ArrayList<Integer> getSortedTimes(Integer user, Integer day) {
		if(!this.map.containsKey(user)) {
			return new ArrayList<Integer>();
		}
		if(!this.map.get(user).containsKey(day)) {
			return new ArrayList<Integer>();
		}
		ArrayList<Integer> list = new ArrayList<Integer>(this.map.get(user).get(day).keySet());
		Collections.sort(list);
		return list;
	}	

	@Override
	public boolean processLine(String line) {
		StringTokenizer st = new StringTokenizer(line, " ");
		Integer user = null;
		Integer day = null;
		Integer time = null;
		Double raw = null;
		
		try {
			if(st.hasMoreTokens()) {
				user = Integer.parseInt(st.nextToken());
			}
			if(st.hasMoreTokens()) {
				String timestamp = st.nextToken();
				int stamp = Integer.parseInt(timestamp);
				day = stamp / 100;
				time = stamp % 100;
			}
			if(st.hasMoreTokens()) {
				raw = Double.parseDouble(st.nextToken());
			}
		} catch(NumberFormatException e) {
			e.printStackTrace();
		}
		
		if(user == null || day == null || time == null || raw == null) {
			System.err.println("Couldn't read user and/or day and/or time and/or raw value.");
			return false;
		}
		this.add(user, day, time, raw);
		return true;
	}
	
	public HashMap<Integer, HashMap<Integer, HashMap<Integer, Double>>> exportMap() {
		return this.map;
	}
	
	

}
