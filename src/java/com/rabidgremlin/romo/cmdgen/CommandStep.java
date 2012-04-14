package com.rabidgremlin.romo.cmdgen;

/**
 * This step generates the data for a Romo command. A Romo audio command consists of 12 bits of data.
 * 
 * The left audio channel is used as a timing/clock channel and consists of 12 pairs of high and low alternations. The right
 * audio channel is used to send the data bits. It consists of 12 values encoded as a high (for an on bit) or mid (for an off
 * bit). Each data bit on the right channel is as long as one high/low clock alternation.
 * 
 * @author jack@rabidgremlin.com
 * 
 */
public class CommandStep implements SequenceStep
{
  private String commandBits;
  private int sampleRate;
  private double msPerCommandBit;

  /**
   * Constructor.
   * 
   * @param commandBits
   *          The command bits represented as a String of 1's and 0's.
   * @param sampleRate
   *          The sample rate. Used to to frames.
   * @param msPerCommandBit
   *          The length of each command bit in milliseconds.
   */
  public CommandStep(String commandBits, int sampleRate, double msPerCommandBit)
  {
    this.commandBits = commandBits;
    this.sampleRate = sampleRate;
    this.msPerCommandBit = msPerCommandBit;
  }

  /**
   * Utility method to calculate the number of frames that one half (a high or low) of a clock signal takes. Need to work in
   * halfs to avoid rounding errors and ensure that each clock half is equal number of frames.
   * 
   * @return Number of frames taken up by half a clock signal.
   */
  private int getFramesPerHalfClock()
  {
    return (int) (sampleRate / 1000 * (msPerCommandBit / 2));
  }

  /**
   * Returns the number of frames required by the command.
   * 
   */
  @Override
  public int getLengthInFrames()
  {
    // 12 bits * size of half clock * 2 (so that size of clock signal) * 2 (doubled for lead in)
    return 12 * getFramesPerHalfClock() * 2 * 2;
  }

  /**
   * Fills the buffer with a low signal as a lead in.
   * 
   * @param buffer
   *          The buffer to fill.
   */
  private void fillLeadIn(double[][] buffer)
  {
    // lead in is half the size of the calculated frame length
    int leadInSize = getLengthInFrames() / 2;

    // fill with low
    for (int loop = 0; loop < leadInSize; loop++)
    {
      buffer[0][loop] = -1;
      buffer[1][loop] = -1;
    }
  }

  /**
   * Populates the left channel with the clock signal.
   * 
   * @param buffer
   *          The buffer to populate.
   */
  private void fillClock(double[][] buffer)
  {
    // get number of frames for half a clock signal
    int framesPerHalfClock = getFramesPerHalfClock();

    // current write position in buffer. Start after lead in
    int bufferPos = getLengthInFrames() / 2;

    // clock signal consists of 12 high and low alternations
    for (int loop = 0; loop < 24; loop++)
    {
      // should we be hi or low
      double clockBit = loop % 2 == 0 ? 1 : -1;

      // write out number of frames based on msPerCommandBit
      for (int frameLoop = 0; frameLoop < framesPerHalfClock; frameLoop++)
      {
        buffer[0][bufferPos++] = clockBit;
      }
    }
  }

  @Override
  public double[][] getData()
  {
    System.out.println("Generating data for command: " + commandBits);

    // create a buffer to hold data
    double[][] buffer = new double[2][getLengthInFrames()];

    // write lead in
    fillLeadIn(buffer);

    // populate the clock data
    fillClock(buffer);

    // create command data
    // figure out frames per command bit
    int framesPerCommandBit = getFramesPerHalfClock() * 2;

    // current write position in buffer. Start after lead in
    int bufferPos = getLengthInFrames() / 2;

    // generate the data for each of the 12 command bits
    for (int loop = 0; loop < 12; loop++)
    {
      // are we writing hi or mid ?
      double commandBit = commandBits.charAt(loop) == '1' ? 1 : -1;

      // write out number of frames based on msPerCommandBit
      for (int frameLoop = 0; frameLoop < framesPerCommandBit; frameLoop++)
      {
        buffer[1][bufferPos++] = commandBit;
      }
    }

    // return the buffer
    return buffer;
  }
}
