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

public class Create_project_tags_adapter extends RecyclerView.Adapter<Create_project_tags_adapter.MyViewHolder>{

    private Context mContext;
    private ArrayList<Tag_Object> tags_list;

    public Create_project_tags_adapter(Context mContext, ArrayList<Tag_Object> tags_list) {
        this.mContext = mContext;
        this.tags_list = tags_list;
        //setHasStableIds(true);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag,parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);
        vHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tags_list.remove(vHolder.getAdapterPosition());
                synchronized (this){
                    notifyDataSetChanged();
                }
            }
        });
        return  vHolder;
    }


    @Override
    public int getItemViewType(int position) {
        return R.layout.tag;
    }

    public void onBindViewHolder(@NonNull MyViewHolder holder, int position){

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

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView Tag_Text;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Tag_Text = itemView.findViewById(R.id.tag);
        }
        void bindData(final Tag_Object viewModel){
            int temp = 1+viewModel.getId();
            String[] array = {"#8E443D", "#511730", "#6A4C93", "#1982C4", "#86BA90", "#376996", "#1D3461", "#42858C", "#397367", "#E7BB41", "#DE6B48", "#8C5383"};
            String color = array[temp%12];
            Tag_Text.setText(viewModel.getTitle()+ " ⓧ");
            Tag_Text.setTextColor(Color.parseColor(color));
        }
    }


}
