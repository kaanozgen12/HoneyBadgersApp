package Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import RetrofitModels.Milestone_Object;
import api.RetrofitClient;
import honeybadgersapp.honeybadgers.Activities.LoginActivity;
import honeybadgersapp.honeybadgers.R;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                if (vHolder.action.getVisibility()==View.INVISIBLE) {
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
            status.setText(viewModel.getStatus());

            if(viewModel.getAmount()!=0){
                amount.setVisibility(View.VISIBLE);
            }if(viewModel.getDescription()!=null){
                description.setVisibility(View.VISIBLE);
            }if(viewModel.getStatus().equalsIgnoreCase("verified") || viewModel.getStatus().equalsIgnoreCase("complete")){
                action.setVisibility(View.VISIBLE);
                if(viewModel.getStatus().equalsIgnoreCase("complete")){
                    status.setVisibility(View.VISIBLE);
                    status.setText("COMPLETE");
                }
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,
                        R.array.milestone_spinner_verified_side, android.R.layout.simple_spinner_item);
                 // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                action.setAdapter(adapter);
                action.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i == 5){
                            new AlertDialog.Builder(mContext)
                                    .setTitle("Okay?")
                                    .setMessage("Are you sure that this milestone is completed?")
                                    .setNegativeButton(android.R.string.no, null)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            new AlertDialog.Builder(mContext)
                                                    .setTitle("Last Decision?")
                                                    .setMessage("This is the last warning.If you approve this dialog, agreed money will be transfered to the freelancer!")
                                                    .setNegativeButton(android.R.string.no, null)
                                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface arg0, int arg1) {

                                                            Map<String, Object> jsonParams = new ArrayMap<>();
                                                            jsonParams.put("is_done", true);
                                                            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"),(new JSONObject(jsonParams)).toString());

                                                            Call<ResponseBody> call = RetrofitClient.getInstance().getApi().verified_milestone_complete("token "+ LoginActivity.getCREDENTIALS()[0],""+viewModel.id,body);
                                                            call.enqueue(new Callback<ResponseBody>() {
                                                                @Override
                                                                public  void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                                                    if (response.isSuccessful()) {
                                                                        Log.d("MyTag","!!!!!!!!!!!!successful :" +viewModel.id);
                                                                        notifyDataSetChanged();
                                                                    }
                                                                }
                                                                @Override
                                                                public  void onFailure(Call<ResponseBody> call, Throwable t) {
                                                                    Log.d("MyTag","!!!!!!!!!!!!!!!!!failllll in is_done");
                                                                }
                                                            });
                                                        }
                                                    }).create().show();
                                        }
                                    }).create().show();
                        }else if(i==0){

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

        }



    }


}
