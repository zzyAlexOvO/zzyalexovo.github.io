class SpongeBob{
   int x;
   int y;
   int x_vel;
   int y_vel;
   PShape shape;
   
   public SpongeBob(){
     this.x = int(random(1200));
     this.y = 500;
     this.x_vel = int(random(5,10));
     this.y_vel = 0;
     this.shape = bobR;
   }
   
   void display(){
     pushMatrix();
     translate(x,y);
     shape(shape);
     popMatrix();
     
     this.x += x_vel;
     this.y += y_vel;
     if(x <= 0){
       x = 0;
       x_vel = -x_vel;
       shape = bobR;
     }else if(x>=1200){
       x = 1200;
       x_vel = -x_vel;
       shape = bobL;
     }
     
     if(bob.y < 500){
       bob.y_vel += gravity;
     }else{
       bob.y_vel = 0;
     }
   }
}
