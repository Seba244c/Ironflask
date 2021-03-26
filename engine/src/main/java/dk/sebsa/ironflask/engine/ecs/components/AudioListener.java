package dk.sebsa.ironflask.engine.ecs.components;

import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.AL_VELOCITY;
import static org.lwjgl.openal.AL10.alListener3f;

import org.joml.Vector3f;

import dk.sebsa.ironflask.engine.audio.AudioManager;
import dk.sebsa.ironflask.engine.ecs.Component;
import dk.sebsa.ironflask.engine.enums.Severity;
import dk.sebsa.ironflask.engine.io.LoggingUtil;

public class AudioListener extends Component {
	private Vector3f lastPosistion = new Vector3f(0, 0, 0);
	
	public AudioListener(AudioManager am) {
    	LoggingUtil.coreLog(Severity.Trace, "Creating AudioListener");
		am.setListener(this);
        alListener3f(AL_POSITION, 0, 0, 0);
        alListener3f(AL_VELOCITY, 0, 0, 0);
    }

    public void setPosition(Vector3f position) {
        alListener3f(AL_POSITION, position.x, position.y, position.z);
    }
    
    @Override
    public void update() {
    	if(entity.getPosition().equals(lastPosistion.x, lastPosistion.y, lastPosistion.z)) return;
    		setPosition(entity.getPosition());
    		lastPosistion = new Vector3f(entity.getPosition().x, entity.getPosition().y, entity.getPosition().z);
    }
}
