package RetrofitModels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Tag_Object implements Serializable
{

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("title")
    @Expose
    private String title;
    private final static long serialVersionUID = -1342096910995845866L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Tag_Object() {

    }

    /**
     *
     * @param id
     * @param title
     */
    public Tag_Object(int id, String title) {
        super();
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass()==Tag_Object.class){
            return this.id==((Tag_Object)obj).getId();
        }else{
            return super.equals(obj);
        }

    }


}
