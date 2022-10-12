# Instruction of steps
## To generate the scene based on already-generated movements
1. Go to "Processing" folder
2. Run "Processing.pde"
3. After the video ends, use the "Movie Maker" at the top menu to generate video from "output" folder.

##  To generate the video from beginning with the original video
1. Open "Movement_processer.ipynb"
2. Run all the cells in order except the last cell.
3. Copy the generated "movement_feature.txt" file into "Process" folder. 
4. Go to "Processing" folder
5. Run "Processing.pde"
6. After the video ends, use the "Movie Maker" at the top menu to generate video from "output" folder.

# Steps
To complete the whole project, I have used the combination of python and processing, where python is mainly for generating the movement feature, and processing is mainly for the video production.

## Python
1. Retrieve all the frames from the original video.
2. For all the frames, replace the blue background with whole black background.
3. Using my own algorithm to find 5 most-red points in each frame.
4. Save all these 5-points set into a text file alone with their correlated frame number.
5. The last cell of the python file could draw green points which is picked by my algorithm in the frames, which can be used to check the accuracy of detection.

## Processing
1. Set the background.
2. Generate the information from the text file and use them to conduct my own character's movement (Patrick).
3. Add the Sponge Bob class to represent this side character and its behavior.
4. Implement Sponge Bob's display method to let it display a square with texture filled.
5. Implement ball class to represent the balls Patrick throws.
6. In short, apart from these movement, behavior, collision design, I also add a sound to play when the collision event occurs.
7. At last, processing would save every frames into a single folder until it reaches 1800. Then I used the "Movie Maker" built-in tool of processing to composite all the frames into one 1-min video.

# Extent of python
1. For the algorithm implementation, for every frame it would check 1/9 points in it. And for each point, it would generate the 20*20 blocks near it. This is similar to template matching, but alternatively, it would ignore all the black blocks, and compare all other blocks with one single color that is generated from the red dot. Then after computing the altered "SSD", I additionally divide it by the number of non-black blocks checked, which results in the final "average similarity" value of that point. Finally after the values for all the pixels in the frame have been computed, it would repressively save the point with least value, and the points that is less than 20 units away from the chose points would not be selected.
2. These algorithm cannot always generate correct output, which might due to lack of experimenting. However, it can be seen that the overall accuracy is more than 90 percent.
