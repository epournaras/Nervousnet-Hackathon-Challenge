package readers;

import java.util.HashMap;
import java.util.StringTokenizer;

import nervousnet.challenge.exceptions.CorruptedRawDataException;
import nervousnet.challenge.tags.Tags;

public class RawValueFileReader extends Reader {
	
	private HashMap<Integer, HashMap<Integer, Double>> map;			//<Day, <Time, raw value>>
	
	public RawValueFileReader() {
		this.map = new HashMap<Integer, HashMap<Integer, Double>>();
	}
	
	private boolean add(Integer day, Integer time, Double raw) {
		if(!this.map.containsKey(day)) {
			this.map.put(day, new HashMap<Integer, Double>());
		}
		if(this.map.get(day).containsKey(time)) {
			System.err.println("Hash map already contains value for " + "day " + day + " time " + time + ".");
			return true;
		}
		this.map.get(day).put(time, raw);
		return true;
	}

	@Override
	public boolean processLine(String line) {
		boolean flag = false;
		try {
			flag = deepProcessLine(line);
		} catch(CorruptedRawDataException e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}
	
	public boolean deepProcessLine(String line) throws CorruptedRawDataException {
		StringTokenizer st = new StringTokenizer(line, ",");
		Integer day = null;
		Integer time = null;
		Double raw = null;
		
		try {
			if(st.hasMoreTokens()) {
				day = Integer.parseInt(st.nextToken());
			}
			if(st.hasMoreTokens()) {
				time = Integer.parseInt(st.nextToken());
			}
			if(st.hasMoreTokens()) {
				raw = Double.parseDouble(st.nextToken());
			}
		} catch(NumberFormatException e) {
			throw new CorruptedRawDataException("Line " + line + " is corrupted.");
		}
		
		if(day == null || time == null || raw == null) {
			throw new CorruptedRawDataException("Line " + line + " is corrupted.");
		}
		
		this.checkDayRange(day);
		this.checkTimeRange(time);
		
		this.add(day, time, raw);
		return true;
	}
	
	private boolean checkDayRange(Integer day) throws CorruptedRawDataException {
		if(Tags.winterStartDay <= day && day <= Tags.winterEndDay) {
			return true;
		}
		if(Tags.summerStartDay <= day && day <= Tags.summerEndDay) {
			return true;
		}		
		throw new CorruptedRawDataException("Illegal day ID: " + day);
	}
	
	private boolean checkTimeRange(Integer time) throws CorruptedRawDataException{
		if(Tags.minTime <= time && time <= Tags.maxTime) {
			return true;
		}
		throw new CorruptedRawDataException("Illegal time ID: " + time);
	}
	
	public HashMap<Integer, HashMap<Integer, Double>> exportValues() {
		return this.map;
	}

}
