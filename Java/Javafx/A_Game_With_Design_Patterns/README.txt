1. To run the code
Simply use "gradle run" in the terminal

2. Instruction on playing
"Left": pressing left button would increase the hero's speed towards left till he reaches the max speed.
"Right": pressing right button would increase the hero's speed towards right till he reaches the max speed.
DO NOTE: Not pressing left and right would make hero eventually stop horizontally.
"Up": pressing up button would increase hero's speed towards up side, which means when you press "up" when the hero is bouncing up, hero would bounce higher, and when you press "up" when the hero is bouncing down,hero would bounce lower.

3. Format and classes reading process
Please see the example configuration file.

Firstly the game engine would take the whole json file.
Secondly the game engine would try to read the level from index "1" which is the start level.
Thirdly the json snippet for level 1 would be parsed to level builder.
Fourthly after the level builder has received the json snippet, it would follow the building procedure.
Respectively,
level builder parse the smaller snippet about level info and hero info into the related builder to call the related factory and receive the completed entity;
parse the smaller snippet about enemies info into the related builder to call the related factory and receive the completed entity;
do the same things for flag, cloud etc. until all the builder processes have been completed.
Sixthly, the game engine would call the level builder's generate method to receive the fully built level object as the current level.
Seventhly, the game may start.

4. Demonstration on variables
The variables of level:
height and width are describing the size of this level.
gravity is describing the gravity of this level, which determines the dropping speed for all the objects that accepts gravity.
max_speed stands for the max speed hero could achieve horizontally.
accelerate is the hero's acceleration rate for moving(eg. when accelerate = 0.1, pressing "Left" every frame would give hero a speed of "0.1" towards left)

The variables of entities:
Image refers to the image path that would be shown for the entity. Specially for entities like enemy who has a list of images, it might be used for the state changing feature in the future, however it has not been implemented yet.
X refers to the x position.
Y refers to the y position.
Height and width refers to the size.
Specially for hero, the size has three options as "small", "medium" and "large" as required and theirs corresponding height and width are default in the hero factory. However I do prefer to move the corresponding height and width value into the configuration file if not this requirement.

##################
PS:
By default, the acceleration rate for hero to go up is by default 3/4 of the given acceleration rate.