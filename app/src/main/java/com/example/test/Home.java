package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {
    RecyclerView rcvCity;
    ArrayList<City> listCity;
    MyAdapter myAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText edName, edCountry, edPopulatin;
    Button btnAdd, btnSignOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findView();
        initListener();
        listCity = new ArrayList<City>();
        myAdapter = new MyAdapter(Home.this, listCity);
        rcvCity.setAdapter(myAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvCity.setLayoutManager(layoutManager);
        ReadDataFireStore();
    }
    private void findView(){
        edName = findViewById(R.id.edName);
        edCountry = findViewById(R.id.edCountry);
        edPopulatin = findViewById(R.id.edPopulation);
        btnAdd = findViewById(R.id.btnAdd);
        btnSignOut = findViewById(R.id.btnSignOut);
        rcvCity = findViewById(R.id.rcvCity);
    }

    private void initListener(){
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Home.this, SignIn.class);
                startActivity(intent);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edName.getText().toString().trim();
                String country = edCountry.getText().toString().trim();
                String population = edPopulatin.getText().toString().trim();
                if (name.isEmpty() || country.isEmpty() || population.isEmpty()){
                    Toast.makeText(Home.this, "Không được để trống thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    WriteDataFireStore(name, country, population);
                }

            }
        });
    }
    private void WriteDataFireStore(String name, String country, String population){
        Map<String, Object> city = new HashMap<>();
        city.put("name", name);
        city.put("country", country);
        city.put("population", population);
        db.collection("City")
                .add(city)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(Home.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        edName.setText("");
                        edCountry.setText("");
                        edPopulatin.setText("");
                        updateData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Home.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void ReadDataFireStore(){
        db.collection("City")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("name");
                                String country = document.getString("country");
                                int population = Integer.parseInt(document.getString("population"));
                                City city = new City(name, country, population);
                                if (!listCity.contains(city)) {
                                    listCity.add(city);
                                }
                                myAdapter.notifyDataSetChanged();
                            }
                        } else {

                        }
                    }
                });
    }
    private void updateData() {
        listCity.clear();
        ReadDataFireStore();
    }

}