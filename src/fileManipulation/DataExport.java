package fileManipulation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

public class DataExport {

	public static void overwriteTextFile(StringBuilder stringBuilder, String file) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {//By trying as  resource, it is automatically closed, if if an error occurs
			out.print(stringBuilder.toString());
        } catch (IOException exception) {//For example if the specified file doesn't exist
            System.out.println("Error trying to overwrite file: " + file);//Used to check whether the file actually exists
            System.out.println(exception.getMessage());//To help find the cause of the error
		}
	}
	
	public static void overwriteTextFile(double value, String filePath) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))) {//By trying as  resource, it is automatically closed, if if an error occurs
            out.print(Double.toString(value));
        } catch (IOException exception) {//For example if the specified file doesn't exist
            System.out.println("Error trying to overwrite file: " + filePath);//Used to check whether the file actually exists
            System.out.println(exception.getMessage());//To help find the cause of the error
		}
	}

	public static void overwriteTextFile(HashSet<String> stringHashSet, String filePath) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))) {//By trying as  resource, it is automatically closed, if if an error occurs
            for (String line : stringHashSet) {
				out.println(line);
			}
        } catch (IOException exception) {//For example if the specified file doesn't exist
            System.out.println("Error trying to overwrite file: " + filePath);//Used to check whether the file actually exists
            System.out.println(exception.getMessage());//To help find the cause of the error
		}
	}

    public static void appendToTextFile(String stringToAppend, String filePath) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)))) {//By trying as  resource, it is automatically closed, if if an error occurs
            out.println(stringToAppend);
        } catch (IOException exception) {//For example if the specified file doesn't exist
            System.out.println("Error trying to append to file: " + filePath);//Used to check whether the file actually exists
            System.out.println(exception.getMessage());//To help find the cause of the error
        }
    }

}
