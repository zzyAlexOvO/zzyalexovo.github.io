class Mariontte{
  int y;
  int y_vel;
  
  void display(int left_arm_x, int left_arm_y,
  int right_arm_x, int right_arm_y,
  int body_x, int body_y,
  int left_leg_x, int left_leg_y,
  int right_leg_x, int right_leg_y){
  int neck_length = 100;
  
  pushMatrix();
  translate(0,y);
  beginShape();
  fill(246,163,165);
  curveVertex(left_arm_x - (body_x-left_arm_x)/2, left_arm_y - (body_y-left_arm_y)/2);
  curveVertex(left_arm_x - (body_x-left_arm_x)/2, left_arm_y - (body_y-left_arm_y)/2); //1
  curveVertex((left_arm_x + left_leg_x+body_x)/3, (left_arm_y + left_leg_y+body_y)/3); //2
  curveVertex(left_leg_x - (body_x-left_leg_x)/2, left_leg_y - (body_y-left_leg_y)/2); //3
  curveVertex((left_leg_x+right_leg_x+body_x)/3,(left_leg_y+right_leg_y+body_x)/3); //4
  curveVertex(right_leg_x - (body_x-right_leg_x)/2, right_leg_y - (body_y-right_leg_y)/2); //5
  curveVertex((right_arm_x+right_leg_x+body_x)/3,(right_arm_y+right_leg_y+body_y)/3); //6
  curveVertex(right_arm_x - (body_x-right_arm_x)/2, right_arm_y - (body_y-right_arm_y)/2); //7
  curveVertex((body_x+right_arm_x)/2,body_y-neck_length/2); //8
  curveVertex((left_arm_x+right_arm_x+body_x)/3,body_y-neck_length*1.5); //9
  curveVertex((body_x+left_arm_x)/2,body_y-neck_length/2); //10
  curveVertex(left_arm_x - (body_x-left_arm_x)/2, left_arm_y - (body_y-left_arm_y)/2); //11
  curveVertex(left_arm_x - (body_x-left_arm_x)/2, left_arm_y - (body_y-left_arm_y)/2);
  endShape();
  popMatrix();
  this.y += y_vel;
  if(y >= 400){
    y = 400;
    y_vel = -y_vel;
  }
  }
  
  void fire(int x, int y){
    balls.add(new ball(x,y));
  }
}
