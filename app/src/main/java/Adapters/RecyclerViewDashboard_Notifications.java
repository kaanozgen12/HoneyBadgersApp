package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Models.Compact_Project_Object;
import honeybadgersapp.honeybadgers.R;

public class RecyclerViewDashboard_Notifications extends RecyclerView.Adapter<RecyclerViewDashboard_Notifications.MyViewHolder>{



    Context mContext;
    List<Compact_Project_Object> notification_cards;

    public RecyclerViewDashboard_Notifications(Context mContext, List<Compact_Project_Object> notification_cards) {
        this.mContext = mContext;
        this.notification_cards = notification_cards;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.compact_project_object,parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);

        vHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"Test click"+String.valueOf(vHolder.getAdapterPosition()),Toast.LENGTH_SHORT).show();
            }
        });
        return  vHolder;
    }




    public void onBindViewHolder(MyViewHolder holder, int position){
//        holder.current_progressbar.setProgress(project_cards.get(position).getList_current());
// get name description and tags
//        switch (project_cards.get(position).getProduct_type()){
//            case "deneme":
//            //    holder.product_button.setBackgroundResource(R.drawable.deneme);
//                break;
//
//        }
        holder.project_name.setText(notification_cards.get(position).getName());
        holder.project_description.setText(notification_cards.get(position).getDescription());
        RecyclerView_tags recyclerAdapter =new RecyclerView_tags(mContext,notification_cards.get(position).getTags());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerViewTags.setLayoutManager(horizontalLayoutManager);
        holder.recyclerViewTags.setAdapter(recyclerAdapter);

        }

    @Override
    public int getItemCount() {
        return notification_cards.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView project_name;
        private TextView project_description;
        private RecyclerView recyclerViewTags;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            project_name = itemView.findViewById(R.id.compact_project_object_name);
            project_description = itemView.findViewById(R.id.compact_project_object_description);
            recyclerViewTags= itemView.findViewById(R.id.compact_project_object_tags_recyclerview);

        }
    }


}
