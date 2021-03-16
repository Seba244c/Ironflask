package dk.sebsa.ironflask.engine.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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
	
	public static List<String> readAllLines(String fileName) throws Exception {
        List<String> list = new ArrayList<>();
        BufferedReader br;
        if(fileName.startsWith("/")) {
        	br = new BufferedReader(new InputStreamReader(Class.forName(FileUtil.class.getName()).getResourceAsStream(fileName)));
        } else {
        	File file = new File(fileName);
			br = new BufferedReader(new FileReader(file));
        }
        
        String line;
        while ((line = br.readLine()) != null) {
            list.add(line);
        }
        
        br.close();
        
        return list;
    }
}
