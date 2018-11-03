package Adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import Models.Compact_Project_Object;
import honeybadgersapp.honeybadgers.R;

public class Comment_adapter extends RecyclerView.Adapter<Comment_adapter.MyViewHolder>{



    private Dialog myDialog;
    private Context mContext;
    private List<Compact_Project_Object> notification_cards;
    //private int lastAnimatedPosition = -1;

    public Comment_adapter(Context mContext, List<Compact_Project_Object> notification_cards) {
        this.mContext = mContext;
        this.notification_cards = notification_cards;
        //setHasStableIds(true);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType){

        final View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        //v = LayoutInflater.from(mContext).inflate(R.layout.compact_project_object,parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);

        vHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }

        });
        return  vHolder;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return notification_cards.size();
    }
    @Override
    public int getItemViewType(final int position) {
        return R.layout.comment_object;
    }


    public synchronized void onBindViewHolder(@NonNull MyViewHolder holder, final int position){

        holder.bindData(notification_cards.get(position));



    }





    public class MyViewHolder extends RecyclerView.ViewHolder{
        private int comment_id;
        private TextView comment_user_email;
        private TextView comment_body;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            comment_user_email = itemView.findViewById(R.id.commet_user_email);
            comment_body = itemView.findViewById(R.id.comment_body);

        }

        public int getId() {
            return comment_id;
        }

        public void setId(int id) {
            this.comment_id = id;
        }

        public  void bindData(final Compact_Project_Object viewModel){}


    }


}

