import java.io.*;
import java.util.HashMap;
import java.util.List;
import processing.sound.*;


SoundFile sound;
PImage bg;
PImage role;
PShape bobL;
PShape bobR;

int counter;

Mariontte main_role;
SpongeBob bob;
HashMap<Integer,HashMap<String,int[]>> movement;
List<ball> balls;
int gravity;

void setup(){
  size(1400,700,P2D);
  noStroke();
  smooth();
  frameRate(30);
  
  main_role = new Mariontte();
  main_role.y = int(random(400));
  main_role.y = int(random(-5,5));
  
  bg = loadImage("sb.jpg");
  role = loadImage("Patrick_Star.png");
  
  bobL = createShape(RECT,0,0,200,200);
  bobL.setTexture(loadImage("SpongeBobL.png"));
  
  bobR = createShape(RECT,0,0,200,200);
  bobR.setTexture(loadImage("SpongeBobR.png"));
  
  bob = new SpongeBob();
  
  movement = new HashMap();
  balls = new ArrayList();
  
   
  String[] file = loadStrings("movement_feature.txt");
  for(String str:file){
    String[] para = str.split(" ");
    movement.put(Integer.valueOf(para[0]),Utility.parse(para[1]));
  }
  
  sound = new SoundFile(this, "Sound.mp3");

   counter = 0;
   gravity = 1;
}
void draw(){
  background(bg);
  textSize(20);
  fill(255,0,0);
  text("SID470482647_Asgmt2Opt1", 10, 20); 
  noFill();
  HashMap<String,int[]> map = movement.get(counter%movement.size());
  main_role.display(map.get("left_hand")[0],map.get("left_hand")[1] + main_role.y,map.get("right_hand")[0],map.get("right_hand")[1] + main_role.y,map.get("body")[0],map.get("body")[1] + main_role.y,map.get("left_leg")[0],map.get("left_leg")[1] + main_role.y,map.get("right_leg")[0],map.get("right_leg")[1] + main_role.y);
  bob.display();
  int luck = int(random(150));
  if(luck == 100){
    main_role.fire(map.get("right_hand")[0],map.get("right_hand")[1]);
  }
  for(ball i:balls){
    if(!i.delete){
      i.display();
      if(i.x >= bob.x - i.size && i.x <= bob.x + 200 + i.size && i.y >= bob.y - i.size && i.y <= bob.y + 200 + i.size){
        i.delete = true;
        bob.y_vel = -20;
        sound.play();
      }
    }
  }
  
  saveFrame("./output/frame####.png");
  counter++;
  if(counter >= 1800){
    exit();
  }
}
