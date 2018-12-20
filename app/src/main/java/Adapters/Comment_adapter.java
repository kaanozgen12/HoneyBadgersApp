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

import RetrofitModels.Comment_Object;
import honeybadgersapp.honeybadgers.R;

public class Comment_adapter extends RecyclerView.Adapter<Comment_adapter.MyViewHolder>{



    private Dialog myDialog;
    private Context mContext;
    private List<Comment_Object> list_of_comments;

    public Comment_adapter(Context mContext, List<Comment_Object> list_of_comments) {
        this.mContext = mContext;
        this.list_of_comments = list_of_comments ;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType){

        final View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
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
        return list_of_comments.size();
    }
    @Override
    public int getItemViewType(final int position) {
        return R.layout.comment_object;
    }


    public synchronized void onBindViewHolder(@NonNull MyViewHolder holder, final int position){

        holder.bindData(list_of_comments.get(position));
    }





    public class MyViewHolder extends RecyclerView.ViewHolder{
        private int comment_id;
        private TextView comment_user_email;
        private TextView comment_body;
        private TextView comment_time;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            comment_user_email = itemView.findViewById(R.id.comment_user_name);
            comment_body = itemView.findViewById(R.id.comment_text_edit);
            comment_time = itemView.findViewById(R.id.comment_time);

        }

        public int getId() {
            return comment_id;
        }

        public void setId(int id) {
            this.comment_id = id;
        }

        public  void bindData(final Comment_Object viewModel){
            comment_id=viewModel.getId();
            //comment_user_email.setText(viewModel.);
            comment_user_email.setText(viewModel.userId.username);
            comment_time.setText(viewModel.updatedAt);
            comment_body.setText(viewModel.description);

        }


    }


}

