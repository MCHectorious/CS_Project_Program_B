package FindingBestModel.BestModel.ProportionProbabilityForCharacterSubModel;


import Training.DataProcessing;

public class ProportionProbabilityForCharacterModel {

    private static StringBuilder stringBuilder = new StringBuilder();
    private static OneCharacterSubstitutions oneCharacterSubstitutions = new OneCharacterSubstitutions();
    private static TwoCharacterSubstitutionFirstHalf twoCharacterSubstitutionFirstHalf = new TwoCharacterSubstitutionFirstHalf();
    private static TwoCharacterSubstitutionSecondHalf twoCharacterSubstitutionSecondHalf = new TwoCharacterSubstitutionSecondHalf();

    public static void execute(String inputText, double[] output, DataProcessing dataProcessing) {
        stringBuilder.setLength(0);//resets the string builder
        for (int i = 0; i < inputText.length(); i++) {
            int lowerBound = (i-1<0)? 0:i-1;//so that early strings can  be used
            int upperBound = (i + 1 > inputText.length()) ? inputText.length() : i + 1;//so that late strings can  be used
            String substring = inputText.substring(lowerBound, upperBound);

            if (upperBound-lowerBound == 1){
                stringBuilder.append(oneCharacterSubstitutions.substitute(substring));
            }else{
                Character firstHalfOutput = twoCharacterSubstitutionFirstHalf.substitute(substring);
                if(firstHalfOutput == null){
                    stringBuilder.append(twoCharacterSubstitutionSecondHalf.substitute(substring));
                }else{
                    stringBuilder.append(firstHalfOutput);
                }
            }


        }
        double[] outputAsArray = dataProcessing.stringToDoubleArray(stringBuilder.toString());
        System.arraycopy(outputAsArray, 0, output, 200, outputAsArray.length);
    }

}
