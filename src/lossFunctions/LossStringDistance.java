package lossFunctions;

import characterOperations.*;
import matrices.Vector;
import training.DataProcessing;

import java.util.ArrayList;

public class LossStringDistance implements Loss {

	private DataProcessing dataProcessing;
	
	public static void main(String[] args) {
		String[] strings1 = {"pleite<F_B_S>Skint/broke","Disco (m)<F_B_S>disc","tostada (f)<F_B_S>toast"};
		String[] strings2 = {"\"pleite\" means \"skint/broke\"","\"disco\" is the male word for \"disc\"","\"tostada\" is the female word for \"toast\""};
		String string1,string2;
		ArrayList<String> instructions;
		for(int i=0;i<strings1.length;i++) {
			string1 = strings1[i];
			string2 = strings2[i];
			
			instructions = generateInstructions(string1,string2);
			System.out.println(string1+" --> "+string2);
			for(int j=instructions.size()-1;j>=0;j--) {
				System.out.print(instructions.get(j)+"\t");
			}
			System.out.println();
		}
	}

	public LossStringDistance(DataProcessing DataPrep) {
		dataProcessing = DataPrep;
	}

	public static ArrayList<CharacterOperation> generateCharacterOperations(String x, String y) {
        int[][] characterEditsArray = new int[x.length() + 1][y.length() + 1];
        
        for(int i=0;i<=x.length();i++) {
        	characterEditsArray[i][0] = i;
        }
        for(int i=0;i<=y.length();i++) {
        	characterEditsArray[0][i] = i;
        }
        
        for (int i = 1; i <= x.length(); i++) {
            for (int j = 1; j <= y.length(); j++) {
                
                characterEditsArray[i][j] = min(characterEditsArray[i - 1][j - 1]
                     + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)), 
                     characterEditsArray[i - 1][j] + 1,
                     characterEditsArray[i][j - 1] + 1);
                
            }
		}

		ArrayList<CharacterOperation> instructions = new ArrayList<>();
        
        for(int i=x.length();i>0;) {
        	for(int j=y.length();j>0;) {
        		int substitutionCost = characterEditsArray[i-1][j-1];
        		int deletionCost = characterEditsArray[i-1][j];
        		int insertionCost = characterEditsArray[i][j-1];
        		
        		if(substitutionCost<insertionCost) {
                	if(deletionCost<substitutionCost) {
						instructions.add(new DeleteOperation());
                		i--;
                	}else {

                		if(x.charAt(i-1) == y.charAt(j-1)) {
							instructions.add(new CopyOperation());
                		}else {
							instructions.add(new SubstituteOperation(x.charAt(i - 1), y.charAt(j - 1)));
                			
                		}
                		i--;
                		j--;
                	}
                }else {
                	if(deletionCost<insertionCost) {
						instructions.add(new DeleteOperation());
                		i--;
                	}else {
						instructions.add(new InsertionOperation(y.charAt(j - 1)));
                		j--;
                	}
                }
        		
        		
        	}
        }
        return instructions;
	}

	private static ArrayList<String> generateInstructions(String x, String y) {
        int[][] characterEditsArray = new int[x.length() + 1][y.length() + 1];

        for(int i=0;i<=x.length();i++) {
        	characterEditsArray[i][0] = i;
        }
        for(int i=0;i<=y.length();i++) {
        	characterEditsArray[0][i] = i;
        }

        for (int i = 1; i <= x.length(); i++) {
            for (int j = 1; j <= y.length(); j++) {

				characterEditsArray[i][j] = min(characterEditsArray[i - 1][j - 1]
								+ costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)),
						characterEditsArray[i - 1][j] + 1,
                     characterEditsArray[i][j - 1] + 1);

            }
		}

		System.out.println(characterEditsArray[x.length()][y.length()]);

		ArrayList<String> instructions = new ArrayList<>();



        for(int i=x.length();i>0;) {
        	for(int j=y.length();j>0;) {
        		int substitutionCost = characterEditsArray[i-1][j-1];
        		int deletionCost = characterEditsArray[i-1][j];
        		int insertionCost = characterEditsArray[i][j-1];

        		if(substitutionCost<insertionCost) {
                	if(deletionCost<substitutionCost) {
						instructions.add("Delete " + x.charAt(i - 1));
                		i--;
                	}else {
						//System.out.println(i+" "+j);
                		if(x.charAt(i-1) == y.charAt(j-1)) {
							instructions.add("Copy " + x.charAt(i - 1));
                		}else {
							instructions.add("Substitute " + x.charAt(i - 1) + " for " + y.charAt(j - 1));

                		}
                		i--;
                		j--;
                	}
                }else {
                	if(deletionCost<insertionCost) {
						instructions.add("Delete " + x.charAt(i - 1));
                		i--;
                	}else {
						instructions.add("Insert " + y.charAt(j - 1));
                		j--;
                	}
                }


			}
        }
        return instructions;
	}
	
	@Override
	public double measureLoss(Vector actualOutput, Vector targetOutput) {
		String actualString = dataProcessing.doubleArrayToString(actualOutput.getData());
		String targetString = dataProcessing.doubleArrayToString(targetOutput.getData());
		
		int maxLength = Math.max(actualString.length(), targetString.length());
		if(maxLength == 0) {
			return 0.0;
		}	
		return (getDistance(actualString,targetString)/maxLength);
	}

	private static int costOfSubstitution(char a, char b) {
		return a == b ? 0 : 1;
	}
	
    private static double getDistance(String x, String y){
    	//System.out.println("Got this far");
        int[][] characterEditsArray = new int[x.length() + 1][y.length() + 1];
        
        for(int i=0;i<=x.length();i++) {
        	characterEditsArray[i][0] = i;
        }
        for(int i=0;i<=y.length();i++) {
        	characterEditsArray[0][i] = i;
        }
        
        for (int i = 1; i <= x.length(); i++) {
            for (int j = 1; j <= y.length(); j++) {
                
                int substitutionCost = characterEditsArray[i - 1][j - 1]
                        + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1));
                int insertionCost = characterEditsArray[i - 1][j] + 1;
                int deletionCost = characterEditsArray[i][j - 1] + 1;
                
                if(substitutionCost<insertionCost) {
                	if(deletionCost<substitutionCost) {
                		characterEditsArray[i][j] = deletionCost;
                		//System.out.print("Delete "+x.charAt(i)+y.charAt(j)+" ");
                	}else {
                		characterEditsArray[i][j] = substitutionCost;
                		//System.out.print("Substitution "+x.charAt(i)+y.charAt(j)+" ");
                	}
                }else {
                	if(deletionCost<insertionCost) {
                		characterEditsArray[i][j] = deletionCost;
                		//System.out.print("Delete "+x.charAt(i)+y.charAt(j)+" ");
                	}else {
                		characterEditsArray[i][j] = insertionCost;
                		//System.out.print("Insertion "+x.charAt(i)+y.charAt(j)+" ");
                	}
                }
                	
                    //characterEditsArray[i][j] = min(characterEditsArray[i - 1][j - 1]
                     //+ costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)), 
                      //characterEditsArray[i - 1][j] + 1,
                      //characterEditsArray[i][j - 1] + 1);
                
            }
        }
     
        //System.out.println();
        
        return characterEditsArray[x.length()][y.length()];


    }

	public double measure(String actualOutput, String targetOutput) {
		int maxLength = Math.max(actualOutput.length(), targetOutput.length());
		if (maxLength == 0) {
			return 0.0;
		}
		//System.out.println("String Distance Being Calculated");
		return getDistance(actualOutput, targetOutput);
		//return (getDistance(actualOutput,targetOutput)/maxLength);
	}
    
    private static int min(int a, int b, int c){
        return Math.min(a,Math.min(b,c));
    }

}
