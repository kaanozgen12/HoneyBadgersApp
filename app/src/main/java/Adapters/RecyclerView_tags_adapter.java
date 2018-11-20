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

import RetrofitModels.Tag_Object;
import honeybadgersapp.honeybadgers.R;

public class RecyclerView_tags_adapter extends RecyclerView.Adapter<RecyclerView_tags_adapter.MyViewHolder>{

    private Context mContext;
    private ArrayList<Tag_Object> tags_list;

    public RecyclerView_tags_adapter(Context mContext, ArrayList<Tag_Object> tags_list) {
        this.mContext = mContext;
        this.tags_list = tags_list;
        //setHasStableIds(true);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag,parent, false);
        return new MyViewHolder(v);
    }



    public void onBindViewHolder(MyViewHolder holder, int position){

        (holder).bindData(tags_list.get(position));
    }

    @Override
    public int getItemCount() {
        return tags_list.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.tag;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView Tag_Text;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Tag_Text = itemView.findViewById(R.id.tag);
        }

        void bindData(final Tag_Object viewModel){
            int temp = 1+viewModel.getId();

            String[] array = {"#FF007F", "#FF0000", "#FF7F00", "#FFABAB17", "#7FFF00", "#00FF00", "#00FF7F", "#00FFFF", "#007FFF", "#0000FF", "#7F00FF", "#FF00FF"};
            String color = array[temp%12];
            Tag_Text.setTextColor(Color.parseColor(color));
            Tag_Text.setText(viewModel.getTitle());

        }
    }


}
