package generalUtilities;

import java.util.concurrent.ThreadLocalRandom;


public class CustomRandom {
	int s1;
	int s2;
	ThreadLocalRandom random;
	public CustomRandom() {
		random = ThreadLocalRandom.current();
    	s1 = random.nextInt();
    	s2 = random.nextInt();
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
		int z0 = s1;
	    int z1 = s2;
	    int result = Math.abs(z0 + z1);
        z1 ^= z0;
        s1 = Integer.rotateLeft(z0, 55) ^ z1 ^ (z1 << 14); 
        s2 = Integer.rotateLeft(z1, 36);
        return result % (bound+1);
	    
	}
	
}
