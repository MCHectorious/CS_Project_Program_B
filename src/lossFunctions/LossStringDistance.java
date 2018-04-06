package lossFunctions;

import java.util.ArrayList;

import characterOperations.*;
import matrices.Vector;
import training.DataPreparation;

public class LossStringDistance implements Loss {
	
	private DataPreparation dataPrep;
	
	public static void main(String[] args) {
		String[] strings1 = {"pleite<F_B_S>Skint/broke","Disco (m)<F_B_S>disc","tostada (f)<F_B_S>toast"};
		String[] strings2 = {"\"pleite\" means \"skint/broke\"","\"disco\" is the male word for \"disc\"","\"tostada\" is the female word for \"toast\""};
		String String1,String2;
		ArrayList<String> instructions;
		for(int i=0;i<strings1.length;i++) {
			String1 = strings1[i];
			String2 = strings2[i];
			
			instructions = generateInstructions(String1,String2);
			System.out.println(String1+" --> "+String2);
			for(int j=instructions.size()-1;j>=0;j--) {
				System.out.print(instructions.get(j)+"\t");
			}
			System.out.println();
		}
	}
	
	public static ArrayList<String> generateInstructions(String x, String y){
        int[][] dp = new int[x.length() + 1][y.length() + 1];
        
        for(int i=0;i<=x.length();i++) {
        	dp[i][0] = i;
        }
        for(int i=0;i<=y.length();i++) {
        	dp[0][i] = i;
        }
        
        for (int i = 1; i <= x.length(); i++) {
            for (int j = 1; j <= y.length(); j++) {
                
                dp[i][j] = min(dp[i - 1][j - 1] 
                     + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)), 
                     dp[i - 1][j] + 1, 
                     dp[i][j - 1] + 1);
                
            }
        }
        
        System.out.println(dp[x.length()][y.length()]);
	
        ArrayList<String> instructions = new ArrayList<>();
        

        
        for(int i=x.length();i>0;) {
        	for(int j=y.length();j>0;) {
        		int substitutionCost = dp[i-1][j-1];
        		int deletionCost = dp[i-1][j];
        		int insertionCost = dp[i][j-1];
        		
        		if(substitutionCost<insertionCost) {
                	if(deletionCost<substitutionCost) {
                		instructions.add("Delete "+x.charAt(i-1));
                		i--;
                	}else {
                		//System.out.println(i+" "+j);
                		if(x.charAt(i-1) == y.charAt(j-1)) {
                			instructions.add("Copy "+x.charAt(i-1));
                		}else {
                			instructions.add("Substitute "+x.charAt(i-1)+" for "+y.charAt(j-1));
                			
                		}
                		i--;
                		j--;
                	}
                }else {
                	if(deletionCost<insertionCost) {
                		instructions.add("Delete "+x.charAt(i-1));
                		i--;
                	}else {
                		instructions.add("Insert "+y.charAt(j-1));
                		j--;
                	}
                }
        		
        		
        	}
        }
        return instructions;
	}
	
	public static ArrayList<CharacterOperation> generateCharacterOperations(String x, String y){
        int[][] dp = new int[x.length() + 1][y.length() + 1];
        
        for(int i=0;i<=x.length();i++) {
        	dp[i][0] = i;
        }
        for(int i=0;i<=y.length();i++) {
        	dp[0][i] = i;
        }
        
        for (int i = 1; i <= x.length(); i++) {
            for (int j = 1; j <= y.length(); j++) {
                
                dp[i][j] = min(dp[i - 1][j - 1] 
                     + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)), 
                     dp[i - 1][j] + 1, 
                     dp[i][j - 1] + 1);
                
            }
        }	
        
        ArrayList<CharacterOperation> instructions = new ArrayList<>();
        
        for(int i=x.length();i>0;) {
        	for(int j=y.length();j>0;) {
        		int substitutionCost = dp[i-1][j-1];
        		int deletionCost = dp[i-1][j];
        		int insertionCost = dp[i][j-1];
        		
        		if(substitutionCost<insertionCost) {
                	if(deletionCost<substitutionCost) {
                		instructions.add(new DeleteOperation());
                		i--;
                	}else {

                		if(x.charAt(i-1) == y.charAt(j-1)) {
                			instructions.add(new CopyOperation());
                		}else {
                			instructions.add(new SubstituteOperation(x.charAt(i-1),y.charAt(j-1)));
                			
                		}
                		i--;
                		j--;
                	}
                }else {
                	if(deletionCost<insertionCost) {
                		instructions.add(new DeleteOperation());
                		i--;
                	}else {
                		instructions.add(new InsertionOperation(y.charAt(j-1)) );
                		j--;
                	}
                }
        		
        		
        	}
        }
        return instructions;
	}
	
	public LossStringDistance(DataPreparation DataPrep) {
		dataPrep = DataPrep;
	}
	
	@Override
	public double measure(Vector actualOutput, Vector targetOutput) {
		String actualString = dataPrep.doubleArrayToString(actualOutput.getData());
		String targetString = dataPrep.doubleArrayToString(targetOutput.getData());
		
		int MaxLength = Math.max(actualString.length(), targetString.length());
		if(MaxLength == 0) {
			return 0.0;
		}	
		return (getDistance(actualString,targetString)/MaxLength);
	}

	
	public static double measure(String actualOutput, String targetOutput) {
		int MaxLength = Math.max(actualOutput.length(), targetOutput.length());
		if(MaxLength == 0) {
			return 0.0;
		}	
		//System.out.println("String Distance Being Calculated");
		return getDistance(actualOutput,targetOutput);
		//return (getDistance(actualOutput,targetOutput)/MaxLength);
	}
	
    private static double getDistance(String x, String y){
    	//System.out.println("Got this far");
        int[][] dp = new int[x.length() + 1][y.length() + 1];
        
        for(int i=0;i<=x.length();i++) {
        	dp[i][0] = i;
        }
        for(int i=0;i<=y.length();i++) {
        	dp[0][i] = i;
        }
        
        for (int i = 1; i <= x.length(); i++) {
            for (int j = 1; j <= y.length(); j++) {
                
                int substitution = dp[i - 1][j - 1] 
                        + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1));
                int insertion = dp[i - 1][j] + 1;
                int deletion = dp[i][j - 1] + 1;
                
                if(substitution<insertion) {
                	if(deletion<substitution) {
                		dp[i][j] = deletion;
                		//System.out.print("Delete "+x.charAt(i)+y.charAt(j)+" ");
                	}else {
                		dp[i][j] = substitution;
                		//System.out.print("Substitution "+x.charAt(i)+y.charAt(j)+" ");
                	}
                }else {
                	if(deletion<insertion) {
                		dp[i][j] = deletion;
                		//System.out.print("Delete "+x.charAt(i)+y.charAt(j)+" ");
                	}else {
                		dp[i][j] = insertion;
                		//System.out.print("Insertion "+x.charAt(i)+y.charAt(j)+" ");
                	}
                }
                	
                    //dp[i][j] = min(dp[i - 1][j - 1] 
                     //+ costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)), 
                      //dp[i - 1][j] + 1, 
                      //dp[i][j - 1] + 1);
                
            }
        }
     
        //System.out.println();
        
        return dp[x.length()][y.length()];


    }

    public static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }
    
    private static int min(int a, int b, int c){
        return Math.min(a,Math.min(b,c));
    }

}
