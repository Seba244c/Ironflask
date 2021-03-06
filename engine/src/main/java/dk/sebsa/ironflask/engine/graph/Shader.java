package dk.sebsa.ironflask.engine.graph;

import static org.lwjgl.opengl.GL20.*;

import dk.sebsa.ironflask.engine.core.Asset;
import dk.sebsa.ironflask.engine.core.AssetManager;
import dk.sebsa.ironflask.engine.io.LoggingUtil;
import dk.sebsa.ironflask.engine.io.LoggingUtil.Severity;
import dk.sebsa.ironflask.engine.utils.FileUtil;

public class Shader extends Asset {
	private final int programId;
	private int vertexShaderId;
	private int fragmentShaderId;
	
	public Shader() throws Exception {
        programId = glCreateProgram();
        if (programId == 0) {
        	LoggingUtil.coreLog(Severity.Error, "Could not create shader");
            throw new Exception("Could not create Shader");
        }
        AssetManager.allAssets.add(this);
    }
	
	public Shader(String name) throws Exception {
        programId = glCreateProgram();
        if (programId == 0) {
        	LoggingUtil.coreLog(Severity.Error, "Could not create shader");
            throw new Exception("Could not create Shader");
        }
        AssetManager.allAssets.add(this);
        
        // Create and verify shader
        createVertexShader(FileUtil.loadResource("/shaders/" + name + "_vertex.glsl"));
        createFragmentShader(FileUtil.loadResource("/shaders/" + name + "_fragment.glsl"));
        link();
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

        glValidateProgram(programId);				
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
