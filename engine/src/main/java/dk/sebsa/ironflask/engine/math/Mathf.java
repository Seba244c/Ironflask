package dk.sebsa.ironflask.engine.math;

import org.joml.Vector3f;

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
	
	public static boolean isVectorZero(Vector3f v) {
		if(v.x != 0) return false;
		if(v.y != 0) return false;
		if(v.z != 0) return false;
		return true;
	}
	
	public static float abs(float val) {
		return java.lang.Math.abs(val);
	}
}