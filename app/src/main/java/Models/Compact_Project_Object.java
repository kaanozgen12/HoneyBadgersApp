package Models;

import java.util.ArrayList;

public class Compact_Project_Object {
    private String name;
    private int highestbid;
    private String numberofbidsandlastupdate;
    private ArrayList<Tag_Object> tags = new ArrayList<>();

    public Compact_Project_Object(String name, int highestbid, String numberofbidsandlastupdate, ArrayList<String> tags) {
        this.name = name;
        this.highestbid = highestbid;
        this.numberofbidsandlastupdate = numberofbidsandlastupdate;
        this.addTags(tags);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHighestbid() {
        return highestbid;
    }


    public String getNumberofbidsandlastupdate() {
        return numberofbidsandlastupdate;
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
