package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class HomeScreenActivity extends AppCompatActivity {

    double total;
    TextView totalPrice;
    ListView listView;
    ArrayList<String> subscriptions = new ArrayList<>() ;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference().child("Subs");
    DatabaseReference mMaintainRef = mRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        totalPrice = (TextView) findViewById(R.id.totalMonthly);
        total = 0;
        listView = (ListView) findViewById(R.id.subsList);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.listview_item, subscriptions);

        listView.setAdapter(arrayAdapter);

        /*if (getIntent().getStringExtra("deleted_sub") != null){
            mMaintainRef.child(getIntent().getStringExtra("deleted_sub")).removeValue();
        }*/

        mMaintainRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                subscriptions.clear();
                arrayAdapter.notifyDataSetChanged();

                DecimalFormat formattedTotal = new DecimalFormat("#.##");

                //add subscriptions to listview
                if (!subscriptions.contains(null) || !subscriptions.contains("")){
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String name = childSnapshot.child("name").getValue(String.class);

                        //format price so that it has decimal spaces even when it is an integer form
                        Double amountTemp = Double.valueOf(formattedTotal.format(Double.valueOf(childSnapshot.child("price").getValue(String.class))));
                        total = total + amountTemp; // calculate the monthly total of the subscriptions
                        subscriptions.add(name);
                    }
                    arrayAdapter.notifyDataSetChanged();

                    //format total to have 2 decimals and display it
                    total = Double.valueOf(formattedTotal.format(total));
                    totalPrice.setText("Total: £"+ total);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(HomeScreenActivity.this, SubInfoActivity.class);
                if (subscriptions.get(position) != null){
                    intent.putExtra("service_id", subscriptions.get(position));
                    startActivity(intent);
                }



            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(HomeScreenActivity.this, AddSubActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.test_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(this, "Item 1 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item2:
                Toast.makeText(this, "Item 2 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.subitem1:
                Toast.makeText(this, "Sub item 1 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.subitem2:
                Toast.makeText(this, "Sub item 2 selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}