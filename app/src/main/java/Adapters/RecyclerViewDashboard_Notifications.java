package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import Models.Dashboard_Notification_Object;
import honeybadgersapp.honeybadgers.R;

public class RecyclerViewDashboard_Notifications extends RecyclerView.Adapter<RecyclerViewDashboard_Notifications.MyViewHolder>{



    Context mContext;
    List<Dashboard_Notification_Object> notification_cards;



    public RecyclerViewDashboard_Notifications(Context mContext, List<Dashboard_Notification_Object> notification_cards) {
        this.mContext = mContext;
        this.notification_cards = notification_cards;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.Dashboard_Notification,parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);

//        vHolder.itemView.setOnClickListener(new View.OnClickListener() {
//
//        });
        return  vHolder;
    }




    public void onBindViewHolder(MyViewHolder holder, int position){
//        holder.current_progressbar.setProgress(project_cards.get(position).getList_current());
//
//        switch (project_cards.get(position).getProduct_type()){
//            case "deneme":
//            //    holder.product_button.setBackgroundResource(R.drawable.deneme);
//                break;
//
//        }
        }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private Button product_button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            product_button = (Button) itemView.findViewById(R.id.button_project_object);

        }
    }


}
