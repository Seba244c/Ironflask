package dk.sebsa.ironflask.engine.math;

import java.util.Random;

public class Math {
	public static int remainder;
	
	public static int wrap(int val, int min, int max) {
		remainder = max - min;
		return (((val - min) % remainder + remainder) % remainder + min);
	}
	
	public static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
	
	public static int mod(int x, int y) {
	    int result = x % y;
	    if (result < 0)
	    {
	        result += y;
	    }
	    return result;
	}

}