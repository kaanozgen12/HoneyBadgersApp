package RetrofitModels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProjectCategory implements Serializable
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
    public ProjectCategory() {
    }

    /**
     *
     * @param id
     * @param title
     */
    public ProjectCategory(int id, String title) {
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

}
