package com.rabidgremlin.romo.cmdgen;

import java.io.File;

/**
 * This class generates a .wav that drives Romo in a square. Making right turns 1 second a part.
 * 
 * @author jack@rabidgremlin.com
 * 
 */
public class AroundTheBlock
{

  public static void main(String[] args)
  {
    try
    {
      CommandGenerator cmdGen = new CommandGenerator(8000, 16, 2);

      // lead in
      cmdGen.addSilence(100);

      // start both tracks full speed
      cmdGen.addCommand(CommandGenerator.LEFT_FORWARD_FULL);
      cmdGen.addSilence(10);
      cmdGen.addCommand(CommandGenerator.RIGHT_FORWARD_FULL);

      for (int loop = 0; loop < 4; loop++)
      {
        // run for a second
        cmdGen.addSilence(1000);

        // turn right
        cmdGen.addCommand(CommandGenerator.RIGHT_REVERSE_FULL);
        cmdGen.addSilence(1000);
        cmdGen.addCommand(CommandGenerator.RIGHT_FORWARD_FULL);
      }

      // full stop
      cmdGen.addCommand(CommandGenerator.LEFT_STOP);
      cmdGen.addSilence(10);
      cmdGen.addCommand(CommandGenerator.RIGHT_STOP);

      // lead out
      cmdGen.addSilence(100);

      // dump the .wav to file
      cmdGen.saveWav(new File("wav/AroundTheBlock.wav"));
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.exit(1);
    }

  }

}
