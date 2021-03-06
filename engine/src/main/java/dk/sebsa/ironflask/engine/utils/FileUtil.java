package dk.sebsa.ironflask.engine.utils;

import java.io.InputStream;
import java.util.Scanner;

public class FileUtil {
	public static String loadResource(String fileName) throws Exception {
        String result;
        try (InputStream in = FileUtil.class.getResourceAsStream(fileName);
                Scanner scanner = new Scanner(in, java.nio.charset.StandardCharsets.UTF_8.name())) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;
    }
}
