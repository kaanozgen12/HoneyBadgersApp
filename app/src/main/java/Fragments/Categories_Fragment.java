package Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Adapters.ActivityAdapterVerifiedProject;
import Adapters.Dashboard_Notifications_adapter;
import Models.Compact_Project_Object;
import honeybadgersapp.honeybadgers.Activities.CategoryActivity;
import honeybadgersapp.honeybadgers.Activities.CreateProject;
import honeybadgersapp.honeybadgers.R;

public class Categories_Fragment extends Fragment {


    View view;
    public List<Compact_Project_Object> list_of_projects =new ArrayList<>();
    RecyclerView mRecylerView;
    int category=1;
    TextView primary_text;
    LinearLayout primary ;
    LinearLayout secondary;
    ImageView back_buttom;
    ActivityAdapterVerifiedProject recyclerAdapter;

    public Categories_Fragment() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.activity_category,container,false);
        mRecylerView = view.findViewById(R.id.project_categories_recyclerview);

        primary_text = view.findViewById(R.id.activity_category_primary_text);
        primary = view.findViewById(R.id.activity_category_primary_activity_layout);
        secondary = view.findViewById(R.id.activity_category_secondary_activity_layout);
        back_buttom = view.findViewById(R.id.project_categories_backbutton);
        primary_text.setVisibility(View.GONE);
        Dashboard_Notifications_adapter recyclerAdapter =new Dashboard_Notifications_adapter(getContext(),list_of_projects);
        mRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecylerView.setAdapter(recyclerAdapter);

        RelativeLayout relative1 = view.findViewById(R.id.button_with_image_above_text1);
        back_buttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category=1;primary.setVisibility(View.VISIBLE);secondary.setVisibility(View.GONE);
            }
        });
        relative1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                category=1;primary.setVisibility(View.GONE);secondary.setVisibility(View.VISIBLE);

            }
        });
        RelativeLayout relative2 = view.findViewById(R.id.button_with_image_above_text2);
        relative2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                category=2;    primary.setVisibility(View.GONE);secondary.setVisibility(View.VISIBLE);
            }
        });
        RelativeLayout relative3 =  view.findViewById(R.id.button_with_image_above_text3);
        relative3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                category=3;    primary.setVisibility(View.GONE);secondary.setVisibility(View.VISIBLE);
            }
        });
        RelativeLayout relative4 =  view.findViewById(R.id.button_with_image_above_text4);
        relative4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                category=4;    primary.setVisibility(View.GONE);secondary.setVisibility(View.VISIBLE);
            }
        });
        RelativeLayout relative5 =  view.findViewById(R.id.button_with_image_above_text5);
        relative5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                category=5;    primary.setVisibility(View.GONE);secondary.setVisibility(View.VISIBLE);

            }
        });
        RelativeLayout relative6 = view.findViewById(R.id.button_with_image_above_text6);
        relative6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                category=6;    primary.setVisibility(View.GONE);secondary.setVisibility(View.VISIBLE);

            }
        });
        RelativeLayout relative7 =  view.findViewById(R.id.button_with_image_above_text7);
        relative7.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                category=7;    primary.setVisibility(View.GONE);secondary.setVisibility(View.VISIBLE);

            }
        });
        RelativeLayout relative8 =  view.findViewById(R.id.button_with_image_above_text8);
        relative8.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                category=8;    primary.setVisibility(View.GONE);secondary.setVisibility(View.VISIBLE);

            }
        });
        RelativeLayout relative9 =  view.findViewById(R.id.button_with_image_above_text9);
        relative9.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                category=9;    primary.setVisibility(View.GONE);secondary.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
    @Override
    public void onResume() {
        super.onResume();

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }




}
