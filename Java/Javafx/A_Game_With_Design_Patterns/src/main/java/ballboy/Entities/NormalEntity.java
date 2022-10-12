package ballboy.Entities;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class NormalEntity implements Entity {

    private ArrayList<Image> images;
    private Image image;
    private double xPos;
    private double yPos;
    private double height;
    private double width;
    private Layer layer;
    private double xVel = 0;
    private double yVel = 0;

    public NormalEntity(double x, double y, double height, double width, Layer layer, ArrayList<String> images){
        this.xPos = x;
        this.yPos = y;
        this.height = height;
        this.width = width;
        this.layer = layer;
        this.images = new ArrayList<Image>();
        for(String i : images){
            this.images.add(new Image(i));
        }
        this.image = this.images.get(0);
    }

    public NormalEntity(double x, double y, double height, double width, Layer layer, String image){
        this.xPos = x;
        this.yPos = y;
        this.height = height;
        this.width = width;
        this.layer = layer;
        this.image = new Image(image);
    }
    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public double getXPos() {
        return this.xPos;
    }

    @Override
    public double getYPos() {
        return this.yPos;
    }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public Layer getLayer() {
        return this.layer;
    }

    @Override
    public void move() {
        this.xPos += xVel;
        this.yPos += yVel;
    }

    @Override
    public void setYVel(double yVel){
        this.yVel = yVel;
    }

    @Override
    public double getYVel(){
        return this.yVel;
    }

    @Override
    public double getXVel(){
        return this.xVel;
    }

    @Override
    public void setXVel(double xVel){
        this.xVel = xVel;
    }

    @Override
    public void think(){
        this.move();
    }

    @Override
    public void setYPos(double yPos){
        this.yPos = yPos;
    }

    @Override
    public void setXPos(double xPos){
        this.xPos = xPos;
    }

    @Override
    public boolean isBackground(){
        if (this.layer != Layer.FOREGROUND){
            return true;
        }else {
            return false;
        }
    }
}
