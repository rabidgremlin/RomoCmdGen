#Readme
This Java code generates .wav files that contain Romo movement commands. Example output files can be found in the _wav_ folder.

## How to use this code?
You need to know some Java programming. Check out [GoStop.java](https://github.com/rabidgremlin/RomoCmdGen/blob/master/src/java/com/rabidgremlin/romo/cmdgen/GoStop.java) and [AroundTheBlock.java](https://github.com/rabidgremlin/RomoCmdGen/blob/master/src/java/com/rabidgremlin/romo/cmdgen/AroundTheBlock.java) for examples.

## How do the Romo audio commands work?
Peter from Romotive supplied me with the following helpful info whilst troubleshooting my Romo setup. 

> Each command is 12 bits-- the clock (10) is repeated 12 times on the left channel.
> 
> The right channel is the data. The first 3 bits (out of 12) are the address 001 address the left motor, 010 addresses the right motor. If you want both motors to change states, you have to send a command addressing each motor. The next 8 bits are the data. 0 (00000000) and 255 (11111111) are full speed in opposite directions. 128 (10000000) is full stop. You can call any command in between. The last bit is the parity bit-- you have to make the number of 1's in the previous 11 bits even. So if you have a command that the first 11 bits are 01000000000, the 12th bit has to be a 1.

And in a subsequent email:

> So basically you have to go low before you can go high. In other words, when the command is zero, you should pull the audio low instead of letting it go to the middle (at all times, not just the lead-in)

## Licenses
This code is released under the Apache License.

It uses code from [The Lab Book Pages](http://www.labbookpages.co.uk/audio/javaWavFiles.html) which appears to have no license specified.
