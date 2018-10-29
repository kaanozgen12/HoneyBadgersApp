package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import RetrofitModels.Tag_Object;
import honeybadgersapp.honeybadgers.R;

public class RecyclerView_tags extends RecyclerView.Adapter<RecyclerView_tags.MyViewHolder>{

    Context mContext;
    ArrayList<Tag_Object> tags_list;

    public RecyclerView_tags(Context mContext, ArrayList<Tag_Object> tags_list) {
        this.mContext = mContext;
        this.tags_list = tags_list;
        setHasStableIds(true);
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


        int temp = 1+new ArrayList( Arrays.asList( mContext.getResources().getStringArray(R.array.Tags) )).indexOf(tags_list.get(position).getTitle());
        Log.d("MyTag","temp: "+temp);
        holder.Tag_Text.setTextColor((temp*Integer.parseInt("1000", 16)+0xFFDE342E));
        holder.Tag_Text.setText(tags_list.get(position).getTitle());
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
        return super.getItemViewType(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView Tag_Text;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Tag_Text = itemView.findViewById(R.id.tag);
        }
    }


}
