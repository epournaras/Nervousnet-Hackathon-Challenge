package challenge;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public abstract class Reader {
	
	public String path;
	
	private boolean skipFirstLine = false;
	
	private BufferedReader in;
	
	public Reader() {
		
	}
	
	public Reader(String path) {
		this.path = path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public void setSkipFirstLine(boolean skip) {
		this.skipFirstLine = skip;
	}
	
	public boolean openFile() {
		boolean successful = false;
		try {
			in = new BufferedReader(new FileReader(path));
			successful = true;
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			successful = false;
		}
		return successful;
	}
	
	public void closeFile() {
		try {
			if(in != null) {
				in.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readFile() {
		try {
			String line = null;
			if(skipFirstLine) {
				line = in.readLine();
				line = null;
			}			
			boolean flag = true;
			while(((line = in.readLine()) != null) && flag) {
				flag = processLine(line);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public abstract boolean processLine(String line);

}
