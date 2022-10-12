package ballboy.Entities;

public class Cloud extends NormalEntity{
    public Cloud(double x, double y, double height, double width, Layer layer, String image) {
        super(x, y, height, width, layer, image);
        this.setXVel(-0.1);
    }

    @Override
    public void think() {
        if (this.getXPos()+this.getWidth() <= 0){
            this.setXPos(3000);
        }
        this.move();
    }
}
