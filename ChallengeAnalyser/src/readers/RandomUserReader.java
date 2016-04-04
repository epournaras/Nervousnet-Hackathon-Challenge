package readers;

import java.util.ArrayList;

public class RandomUserReader extends Reader{
	
	private ArrayList<Integer> users;
	
	public RandomUserReader() {
		users = new ArrayList<Integer>();
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
	
	public ArrayList<Integer> getChosenUsers() {
		return this.users;
	}

}
