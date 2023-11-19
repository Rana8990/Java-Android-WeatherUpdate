package com.example.weatherupdate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class History extends AppCompatActivity {

    private ListView listView;
    TextView textView;
    private ArrayList<String> historyList;
    private ArrayAdapter<String> adapter;
    private FirebaseFirestore dbroot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listView = findViewById(R.id.listView);
        historyList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyList);
        listView.setAdapter(adapter);

        dbroot = FirebaseFirestore.getInstance();

        textView = findViewById(R.id.back);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Fetch history data from Firestore
        fetchHistoryData();

    }

    private void fetchHistoryData() {
        dbroot.collection("History")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Assuming the data is stored as a Map
                            Map<String, Object> historyData = document.getData();
                            historyList.add(historyData.toString()); // Convert the Map to a string
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("HistoryActivity", "Error getting history data", task.getException());
                    }
                });
    }
}