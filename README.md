# EasyTravel
A minecraft spigot plugin to help players navigate to far destinations in their minecraft world

## What it is
EasyTravel is a minecraft plugin for bukkit and spigot servers. The plugin provides features to help players manage their coordinates and to help them travel form one location to another. EasyTravel neither modifies the vanilla gameplay nor gives any player an unfair advantage.

## How it works
There are some basic concepts which every user should know in order to use the full potential of this plugin:
### Private and public waypoints
Waypoints allow the players to store their coordinates under a specific name, so they can always access them.
There are two kinds of waypoints: private and public ones
Because knowing the coordinates of a player gives others a big advantage, most players want their coordinates to be secret. Private waypoints can only be accessed by the player who created them. On the other hand there are also scenarios where you want everybody to be able to see your coordinates. Public waypoints can be seen by everybody but only edited by their creator.
### Travel
When playing minecraft you often have to travel to specifiy locations. With waypoints you can store the coordinates of these locations and with travelling you can get to them. The travel feature in EasyTravel works like a navy in a car. It tells you how long you still have to travel, how far your destination is and where you have to go. The plugin itself doesn't travel for the players, they still have to walk every block by themselves, the plugin just shows them the fastest route to do so.

## Installation
You install this plugin just like you would install any other
1. Download the .jar file (download link coming soon)
2. Drop the .jar file in the plugins folder of the server (the plugin should work on bukkit, spigot and paper)
3. Restart the sever
4. Read the documentation and enjoy

## Examples
### Waypoints
You have just finished your new base. Now you want to save the coordinates of it. So you create a private waypoint: <br>
`/addwaypoint MyNewBase`
Create a new private waypoint with the coordinates you are currently standing on. <br>
<br>
Now you find that the name "MyNewBase" isn't the most original one, so you want to rename your waypoint: <br>
`/editwaypoint MyNewBase name ABetterName`
Rename your waypoint "MyNewBase" to "ABetterName". <br>
<br>
After some serious base renovations the entrance of your base has moved, so you want to change the coordinates stored in your waypoint: <br>
`/editwaypoint ABetterName position self`
Change the coordinates of the waypoint "ABetterName" to where you are currently standing.

## To-do
- Add direction bossbar to travel
- Expand README and documentation
- Add player friend system
- Allow player as travel destination
- Add config file
- Clean-up command code
- Set up a spigot plugin page
