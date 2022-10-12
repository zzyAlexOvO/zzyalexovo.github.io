Apart from the original readme instructions.

I implemented the save&load function where pressing "s" key would save the current state of the game, and pressing "L" key would load the last saved state. Loading without any saved state would have no effect.

And for the square cat's configuration in the level object:
      "squareCat": {
        "type": "squareCat",
        "image": "squareCat.png",
        "startX": 150.0,
        "startY": 300.0
      }

To specify the score and slime colour the game would contain, put: colours: ["red","green","blue"] in the root level.

The scores can be seen at the top-left corner of the window.

I have used two design pattern except prototype pattern.

For the observe pattern see the package named "observer"
For the memento pattern see the package name "memento"

Cheatmode is unfortunately not implemented.