package app.model;


import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LanguageManager {
    private static final String root = "src/main/resources/languagePack";
    public List<String> languages;
    private Map<String,String> resources;

    public void setResources(String language) throws IOException {
        System.out.println("set language to " + language);
        Reader reader = Files.newBufferedReader(Paths.get(root,language+".json"));
        resources = new Gson().fromJson(reader, Map.class);
    }

    public String getResource(String key){
        return resources.get(key);
    }

    public List<String> getLanguages(){
        return this.languages;
    }

    public void initLanguages(){
        //System.out.println("initialize languages");
        languages = new ArrayList<>();
        File f = new File(root);
        if (!f.exists()) {
            System.out.println(root + " not exists");
            return;
        }

        File fa[] = f.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (fs.isFile()) {
                //System.out.println("language file found "+fs.getName().replace(".json",""));
                languages.add(fs.getName().replace(".json",""));
                }
            }
        }
}
