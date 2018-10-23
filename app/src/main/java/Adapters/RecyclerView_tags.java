package Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import Models.Tag_Object;
import honeybadgersapp.honeybadgers.R;

public class RecyclerView_tags extends RecyclerView.Adapter<RecyclerView_tags.MyViewHolder>{

    Context mContext;
    ArrayList<Tag_Object> tags_list;

    public RecyclerView_tags(Context mContext, ArrayList<Tag_Object> tags_list) {
        this.mContext = mContext;
        this.tags_list = tags_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.tag,parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);
        return  vHolder;
    }


    public void onBindViewHolder(MyViewHolder holder, int position){

        switch (tags_list.get(position).getName()) {
            case "Java": {
                holder.Tag_Text.setTextColor(Color.parseColor("#FFAF5E32"));
                break;
            }
            case "Python": {
                holder.Tag_Text.setTextColor(Color.parseColor("#FFAB6EBE"));
                break;
            }
            case "C++": {
                holder.Tag_Text.setTextColor(Color.parseColor("#FF1827C4"));
                break;
            }
            case "Painting": {
                holder.Tag_Text.setTextColor(Color.parseColor("#FF8ECDD4"));
                break;
            }
            default:
                break;
        }
        holder.Tag_Text.setText(tags_list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return tags_list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView Tag_Text;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Tag_Text = itemView.findViewById(R.id.tag);
        }
    }


}
