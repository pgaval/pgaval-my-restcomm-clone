package org.mobicents.servlet.sip.restcomm.tts.ttsapi;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * @author <a href="mailto:gvagenas@gmail.com">George Vagenas</a>
 */

public class Convertor {

	public static List<URI> convert(URI source) throws Exception{
//		File sourceFile = new File
		URL sourceURL = source.toURL();
		File sourceFile = File.createTempFile("source", ".mp3");
		sourceFile.deleteOnExit();
		FileUtils.copyURLToFile(sourceURL, sourceFile);
		String target = sourceFile.getPath().replaceAll("mp3", "wav");
		File targetFile = new File(target);
		targetFile.deleteOnExit();
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("pcm_s16le");
		audio.setSamplingRate(8000);
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("wav");
		attrs.setAudioAttributes(audio);
		Encoder encoder = new Encoder();
		encoder.encode(sourceFile, targetFile, attrs);
		List<URI> result = new ArrayList<URI>();
		result.add(sourceFile.toURI());
		result.add(targetFile.toURI());
		return result;
	}	
}
