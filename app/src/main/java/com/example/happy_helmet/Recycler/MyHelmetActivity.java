package com.example.happy_helmet.Recycler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happy_helmet.Adapters.MyHelmetAdapter;
import com.example.happy_helmet.AddHelmet;
import com.example.happy_helmet.GetterSetters.HelmetDetails;
import com.example.happy_helmet.MainActivity;
import com.example.happy_helmet.ProfileActivity;
import com.example.happy_helmet.R;
import com.example.happy_helmet.ShareBarcode;
import com.example.happy_helmet.UserComfirmActivity;

import java.util.ArrayList;
import java.util.List;

import static com.example.happy_helmet.LoginActivity.MY_PREFS_NAME;
import static com.example.happy_helmet.MainActivity.myHelmetDetails;

public class MyHelmetActivity extends AppCompatActivity {


    Button addHelmet;
    RecyclerView mRecycleView;
    List<HelmetDetails> myHelmetList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_helmet);
        getSupportActionBar().setTitle("Helmet Details");

        addHelmet = (Button)findViewById(R.id.addHelmet);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }

        addHelmet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyHelmetActivity.this, AddHelmet.class);
                startActivity(intent);

            }
        });

        mRecycleView = (RecyclerView) findViewById(R.id.recycler_reg_helmet);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MyHelmetActivity.this, 1);
        mRecycleView.setLayoutManager(gridLayoutManager);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        myHelmetList = new ArrayList<>();
        if (myHelmetDetails != null) {
            for (int i = 0; i < myHelmetDetails.size(); i++) {
                if (myHelmetDetails.get(i).getUser().equals(prefs.getString("loggedUserId", ""))) {
                    myHelmetList.add(myHelmetDetails.get(i));
                }
            }
            MyHelmetAdapter myAdapter = new MyHelmetAdapter(MyHelmetActivity.this, myHelmetList);
            mRecycleView.setAdapter(myAdapter);
        }


    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MyHelmetActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }



//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        Intent intent = null;
//        switch (item.getItemId()) {
//            case R.id.home:
//                intent = new Intent(this, MainActivity.class);
//                break;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//        startActivity(intent);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId()==R.id.home) {
//            BackupService.enqueueWork(this);
//            return(true);
//        }
//        else if (item.getItemId()==R.id.restore) {
//            startActivity(new Intent(this, MainActivity.class));
//            return(true);
//        }
//        return(super.onOptionsItemSelected(item));
//    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
