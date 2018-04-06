package generalUtilities;

import java.util.ArrayList;
import java.util.Collections;

import models.Model;

public class Util {

	public static boolean isAllZeroes(double[] array) {
		boolean output = true;
		for(int i=0;i<array.length;i++) {
			if(array[i]!=0) {
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
		for(int i=0;i<array.length;i++) {
			builder.append(array[i].toString()).append(",");
		}
		return builder.toString();
	}
	
	
	public static String arrayToString(int[] array) {
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<array.length;i++) {
			builder.append(array[i]).append(",");
		}
		return builder.toString();
	}
	
	public static String arrayToString(Character[] array) {
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<array.length;i++) {
			builder.append(array[i]).append(",");
		}
		return builder.toString();
	}
	
	public static String arrayToString(double[] array) {
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<array.length;i++) {
			builder.append(array[i]).append(",");
		}
		return builder.toString();
	}
	
	public static String padArrayWithTabs(double[] array) {
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<array.length;i++) {
			builder.append(array[i]).append("\t");
		}
		return builder.toString();
	}

	public static double getLowerQuartile(ArrayList<Double> inputs) {
		ArrayList<Double> values = inputs;
		Collections.sort(values);
		return values.get(values.size()/4);
	}
	
	public static double getUpperQuartile(ArrayList<Double> inputs) {
		ArrayList<Double> values = inputs;
		Collections.sort(values);
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
