package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SubInfoActivity extends AppCompatActivity {

    DatabaseReference mSubRef;
    ArrayList<String> subDetails = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    private TextView ServiceName;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_info);

        ServiceName = (TextView) findViewById(R.id.subTitile);
        ServiceName.setText(getIntent().getStringExtra("service_id"));

        mSubRef = FirebaseDatabase.getInstance().getReference("Subs");
        list = findViewById(R.id.detailList);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subDetails);
        list.setAdapter(arrayAdapter);

        mSubRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String name = String.valueOf(getIntent().getStringExtra("service_id"));
                    String description = dataSnapshot.child(String.valueOf(getIntent().getStringExtra("service_id"))).child("description").getValue(String.class);
                    String price = dataSnapshot.child(String.valueOf(getIntent().getStringExtra("service_id"))).child("price").getValue(String.class);
                    String date = dataSnapshot.child(String.valueOf(getIntent().getStringExtra("service_id"))).child("date").getValue(String.class);
                    String frequency = dataSnapshot.child(String.valueOf(getIntent().getStringExtra("service_id"))).child("frequency").getValue(String.class);

                    // Set text of textview to subscription name
                    ServiceName.setText(name);

                    //Add sub details to the arraylist
                    subDetails.add(name);
                    subDetails.add(description);
                    subDetails.add("£" + price);
                    subDetails.add(date);
                    subDetails.add(frequency);
                    arrayAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sub_info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                AlertDialog.Builder altdial = new AlertDialog.Builder(SubInfoActivity.this);
                altdial.setMessage("Are you sure you want to delete this Subscription?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(SubInfoActivity.this, HomeScreenActivity.class);
                                intent.putExtra("deleted_sub", ServiceName.getText());
                                subDetails.clear();
                                arrayAdapter.notifyDataSetChanged();
                                String name = ServiceName.getText().toString();
                                deleteSub(name);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteSub(String subName) {
        DatabaseReference delete = FirebaseDatabase.getInstance().getReference("Subs").child(subName);
        delete.removeValue();

        Toast.makeText(this, "Subscription deleted", Toast.LENGTH_LONG);
    }

}
