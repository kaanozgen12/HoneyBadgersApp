package Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import honeybadgersapp.honeybadgers.R;

public class MyProjects_project_description_Fragment extends Fragment {


    View view;
    public Button edit_description;
    public EditText description;

    public MyProjects_project_description_Fragment() {

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.myprojects_project_description,container,false);
        edit_description= view.findViewById(R.id.my_projects_project_description_edit_button);
        description= view.findViewById(R.id.my_projects_project_description_edit);
        description.setInputType(InputType.TYPE_NULL);

        edit_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edit_description.getText().toString().equalsIgnoreCase("EDIT")) {
                    description.setInputType(InputType.TYPE_CLASS_TEXT);
                    edit_description.setText("DONE");
                }else{
                    description.setInputType(InputType.TYPE_NULL);
                    edit_description.setText("EDIT");
                }
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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


