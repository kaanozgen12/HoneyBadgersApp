package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import Models.ActivityCard;
import honeybadgersapp.honeybadgers.R;

public class ActivityAdapterVerifiedProject extends RecyclerView.Adapter<ActivityAdapterVerifiedProject.MyViewHolder>{


    private Context mContext;
    private List<ActivityCard> activity_cards;

    public ActivityAdapterVerifiedProject(Context mContext, List<ActivityCard> activity_cards) {
        this.mContext = mContext;
        this.activity_cards = activity_cards;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType){

        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_card, parent, false);
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
        return activity_cards.size();
    }
    @Override
    public int getItemViewType(final int position) {
        return R.layout.activity_card;
    }


    public synchronized void onBindViewHolder(@NonNull MyViewHolder holder, final int position){

        holder.bindData(activity_cards.get(position));



    }





    public class MyViewHolder extends RecyclerView.ViewHolder{
        private int activity_id;
        private TextView name;
        private TextView date;
        private FloatingActionButton image;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.activity_card_name);
            date = itemView.findViewById(R.id.activity_card_date);
            image = itemView.findViewById(R.id.activity_card_image);

        }

        public int getId() {
            return activity_id;
        }

        public void setId(int id) {
            this.activity_id = id;
        }

        public  void bindData(final ActivityCard viewModel){



        }


    }


}

