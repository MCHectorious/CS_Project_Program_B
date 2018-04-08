package generalUtilities;

import models.Model;

import java.util.ArrayList;

public class Utilities {

	public static String arrayToString(Model[] array) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Model model : array) {
			stringBuilder.append(model.toString()).append(",");
		}
		return stringBuilder.toString();
	}
	
	
	public static String arrayToString(int[] array) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i : array) {
			stringBuilder.append(i).append(",");
		}
		return stringBuilder.toString();
	}
	
	public static String arrayToString(Character[] array) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Character character : array) {
			stringBuilder.append(character).append(",");
		}
		return stringBuilder.toString();
	}
	
	public static String arrayToString(double[] array) {
		StringBuilder stringBuilder = new StringBuilder();
		for (double d : array) {
			stringBuilder.append(d).append(",");
		}
		return stringBuilder.toString();
	}

	public static String arrayToString(Double[] array) {
		StringBuilder builder = new StringBuilder();
		for (Double d : array) {
			builder.append(d).append(",");
		}
		return builder.toString();
	}

	public static double getUpperQuartile(ArrayList<Double> inputs) {
		ArrayList<Double> values = new QuickSort().sort(inputs);
		return values.get(3*values.size()/4);
	}
	
    public static String convertToURLFormat(String string){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
        	switch(string.charAt(i)) {
        	case ' ':
        		stringBuilder.append("+");
        		break;
        	case ',':
        		stringBuilder.append("%2C");
        		break;
        	default:
        		stringBuilder.append(string.charAt(i));
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
