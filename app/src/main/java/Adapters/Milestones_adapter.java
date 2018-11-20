package Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import RetrofitModels.Milestone_Object;
import honeybadgersapp.honeybadgers.R;

public class Milestones_adapter extends RecyclerView.Adapter<Milestones_adapter.MyViewHolder>{



    private Context mContext;
    private List<Milestone_Object> milestones ;



    public Milestones_adapter(Context mContext, List<Milestone_Object> temp ) {
        this.mContext = mContext;
        this.milestones = temp;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType){

        final View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);
        vHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(mContext)
                        .setTitle("Delete?")
                        .setMessage("Are you sure you want to delete the milestone?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                milestones.remove(vHolder.getAdapterPosition());
                                notifyDataSetChanged();
                            }
                        }).create().show();
                return true;
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
        return milestones.size();
    }
    @Override
    public int getItemViewType(final int position) {
        return R.layout.milestone_object;
    }

    public void removeItem(int position) {
        milestones.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Milestone_Object item, int position) {
        milestones.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }



    public synchronized void onBindViewHolder(@NonNull MyViewHolder holder, final int position){

        ( holder).bindData(milestones.get(position));
    }






    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView amount;
        private TextView description;
        private TextView status;
        private Spinner action;
        protected View container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView;
            amount = itemView.findViewById(R.id.milestone_object_amount_edit);
            description = itemView.findViewById(R.id.milestone_object_description_edit);
            status = itemView.findViewById(R.id.milestone_object_status_edit);
            action=  itemView.findViewById(R.id.milestone_object_activities_edit);

        }

        public  void bindData(final Milestone_Object viewModel){
            amount.setText(""+viewModel.getAmount());
            description.setText(viewModel.getDescription());

            if(viewModel.getAmount()!=0){
                amount.setVisibility(View.VISIBLE);
            }if(viewModel.getDescription()!=null){
                description.setVisibility(View.VISIBLE);
            }

        }



    }


}
