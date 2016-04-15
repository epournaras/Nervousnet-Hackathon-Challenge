package nervousnet.challenge;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import nervousnet.challenge.exceptions.MissingDataException;
import nervousnet.challenge.exceptions.UnsupportedRawFileFormatException;
import nervousnet.challenge.tags.Tags;

public class Dumper {
	
	private HashMap<Integer, LinkedHashMap<Integer, LinkedHashMap<Integer, Double>>> map;
	private ArrayList<Integer> users = null;
	
	private String path = Tags.outputPath;
	
	/**
	 * Dumps the resulting hash map to disk. Note the following:
	 * 	- files are written on default path "output/"
	 * 	- resulting hash map must contain all user, day and time IDs as raw
	 * value hash map contains, only with corresponding summarized values. 
	 * Otherwise, results will not be dumped.
	 * @param map - the resulting hash map
	 */
	public void dump(HashMap<Integer, LinkedHashMap<Integer, LinkedHashMap<Integer, Double>>> map) {
		System.out.println("Data dumping on path " + path + " ...");
		this.map = map;
		this.makeDirectories();
		
		this.readRandomlyChosenUsers();
		
		try {
			this.check();
			this.print();
		} catch(MissingDataException e) {
			e.printStackTrace();
			System.err.println("Data will not be dumped to disk!");
		}
		System.out.println("Data dumping finished!");
	}
	
	private void readRandomlyChosenUsers() {
		if(users == null) {
//			RandomUserReader rur = new RandomUserReader();
//			rur.setPath("randomlyChosenUsers.txt");
//			rur.setSkipFirstLine(false);
//			rur.openFile();
//			rur.readFile();
//			rur.closeFile();
//			users = rur.getChosenUsers();
			users = new ArrayList<Integer>();
			File folder = new File(Tags.rawPath);
			File[] listOfFiles = folder.listFiles();
			for(File file : listOfFiles) {
				try {
					users.add(extractUserID(file.getName()));
				} catch (UnsupportedRawFileFormatException e) {
					e.printStackTrace();
				}
			}
		}		
	}
	
	/**
	 * Dumps the resulting hash map to disk. Note the following:
	 * 	- files are written on the path specified
	 * 	- directories on custom path must be created manually
	 * 	- resulting hash map must contain all user, day and time IDs as raw
	 * value hash map contains, only with corresponding summarized values. 
	 * Otherwise, results will not be dumped.
	 * @param path - custom output path
	 * @param map - the resulting hash map
	 */
	public void dump(String path, HashMap<Integer, LinkedHashMap<Integer, LinkedHashMap<Integer, Double>>> map) {
		this.path = path;
		this.dump(map);
	}
	
	/**
	 * Creates and initializes the output hash map. The output hash map
	 * is initialized by user and day IDs from the rawMap provided. Note
	 * that time IDs must be inserted manually.
	 * @param rawMap - the hash map containing raw values
	 * @return Returns initialized output hash map
	 */
	public static HashMap<Integer, LinkedHashMap<Integer, LinkedHashMap<Integer, Double>>> initOutputMap() {
//		RandomUserReader rur = new RandomUserReader();
//		rur.setPath("randomlyChosenUsers.txt");
//		rur.setSkipFirstLine(false);
//		rur.openFile();
//		rur.readFile();
//		rur.closeFile();
		ArrayList<Integer> users = new ArrayList<Integer>();
		
		File folder = new File(Tags.rawPath);
		File[] listOfFiles = folder.listFiles();
		for(File file : listOfFiles) {
			try {
				users.add(extractUserID(file.getName()));
			} catch (UnsupportedRawFileFormatException e) {
				e.printStackTrace();
			}
		}
		
		HashMap<Integer, LinkedHashMap<Integer, LinkedHashMap<Integer, Double>>> outputMap = new HashMap<Integer, LinkedHashMap<Integer, LinkedHashMap<Integer, Double>>>();
		for(Integer user : users) {
			outputMap.put(user, new LinkedHashMap<Integer, LinkedHashMap<Integer, Double>>());
			for(int day = Tags.winterStartDay; day <= Tags.winterEndDay; day++) { 
				outputMap.get(user).put(day, new LinkedHashMap<Integer, Double>());
			}
			for(int day = Tags.summerStartDay; day <= Tags.summerEndDay; day++) {
				outputMap.get(user).put(day, new LinkedHashMap<Integer, Double>());
			}
		}		
		return outputMap;
	}
	
	private void makeDirectories() {
		File folder = new File(path);
		folder.mkdirs();
	}
	
	private boolean check() throws MissingDataException {
		for(Integer user : users) {
			for(int day = Tags.winterStartDay; day <= Tags.winterEndDay; day++) {
				for(int time = Tags.minTime; time <= Tags.maxTime; time++) {
					this.checkAvailability(user, day, time);
				}
			}
			for(int day = Tags.summerStartDay; day <= Tags.summerEndDay; day++) {
				for(int time = Tags.minTime; time <= Tags.maxTime; time++) {
					this.checkAvailability(user, day, time);
				}
			}
		}
		return true;
	}
	
	private static Integer extractUserID(String filename) throws UnsupportedRawFileFormatException {
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
	
	
//	private boolean check() {
//		for(Integer user : users) {
//			if(!this.map.containsKey(user)) {
//				this.printError("Missing data about user " + user + ".");
//				return false;
//			}
//			for(int day = Tags.winterStartDay; day <= Tags.winterEndDay; day++) {
//				if(!this.map.get(user).containsKey(day)) {
//					this.printError("Missing data about user " + user + " for day " + day + ".");
//					return false;
//				}
//				for(int time = Tags.minTime; time <= Tags.maxTime; time++) {
//					if(!this.map.get(user).get(day).containsKey(time)) {
//						this.printError("Missing data about user " + user + " for day " + day + " for time " + time + ".");
//						return false;
//					}
//				}
//			}
//			for(int day = Tags.summerStartDay; day <= Tags.summerEndDay; day++) {
//				if(!this.map.get(user).containsKey(day)) {
//					this.printError("Missing data about user " + user + " for day " + day + ".");
//					return false;
//				}
//				for(int time = Tags.minTime; time <= Tags.maxTime; time++) {
//					if(!this.map.get(user).get(day).containsKey(time)) {
//						this.printError("Missing data about user " + user + " for day " + day + " for time " + time + ".");
//						return false;
//					}
//				}
//			}
//		}
//		return true;
//	}
	
	/**
	 * Checks if hash map contains user specified
	 * @param user - user ID
	 * @return true if hash map contains the user. Otherwise, throws {@link MissingDataException}
	 * @throws MissingDataException
	 */
	private boolean checkAvailability(Integer user) throws MissingDataException{
		if(!this.map.containsKey(user)) {
			throw new MissingDataException("Data with user ID " + user + " missing.");
		}
		return true;
	}
	
	/**
	 * Checks if hash map contains user and day specified
	 * @param user - user ID
	 * @param day - day ID
	 * @return true if hash map contains both, the user ID and the day ID. 
	 * Throws {@link MissingDataException} otherwise.
	 * @throws MissingDataException
	 */
	private boolean checkAvailability(Integer user, Integer day) throws MissingDataException {
		if(this.checkAvailability(user)) {
			if(!this.map.get(user).containsKey(day)) {
				throw new MissingDataException("Data with user ID " + user + " and day ID " + day + " missing.");
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
	 * {@link MissingDataException} exception otherwise
	 * @throws MissingDataException
	 */
	private boolean checkAvailability(Integer user, Integer day, Integer time) throws MissingDataException {
		if(this.checkAvailability(user, day)) {
			if(!this.map.get(user).get(day).containsKey(time)) {
				throw new MissingDataException("Data with user ID " + user + ", day ID " + day + " and time ID " + time + " missing.");
			}
		}
		return true;
	}
	
	private void printError(String error) {
		System.err.println(error);
	}
	
	private void print() {
		for(Integer user : users) {
			StringBuilder sb = new StringBuilder();
			for(int day = Tags.winterStartDay; day <= Tags.winterEndDay; day++) {
				for(int time = Tags.minTime; time <= Tags.maxTime; time++) {
					double centroid = this.map.get(user).get(day).get(time);
					sb.append(day + "," + time + "," + centroid + "\n");
				}
			}
			for(int day = Tags.summerStartDay; day <= Tags.summerEndDay; day++) {
				for(int time = Tags.minTime; time <= Tags.maxTime; time++) {
					double centroid = this.map.get(user).get(day).get(time);
					sb.append(day + "," + time + "," + centroid + "\n");
				}
			}
			
			PrintWriter out = null;
			String path = this.path + "user_" + user + ".txt";
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
