gSurvivalExtras
==========

Minecraft Bukkit plugin for some smaller sub-plugins    


> Since the submodules do not pre-register permissions they default to OP  

### Configuration

The configuration of the main mod is very simple..  
It only wants to know what mods he needs to load!  
* mods: list of mods to load  

InfoSign
--------
Infosign is used to create story-tellers,  
when you click it with the configured read tool you will get the story played.  
when you click it with the configured write tool you can use commandline to manipulate the story.

### Settings
* settings.readTool: Tool to use for reading (default: BOOK)
* settings.editTool: Tool to use for editing (default: PAPER)
* settings.delay: Milliseconds per character for the message to delay before next (default 70)
* data: {} Don't touch this! its the content/data holder for the stories, you can modify it if you know what you are doing!

### Permissions
The permissions are not fully integrated yet!
* infosign.edit - Allow user to use edit-tool/commands
* infosign.reload - Allow user to reload the config (not implemented)
* infosign.* - Allow user to all permissions (not implemented)

### Commands
> Story manipulations happens in the memory, to finish it run save!
* /infosign print [from [to]] - Prints the 'story'
* /infosign add[:index] <text> - Adds a new line (on index)
* /infosign set <index> <text> - Changes a specified line
* /infosign remove <index> - Removes a specified line
* /infosign clear - Removes sign from infosign/clear list
* /infosign save - Saves to disk
* /infosign reload - Reloads from disk

### Tricks
To make some more tension let the next line wait longer then expected by adding spaces and a backtick.  
Those characters will be counted with the delay since it is stringLength*delay, just gives that extra thrill!

SWRS - Simple Wireless RedStone
------------
Create simple begin->endpoint wireless redstone connections!  
This is far-out the most useful module till now.  
This is limited to a single world as in the receiver/sender needs to be in the same world as the transmitter!  
  
This module has no configuration and is with that an 'stupid' system, be warned, this can give strange reactions when using multiple sources on single transmitter!  
There only is one permission:
* gsurvivalextras.swrs: Enables user to create the elements.  

### Transmitter
As first the transmitter needs to be build, when thats been placed you can place an receiver/sensor
Just place a sign with `[transmitter]` in the first line and your done!
*Warning: This can only be a sign-post*

### Receiver/Sensor
Place a sign with as first line `[receiver]` or `[sensor]`, when done the other 3 lines will be filled in with the coordinates of the transmitter.
This will power the transmitter if triggered!  
Sensor works when you have it under the ground.  

To hook up multiple receivers/sensors to a single transmitter, destroy and replace the transmitter and place the new receiver/sensor.
*Warning: Because this system is 'stupid', in combination with multiple sources, when more are on, when one drops the transmitter drops!*
Using multiple sensors in an area will be advised since the movement is not always catched. The sensor is writting to look at from-to position and determine if you still are on a sensor to the same transmitter!


RSNetherrack
------------
Create amazing fire effects by setting or extinguishing netherrack using redstone!  
This does give some client-side load if you overuse it!  
### Issue
When using in combination with SWRS if the transmitter is directly under, with no other wires attached, when turning it will not update and so not extinguish.

NoCreeperWorldDamage
--------------------
Nothing much to tell here.. just disables world damage caused by creepers..  
Will allow TNT damage!

NoGrow - Unfinished!
------
This plugin can be used to make the entire server or areas not grow plants, so you get a static world.  
Or just because you do not want survivallers to be able to eat home-grown stuff