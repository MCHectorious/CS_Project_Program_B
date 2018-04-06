package fileManipulation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashSet;

public class DataExport {

	public static void overwriteToTextFile(StringBuilder builder, String file) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter( new FileWriter(file) ));
			out.print(builder.toString());
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if (out != null){
					out.close();
			}
		}
	}
	
	public static void overwriteToTextFile(double value, String file) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter( new FileWriter(file) ));
			out.print( Double.toString(value) );
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if (out != null){
				out.close();
			}
		}
	}
	
	public static void appendToTextFile(String data, String file) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(file,true)));
			out.println(data);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if (out != null){
				out.close();
			}
		}
	}
	
	public static void overwriteToTextFile(HashSet<String> data, String file) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter( new FileWriter(file) ));
			for(String line:data) {
				out.println(line);
			}
			out.close();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if (out != null){
				out.close();
			}
		}
	}
	
}
