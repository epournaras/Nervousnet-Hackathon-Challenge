package challenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * This class is used to calculate which day belongs
 * to which week. Day tags are starting from number 195.
 * That was Thursday, January, 1st 2009. It belongs to
 * week number 0.
 * 
 * it is used when weekly clustering is applied on raw data.
 * @author nikolijo
 *
 */
public class WeekCalculator {
	
	public ArrayList<Integer> days;
	
	private Integer currentWeek = null;
	
	public WeekCalculator() {
		this.init();
	}
	
	/**
	 * For testing purposes only!
	 */
	public void init() {
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
	 * This is the most important method. It actually performs calculation.
	 * The beginning day is 195 and it belongs to week 0.
	 * @param day - day tag
	 * @return integer number >= 0 which represents a week
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
	 * For testing purposes only!
	 */
	public void print() {
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
		WeekCalculator wc = new WeekCalculator();
		//wc.print();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			
			while(true) {
				String s = in.readLine();
				int day = Integer.parseInt(s);
				if(day == 0) {
					break;
				} else {
					System.out.println("Week is " + WeekCalculator.calculateWeek(day));
				}			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}

}
