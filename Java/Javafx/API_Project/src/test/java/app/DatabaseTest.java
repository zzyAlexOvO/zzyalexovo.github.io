package app;

import app.model.DBmanager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseTest {
    static DBmanager dBmanager;

    @BeforeAll
    static public void init(){
        dBmanager = new DBmanager();
    }
    @BeforeEach
    public void refresh(){
        dBmanager.removeDB();
        dBmanager.createDB();
        dBmanager.setupDB();
    }
    @Test
    public void breadcrumbTest(){
        dBmanager.insertItem("comic","apple",1);
        List<String[]> list = dBmanager.listItems();
        List<String[]> expect = new ArrayList<>();
        expect.add(new String[]{"apple","comic","1"});
        assertTrue(Arrays.equals(list.get(0), expect.get(0)));
    }
    @Test
    public void checkCacheTest(){
        dBmanager.insertCache("comic",1,"josh","info");
        assertTrue(dBmanager.checkCache("comic",1).equals("info"));
        assertTrue(dBmanager.checkCache("comic",2) == null);
        assertTrue(dBmanager.checkCache("comic","josh").equals("info"));
        assertTrue(dBmanager.checkCache("comic","alex") == null);
        assertTrue(dBmanager.checkCache("character",1) == null);
        assertTrue(dBmanager.checkCache("character","josh") == null);
    }
    @Test
    void clearCacheTest(){
        dBmanager.insertCache("comic",1,"josh","info");
        dBmanager.clearCache();
        assertTrue(dBmanager.checkCache("comic",1) == null);
        assertTrue(dBmanager.checkCache("comic","josh") == null);
    }
    @Test
    void updateCacheTest(){
        dBmanager.insertCache("comic",1,"josh","info");
        dBmanager.updateCache("comic",1,"josh","new info");
        assertTrue(dBmanager.checkCache("comic","josh").equals("new info"));
    }
}
