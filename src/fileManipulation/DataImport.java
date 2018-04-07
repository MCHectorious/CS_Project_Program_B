package fileManipulation;

import dataStructures.Flashcard;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class DataImport {
	public static ArrayList<String> getLines(String file){
		ArrayList<String> output = new ArrayList<>();
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(file));
			String line;
		    while ((line = br.readLine()) != null){
		    	output.add(line);
		    }
		} catch (IOException exception) {
			System.out.println("Error trying to access file: " + file);
			System.out.println(exception.getMessage());
		}finally {
			if (br != null){
				try {
					br.close();
				} catch (IOException exception) {
					System.out.println("Error trying to close connection to file: " + file);
					System.out.println(exception.getMessage());
				}
			}
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
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(file));
			String line;
		    while ((line = br.readLine()) != null){
		    	output = Double.parseDouble(line);
		    }
		} catch (IOException exception) {
			System.out.println("Error trying to get double from file: " + file);
			System.out.println(exception.getMessage());
		}finally {
			if (br != null){
				try {
					br.close();
				} catch (IOException exception) {
					System.out.println("Error trying to close connection to file: " + file);
					System.out.println(exception.getMessage());
				}
			}
		}
		return output;
	}
	
	public static HashSet<String> getUniqueLines(String file){
		HashSet<String> output = new HashSet<>();
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(file));
			String line;
		    while ((line = br.readLine()) != null){
		    	output.add(line);
		    }
		} catch (IOException exception) {
			System.out.println("Error trying to access file: " + file);
			System.out.println(exception.getMessage());
		}finally {
			if (br != null){
				try {
					br.close();
				} catch (IOException exception) {
					System.out.println("Error trying to close connection to file: " + file);
					System.out.println(exception.getMessage());
				}
			}
		}
		
		return output;
		
	}

	public static ArrayList<Flashcard> getFlashcardListFromTextFile(String file){
		ArrayList<Flashcard> output = new ArrayList<>();
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null){
				output.add(new Flashcard(line));
			}
		} catch (IOException exception) {
			System.out.println("Error trying to get flashcard list from file: " + file);
			System.out.println(exception.getMessage());
		}finally {
			if (br != null){
				try {
					br.close();
				} catch (IOException exception) {
					System.out.println("Error trying to close connection to file: " + file);
					System.out.println(exception.getMessage());
				}
			}
		}


		return output;
	}
}
