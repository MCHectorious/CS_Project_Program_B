package generalUtilities;

import models.Model;

import java.util.ArrayList;

public class Utilities {

	public static boolean isAllZeroes(double[] array) {
		boolean output = true;
		for (double d : array) {
			if (d != 0) {
				output = false;
				break;
			}
		}
		return output;
	}
	
	public static double log2(double x) {
		return Math.log(x)*1.44269504089;
	}
	
	
	public static String arrayToString(Model[] array) {
		StringBuilder builder = new StringBuilder();
		for (Model model : array) {
			builder.append(model.toString()).append(",");
		}
		return builder.toString();
	}
	
	
	public static String arrayToString(int[] array) {
		StringBuilder builder = new StringBuilder();
		for (int i : array) {
			builder.append(i).append(",");
		}
		return builder.toString();
	}
	
	public static String arrayToString(Character[] array) {
		StringBuilder builder = new StringBuilder();
		for (Character character : array) {
			builder.append(character).append(",");
		}
		return builder.toString();
	}
	
	public static String arrayToString(double[] array) {
		StringBuilder builder = new StringBuilder();
		for (double d : array) {
			builder.append(d).append(",");
		}
		return builder.toString();
	}

	public static String arrayToString(Double[] array) {
		StringBuilder builder = new StringBuilder();
		for (Double d : array) {
			builder.append(d).append(",");
		}
		return builder.toString();
	}
	
	public static String padArrayWithTabs(double[] array) {
		StringBuilder builder = new StringBuilder();
		for (double d : array) {
			builder.append(d).append("\t");
		}
		return builder.toString();
	}

	public static double getLowerQuartile(ArrayList<Double> inputs) {
		ArrayList<Double> values = new QuickSort().sort(inputs);
		return values.get(values.size()/4);
	}
	
	public static double getUpperQuartile(ArrayList<Double> inputs) {
		ArrayList<Double> values = new QuickSort().sort(inputs);
		return values.get(3*values.size()/4);
	}
	
    public static String convertToURLFormat(String string){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
        	switch(string.charAt(i)) {
        	case ' ':
        		builder.append("+");
        		break;
        	case ',':
        		builder.append("%2C");
        		break;
        	default:
        		builder.append(string.charAt(i));
        		break;
        	}
        }
        return builder.toString();
    }
	
    public static int countOfCharInString(Character c,String s){
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i)==c){
                count++;
            }
        }
        return count;
    }
    
}
