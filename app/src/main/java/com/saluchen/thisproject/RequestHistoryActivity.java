package com.saluchen.thisproject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saluchen.thisproject.Database.CurrentRequest;

import java.util.ArrayList;
import java.util.List;

public class RequestHistoryActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RequestHistoryAdapter adapter;
    private List<RequestHistoryItem> requestHistoryList;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.request_card_progress_bar);

        recyclerView = (RecyclerView) findViewById(R.id.request_history_recycler_view);
        requestHistoryList = new ArrayList<>();
        adapter = new RequestHistoryAdapter(this, requestHistoryList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        if (isInternetConnected()) {
            prepareList();
        }

    }

    public void prepareList() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        progressBar.setVisibility(View.VISIBLE);

        database.child(Config.TABLE_REQUEST).child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot request: dataSnapshot.getChildren()) {
                            CurrentRequest currentRequest = request.getValue(CurrentRequest.class);
                            String item = currentRequest.request_title;
                            String detail = currentRequest.details;
                            String date = currentRequest.datetime;
                            String location = currentRequest.latitude + ", " + currentRequest.longitude;
                            String status;
                            if (currentRequest.accept_id.equals("0")) {
                                status = "Pending";
                            } else {
                                status = "Request Accepted";
                            }

                            RequestHistoryItem requestItem = new RequestHistoryItem(item, detail,
                                    date, location, status);
                            requestHistoryList.add(requestItem);
                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public boolean isInternetConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
