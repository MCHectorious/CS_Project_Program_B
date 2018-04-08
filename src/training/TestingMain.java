package training;


public class TestingMain {

	public static void main(String[] args) {
		System.out.println("Hello");
		System.out.println(convertFlashcardToSentence("What is data?","Raw, unorganized facts that are yet to be processed"));
		
	
		
		/*
		int [] layerTypeSet = {0,1};
		int [] hiddenDimensionSet = {1,2,3,4,5,6,7,8,9,10,15,20,30,40,50,75,100,110,125,150};
		
		
		int maxArraySize = 3;
		
		for(int j=2;j<=maxArraySize;j++) {
			int[][] layerTypes = new int[(int) Math.pow(layerTypeSet.length, j)][j];
			for(int i=0;i<=Math.pow(layerTypeSet.length, j)-1;i++) {
				for(int k=0;k<j;k++) {
					//System.out.println(i+" "+Math.pow(layerTypeSet.length, k)+" "+(int) Math.floor((i)/(int) Math.floor(Math.pow(layerTypeSet.length, k)) ) );
					layerTypes[i][k] =  layerTypeSet[ (int) Math.floor((i)/(int) Math.floor(Math.pow(layerTypeSet.length, k)) ) % layerTypeSet.length ]; 
					//System.out.println(i+" "+(int) (Math.pow(layerTypeSet.length, k))+" "+(i % (int) (Math.pow(layerTypeSet.length, k))));
					//layerTypes[i][k] =  (i % (int) (Math.pow(layerTypeSet.length, k)));
				}
			}
			
			for(int i=0;i<layerTypes.length;i++) {
				System.out.println(Utilities.arrayToString(layerTypes[i]));
			}
			
			int[][] hiddenDims = new int[(int) Math.pow(hiddenDimensionSet.length, j-1)][j-1];
			for(int i=0;i<=Math.pow(hiddenDimensionSet.length, j-1)-1;i++) {
				for(int k=0;k<j-1;k++) {
					hiddenDims[i][k] =  hiddenDimensionSet[ (int) Math.floor((i)/(int) Math.floor(Math.pow(hiddenDimensionSet.length, k)) ) % hiddenDimensionSet.length ];
				}
			}
			
			for(int i=0;i<hiddenDims.length;i++) {
				System.out.println(Utilities.arrayToString(hiddenDims[i]));
			}
			
		}
		
		
		
		//System.out.println('ö' == 'ö');
		//Character a = 'ö';
		//String s = "höle";
		//Character b = s.charAt(1);
		//System.out.println(a == b);
		/*
		CustomRandom util = new CustomRandom();
		
		//TestingDataSet data = new TestingDataSet(util);
		
		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", util);
		
		String savePath = "Models/CardToSentence.txt";

		//LinearLayer model = new LinearLayer(new double[] {1.01,2.01,3.01,4.01,5.01,6.01,7.01,8.01,9.01}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);
		
		//ArrayList<Layer> layers = new ArrayList<>();
		//layers.add(new LinearLayer(new double[] {0.01,0.01,0.01,0.01,0.01,0.01,0.01,0.01,0.01,0.01}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR));
		
		//layers.add(new FeedForwardLayer(new double[] {1.01,2.01,2.01,3.01,3.01,3.01,4.01,4.01,4.01}, new double[] {-1.01,2.01,-3.01}, new RoughTanhUnit()));
		
		//Model model = new NeuralNetworkModel(layers);
		
		//Model model = new NeuralNetworkModel(4, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, 15, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, util);
		
		//Model model = new AverageModel(data.getTrainingDataSteps());
		
		Model model = new CharacterManipulationFromStringDistanceModel(data.getTrainingDataSteps(), data.getDataProcessing(), util);
		
		
		/*StringBuilder builder  = new StringBuilder();
		
		Vector output = new Vector(3);
		
		model.run(new Vector(new double[] {0.25,-0.75,-0.25}), output );
		output.toString(builder);
		builder.append("\n");
		model.run(new Vector(new double[] {-0.25,-0.75,-0.25}), output );
		output.toString(builder);
		builder.append("\n");
		model.run(new Vector(new double[] {-0.15,-0.65,-0.95}), output );
		output.toString(builder);
		builder.append("\n");
		model.run(new Vector(new double[] {-0.85,-0.35,-0.995}), output );
		output.toString(builder);
		builder.append("\n");
		model.run(new Vector(new double[] {-0.65,-0.765,-0.765}), output );
		output.toString(builder);
		builder.append("\n");
		model.run(new Vector(new double[] {-0.125,-0.715,-0.85}), output );
		output.toString(builder);
		builder.append("\n");
		System.out.println(builder.toString());
		*/
		
		//FeedForwardLayer model = new FeedForwardLayer(3, 3, new RoughTanhUnit(), util);
		
		/*ArrayList<Model> models = new ArrayList<>();
		AdvancedCopyingModel model1 = new AdvancedCopyingModel(1, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);
		models.add(model1);
		models.add(new ProportionProbabilityForCharacterModel(data.getTrainingDataSteps(), 1, data.getDataProcessing(), util));
		models.add(new BasicCopyingModel());
		
		Model model = new StackingEnsembleModel(models, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, util);
		*/

        //NeuralNetworkModel model2 = new NeuralNetworkModel(3, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, 10, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, util);
		//models.add(model2);

        //AveragingEnsembleModel model = new AveragingEnsembleModel(models, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);


        //Model model = new ProportionProbabilityForCharacterModel(data.getTrainingDataSteps(), 1, data.getDataProcessing(), util);
		
		/*
		int numOfTrainingEpochs = 1000;
		int displayReportPeriod = 20;
		int showEpochPeriod = 5;
		int checkMinimumPeriod = 20;

		
		double lossOriginal = (new ModelTrainer(0.001,0.9999,0.99999,0.1)).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod , savePath, util);
		
		//System.out.println("Original Loss: "+lossOriginal);
		
		DataSplitOperation splitOp = DataSplitter.getBestDataSplit(data.getTrainingDataSteps(), model, data.getDataProcessing());
		
		ArrayList<DataStep> goodValues = DataSplitter.getStepsInSplit(data.getTrainingDataSteps(), splitOp);
		ArrayList<DataStep> badValues = DataSplitter.getStepsNotInSplit(data.getTrainingDataSteps(), splitOp);
	
		
		Model model1 = new CharacterManipulationFromStringDistanceModel(goodValues, data.getDataProcessing(), util);
		Model model2 = new ProportionProbabilityForCharacterModel(badValues, 2, data.getDataProcessing(), util);
		
		double loss1 = (new ModelTrainer(0.001,0.9999,0.99999,0.1)).train(numOfTrainingEpochs, model1, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		
		double loss2 = (new ModelTrainer(0.001,0.9999,0.99999,0.1)).train(numOfTrainingEpochs, model2, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		
		//System.out.println("Good: "+goodValues.size()+"\t Bad: "+badValues.size());
		System.out.println("Original Loss: "+lossOriginal);
		//System.out.println("Loss 1: "+loss1);
		//System.out.println("Loss 2: "+loss2);

		double newLoss = (double)(loss1*goodValues.size()+loss2*badValues.size())/(double)(goodValues.size()+badValues.size());
		System.out.println("New Loss: "+newLoss);
		
		*/
		
	}


    private static String convertFlashcardToSentence(String flashcardFront, String flashcardBack) {

        String front = flashcardFront.trim();
        if (front.charAt(front.length()-1)=='?'){
        	front = front.substring(0,front.length()-1);
            System.out.println(front);
        }
        
        String back = Character.toLowerCase(flashcardBack.charAt(0)) +flashcardBack.trim().substring(1);
        String frontToTest = front.toLowerCase();


        if(frontToTest.contains("...")){
            if ( frontToTest.substring( frontToTest.length()-3,frontToTest.length() ).equals("...") ){
                return front.substring(0,frontToTest.length()-3)+" "+flashcardBack;
            }else{
                return front.substring(0,front.indexOf("..."))+" "+back+" "+front.substring(front.indexOf("...")+3);
            }
        }
        
        if(!frontToTest.contains(" ")){
            return front+" means "+back;
        }
        
        if(frontToTest.length()>19) {
            if (frontToTest.substring(0,19).equals("give advantages of ")){
                return "Advantages of "+Character.toLowerCase(front.charAt(19))+front.substring(20)+" are that "+back;
            }
        }
             
        if(frontToTest.length()>18) {
            if (frontToTest.substring(0,17).equals("what happens when")){
                return "When "+Character.toLowerCase(front.charAt(18))+front.substring(19)+", "+back;
            }
        }
        
        if(frontToTest.length()>17) {
            if (frontToTest.substring(0,17).equals("give examples of ")){
                return "Examples of "+Character.toLowerCase(front.charAt(17))+front.substring(18)+" are "+back;
            }

        }
        
        
        if(frontToTest.length()>16) {
            if (frontToTest.substring(0,16).equals("what happens at ")){
                return "At "+Character.toLowerCase(front.charAt(16))+front.substring(17)+", "+back;
            }    
            
            if(frontToTest.substring(frontToTest.length()-16).equals("by which factors") ){
                return "The factors affecting "+Character.toLowerCase(front.charAt(0))+front.substring(1,frontToTest.length()-16)+"are "+back;
            }
        }
         
        if(frontToTest.length()>14) {
            if (frontToTest.substring(0,13).equals("how would you")){
                return "You would "+Character.toLowerCase(front.charAt(14))+front.substring(15)+" "+back;
            }   
        }
              
        if(frontToTest.length()>11) {
            if(frontToTest.substring(frontToTest.length()-11).equals("explain why") ){
                return front.substring(0,frontToTest.length()-11)+"because "+back;
            }
        }
        
        if(frontToTest.length()>10) {
        	String start = frontToTest.substring(0,10);
            if (start.equals("how would ")){
                return front.substring(10)+" by "+back;
            }
            
            if(start.equals("what does ")){
                if(frontToTest.substring(frontToTest.length()-9,frontToTest.length()).equals("stand for") ){
                    return Character.toUpperCase(front.charAt(10))+front.substring(11,frontToTest.length()-10)+" stands for "+back;
                }

                if(frontToTest.substring(frontToTest.length()-9,frontToTest.length()).equals("represent") ){
                    return Character.toUpperCase(front.charAt(10))+front.substring(11,frontToTest.length()-10)+" represents "+back;
                }

                if(frontToTest.substring(frontToTest.length()-4,frontToTest.length()).equals("mean") ){
                    return Character.toUpperCase(front.charAt(10))+front.substring(11,frontToTest.length()-5)+" means "+back;
                }
            }
            
        }

        if(frontToTest.length()>9) {
        	String start = frontToTest.substring(0,9);
        	
            if(start.equals("how does ")){
                return Character.toUpperCase(front.charAt(9))+front.substring(10)+"s by "+back;
            }
            
            if (start.equals("what are ") ){
                return Character.toUpperCase(front.charAt(9))+front.substring(10)+" are "+back;
            }
            
            if (start.equals("describe ")){
                return Character.toUpperCase(front.charAt(9))+front.substring(10)+": "+back;
            } 
            
            if (start.equals("how does ")){
                return Character.toUpperCase(front.charAt(9))+front.substring(10)+" by "+back;
            } 
            
            if (start.equals("where do ")){
                return Character.toUpperCase(front.charAt(9))+front.substring(10)+" in "+back;
            }

        }

        if(frontToTest.length()>8) {
        	String start = frontToTest.substring(0,8);
        	
            if (start.equals("what is ") ){
                return Character.toUpperCase(front.charAt(8))+front.substring(9)+" is "+back;
            } 
            
            if(start.equals("what do ")){
                if(frontToTest.substring(frontToTest.length()-3).equals("for") ){
                    return Character.toUpperCase(front.charAt(8))+front.substring(9,frontToTest.length()-4)+" for "+back;
                }
            }
            
            if (start.equals("how can ")){
                return Character.toUpperCase(front.charAt(8))+front.substring(9)+" by "+back;
            }  
            
            if (start.equals("when do ")){
                return Character.toUpperCase(front.charAt(8))+front.substring(9)+" when "+back;
            }
        }       
        
        if(frontToTest.length()>7) {
        	String start = frontToTest.substring(0,7);
        	
            if(start.equals("define ")){
                return Character.toUpperCase(front.charAt(7))+front.substring(8)+": "+back;
            }   
            
            if (start.equals("how do ")){
                return Character.toUpperCase(front.charAt(7))+front.substring(8)+" by "+back;
            }
        }
        
        if(frontToTest.length()>6) {
            if (frontToTest.substring(0,6).equals("state ")){
                return Character.toUpperCase(front.charAt(6))+front.substring(7)+": "+back;
            }
        }
        
        if(frontToTest.length()>5) {
        	String start = frontToTest.substring(0,5);
            if (start.equals("give ")){
                return Character.toUpperCase(front.charAt(5))+front.substring(6)+": "+back;
            }
            
            if (start.equals("name ")){
                return Character.toUpperCase(front.charAt(5))+front.substring(6)+": "+back;
            }
            
            if (start.equals("list ")){
                return Character.toUpperCase(front.charAt(5))+front.substring(6)+": "+back;
            }  
        }


        String input = flashcardFront+"<F_B_S>"+flashcardBack;
        String extra= (input.length()>100)? input.substring(100):"";

        //double[] inputArray = DataProcessing.stringToDoubleArray(input);
        //double[] outputArray = LinearLayer.run(inputArray);

        //return DataProcessing.doubleArrayToString(outputArray)+extra;

        return "Failed";
        
    }

	/*
	int [] layerTypeSet = {0,1};
	int [] hiddenDimensionSet = {1,2,3,4,5,6,7,8,9,10,15,20,30,40,50,75,100,110,125,150};
	
	
	int maxArraySize = 3;
	
	for(int j=2;j<=maxArraySize;j++) {
		int[][] layerTypes = new int[(int) Math.pow(layerTypeSet.length, j)][j];
		for(int i=0;i<=Math.pow(layerTypeSet.length, j)-1;i++) {
			for(int k=0;k<j;k++) {
				//System.out.println(i+" "+Math.pow(layerTypeSet.length, k)+" "+(int) Math.floor((i)/(int) Math.floor(Math.pow(layerTypeSet.length, k)) ) );
				layerTypes[i][k] =  layerTypeSet[ (int) Math.floor((i)/(int) Math.floor(Math.pow(layerTypeSet.length, k)) ) % layerTypeSet.length ]; 
				//System.out.println(i+" "+(int) (Math.pow(layerTypeSet.length, k))+" "+(i % (int) (Math.pow(layerTypeSet.length, k))));
				//layerTypes[i][k] =  (i % (int) (Math.pow(layerTypeSet.length, k)));
			}
		}
		
		for(int i=0;i<layerTypes.length;i++) {
			System.out.println(Utilities.arrayToString(layerTypes[i]));
		}
		
		int[][] hiddenDims = new int[(int) Math.pow(hiddenDimensionSet.length, j-1)][j-1];
		for(int i=0;i<=Math.pow(hiddenDimensionSet.length, j-1)-1;i++) {
			for(int k=0;k<j-1;k++) {
				hiddenDims[i][k] =  hiddenDimensionSet[ (int) Math.floor((i)/(int) Math.floor(Math.pow(hiddenDimensionSet.length, k)) ) % hiddenDimensionSet.length ];
			}
		}
		
		for(int i=0;i<hiddenDims.length;i++) {
			System.out.println(Utilities.arrayToString(hiddenDims[i]));
		}
		
	}
	
	
	
	//System.out.println('ö' == 'ö');
	//Character a = 'ö';
	//String s = "höle";
	//Character b = s.charAt(1);
	//System.out.println(a == b);
	/*
	CustomRandom util = new CustomRandom();
	
	//TestingDataSet data = new TestingDataSet(util);
	
	FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", util);
	
	String savePath = "Models/CardToSentence.txt";

	//LinearLayer model = new LinearLayer(new double[] {1.01,2.01,3.01,4.01,5.01,6.01,7.01,8.01,9.01}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);
	
	//ArrayList<Layer> layers = new ArrayList<>();
	//layers.add(new LinearLayer(new double[] {0.01,0.01,0.01,0.01,0.01,0.01,0.01,0.01,0.01,0.01}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR));
	
	//layers.add(new FeedForwardLayer(new double[] {1.01,2.01,2.01,3.01,3.01,3.01,4.01,4.01,4.01}, new double[] {-1.01,2.01,-3.01}, new RoughTanhUnit()));
	
	//Model model = new NeuralNetworkModel(layers);
	
	//Model model = new NeuralNetworkModel(4, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, 15, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, util);
	
	//Model model = new AverageModel(data.getTrainingDataSteps());
	
	Model model = new CharacterManipulationFromStringDistanceModel(data.getTrainingDataSteps(), data.getDataProcessing(), util);
	
	
	/*StringBuilder builder  = new StringBuilder();
	
	Vector output = new Vector(3);
	
	model.run(new Vector(new double[] {0.25,-0.75,-0.25}), output );
	output.toString(builder);
	builder.append("\n");
	model.run(new Vector(new double[] {-0.25,-0.75,-0.25}), output );
	output.toString(builder);
	builder.append("\n");
	model.run(new Vector(new double[] {-0.15,-0.65,-0.95}), output );
	output.toString(builder);
	builder.append("\n");
	model.run(new Vector(new double[] {-0.85,-0.35,-0.995}), output );
	output.toString(builder);
	builder.append("\n");
	model.run(new Vector(new double[] {-0.65,-0.765,-0.765}), output );
	output.toString(builder);
	builder.append("\n");
	model.run(new Vector(new double[] {-0.125,-0.715,-0.85}), output );
	output.toString(builder);
	builder.append("\n");
	System.out.println(builder.toString());
	*/
	
	//FeedForwardLayer model = new FeedForwardLayer(3, 3, new RoughTanhUnit(), util);
	
	/*ArrayList<Model> models = new ArrayList<>();
	AdvancedCopyingModel model1 = new AdvancedCopyingModel(1, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);
	models.add(model1);
	models.add(new ProportionProbabilityForCharacterModel(data.getTrainingDataSteps(), 1, data.getDataProcessing(), util));
	models.add(new BasicCopyingModel());
	
	Model model = new StackingEnsembleModel(models, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, util);
	*/

    //NeuralNetworkModel model2 = new NeuralNetworkModel(3, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, 10, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, util);
	//models.add(model2);

    //AveragingEnsembleModel model = new AveragingEnsembleModel(models, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);


    //Model model = new ProportionProbabilityForCharacterModel(data.getTrainingDataSteps(), 1, data.getDataProcessing(), util);
	
	/*
	int numOfTrainingEpochs = 1000;
	int displayReportPeriod = 20;
	int showEpochPeriod = 5;
	int checkMinimumPeriod = 20;

	
	double lossOriginal = (new ModelTrainer(0.001,0.9999,0.99999,0.1)).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod , savePath, util);
	
	//System.out.println("Original Loss: "+lossOriginal);
	
	DataSplitOperation splitOp = DataSplitter.getBestDataSplit(data.getTrainingDataSteps(), model, data.getDataProcessing());
	
	ArrayList<DataStep> goodValues = DataSplitter.getStepsInSplit(data.getTrainingDataSteps(), splitOp);
	ArrayList<DataStep> badValues = DataSplitter.getStepsNotInSplit(data.getTrainingDataSteps(), splitOp);

	
	Model model1 = new CharacterManipulationFromStringDistanceModel(goodValues, data.getDataProcessing(), util);
	Model model2 = new ProportionProbabilityForCharacterModel(badValues, 2, data.getDataProcessing(), util);
	
	double loss1 = (new ModelTrainer(0.001,0.9999,0.99999,0.1)).train(numOfTrainingEpochs, model1, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
	
	double loss2 = (new ModelTrainer(0.001,0.9999,0.99999,0.1)).train(numOfTrainingEpochs, model2, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
	
	//System.out.println("Good: "+goodValues.size()+"\t Bad: "+badValues.size());
	System.out.println("Original Loss: "+lossOriginal);
	//System.out.println("Loss 1: "+loss1);
	//System.out.println("Loss 2: "+loss2);

	double newLoss = (double)(loss1*goodValues.size()+loss2*badValues.size())/(double)(goodValues.size()+badValues.size());
	System.out.println("New Loss: "+newLoss);
	
	*/

}
