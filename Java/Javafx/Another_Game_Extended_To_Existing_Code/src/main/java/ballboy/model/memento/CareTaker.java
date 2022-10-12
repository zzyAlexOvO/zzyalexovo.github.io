package ballboy.model.memento;

public class CareTaker {
    private Memento memento;

    public void save(Memento memento){
        this.memento = memento;
    }

    public Memento load(){
        return memento;
    }
}
