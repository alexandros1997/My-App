package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddSubActivity extends AppCompatActivity {

    private Button AddSub;
    private EditText ServiceName;
    private EditText ServiceDescription;
    private EditText ServicePrice;
    private EditText PaymentDate;
    private Spinner PaymentFrequency;

    Subscriptions subs;

    long subId = 0;
    long amount = 0;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub);

        ServiceName = (EditText)findViewById(R.id.serviceName);
        ServiceDescription = (EditText)findViewById(R.id.serviceDescription);
        ServicePrice = (EditText)findViewById(R.id.servicePrice);
        PaymentDate = (EditText)findViewById(R.id.serviceDate);
        PaymentFrequency = (Spinner)findViewById(R.id.serviceFrequency);

        AddSub = (Button)findViewById(R.id.btnAdd);

        Spinner frequency = findViewById(R.id.serviceFrequency);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.payment_frequency, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequency.setAdapter(adapter);

        subs = new Subscriptions();

        mRootRef = FirebaseDatabase.getInstance().getReference().child("Subs");
        mRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    subId = (dataSnapshot.getChildrenCount())-1;
                    amount = (dataSnapshot.getChildrenCount())-1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        AddSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddSubActivity.this, HomeScreenActivity.class);

                String sName = ServiceName.getText().toString();
                String sDescription = ServiceDescription.getText().toString();
                String sPrice = ServicePrice.getText().toString();
                String sDate = PaymentDate.getText().toString();
                String sFrequency = PaymentFrequency.getSelectedItem().toString();

                subs.setName(sName);
                subs.setDescription(sDescription);
                subs.setPrice(sPrice);
                subs.setDate(sDate);
                subs.setFrequency(sFrequency);
                mRootRef.child(sName).setValue(subs);


                startActivity(intent);
            }
        });
    }
}
