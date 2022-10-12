import processing.opengl.*;

ArrayList<Ball> balls;
ArrayList<PShape> globes;
Box box;

int radius = 15;
int listSize = 0;

double gravity;

void setup() {
  size (600,600,P3D);
  noStroke();
  smooth();
  balls = new ArrayList();
  globes = new ArrayList();
  box = new Box();
  PImage football = loadImage("texture\\football.jpg");
  PImage basketball = loadImage("texture\\Basketball.png");
  PImage snooker = loadImage("texture\\8.png");
  
  
  PShape globe1 = createShape(SPHERE, 20);
  globe1.setTexture(football);
  globe1.setStroke(color(0, 0));  
  
  PShape globe2 = createShape(SPHERE, 20);
  globe2.setTexture(basketball);
  globe2.setStroke(color(0, 0));  
  
  PShape globe3 = createShape(SPHERE, 20);
  globe3.setTexture(snooker);
  globe3.setStroke(color(0, 0));  
  
  globes.add(globe1);
  globes.add(globe2);
  globes.add(globe3);
  
  gravity = 1;
  
}

void draw(){
   background(255);
   translate(width/2,height/2,-300);

  for (int i=0; i< balls.size();i++) {  
    Ball thisBall = balls.get(i);
    thisBall.move();
    thisBall.display();
    thisBall.collide();
  }
  box.display();
}


void mouseClicked(){
  PVector location = new PVector(mouseX-width/2,mouseY-height/2,285);
  PVector speed = new PVector(random(-10,10),random(-10,10),random(-10,10));
  balls.add(new Ball(speed, radius,location,globes.get(int(random(0,globes.size())))));
}
  
