package honeybadgersapp.honeybadgers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import honeybadgersapp.honeybadgers.R;
public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        RelativeLayout relative1 = (RelativeLayout) findViewById(R.id.button_with_image_above_text1);
        relative1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(CategoryActivity.this, CreateProject.class);
                i.putExtra("categoryname","Websites, IT and Software");
                i.putExtra("category",1);
                i.putExtra("project_id", "-1");
                startActivity(i);
                finish();
            }
        });
        RelativeLayout relative2 = (RelativeLayout) findViewById(R.id.button_with_image_above_text2);
        relative2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(CategoryActivity.this, CreateProject.class);
                i.putExtra("categoryname","Mobile Phones and Computing");
                i.putExtra("category",2);
                i.putExtra("project_id", "-1");
                startActivity(i);
                finish();

            }
        });
        RelativeLayout relative3 = (RelativeLayout) findViewById(R.id.button_with_image_above_text3);
        relative3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(CategoryActivity.this, CreateProject.class);
                i.putExtra("categoryname","Translation and Languages");
                i.putExtra("category",3);
                i.putExtra("project_id", "-1");
                startActivity(i);
                finish();

            }
        });
        RelativeLayout relative4 = (RelativeLayout) findViewById(R.id.button_with_image_above_text4);
        relative4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(CategoryActivity.this, CreateProject.class);
                i.putExtra("categoryname","Engineering and Science");
                i.putExtra("category",4);
                i.putExtra("project_id", "-1");
                startActivity(i);
                finish();
            }
        });
        RelativeLayout relative5 = (RelativeLayout) findViewById(R.id.button_with_image_above_text5);
        relative5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(CategoryActivity.this, CreateProject.class);
                i.putExtra("categoryname","Writing and Content");
                i.putExtra("category",5);
                i.putExtra("project_id", "-1");
                startActivity(i);
                finish();
            }
        });
        RelativeLayout relative6 = (RelativeLayout) findViewById(R.id.button_with_image_above_text6);
        relative6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(CategoryActivity.this, CreateProject.class);
                i.putExtra("categoryname","Sales and Marketing");
                i.putExtra("category",6);
                i.putExtra("project_id", "-1");
                startActivity(i);
                finish();
            }
        });
        RelativeLayout relative7 = (RelativeLayout) findViewById(R.id.button_with_image_above_text7);
        relative7.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(CategoryActivity.this, CreateProject.class);
                i.putExtra("categoryname","Design, Media and Architecture");
                i.putExtra("category",7);
                i.putExtra("project_id", "-1");
                startActivity(i);
                finish();
            }
        });
        RelativeLayout relative8 = (RelativeLayout) findViewById(R.id.button_with_image_above_text8);
        relative8.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(CategoryActivity.this, CreateProject.class);
                i.putExtra("categoryname","Other");
                i.putExtra("category",8);
                i.putExtra("project_id", "-1");
                startActivity(i);
                finish();
            }
        });
    }
}
