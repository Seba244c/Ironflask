package dk.sebsa.ironflask.engine.graph;

import dk.sebsa.ironflask.engine.core.Asset;
import dk.sebsa.ironflask.engine.core.AssetManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture extends Asset {
	private final int id;
	
	public Texture(String fileName) throws Exception {
        this(loadTexture(fileName));
        String[] split = fileName.split("/");
        this.name = split[split.length-1].split("\\.")[0];
    }
	
	public Texture(int id) {
        this.id = id;
        AssetManager.allAssets.add(this);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public int getId() {
        return id;
    }

    private static int loadTexture(String fileName) throws Exception {
    	IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer channelsBuffer = BufferUtils.createIntBuffer(1);
        ByteBuffer data;
        
        // Load texture
        InputStream is = Texture.class.getResourceAsStream(fileName);
		byte[] bytes = new byte[8000];
		int curByte = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		try {
			while((curByte = is.read(bytes)) != -1) { bos.write(bytes, 0, curByte);}
			is.close();
		}
		catch (IOException e) { e.printStackTrace(); }
		
		bytes = bos.toByteArray();
		ByteBuffer buffer = (ByteBuffer) BufferUtils.createByteBuffer(bytes.length);
		buffer.put(bytes).flip();
		data = stbi_load_from_memory(buffer, widthBuffer, heightBuffer, channelsBuffer, 4);

        // Create a new OpenGL texture
        int textureId = glGenTextures();
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, textureId);

        // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // Upload the texture data
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, widthBuffer.get(), heightBuffer.get(), 0,
                GL_RGBA, GL_UNSIGNED_BYTE, data);
        // Generate Mip Map
        glGenerateMipmap(GL_TEXTURE_2D);

        stbi_image_free(data);

        return textureId;
    }

	@Override
	public void cleanup() {
		glDeleteTextures(id);
	}
}
