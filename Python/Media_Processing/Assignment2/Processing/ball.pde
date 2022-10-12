class ball{
  int x;
  int y;
  int x_vel;
  int y_vel;
  int size;
  boolean delete;
  
  public ball(int x, int y){
    this.x = x;
    this.y = y;
    this.x_vel = int(random(5,10));
    this.y_vel = 0;
    this.size = int(random(10,20));
    delete = false;
  }
  
  void display(){
    beginShape();
    fill(246,163,165);
    circle(x, y, size);
    endShape();
    noFill();
    
    this.y += y_vel;
    this.x += x_vel;
    this.y_vel += gravity;
    if(this.y >= 700-size){
      y = 700-size;
      y_vel = int(-0.8 * y_vel);
    }
  }
}
