package fileManipulation;

import dataStructures.Flashcard;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class DataImport {
	public static ArrayList<String> getLinesFromTextFile(String filePath){
		ArrayList<String> lines = new ArrayList<>();
		BufferedReader bufferedReader = null;
		try{
			bufferedReader = new BufferedReader(new FileReader(filePath));
			String line;
		    while ((line = bufferedReader.readLine()) != null){
		    	lines.add(line);
		    }
		} catch (IOException exception) {
			System.out.println("Error trying to access file: " + filePath);
			System.out.println(exception.getMessage());
		}finally {
			if (bufferedReader != null){
				try {
					bufferedReader.close();
				} catch (IOException exception) {
					System.out.println("Error trying to close connection to file: " + filePath);
					System.out.println(exception.getMessage());
				}
			}
		}
		return lines;
	}
	public static double[] getDoubleArrayFromLine(String line) {
		String[] doublesAsStrings = line.split("\t");
		double[] outputDoubleArray = new double[doublesAsStrings.length];
		for(int i=0;i<doublesAsStrings.length;i++) {
			outputDoubleArray[i] = Double.parseDouble( doublesAsStrings[i]);
		}
		return outputDoubleArray;
	}
	public static double getDoubleFromFile(String filePath) {
		double output = 0;
		BufferedReader bufferedReader = null;
		try{
			bufferedReader = new BufferedReader(new FileReader(filePath));
			String line;
		    while ((line = bufferedReader.readLine()) != null){
		    	output = Double.parseDouble(line);
		    }
		} catch (IOException exception) {
			System.out.println("Error trying to get double from file: " + filePath);
			System.out.println(exception.getMessage());
		}finally {
			if (bufferedReader != null){
				try {
					bufferedReader.close();
				} catch (IOException exception) {
					System.out.println("Error trying to close connection to file: " + filePath);
					System.out.println(exception.getMessage());
				}
			}
		}
		return output;
	}
	
	public static HashSet<String> getUniqueLinesFromTextFile(String filePath){
		HashSet<String> output = new HashSet<>();
		BufferedReader bufferedReader = null;
		try{
			bufferedReader = new BufferedReader(new FileReader(filePath));
			String line;
		    while ((line = bufferedReader.readLine()) != null){
		    	output.add(line);
		    }
		} catch (IOException exception) {
			System.out.println("Error trying to access file: " + filePath);
			System.out.println(exception.getMessage());
		}finally {
			if (bufferedReader != null){
				try {
					bufferedReader.close();
				} catch (IOException exception) {
					System.out.println("Error trying to close connection to file: " + filePath);
					System.out.println(exception.getMessage());
				}
			}
		}
		
		return output;
		
	}

	public static ArrayList<Flashcard> getFlashcardListFromTextFile(String file){
		ArrayList<Flashcard> output = new ArrayList<>();
		BufferedReader bufferedReader = null;
		try{
			bufferedReader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = bufferedReader.readLine()) != null){
				output.add(new Flashcard(line));
			}
		} catch (IOException exception) {
			System.out.println("Error trying to get flashcard list from file: " + file);
			System.out.println(exception.getMessage());
		}finally {
			if (bufferedReader != null){
				try {
					bufferedReader.close();
				} catch (IOException exception) {
					System.out.println("Error trying to close connection to file: " + file);
					System.out.println(exception.getMessage());
				}
			}
		}


		return output;
	}
}
