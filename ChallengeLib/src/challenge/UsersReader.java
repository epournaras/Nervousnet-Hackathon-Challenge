package challenge;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class UsersReader extends Reader{
	
	public static final int N = 1000;
	
	public static int minUserID = 1000;
	public static int maxUserID = 7444;
	
	private HashSet<Integer> randomUsers;
	private ArrayList<Integer> users;
	
	public UsersReader() {
		users = new ArrayList<>();
		randomUsers = new HashSet<Integer>();
	}

	@Override
	public boolean processLine(String line) {
		Integer user = null;
		try {
			user = Integer.parseInt(line);
		}catch(Exception e) {
			user = null;
		}
		if(user == null) {
			return false;
		}
		this.users.add(user);		
		return true;
	}
	
	public void chooseRandomly() {
		for(int i = 0; i < N; i++) {
			int user = ThreadLocalRandom.current().nextInt(minUserID, maxUserID);
			if(isUserOK(user)) {
				this.randomUsers.add(user);
			} else {
				i--;
			}
		}
		this.printChosenUsers();
	}
	
	private boolean isUserOK(Integer user) {
		if(randomUsers.contains(user)) {
			return false;
		}
		
		boolean flag = false;
		for(Integer u : users) {
			if(u.equals(user)) {
				flag = true;
				break;
			}
		}
		
		if(flag) {
			return true;
		} else {
			return false;
		}
	}
	
	private void printChosenUsers() {
		StringBuilder sb = new StringBuilder();
		int cnt = 0;
		Iterator<Integer> iter = this.randomUsers.iterator();
		while(iter.hasNext()) {
			cnt++;
			Integer user = iter.next();
			sb.append(user + "\n");
		}
		System.out.println("Number of chosen users is " + cnt);
		
		PrintWriter out = null;
		String path = "randomlyChosenUsers.txt";
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
	
	public static void main(String[] args) {
		UsersReader rur = new UsersReader();
		rur.setPath("availableUsers.txt");
		rur.setSkipFirstLine(false);
		rur.openFile();
		rur.readFile();
		rur.closeFile();
		rur.chooseRandomly();
	}
	
	

}
