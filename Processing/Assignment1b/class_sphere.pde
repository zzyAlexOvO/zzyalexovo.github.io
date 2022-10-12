class Ball {

  PVector location;
  PVector speed;
  int radius; 
  PShape globe;

  Ball(PVector speed, int radius, PVector location, PShape globe) {
    this.speed = speed;
    this.radius = radius;
    this.location =location;
    this.globe = globe;
  }  

  void move() {
    speed.y += gravity;
    location.add(speed);

    if (location.x > box.boxsize/2-radius) {
      location.x= box.boxsize/2-radius;
      speed.x*=-0.8;
    } else if (location.x < -box.boxsize/2 + radius) {
      location.x = -box.boxsize/2 +radius;
      speed.x*=-0.8;
    }
    if (location.y > box.boxsize/2-radius) {
      location.y = box.boxsize/2-radius ;
      if(speed.y <= 1){
        speed.x *= 0.9;
        speed.z *= 0.9;
        speed.y*= -0.8;
      }else{
        speed.y*= -0.8;
      }
    } else if (location.y < -box.boxsize/2 + radius) {
      location.y= -box.boxsize/2+radius;
      speed.y*= -0.8;
    }
    if (location.z > box.boxsize/2-radius) {
      location.z = box.boxsize/2-radius;
      speed.z*= -0.8;
    } else if (location.z < -box.boxsize/2 + radius) {
      location.z = -box.boxsize/2 + radius;
      speed.z*= -0.8;
    }
  }

  void collide() {
    for (int i=0; i < balls.size() -1; i++) {
      Ball ballA = balls.get(i);
      for (int j= i+1; j < balls.size(); j++) {
        Ball ballB = balls.get(j);
        if (!ballA.equals(ballB) && ballA.location.dist(ballB.location) < (ballA.radius+ ballB.radius)) {
          bounce(ballA, ballB);
        }
      }
    }
  }

  void display() {
    pushMatrix();
    translate(location.x, location.y, location.z);
    rotateY(speed.y * PI/20);
    rotateX(speed.x * PI/20);
    rotateZ(speed.z * PI/20);
    shape(this.globe);
    popMatrix();
  }

  void bounce(Ball ballA, Ball ballB) {
    PVector ab = new PVector();
    ab.set(ballA.location);
    ab.sub(ballB.location);
    ab.normalize();
    while (ballA.location.dist(ballB.location) < (ballA.radius + ballB.radius)) {
      ballA.location.add(ab);
    }
    PVector x = PVector.sub(ballA.location, ballB.location);
    x.normalize();
    PVector y = PVector.sub(ballA.speed, ballB.speed);
    PVector z = componentVector(x, y);
    x.sub(z);
    ballA.speed = PVector.add(x, ballB.speed);
    ballB.speed= PVector.add(z, ballB.speed);
  }

  PVector componentVector (PVector vector, PVector directionVector) {
    directionVector.normalize();
    directionVector.mult(vector.dot(directionVector));
    return directionVector;
  }
}
