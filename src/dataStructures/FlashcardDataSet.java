package dataStructures;

import fileManipulation.DataImport;
import generalUtilities.CustomRandom;
import generalUtilities.Utilities;
import lossFunctions.LossStringDistance;
import matrices.Vector;
import models.Model;
import training.DataProcessing;

import java.util.ArrayList;
import java.util.List;

public class FlashcardDataSet implements DataSet{
	
	private DataSequence training = new DataSequence();
	private DataSequence testing = new DataSequence();
    private final static String border = "==============================================================================================";
    private final double ReciprocalOfTrainingSize;
	private CustomRandom util;
    private DataProcessing dataPrep;
    private Vector modelOutput = new Vector(DataProcessing.FIXED_VECTOR_SIZE);
    private Vector modelInput = new Vector(DataProcessing.FIXED_VECTOR_SIZE);
	private LossStringDistance stringLoss;
	
	public int getTrainingSize() {
		return training.getSize();
	}
	
	public double getReciprocalOfTrainingSize() {
		return ReciprocalOfTrainingSize;
	}

	public List<DataStep> getTrainingDataSteps() {
		return training.getDataSteps();
	}
	
	public List<DataStep> getTestingDataSteps() {
		return testing.getDataSteps();
	}
	
	public int getTestingSize() {
		return testing.getSize();
	}
	
	public FlashcardDataSet(String path, CustomRandom utility) {
		util = utility;

		ArrayList<String> lines = DataImport.getLines(path);
        dataPrep = new DataProcessing(lines);
		stringLoss = new LossStringDistance(dataPrep);

		//ArrayList<Map<Character,Integer>> flashcardCounts = new ArrayList<>();
		//ArrayList<Map<Character,Integer>> translationCounts = new ArrayList<>();


		//int tempCounter = 0;
		for(String line: lines) {
			//System.out.println(line);

			/*String flashcard = Flashcard.getFlashcard(line);
			String translation = Flashcard.getFlashcard(line);
			for(int i=0;i<flashcard.length();i++) {
				if(flashcardCounts.size()==i) {
					flashcardCounts.add(new HashMap<Character,Integer>());
				}
				boolean found = false;
				for(Character c:flashcardCounts.get(i).keySet()) {
					if(c.charValue()==flashcard.charAt(i)) {
						int prevValue = flashcardCounts.get(i).get(c);
						flashcardCounts.get(i).remove(c);
						flashcardCounts.get(i).put(c, prevValue+1);
						found = true;
						break;
					}
				}
				if(!found) {
					flashcardCounts.get(i).put(flashcard.charAt(i), 1);
				}
			}
			for(int i=0;i<translation.length();i++) {
				if(translationCounts.size()==i) {
					translationCounts.add(new HashMap<Character,Integer>());
				}
				boolean found = false;
				for(Character c:translationCounts.get(i).keySet()) {
					if(c.charValue()==translation.charAt(i)) {
						int prevValue = translationCounts.get(i).get(c);
						translationCounts.get(i).remove(c);
						translationCounts.get(i).put(c, prevValue+1);
						found = true;
						break;
					}
				}
				if(!found) {
					translationCounts.get(i).put(translation.charAt(i), 1);
				}
			}*/

			/*for(int i=0;i<3;i++) {
				double[] FlashcardDoubleArray = dataPrep.stringToDoubleArray(Flashcard.getFlashcard(line));
				System.out.println("Input: "+Utilities.arrayToString(FlashcardDoubleArray));
			}
			for(int i=0;i<3;i++) {
				double[] TranslationDoubleArray = dataPrep.stringToDoubleArray(Flashcard.getFlashcard(line));
				System.out.println("Output: "+Utilities.arrayToString(TranslationDoubleArray));
			}*/
			double[] FlashcardDoubleArray = dataPrep.stringToDoubleArray(Flashcard.getFlashcard(line));
			double[] TranslationDoubleArray = dataPrep.stringToDoubleArray(Flashcard.getTranslation(line));
            //DataExport.appendToTextFile(Utilities.padArrayWithTabs(FlashcardDoubleArray)+"\t"+Utilities.padArrayWithTabs(TranslationDoubleArray), "Models/Data Numerical.txt");
			if(util.randomBoolean() || util.randomBoolean()) {
				//DataStep dataStep = new DataStep()//System.out.println( new Vector(FlashcardDoubleArray).toString() );
				//training.addDataStep(FlashcardDoubleArray, TranslationDoubleArray,Flashcard.getFlashcard(line),Flashcard.getTranslation(line));
				//System.out.print(Flashcard.getFlashcard(line)+Flashcard.getTranslation(line));
				training.addDataStepsWithCapitilisationVariation(FlashcardDoubleArray, TranslationDoubleArray,Flashcard.getFlashcard(line),Flashcard.getTranslation(line),dataPrep);
				//System.out.println("Training: "+line);

				//System.out.println(tempCounter++);
				//System.out.println( new DataStep(FlashcardDoubleArray,TranslationDoubleArray).input.toString());
				//System.out.println( (new Vector(FlashcardDoubleArray)).toString() );
			}else {
				//System.out.println("Testing: "+line);
				testing.addDataStep(FlashcardDoubleArray, TranslationDoubleArray,Flashcard.getFlashcard(line),Flashcard.getTranslation(line));
			}
		}
		System.out.println("Total phrases = " + dataPrep.getNumOfPhrases());
		System.out.println(training.getSize() + " steps in training set");
		System.out.println(testing.getSize() + " steps in testing set");
		ReciprocalOfTrainingSize = 1.0/training.getSize();
		//System.out.println("Training Size: "+training.getSize());
		//System.out.println(String.format("%.15f", ReciprocalOfTrainingSize));

        //for(int i=0;i<training.getSize();i++) {
			//StringBuilder builder = new StringBuilder();
			//training.getDataSteps().get(i).getInputVector().toString(builder);
			//System.out.println(builder.toString());

        //}

		/*DataExport.appendToTextFile("Flashcard Values:\n", "Models/Temp.txt");
		for(int i=0;i<flashcardCounts.size();i++) {
			StringBuilder tempBuilder = new StringBuilder();
			for(Character c: flashcardCounts.get(i).keySet()) {
				tempBuilder.append(c).append("\t").append(flashcardCounts.get(i).get(c)).append("\t");
			}

			DataExport.appendToTextFile(tempBuilder.toString(), "Models/Temp.txt");
		}
		DataExport.appendToTextFile("Translation Values:\n", "Models/Temp.txt");
		for(int i=0;i<translationCounts.size();i++) {
			StringBuilder tempBuilder = new StringBuilder();
			for(Character c: translationCounts.get(i).keySet()) {
				tempBuilder.append(c).append("\t").append(translationCounts.get(i).get(c)).append("\t");
			}

			DataExport.appendToTextFile(tempBuilder.toString(), "Models/Temp.txt");
		}*/


    }

    public DataProcessing getDataPrep() {
        return dataPrep;
    }
	
	@Override
	public void DisplayReport(Model model) {
		DataStep example =  testing.getRandom(util) ;


		StringBuilder builder = new StringBuilder(300);

		builder.append(border+"\nReport:\n");

		builder.append("Input: ").append(example.getInputText()).append("\n");

        builder.append(Utilities.arrayToString(example.getInput())).append("\n");
		
		builder.append("Expected Output: \t").append( example.getOutputText()).append("\n");

        builder.append(Utilities.arrayToString(example.getTargetOutput())).append("\n");

		
		modelInput.setData(example.getInput());


        model.run(new DataStep(modelInput), modelOutput);
		
		String modelOutputString = dataPrep.doubleArrayToString(modelOutput.getData());

		builder.append("Actual Output: \t\t").append(modelOutputString ).append("\n");

        builder.append(Utilities.arrayToString(modelOutput.getData())).append("\n");
		
				
		builder.append("String Distance: ").append(stringLoss.measure(modelOutputString, example.getOutputText()));
		
		builder.append("\n").append(border);


		System.out.println(builder.toString());
		
	}


}
