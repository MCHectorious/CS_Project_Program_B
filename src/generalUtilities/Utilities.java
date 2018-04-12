package generalUtilities;

import models.Model;

import java.util.ArrayList;

public class Utilities {

	public static String arrayToString(Model[] array) {
		StringBuilder stringBuilder = new StringBuilder();//Uses a string builder for faster appending
		for (Model model : array) {
			stringBuilder.append(model.toString()).append(",");//Uses toString because using the provided description would take up too much space for some models.
		}
		return stringBuilder.toString();
	}
	
	
	public static String arrayToString(int[] array) {
		StringBuilder stringBuilder = new StringBuilder();//Uses a string builder for faster appending
		for (int i : array) {
			stringBuilder.append(i).append(",");//uses comma separated values because that is the standard in Java
		}
		return stringBuilder.toString();
	}
	
	public static String arrayToString(Character[] array) {
		StringBuilder stringBuilder = new StringBuilder();//Uses a string builder for faster appending
		for (Character character : array) {
			stringBuilder.append(character).append(",");//uses comma separated values because that is the standard in Java
		}
		return stringBuilder.toString();
	}
	
	public static String arrayToString(double[] array) {
		StringBuilder stringBuilder = new StringBuilder();//Uses a string builder for faster appending
		for (double d : array) {
			stringBuilder.append(d).append(",");//uses comma separated values because that is the standard in Java
		}
		return stringBuilder.toString();
	}

	public static String arrayToString(Double[] array) {
		StringBuilder builder = new StringBuilder();//Uses a string builder for faster appending
		for (Double d : array) {
			builder.append(d).append(",");//uses comma separated values because that is the standard in Java
		}
		return builder.toString();
	}

	public static double getUpperQuartile(ArrayList<Double> inputs) {
		ArrayList<Double> values = new QuickSort().sort(inputs);//Uses quick sort because it is very efficient
		return values.get(3*values.size()/4);//The upper quartile is the value of  3/4 from the smallest value
	}
	
    public static String convertToURLFormat(String string){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
        	switch(string.charAt(i)) {
        	case ' ':
        		stringBuilder.append("+");//Converts spaces into pluses
        		break;//So that is doesn't copy the original as well
        	case ',':
        		stringBuilder.append("%2C");//Converts commas into the string representing commas
        		break;//So that is doesn't copy the original as well
        	default:
        		stringBuilder.append(string.charAt(i));//Otherwise, just copy the string
        		break;
        	}
        }
        return stringBuilder.toString();
    }
	
    public static int countOfCharacterInString(Character c, String s){
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i)==c){
                count++;
            }
        }
        return count;
    }
    
}
