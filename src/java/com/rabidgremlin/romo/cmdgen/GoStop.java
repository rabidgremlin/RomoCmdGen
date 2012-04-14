package com.rabidgremlin.romo.cmdgen;

import java.io.File;

/**
 * This class generates a .wav that moves Romo at full speed forward for 1 second and then stops him.
 * 
 * @author jack@rabidgremlin.com
 * 
 */
public class GoStop
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

      // run for a second
      cmdGen.addSilence(1000);

      // full stop
      cmdGen.addCommand(CommandGenerator.LEFT_STOP);
      cmdGen.addSilence(10);
      cmdGen.addCommand(CommandGenerator.RIGHT_STOP);

      // lead out
      cmdGen.addSilence(100);

      // dump the .wav to file
      cmdGen.saveWav(new File("wav/GoStop.wav"));
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.exit(1);
    }

  }

}
