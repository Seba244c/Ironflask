package dk.sebsa.ironflask.engine.audio;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.openal.ALCCapabilities;

import dk.sebsa.ironflask.engine.ecs.components.AudioListener;
import dk.sebsa.ironflask.engine.enums.Severity;
import dk.sebsa.ironflask.engine.io.LoggingUtil;
import dk.sebsa.ironflask.engine.math.Vector2f;

import org.lwjgl.openal.ALC;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.joml.Vector3f;
import org.lwjgl.openal.AL;

public class AudioManager {
	public long device;
    public long context;
    public ALCCapabilities deviceCaps;
    private AudioListener listener;
    private final List<AudioClip> audioClips;
    private final Map<String, AudioSource> soundSourceMap;
    
    public static AudioManager instance;
    
    public AudioManager() {
    	LoggingUtil.coreLog(Severity.Info, "Creating AudioManager");
    	audioClips = new ArrayList<>();
        soundSourceMap = new HashMap<>();
        
        init();
    }
    
    public void setInstance() {
    	instance = this;
    }
    
    private void init() {
    	LoggingUtil.coreLog(Severity.Info, "Initliazing OpenAL");
    	this.device = alcOpenDevice((ByteBuffer) null);
        if (device == NULL) {
            throw new IllegalStateException("Failed to open the default OpenAL device.");
        }
        deviceCaps = ALC.createCapabilities(device);
        alcMakeContextCurrent(context);
        
        
        this.context = alcCreateContext(device, (IntBuffer) null);
        if (context == NULL) {
            throw new IllegalStateException("Failed to create OpenAL context.");
        }
        alcMakeContextCurrent(context);
        AL.createCapabilities(deviceCaps);
    }
    
    public void addSoundSource(String name, AudioSource soundSource) {
        this.soundSourceMap.put(name, soundSource);
    }

    public AudioSource getSoundSource(String name) {
        return this.soundSourceMap.get(name);
    }

    public void playSoundSource(String name) {
        AudioSource soundSource = this.soundSourceMap.get(name);
        if (soundSource != null && !soundSource.isPlaying()) {
            soundSource.play();
        }
    }

    public void removeSoundSource(String name) {
        this.soundSourceMap.remove(name);
    }

    public void addAudioClip(AudioClip ac) {
        this.audioClips.add(ac);
    }

    public AudioListener getListener() {
        return this.listener;
    }

    public void setListener(AudioListener listener) {
        this.listener = listener;
    }

    public void updateListenerPosition(Vector2f p) {        
        listener.setPosition(new Vector3f(p.x, p.y, 0));
    }
    
	public void cleanup() {
		LoggingUtil.coreLog(Severity.Info, "Closing AudioManager");
        clear();
        
        if (context != NULL) {
            alcDestroyContext(context);
        }
        if (device != NULL) {
            alcCloseDevice(device);
        }
    }
	
	public void stopAll() {
    	for(AudioSource source : soundSourceMap.values()) {
    		source.stop();
    	}
    }
	
	public void clear() {
    	stopAll();
        for (AudioSource soundSource : soundSourceMap.values()) {
            soundSource.cleanup();
        }
        soundSourceMap.clear();
        audioClips.clear();
    }
}
