package readers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import nervousnet.challenge.exceptions.IllegalHashMapArgumentException;
import nervousnet.challenge.exceptions.MissingDataException;
import nervousnet.challenge.exceptions.MissingFileException;
import nervousnet.challenge.exceptions.NullArgumentException;
import nervousnet.challenge.exceptions.UnsupportedRawFileFormatException;
import nervousnet.challenge.tags.Tags;

public class Loader {
	
	private HashMap<Integer, LinkedHashMap<Integer, LinkedHashMap<Integer, Double>>> map;			//<User, <Day, <Time, raw value>>>
	
	private String rawPath = Tags.rawPath;
	private String basePath = "";
	
	private ArrayList<Integer> users = null;
	
	public Loader() {
		this.map = new HashMap<Integer, LinkedHashMap<Integer, LinkedHashMap<Integer, Double>>>();
	}
	
	/**
	 * Setting path to directory holding all raw-value files.
	 */
	public void setRawPath(String path) {
		this.rawPath = path;
	}
	
	public void setBasePath(String base) {
		this.basePath = base;
	}
	
	/** 
	 * Loads raw values from the default path. Note the following:
	 * 	- default path is "raw/"
	 * 	- all files from default directory will be loaded
	 * 	- reference to original map is returned!
	 * 
	 * @return - hash map containing raw values
	 */
	public HashMap<Integer, LinkedHashMap<Integer, LinkedHashMap<Integer, Double>>> exportRawValues() throws /*UnsupportedRawFileFormatException, */MissingDataException, MissingFileException {
		this.load();
		return this.map;
	}
	
	/**
	 * Loads raw values from the default path into a hash map, but it returns a deep-clone
	 * of the hashMap. Note the following:
	 * 	- default path is "raw/"
	 * 	- a cloned hash map is returned. Since this doubles the amount of data in memory,
	 * some memory issues may arise. On the other hand, raw values stay untouched.
	 * 
	 * @return Returns deep-clone hash map containing raw values
	 */
	public HashMap<Integer, LinkedHashMap<Integer, LinkedHashMap<Integer, Double>>> exportClonedRawValues() throws UnsupportedRawFileFormatException, MissingDataException, MissingFileException {
		this.load();
		HashMap<Integer, LinkedHashMap<Integer, LinkedHashMap<Integer, Double>>> clonedMap = new HashMap<Integer, LinkedHashMap<Integer, LinkedHashMap<Integer, Double>>>();
		try {
			for(Integer user : this.getSortedUsers()) {
				clonedMap.put(user, new LinkedHashMap<Integer, LinkedHashMap<Integer, Double>>());
				for(Integer day : this.getSortedDays(user)) {
					clonedMap.get(user).put(day, new LinkedHashMap<Integer, Double>());
					for(int time = Tags.minTime; time <= Tags.maxTime; time++) {
						double raw = this.map.get(user).get(day).get(time);
						clonedMap.get(user).get(day).put(time, raw);
					}
				}
			}
		} catch(NullArgumentException e) {
			// do nothing, it won't happen
		} catch(IllegalHashMapArgumentException e) {
			// do nothing, it won't happen
		}	
		
		return clonedMap;
	}
	
	private void readRandomlyChosenUsers() {
		if(users == null) {
			RandomUserReader rur = new RandomUserReader();
			rur.setPath(basePath + "randomlyChosenUsers.txt");
			rur.setSkipFirstLine(false);
			rur.openFile();
			rur.readFile();
			rur.closeFile();
			users = rur.getChosenUsers();
		}		
	}
	
	/**
	 * Returns user IDs sorted in ascending order from the original hash map.
	 */
	public ArrayList<Integer> getSortedUsers() {
		ArrayList<Integer> sortedUsers = new ArrayList<Integer>(this.map.keySet());
		Collections.sort(sortedUsers);
		return sortedUsers;
	}
	
	/**
	 * Returns day IDs sorted in ascending order for user specified
	 * from the original hash map.
	 * @param user - user ID
	 * @return list of sorted day IDs if hash map contains the user ID specified.
	 * If user ID is null, then {@link NullArgumentException} is thrown.
	 * If hash map does not contain user ID specified, then {@link IllegalHashMapArgumentException} is thrown
	 */
	public ArrayList<Integer> getSortedDays(Integer user) throws IllegalHashMapArgumentException, NullArgumentException {
		if(user == null) {
			throw new NullArgumentException("User ID is null!");
		}
		ArrayList<Integer> sortedDays = null;
		if(checkAvailability(user)) {
			sortedDays = new ArrayList<Integer>(this.map.get(user).keySet());
		}		
		return sortedDays;
	}
	
	/**
	 * Returns raw value for user ID, day ID and time ID specified.
	 * 
	 * @param user - User ID
	 * @param day - Day ID
	 * @param time - time ID
	 * @return Returns raw value for arguments specified. If any of the
	 * arguments is null, {@link NullArgumentException} is thrown. If hash map
	 * does not contain at least one of the arguments specified, {@link IllegalHashMapArgumentException}
	 * is thrown.
	 */
	public Double getRawValue(Integer user, Integer day, Integer time) throws IllegalHashMapArgumentException, NullArgumentException {
		if(user == null) {
			throw new NullArgumentException("User ID is null!");
		}
		if(day == null) {
			throw new NullArgumentException("Day ID is null!");
		}
		if(time == null) {
			throw new NullArgumentException("Time ID is null!");
		}
		if(this.checkAvailability(user, day, time)) {
			return this.map.get(user).get(day).get(time);
		}
		return this.map.get(user).get(day).get(time);
	}
	
	/**
	 * Returns list of raw values for user ID and day ID specified
	 * sorted in ascending order of time ID.
	 * @param user - user ID
	 * @param day - day ID
	 * @return Returns list of raw values for arguments specified.
	 * If any of the arguments is null, then {@link NullArgumentException} is thrown.
	 * If the hash map does not contain any of the arguments, then {@link IllegalHashMapArgumentException} is thrown.
	 */
	public ArrayList<Double> getSortedRawValues(Integer user, Integer day) throws NullArgumentException, IllegalHashMapArgumentException {
		if(user == null) {
			throw new NullArgumentException("User ID is null!");
		}
		if(day == null) {
			throw new NullArgumentException("Day ID is null!");
		}
		if(!this.checkAvailability(user, day)) {
			return null;
		}
		ArrayList<Double> raws = new ArrayList<Double>();
		Iterator<Integer> iter = this.map.get(user).get(day).keySet().iterator();
		while(iter.hasNext()) {
			Integer time = iter.next();
			raws.add(this.map.get(user).get(day).get(time));
		}
		return raws;
	}
	
	private void load() throws /*UnsupportedRawFileFormatException,*/ MissingDataException, MissingFileException {
//		File folder = new File(rawPath);
//		File[] listOfFiles = folder.listFiles();
		System.out.println("Loading files from: " + rawPath);
		this.readRandomlyChosenUsers();
		for(Integer user : users) {
			String filename = "user_" + user + ".txt";
			File folder = new File(rawPath);
			File[] listOfFiles = folder.listFiles();
			boolean found = false;
			for(File file : listOfFiles) {
				if(filename.equals(file.getName())) {
					found = true;
					break;
				}
			}
			if(!found) {
				throw new MissingFileException("File " + rawPath + filename + " is missing.");
			}
			//Integer user = extractUserID(file.getName());
			RawValueFileReader rvfr = new RawValueFileReader();
			String path = rawPath + filename;
			rvfr.setSkipFirstLine(false);
			rvfr.setPath(path);
			rvfr.openFile();
			rvfr.readFile();
			rvfr.closeFile();
			HashMap<Integer, HashMap<Integer, Double>> rawmap = rvfr.exportValues();
			this.add(rawmap, user);
		}
		System.out.println("Loading files finished.");
	}
	
	private void add(HashMap<Integer, HashMap<Integer, Double>> rawmap, Integer user) throws MissingDataException {
		for(int day = Tags.winterStartDay; day <= Tags.winterEndDay; day++) {
			for(int time = Tags.minTime; time <= Tags.maxTime; time++) {
				if(rawmap.containsKey(day)) {
					if(rawmap.get(day).containsKey(time)) {
						this.add(user, day, time, rawmap.get(day).get(time));
					} else {
						throw new MissingDataException("Data with user ID " + user + ", day ID " + day + " and time ID " + time + " missing from raw files.");
					}
				} else {
					throw new MissingDataException("Data with user ID " + user + " and day ID " + day + " missing from raw files.");
				}
			}
		}
		for(int day = Tags.summerStartDay; day <= Tags.summerEndDay; day++) {
			for(int time = Tags.minTime; time <= Tags.maxTime; time++) {
				if(rawmap.containsKey(day)) {
					if(rawmap.get(day).containsKey(time)) {
						this.add(user, day, time, rawmap.get(day).get(time));
					} else {
						throw new MissingDataException("Data with user ID " + user + ", day ID " + day + " and time ID " + time + " missing from raw files.");
					}
				} else {
					throw new MissingDataException("Data with user ID " + user + " and day ID " + day + " missing from raw files.");
				}
			}
		}
	}
	
	private boolean add(Integer user, Integer day, Integer time, Double raw) {
		if(!this.map.containsKey(user)) {
			this.map.put(user, new LinkedHashMap<Integer, LinkedHashMap<Integer, Double>>());
		}
		if(!this.map.get(user).containsKey(day)) {
			this.map.get(user).put(day, new LinkedHashMap<Integer, Double>());
		}
		if(this.map.get(user).get(day).containsKey(time)) {
			System.err.println("Hash map already contains value for user " + user + " day " + day + " time " + time + ".");
			return false;
		}
		this.map.get(user).get(day).put(time, raw);
		return true;
	}
	
	private Integer extractUserID(String filename) throws UnsupportedRawFileFormatException {
		StringTokenizer st = new StringTokenizer(filename, ".");
		Integer user = null;
		try {
			if(st.hasMoreTokens()) {
				String subname = st.nextToken();
				StringTokenizer stt = new StringTokenizer(subname, "_");
				stt.nextToken();
				if(stt.hasMoreTokens()) {
					user = Integer.parseInt(stt.nextToken());
				}
			}
		} catch(Exception e) {
			throw new UnsupportedRawFileFormatException("File " + filename + " is not of supported format! Filename should be formatted as 'user_XXXX' where XXXX represents the user ID.");
		}
		return user;
	}
	
	/**
	 * Checks if hash map contains user specified
	 * @param user - user ID
	 * @return true if hash map contains the user. Otherwise, throws {@link IllegalHashMapArgumentException}
	 * @throws IllegalHashMapArgumentException
	 */
	private boolean checkAvailability(Integer user) throws IllegalHashMapArgumentException{
		if(!this.map.containsKey(user)) {
			throw new IllegalHashMapArgumentException("Couldn't find user with ID " + user + " in hash map.");
		}
		return true;
	}
	
	/**
	 * Checks if hash map contains user and day specified
	 * @param user - user ID
	 * @param day - day ID
	 * @return true if hash map contains both, the user ID and the day ID. 
	 * Throws {@link IllegalHashMapArgumentException} otherwise.
	 * @throws IllegalHashMapArgumentException
	 */
	private boolean checkAvailability(Integer user, Integer day) throws IllegalHashMapArgumentException {
		if(this.checkAvailability(user)) {
			if(!this.map.get(user).containsKey(day)) {
				throw new IllegalHashMapArgumentException("Couldn't find user with ID " + user + " and day ID " + day + " in hash map.");
			}
		}
		return true;
	}
	
	/**
	 * Checks if hash map contains user, day and time specified
	 * @param user - user ID
	 * @param day - day ID
	 * @param time - time ID
	 * @return true if hash map contains user, day and time IDs. Throws
	 * {@link IllegalHashMapArgumentException} exception otherwise
	 * @throws IllegalHashMapArgumentException
	 */
	private boolean checkAvailability(Integer user, Integer day, Integer time) throws IllegalHashMapArgumentException {
		if(this.checkAvailability(user, day)) {
			if(!this.map.get(user).get(day).containsKey(time)) {
				throw new IllegalHashMapArgumentException("Couldn't find user with ID " + user + ", day ID " + day + " and time ID " + time + " in hash map.");
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		Loader loader = new Loader();
		try {
			loader.exportRawValues();
		} /*catch (UnsupportedRawFileFormatException e) {
			e.printStackTrace();
		} */catch (MissingDataException e) {
			e.printStackTrace();
		} catch(MissingFileException e) {
			e.printStackTrace();
		}
	}

}
