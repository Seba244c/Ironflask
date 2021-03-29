package dk.sebsa.ironflask.engine.math;

public class Mathf {
	public static float remainder;
	
	public static float clamp(float val, float min, float max) {
		if(val<min) return min;
		if(val>max) return max;
		return val;
	}
	
	public static float wrap(float val, float min, float max) {
		remainder = max - min;
		return ((val - min) % remainder + remainder) % remainder + min;
	}
	
	public static float abs(float val) {
		return java.lang.Math.abs(val);
	}
}