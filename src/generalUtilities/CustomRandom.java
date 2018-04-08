package generalUtilities;

import java.util.concurrent.ThreadLocalRandom;


public class CustomRandom {
    private int stateVariable1, stateVariable2;

    private ThreadLocalRandom random;
	public CustomRandom() {
		random = ThreadLocalRandom.current();
    	stateVariable1 = random.nextInt();
    	stateVariable2 = random.nextInt();
	}
	
	public double[] randomDoubleArray(int length) {
		return random.doubles(length,-0.6,0.6).toArray();
	}
	
	public double[] randomDoublesBetween0and1(int length) {
		return random.doubles(length).toArray();
	}
	
	public int[] randomIntArray(int length, int maxVale) {
		return random.ints(length, 0, maxVale).distinct().toArray();
	}
	
	public double randomDouble() {
		return random.nextDouble();
	}
	
	public boolean randomBoolean() {
		return random.nextBoolean();
	}
	
	public int randomInt(int bound) {
		int z0 = stateVariable1;
	    int z1 = stateVariable2;
	    int result = Math.abs(z0 + z1);
        z1 ^= z0;
        stateVariable1 = Integer.rotateLeft(z0, 55) ^ z1 ^ (z1 << 14);
        stateVariable2 = Integer.rotateLeft(z1, 36);
        return result % (bound+1);
	    
	}
	
}
