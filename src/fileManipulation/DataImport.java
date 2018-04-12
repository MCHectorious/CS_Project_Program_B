package fileManipulation;

import dataStructures.Flashcard;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class DataImport {
	public static ArrayList<String> getLinesFromTextFile(String filePath){
		ArrayList<String> lines = new ArrayList<>();//Initialises output
		BufferedReader bufferedReader = null;
		try{
			bufferedReader = new BufferedReader(new FileReader(filePath));//Gets access to the file
			String line;
		    while ((line = bufferedReader.readLine()) != null){//i.e. while there is still a line to read
		    	lines.add(line);
		    }
		} catch (IOException exception) {//For example if the file doesn't exist
			System.out.println("Error trying to access file: " + filePath);//Used to check whether the file actually exists
			System.out.println(exception.getMessage());//To help find the cause of the error
		}finally {
			if (bufferedReader != null){//Checks whether the buffered reader was initialised correctly
				try {
					bufferedReader.close();
				} catch (IOException exception) {//For example if the file doesn't exist
					System.out.println("Error trying to close connection to file: " + filePath);//Used to check whether the file actually exists
					System.out.println(exception.getMessage());//To help find the cause of the error
				}
			}
		}
		return lines;
	}
	public static double[] getDoubleArrayFromLine(String line) {
		String[] doublesAsStrings = line.split("\t");//Assumes that the array was tab-separated
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
			bufferedReader = new BufferedReader(new FileReader(filePath));//Gets access to the file
			String line;
		    while ((line = bufferedReader.readLine()) != null){//i.e. while there is still a line to read
		    	output = Double.parseDouble(line);
		    }
		} catch (IOException exception) {//For example if the file doesn't exist
			System.out.println("Error trying to get double from file: " + filePath);//Used to check whether the file actually exists
			System.out.println(exception.getMessage());//To help find the cause of the error
		}finally {
			if (bufferedReader != null){//Checks whether the buffered reader was initialised correctly
				try {
					bufferedReader.close();
				} catch (IOException exception) {//For example if the file doesn't exist
					System.out.println("Error trying to close connection to file: " + filePath);//Used to check whether the file actually exists
					System.out.println(exception.getMessage());//To help find the cause of the error
				}
			}
		}
		return output;
	}
	
	public static HashSet<String> getUniqueLinesFromTextFile(String filePath){
		HashSet<String> output = new HashSet<>();
		BufferedReader bufferedReader = null;
		try{
			bufferedReader = new BufferedReader(new FileReader(filePath));//Gets access to the file
			String line;
		    while ((line = bufferedReader.readLine()) != null){//i.e. while there is still a line to read
		    	output.add(line);
		    }
		} catch (IOException exception) {//For example if the file doesn't exist
			System.out.println("Error trying to access file: " + filePath);//Used to check whether the file actually exists
			System.out.println(exception.getMessage());//To help find the cause of the error
		}finally {
			if (bufferedReader != null){//Checks whether the buffered reader was initialised correctly
				try {
					bufferedReader.close();
				} catch (IOException exception) {//For example if the file doesn't exist
					System.out.println("Error trying to close connection to file: " + filePath);//Used to check whether the file actually exists
					System.out.println(exception.getMessage());//To help find the cause of the error
				}
			}
		}
		
		return output;
		
	}

	public static ArrayList<Flashcard> getFlashcardListFromTextFile(String file){
		ArrayList<Flashcard> output = new ArrayList<>();
		BufferedReader bufferedReader = null;
		try{
			bufferedReader = new BufferedReader(new FileReader(file));//Gets access to the file
			String line;
			while ((line = bufferedReader.readLine()) != null){//i.e. while there is still a line to read
				output.add(new Flashcard(line));
			}
		} catch (IOException exception) {//For example if the file doesn't exist
			System.out.println("Error trying to get flashcard list from file: " + file);//Used to check whether the file actually exists
			System.out.println(exception.getMessage());//To help find the cause of the error
		}finally {
			if (bufferedReader != null){//Checks whether the buffered reader was initialised correctly
				try {
					bufferedReader.close();
				} catch (IOException exception) {//For example if the file doesn't exist
					System.out.println("Error trying to close connection to file: " + file);//Used to check whether the file actually exists
					System.out.println(exception.getMessage());//To help find the cause of the error
				}
			}
		}


		return output;
	}
}
