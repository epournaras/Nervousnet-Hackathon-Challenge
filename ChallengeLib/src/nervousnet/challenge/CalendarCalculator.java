package nervousnet.challenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import nervousnet.challenge.tags.DAYS;

/**
 * Performs calendar calculations such as: determining week
 * number based on day ID, determining day of the week based
 * on day ID and determining day ID based on week number and
 * day of the week. 
 *  
 * Day IDs start from number 195. That was Thursday, January, 
 * 1st 2009. It belongs to week number 0.
 * 
 * @author nikolijo
 *
 */
public class CalendarCalculator {
	
	private ArrayList<Integer> days;
	
	private Integer currentWeek = null;
	
	private CalendarCalculator() {
		this.init();
	}
	
	/**
	 * For testing purposes only!
	 */
	private void init() {
		days = new ArrayList<Integer>();
		days.add(195);
		days.add(196);
		days.add(197);
		days.add(198);
		days.add(199);
		days.add(200);
		days.add(201);
		days.add(202);
		days.add(203);		
		days.add(204);
		days.add(205);		
		days.add(206);
		days.add(207);
		days.add(208);
		days.add(209);
		days.add(210);
		days.add(211);		
		days.add(212);
		days.add(213);		
		days.add(214);
		days.add(215);
		days.add(216);
		days.add(217);
		days.add(218);
		days.add(219);
		days.add(220);
		days.add(221);
		days.add(222);
		days.add(223);
		days.add(224);		
	}
	
	/**
	 * Calculates the week ID given the day ID
	 * @param day - day ID
	 * @return week number, [1, 52]
	 */
	public static int calculateWeek(int day) {
		// 27 = 195/7 !!
		int k = day/7;
		int m = day % 7;
		if(m < 3) {
			// Friday through Sunday
			return k - 27 - 1;
		} else {
			// Monday through Thursday
			return k - 27;
		}
	}
	
	/**
	 * Calculates the day in a week given the day ID.
	 * @param day - day ID
	 * @return enum DAYS, [DAYS.MONDAY, DAYS.SUNDAY]
	 */
	public static DAYS calculateDayOfWeek(int day) {
		int m = day % 7;
		switch (m) {
		case 0:			
			return DAYS.FRIDAY;
		case 1:
			return DAYS.SATURDAY;
		case 2:
			return DAYS.SUNDAY;
		case 3:
			return DAYS.MONDAY;
		case 4:
			return DAYS.TUESDAY;
		case 5:
			return DAYS.WEDNESDAY;
		case 6:
			return DAYS.THURSDAY;
		default:
				return null;
		}
	}
	
	/**
	 * Calculated the day ID based on week number and day
	 * of the week specified.
	 * @param week - week number [1, 52]
	 * @param day - day of the week [DAYS.MONDAY, DAYS.SUNDAY]
	 * @return day ID
	 */
	public static int calculateDay(int week, DAYS day) {
		if(week < 1 || week > 52 || day == null) {
			return -1;
		}
		switch (day) {
		case MONDAY:
			return (week+28)*7-4;
		case TUESDAY:
			return (week+28)*7-3;
		case WEDNESDAY:
			return (week+28)*7-2;
		case THURSDAY:
			return (week+28)*7-1;
		case FRIDAY:
			return (week+28)*7;
		case SATURDAY:
			return (week+28)*7+1;
		case SUNDAY:
			return (week+28)*7+2;
		default:
			return -1;
		}		
	}
	
	/**
	 * For testing purposes only!
	 */
	private void print() {
		int firstDay = days.get(0);
		int dayOfWeek = firstDay % 7;
		
			
		StringBuilder sb = new StringBuilder();
		sb.append("MON\tTUE\tWED\tTHU\tFRI\tSAT\tSUN\n");
		for(int i = 0; i < dayOfWeek - 3; i++) {
			sb.append("-\t");
		}
		
		this.currentWeek = calculateWeek(firstDay);
		//Integer toExpect = null;
		for(int j = 0; j < this.days.size(); j++) {
			int week = calculateWeek(this.days.get(j));
			if(!this.currentWeek.equals(week)) {
				sb.append("\n");
				this.currentWeek = week;
			} 
//			if(toExpect != null) {
//				int difference = (this.days.get(j) % 7) - toExpect;
//			}
//			toExpect = ((this.days.get(j) % 7) + 1) % 7;
			sb.append(this.days.get(j) + "\t");
			
		}
		System.out.println(sb.toString());
	}
	
	public static void main(String[] args) {
		System.out.println("Week 1, Monday " + CalendarCalculator.calculateDay(1, DAYS.MONDAY));
		System.out.println("Week 2, Tuesday " + CalendarCalculator.calculateDay(2, DAYS.TUESDAY));
		System.out.println("Week 5, Wednesday " + CalendarCalculator.calculateDay(5, DAYS.WEDNESDAY));
		System.out.println("Week 28, Thursday " + CalendarCalculator.calculateDay(28, DAYS.THURSDAY));
		System.out.println("Week 31, Friday " + CalendarCalculator.calculateDay(31, DAYS.FRIDAY));
		System.out.println("Week 29, Saturday " + CalendarCalculator.calculateDay(29, DAYS.SATURDAY));
		System.out.println("Week 7, Sunday " + CalendarCalculator.calculateDay(7, DAYS.SUNDAY));
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			
			while(true) {
				String s = in.readLine();
				int day = Integer.parseInt(s);
				if(day == 0) {
					break;
				} else {
					System.out.println("Week is " + CalendarCalculator.calculateWeek(day) + " Day is " + CalendarCalculator.calculateDayOfWeek(day));
				}			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}

}
