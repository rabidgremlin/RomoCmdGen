package com.rabidgremlin.romo.cmdgen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.labbookpages.WavFile;

/**
 * This class generates a .wav file containing a series of audio commands for Romo.
 * 
 * @author jack@rabidgremlin.com
 * 
 */
public class CommandGenerator
{
  public final static String LEFT_FORWARD_FULL = "001111111111";
  public final static String RIGHT_FORWARD_FULL = "010000000001";
  public final static String LEFT_REVERSE_FULL = "001000000001";
  public final static String RIGHT_REVERSE_FULL = "010111111111";
  public final static String LEFT_STOP = "001100000000";
  public final static String RIGHT_STOP = "010100000000";

  private int sampleRate;
  private int bitsPerSample;
  private double msPerCommandBit;
  private List<SequenceStep> sequence = new ArrayList<SequenceStep>();

  /**
   * Construct the generator with defaults that seem to work well.
   * 
   */
  public CommandGenerator()
  {
    // sample rate = 8000, 16 bit wave, each command bit is 2ms long
    this(8000, 16, 2);
  }

  public CommandGenerator(int sampleRate, int bitsPerSample, double msPerCommandBit)
  {
    this.sampleRate = sampleRate;
    this.bitsPerSample = bitsPerSample;
    this.msPerCommandBit = msPerCommandBit;
  }

  /**
   * Adds a command. Commands are represented by a string of 1's and 0's. A command string should be 12 digits long.
   * 
   * The first 3 bits of the command are the address of the thing you wish to send a command to. 001 is the right motor. 010 is
   * the left motor.
   * 
   * The next 8 bits is the value you wish to send. eg 11111111 = 255, 00000000 = 0, 1000000 = 128.
   * 
   * The last bit is a parity bit. It needs to be set so that the number of 1's in the command is even.
   * 
   * @param commandBits
   *          The command as a string of 1's and 0's.
   */
  public void addCommand(String commandBits)
  {
    sequence.add(new CommandStep(commandBits, sampleRate, msPerCommandBit));

  }

  /**
   * This method adds silence for the specified number of milliseconds to the wave.
   * 
   * @param ms
   *          Milliseconds of silence to add.
   */
  public void addSilence(int ms)
  {
    sequence.add(new SilenceStep(sampleRate, ms));
  }

  /**
   * This method writes out the command to the specified .wav file.
   * 
   * @param file
   *          The File to write to.
   * @throws Exception
   *           Thrown if something goes wrong.
   */
  public void saveWav(File file) throws Exception
  {
    System.out.println("Saving command to " + file.getAbsolutePath());

    // figure out how many frames our .wav needs
    int framesNeeded = 0;
    for (SequenceStep step : sequence)
    {
      framesNeeded += step.getLengthInFrames();
    }

    // create stero .wav file with specified params
    WavFile wavFile = WavFile.newWavFile(file, 2, framesNeeded, bitsPerSample, sampleRate);

    // loop through each step and write it's audio data to the wav.
    for (SequenceStep step : sequence)
    {
      wavFile.writeFrames(step.getData(), step.getLengthInFrames());
    }

    // close the file
    wavFile.close();

    System.out.println("Done");
  }

}
