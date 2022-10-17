package app.model;

import javafx.scene.control.Hyperlink;

import java.util.ArrayList;
import java.util.List;

public class BreadcrumbObserver {
    private List<Hyperlink> links;
    public BreadcrumbObserver(){
        this.links = new ArrayList<>();
    }
    public void update(Hyperlink link){
        this.links.add(link);
        if (this.links.size() > 6){
            this.links.remove(0);
        }
    }
    public List<Hyperlink> get(){
        return links;
    }
}
