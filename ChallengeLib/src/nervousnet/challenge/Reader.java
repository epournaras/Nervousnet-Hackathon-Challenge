package nervousnet.challenge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

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
		if(in == null) {
			return;
		}
		//StringBuilder sb = new StringBuilder();
		try {
			String line = null;
			if(skipFirstLine) {
				line = in.readLine();
				//sb.append(line + System.lineSeparator());
				line = null;
			}			
			boolean flag = true;
			while(((line = in.readLine()) != null) && flag) {
				//sb.append(line + System.lineSeparator());
				flag = processLine(line);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
//			PrintWriter out = null;
//			String path = "newRaw/" + "user_" + user + ".txt";
//			try {
//				out = new PrintWriter(new BufferedWriter(new FileWriter(path, false)));
//				out.print(sb.toString());
//			} catch(Exception e) {
//				e.printStackTrace();
//			} finally {
//				if(out != null) {
//					out.flush();
//					out.close();
//				}
//			}
		}
	}
	
	public abstract boolean processLine(String line);

}
