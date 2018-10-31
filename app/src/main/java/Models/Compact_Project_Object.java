package Models;

import java.util.ArrayList;

import RetrofitModels.Tag_Object;

public class Compact_Project_Object {
    private  int id;
    private String name;
    private int highestbid;
    private String numberofbidsandlastupdate;
    private ArrayList<Tag_Object> tags;


    public Compact_Project_Object(int id, String name, int highestbid, String numberofbidsandlastupdate, ArrayList<Tag_Object> tags) {
        this.id = id;
        this.name = name;
        this.highestbid = highestbid;
        this.numberofbidsandlastupdate = numberofbidsandlastupdate;
        this.tags = tags;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setHighestbid(int highestbid) {
        this.highestbid = highestbid;
    }

    public String getNumberofbidsandlastupdate() {
        return numberofbidsandlastupdate;
    }

    public void setNumberofbidsandlastupdate(String numberofbidsandlastupdate) {
        this.numberofbidsandlastupdate = numberofbidsandlastupdate;
    }

    public ArrayList<Tag_Object> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag_Object> tags) {
        this.tags = tags;
    }


}
