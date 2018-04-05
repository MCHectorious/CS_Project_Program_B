package fileManipulation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;

public class DataImport {
	public static ArrayList<String> getLines(String file){
		ArrayList<String> output = new ArrayList<>();
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
		    while ((line = br.readLine()) != null){
		    	output.add(line);
		    }
		    br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return output;
	}
	public static double[] getDoubleArrayFromLine(String string) {
		String[] stringValues = string.split("\t");
		double[] values = new double[stringValues.length];
		for(int i=0;i<stringValues.length;i++) {
			values[i] = Double.parseDouble( stringValues[i]);
		}
		return values;
	}
	public static double getDoubleFromFile(String file) {
		double output = 0;
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
		    while ((line = br.readLine()) != null){
		    	output = Double.parseDouble(line);
		    }
		    br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return output;
	}
	
	public static HashSet<String> getUniqueLines(String file){
		HashSet<String> output = new HashSet<>();
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
		    while ((line = br.readLine()) != null){
		    	output.add(line);
		    }
		    br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return output;
		
	}
}
