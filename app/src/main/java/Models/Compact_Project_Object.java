package Models;

import java.util.ArrayList;

import RetrofitModels.Tag_Object;

public class Compact_Project_Object {
    private  int id;
    private String name;
    private String highestbid;
    private String numberofbidsandlastupdate ;
    private boolean isVerified;
    private boolean isMoneySignFirst;
    private boolean isApproved;
    private ArrayList<Tag_Object> tags;


    public Compact_Project_Object(int id, String name, String highestbid, String numberofbidsandlastupdate, ArrayList<Tag_Object> tags,boolean isMoneySignFirst, boolean isApproved) {
        this.id = id;
        this.name = name;
        this.highestbid = highestbid;
        this.numberofbidsandlastupdate = "0 bids - ";
        this.tags = tags;
        this.isMoneySignFirst=isMoneySignFirst;
        this.isApproved=isApproved;
    }


    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
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

    public String getHighestbid() {
        return highestbid;
    }

    public void setHighestbid(String highestbid) {
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

    public boolean isMoneySignFirst() {
        return isMoneySignFirst;
    }

    public void setMoneySignFirst(boolean moneySignFirst) {
        isMoneySignFirst = moneySignFirst;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    @Override
    public boolean equals( Object obj) {
        if(obj.getClass()==Compact_Project_Object.class){
           return id == ( (Compact_Project_Object) obj).id;
        }
        return false;
    }
}
