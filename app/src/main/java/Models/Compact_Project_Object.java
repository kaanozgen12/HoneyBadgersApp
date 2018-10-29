package Models;

import java.util.ArrayList;

import Adapters.RecyclerView_tags;
import RetrofitModels.Tag_Object;

public class Compact_Project_Object {
    private  int id;
    private String name;
    private int highestbid;
    private String numberofbidsandlastupdate;
    private ArrayList<Tag_Object> tags = new ArrayList<>();
    private RecyclerView_tags mRecylerView;

    public Compact_Project_Object(int id, String name, int highestbid, String numberofbidsandlastupdate, ArrayList<Tag_Object> tags) {
        this.id = id;
        this.name = name;
        this.highestbid = highestbid;
        this.numberofbidsandlastupdate = numberofbidsandlastupdate;
        this.tags = tags;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RecyclerView_tags getmRecylerView() {
        return mRecylerView;
    }

    public void setmRecylerView(RecyclerView_tags mRecylerView) {
        this.mRecylerView = mRecylerView;
    }

    public void setHighestbid(int highestbid) {
        this.highestbid = highestbid;
    }

    public void setNumberofbidsandlastupdate(String numberofbidsandlastupdate) {
        this.numberofbidsandlastupdate = numberofbidsandlastupdate;
    }

    public void setTags(ArrayList<Tag_Object> tags) {
        this.tags = tags;
    }

    public ArrayList<Tag_Object> getTags() {
        return tags;
    }
}
