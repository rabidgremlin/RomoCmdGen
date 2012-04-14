package com.rabidgremlin.romo.cmdgen;

/**
 * This step generates a block of silence.
 * 
 * @author jack@rabidgremlin.com
 * 
 */
public class SilenceStep implements SequenceStep
{
  private int sampleRate;
  private int ms;

  /**
   * Constructor.
   * 
   * @param sampleRate
   *          The sample rate. Needed to calculate length in frames.
   * @param ms
   *          The milliseconds of silence to generate.
   */
  public SilenceStep(int sampleRate, int ms)
  {
    this.sampleRate = sampleRate;
    this.ms = ms;
  }

  /**
   * Calculates the length in frames.
   * 
   */
  @Override
  public int getLengthInFrames()
  {
    return sampleRate / 1000 * ms;
  }

  /**
   * Generates the data for the silence.
   * 
   */
  @Override
  public double[][] getData()
  {
    System.out.println("Generating data for " + ms + "ms of silence");

    return new double[2][getLengthInFrames()];
  }

}
