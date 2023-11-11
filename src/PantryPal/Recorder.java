package PantryPal;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

/** Class responsible for recording audio using the Java Sound API. */
class AudioRecorder {
  private AudioFormat audioFormat;
  private TargetDataLine targetDataLine;
  private boolean isRecording;

  /** Constructor to set up the audio format. */
  public AudioRecorder() {
    audioFormat = new AudioFormat(44100, 16, 2, true, false);
  }

  public boolean isRecording() {
    return isRecording;
  }

  public void start() {
    if (isRecording) {
      return;
    }

    try {
      // Get the TargetDataLine from the AudioSystem
      DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
      targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
      targetDataLine.open(audioFormat);
      targetDataLine.start();

      isRecording = true;

      // Start a new thread to handle the recording process
      new Thread(
              () -> {
                try {
                  AudioInputStream audioInputStream = new AudioInputStream(targetDataLine);
                  AudioSystem.write(
                      audioInputStream, AudioFileFormat.Type.WAVE, new java.io.File("output.wav"));
                } catch (Exception e) {
                  e.printStackTrace();
                }
              })
          .start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** Stop recording audio. */
  public void stop() {
    if (!isRecording) {
      return;
    }

    try {
      targetDataLine.stop();
      targetDataLine.close();
      isRecording = false;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
