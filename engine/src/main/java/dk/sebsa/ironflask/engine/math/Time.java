package dk.sebsa.ironflask.engine.math;

import java.util.concurrent.TimeUnit;

import dk.sebsa.ironflask.engine.enums.Severity;
import dk.sebsa.ironflask.engine.io.LoggingUtil;

public class Time {
public static float timeScale = 1;
	
	private static long rawTime;
	private static float time;
	private static double rawDelta;
	private static float deltaTime;
	private static float unscaledDelta;
	private static final long second = 1000000000L;
	
	private static long startTime;
	private static long lastFrameTime;
	private static long frameStartTime;
	private static long framePassedTime;
	
	public static void init() {
		LoggingUtil.coreLog(Severity.Info, "Initliliazing Time system");
		 lastFrameTime = System.nanoTime();
		 startTime = lastFrameTime;
	}
	
	public static void process() {
		rawTime = System.nanoTime();
		time = (float) (TimeUnit.MILLISECONDS.convert(rawTime - startTime, TimeUnit.NANOSECONDS));
		
		frameStartTime = rawTime;
		framePassedTime = frameStartTime - lastFrameTime;
		lastFrameTime = frameStartTime;
		
		rawDelta = framePassedTime / (double) second;
		if(rawDelta > 0.01f) {
			deltaTime = (float) (0.01 * timeScale);
			unscaledDelta = 0.01f;
		}
		else {
			deltaTime = (float) (rawDelta * timeScale);
			unscaledDelta = (float) rawDelta;
		}
	}

	public static final float getTime() {
		return time;
	}

	public static final float getDeltaTime() {
		return deltaTime;
	}

	public static final float getUnscaledDelta() {
		return unscaledDelta;
	}
}
