package Models;

import java.util.ArrayList;

public class Compact_Project_Object {
    private String name;
    private String description;
    private ArrayList<Tag_Object> tags = new ArrayList<>();


    public Compact_Project_Object(String name, String description,ArrayList<String>tags) {
        this.name = name;
        this.description = description;
        this.addTags(tags);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void addTags(ArrayList<String> tags){
        for (int i = 0; i< tags.size(); i++){
            this.tags.add(new Tag_Object(tags.get(i)));
        }
    }

    public ArrayList<Tag_Object> getTags() {
        return tags;
    }
}
