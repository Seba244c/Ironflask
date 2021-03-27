package dk.sebsa.ironflask.engine.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.lwjgl.BufferUtils;

public class FileUtil {
	public static String loadResource(String fileName) throws Exception {
        String result;
        try (InputStream in = FileUtil.class.getResourceAsStream(fileName);
                Scanner scanner = new Scanner(in, java.nio.charset.StandardCharsets.UTF_8.name())) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;
    }
	
	public static void zipSingleFile(Path source, String zipFileName, boolean deleteSource) throws IOException {

        try (
            ZipOutputStream zos = new ZipOutputStream(
                new FileOutputStream(zipFileName));
            FileInputStream fis = new FileInputStream(source.toFile());
        ) {

            ZipEntry zipEntry = new ZipEntry(source.getFileName().toString());
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            zos.closeEntry();
        }
        if(deleteSource) source.toFile().delete();
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
	
	public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
		ByteBuffer buffer;

        Path path = Paths.get(resource);
        if (Files.isReadable(path)) {
            try (SeekableByteChannel fc = Files.newByteChannel(path)) {
                buffer = BufferUtils.createByteBuffer((int) fc.size() + 1);
                while (fc.read(buffer) != -1) ;
            }
        } else {
        	try (InputStream source = FileUtil.class.getResourceAsStream(resource); ReadableByteChannel rbc = Channels.newChannel(source))
            {
                buffer = BufferUtils.createByteBuffer(bufferSize);

                while (true)
                {
                    int bytes = rbc.read(buffer);
                    if (bytes == -1) {
                        break;
                    }
                    if (buffer.remaining() == 0)
                    {
                        buffer.flip();
                    	buffer = BufferUtils.createByteBuffer(buffer.capacity() * 2).put(buffer);
                    }
                }
            }
        }

        buffer.flip();
        return buffer;
    }
	
	public static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }
}
