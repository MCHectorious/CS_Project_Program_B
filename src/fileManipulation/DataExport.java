package fileManipulation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

public class DataExport {

	public static void overwriteTextFile(StringBuilder stringBuilder, String file) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
			out.print(stringBuilder.toString());
        } catch (IOException exception) {
            System.out.println("Error trying to overwrite file: " + file);
            System.out.println(exception.getMessage());
		}
	}
	
	public static void overwriteTextFile(double value, String filePath) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))) {
            out.print(Double.toString(value));
        } catch (IOException exception) {
            System.out.println("Error trying to overwrite file: " + filePath);
            System.out.println(exception.getMessage());
		}
	}
	
	public static void appendToTextFile(String stringToAppend, String filePath) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)))) {
			out.println(stringToAppend);
        } catch (IOException exception) {
            System.out.println("Error trying to append to file: " + filePath);
            System.out.println(exception.getMessage());
		}
	}
	
	public static void overwriteTextFile(HashSet<String> stringHashSet, String filePath) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))) {
            for (String line : stringHashSet) {
				out.println(line);
			}
        } catch (IOException exception) {
            System.out.println("Error trying to overwrite file: " + filePath);
            System.out.println(exception.getMessage());
		}
	}
	
}
