package dk.sebsa.ironflask.engine.graph;

import static org.lwjgl.opengl.GL20.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import dk.sebsa.ironflask.engine.core.Asset;
import dk.sebsa.ironflask.engine.core.AssetManager;
import dk.sebsa.ironflask.engine.io.LoggingUtil;
import dk.sebsa.ironflask.engine.enums.*;
import dk.sebsa.ironflask.engine.math.Color;
import dk.sebsa.ironflask.engine.utils.FileUtil;

public class Shader extends Asset {
	private int programId;
	private int vertexShaderId;
	private int fragmentShaderId;
	
	private static List<Shader> shaders = new ArrayList<>();
	private Map<String, Integer> uniforms;
	
	public Shader(String name) throws Exception {
		super(name);
		programId = glCreateProgram();
        if (programId == 0) {
        	LoggingUtil.coreLog(Severity.Error, "Could not create shader");
            throw new Exception("Could not create Shader");
        }
        AssetManager.allAssets.add(this);
        
        // Uniforms
        uniforms = new HashMap<>();
        
        // Create and verify shader
        String[] str = FileUtil.loadResource("/shaders" + name + ".glsl").split("///#ENDVERTEX");
        createVertexShader(str[0]);
        createFragmentShader(str[1]);
        link();
        
        shaders.add(this);
    }
	
	public static Shader getShader(String name) {
		for(int i = 0; i < shaders.size(); i++) {
			if(shaders.get(i).name.equals(name)) {
				return shaders.get(i);
			}
		}
		return null;
	}
	
	public void createUniform(String uniformName) throws Exception {
		if(uniforms.containsKey(uniformName));
	    int uniformLocation = glGetUniformLocation(programId,
	        uniformName);
	    if (uniformLocation < 0) {
	        throw new Exception("Could not find uniform:" +
	            uniformName);
	    }
	    uniforms.put(uniformName, uniformLocation);
	}
	
	public void setUniform(String uniformName, Matrix4f value) {
        // Dump the matrix into a float buffer
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(uniforms.get(uniformName), false,
                               value.get(stack.mallocFloat(16)));
        }
    }
	
	public void setUniform(String uniformName, Color value) {
        glUniform3f(uniforms.get(uniformName), value.r, value.g, value.b);
    }
	
	public void setUniform(String uniformName, int value) {
	    glUniform1i(uniforms.get(uniformName), value);
	}


    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }
    
    protected int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }
    
    public void link() throws Exception {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }

        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }
}
