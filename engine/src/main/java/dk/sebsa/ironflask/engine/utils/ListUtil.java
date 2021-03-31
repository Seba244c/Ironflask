package dk.sebsa.ironflask.engine.utils;

import java.util.List;

public class ListUtil {
	public static float[] listToArray(List<Float> list) {
		int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];
        for (int i = 0; i < size; i++) {
            floatArr[i] = list.get(i);
        }
        return floatArr;
	}
}
