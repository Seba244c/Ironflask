package dk.sebsa.ironflask.engine.audio;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.stb.STBVorbis.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;

import dk.sebsa.ironflask.engine.core.Asset;
import dk.sebsa.ironflask.engine.throwable.AssetExistsException;
import dk.sebsa.ironflask.engine.utils.FileUtil;

public class AudioClip extends Asset {
	private final int bufferId;
    private ShortBuffer pcm = null;
	
    private static List<AudioClip> audioClips = new ArrayList<AudioClip>();
	private static int i;
	
	public AudioClip(String name) throws IOException, AssetExistsException {
		super(name);
		this.bufferId = alGenBuffers();
		
        try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
            ShortBuffer pcm;
            if(name.startsWith("/")) pcm = readVorbis(name+".ogg", 32 * 1024, info, true);
            else pcm = readVorbis(name+".ogg", 32 * 1024, info, false);

            // Copy to buffer
            alBufferData(bufferId, info.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, pcm, info.sample_rate());
        }
        AudioManager.instance.addAudioClip(this);
        audioClips.add(this);
    }
	
	public int getBufferId() {
        return this.bufferId;
    }
	
	@Override
	public void cleanup() {
    	alDeleteBuffers(this.bufferId);
        if (pcm != null) {
            memFree(pcm);
        }
    }
    
    @SuppressWarnings("resource")
	private ShortBuffer readVorbis(String resource, int bufferSize, STBVorbisInfo info, boolean jarFile) throws IOException {
    	ByteBuffer vorbis = FileUtil.ioResourceToByteBuffer(resource, bufferSize);
        
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer error = stack.mallocInt(1);
            long decoder = stb_vorbis_open_memory(vorbis, error, null);
            if (decoder == NULL) {
                throw new IOException("Failed to open Ogg Vorbis file. Error: " + error.get(0));
            }

            stb_vorbis_get_info(decoder, info);

            int channels = info.channels();

            int lengthSamples = stb_vorbis_stream_length_in_samples(decoder);

            pcm = memAllocShort(lengthSamples);

            pcm.limit(stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm) * channels);
            stb_vorbis_close(decoder);

            return pcm;
        }
    }
    
    public static AudioClip getClip(String name) {
    	for(i = 0; i < audioClips.size(); i++) {
    		if(audioClips.get(i).name.equals(name)) return audioClips.get(i);
    	}
    	return null;
    }
    
    public static List<AudioClip> getClips() {
    	return audioClips;
    }
}
