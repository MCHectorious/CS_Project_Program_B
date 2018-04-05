package fileManipulation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashSet;

public class DataExport {

	public static void overwriteToTextFile(StringBuilder builder, String file) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter( new FileWriter(file) ));
			out.print(builder.toString());
			out.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void overwriteToTextFile(double value, String file) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter( new FileWriter(file) ));
			out.print( Double.toString(value) );
			out.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void appendToTextFile(String data, String file) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file,true)));
			out.println(data);
			out.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void overwriteToTextFile(HashSet<String> data, String file) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter( new FileWriter(file) ));
			for(String line:data) {
				out.println(line);
			}
			out.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
