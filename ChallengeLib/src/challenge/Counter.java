package challenge;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Counter {
	
	private HashMap<Integer, Integer> dataAvailabilityMap;							// <Day, num of users with full data>
	private HashMap<Integer, ArrayList<Integer>> missingUsers;						// <User, list of days user doesn't have full measurements of>>
	private HashMap<Integer, HashMap<Integer, HashMap<Integer, Double>>> map;		// <User, <Day, <Time, raw value>>>
	
	private int start1 = 213;
	private int end1 = 240;
	private int start2 = 388;
	private int end2 = 415;
	
	private ArrayList<Integer> availableUsers = new ArrayList<Integer>();
	private ArrayList<Integer> errorUsers = new ArrayList<Integer>();
	
	public Counter() {
		this.dataAvailabilityMap = new HashMap<Integer, Integer>();
		this.missingUsers = new HashMap<Integer, ArrayList<Integer>>();
	}
	
	private void setRawMap(HashMap<Integer, HashMap<Integer, HashMap<Integer, Double>>> map) {
		this.map = map;
	}
	
	private void addUp(Integer day, int increment) {
		if(!this.dataAvailabilityMap.containsKey(day)) {
			this.dataAvailabilityMap.put(day, increment);
			return;
		}
		int sum = this.dataAvailabilityMap.remove(day);
		sum += increment;
		this.dataAvailabilityMap.put(day, sum);
	}
	
	private void addMissingUser(Integer user, Integer day) {
		if(!this.missingUsers.containsKey(user)) {
			this.missingUsers.put(user, new ArrayList<Integer>());
		}
		this.missingUsers.get(user).add(day);
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
	
	private ArrayList<Integer> getSortedDaysInDataAvailability() {
		ArrayList<Integer> sortedDays = new ArrayList<Integer>(this.dataAvailabilityMap.keySet()); 
		Collections.sort(sortedDays);
		return sortedDays;
	}
	
	private ArrayList<Integer> getSortedUsersInMissingUsers() {
		ArrayList<Integer> sortedDays = new ArrayList<Integer>(this.missingUsers.keySet());
		Collections.sort(sortedDays);
		return sortedDays;
	}
	
	private Integer getCountFromDataAvailability(Integer day) {
		if(!this.dataAvailabilityMap.containsKey(day)) {
			return null;
		}
		return this.dataAvailabilityMap.get(day);
	}
	
	private ArrayList<Integer> getMissingDays(Integer user) {
		if(!this.missingUsers.containsKey(user)) {
			return new ArrayList<Integer>();
		}
		return this.missingUsers.get(user);
	}
	
	
	
	private void count() {
		for(Integer user : this.getSortedUsers(map)) {
			for(Integer day : this.getSortedDays(map, user)) {
				boolean flag = true;
				for(int time = 1; time <=48; time++) {
					if(this.get(map, user, day, time) == null) {
						flag = flag && false;
						break;
					}
				}
				if(flag) {
					this.addUp(day, 1);
				}
			}
		}
	}
	
	private void missingDays() {
		for(Integer user : this.getSortedUsers(map)) {
			//for(Integer day : this.getSortedDays(map, user)) {
			for(int day = 199; day <= 564; day++) {
				boolean flag = true;
				for(int time = 1; time <=48; time++) {
					if(this.get(map, user, day, time) == null) {
						flag = flag && false;
						break;
					}
				}
				if(!flag) {
					this.addMissingUser(user, day);
				}
			}
		}
	}
	
	public void availableUsers() {		
		for(Integer user : this.getSortedUsersInMissingUsers()) {
			if(checkRange(this.getMissingDays(user))) {
				this.availableUsers.add(user);
			} else {
				this.errorUsers.add(user);
			}
		}
	}
	
	private boolean checkRange(ArrayList<Integer> days) {
		if(days.isEmpty()) {
			return false;
		}
		for(Integer day : days) {
			if(start1 <= day && day <= end1) {
				return false;
			}
			if(start2 <= day && day <=end2) {
				return false;
			}
		}
		return true;
	}
	
	public void print() {
		int weekReference = 0;
		StringBuilder sb = new StringBuilder();
		sb.append("\tMON\tTUE\tWEN\tTHU\tFRI\tSAT\tSUN");
		for(int day = 199; day <= 564; day++) {
			Integer count = this.getCountFromDataAvailability(day);
			int week = WeekCalculator.calculateWeek(day);
			if(week != weekReference) {
				sb.append("\n");
				weekReference = week;
			}
			if(count != null) {
				sb.append("\t" + count);				
			} else {
				sb.append("\t - ");
			}			
		}
		System.out.println(sb.toString());
		
		sb = new StringBuilder();
		for(Integer user : this.getSortedUsersInMissingUsers()) {
			sb.append("User: " + user);
			for(Integer day : this.getMissingDays(user)) {
				sb.append(" " + day + ",");
			}
			sb.append("\n");
		}
		System.out.println(sb.toString());
		
		System.out.println("\n\n Number of available users is: " + this.availableUsers.size());
		System.out.println("\n\n Number of error users is: " + this.errorUsers.size());
		
		printToFile();
	}
	
	public void printToFile() {
		PrintWriter out = null;
		String path = "availableUsers.txt";
		StringBuilder sb = new StringBuilder();
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(path, false)));			
			for(Integer user : this.availableUsers) {
				sb.append(user + "\n");
			}
			out.println(sb.toString());
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(out != null) {
				out.flush();
				out.close();
			}			
		}
		out = null;
		sb = new StringBuilder();
		path = "missingDataUsers.txt";
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(path, false)));			
			for(Integer user : this.errorUsers) {
				sb.append(user + "\n");
			}
			out.println(sb.toString());
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(out != null) {
				out.flush();
				out.close();
			}			
		}
	}
	
	
	public void action(HashMap<Integer, HashMap<Integer, HashMap<Integer, Double>>> map) {
		this.setRawMap(map);
		this.count();
		this.missingDays();
	}
	

}
