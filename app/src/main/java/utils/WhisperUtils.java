package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/** Implementation of the VoiceToText interface using the OpenAI Whisper ASR API. */
public class WhisperUtils {
  public static final String filePath = "output.wav";

  public static InputStream getClientInStream() {
    InputStream in;
    try {
      File file = new File(filePath);
      in = new FileInputStream(file);
      return in;
    } catch (Exception e) {
      System.err.println("failed opening file " + filePath);
      return null;
    }
  }

  public static void copyInputToOutputStream(OutputStream outputStream, InputStream inputStream)
      throws IOException {
    byte[] buffer = new byte[1024];
    int bytesRead;
    while ((bytesRead = inputStream.read(buffer)) != -1) {
      outputStream.write(buffer, 0, bytesRead);
    }
    inputStream.close();
    // Implementation omitted for brevity
  }
}
