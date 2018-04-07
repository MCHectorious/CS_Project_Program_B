package fileManipulation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

public class DataExport {

	public static void overwriteToTextFile(StringBuilder builder, String file) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
			out.print(builder.toString());
        } catch (IOException e) {
            System.out.println("Error trying to overwrite file: " + file);
            System.out.println(e.getMessage());
		}
	}
	
	public static void overwriteToTextFile(double value, String file) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
            out.print(Double.toString(value));
        } catch (IOException e) {
            System.out.println("Error trying to overwrite file: " + file);
            System.out.println(e.getMessage());
		}
	}
	
	public static void appendToTextFile(String data, String file) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
			out.println(data);
        } catch (IOException e) {
            System.out.println("Error trying to append to file: " + file);
            System.out.println(e.getMessage());
		}
	}
	
	public static void overwriteToTextFile(HashSet<String> data, String file) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
            for (String line : data) {
				out.println(line);
			}
        } catch (IOException exception) {
            System.out.println("Error trying to overwrite file: " + file);
            System.out.println(exception.getMessage());
		}
	}
	
}
