package com.visit.edishapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.Document;
import ui.DocumentRecyclerAdapter;
import util.DocumentApi;
import com.visit.edishapp.R;

import android.os.Bundle;

public class DocumentListActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private List<Document> journalList;
    private RecyclerView recyclerView;
    private DocumentRecyclerAdapter journalRecyclerAdapter;

    private CollectionReference collectionReference = db.collection("Document");
    private TextView noJournalEntry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_list);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        noJournalEntry = findViewById(R.id.list_no_thoughts);

        journalList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.action_add:
//                //Take users to add Journal
//                if (user != null && firebaseAuth != null) {
//                    startActivity(new Intent(DocumentListActivity.this,
//                            DocumentView.class));
//                    //finish();
//                }
//                break;
//            case R.id.action_signout:
//                //sign user out!
//                if (user != null && firebaseAuth != null) {
//                    firebaseAuth.signOut();
//
//                    startActivity(new Intent(DocumentListActivity.this,
//                            MainActivity.class));
//                    //finish();
//                }
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    protected void onStart() {
        super.onStart();

        collectionReference.whereEqualTo("userId", DocumentApi.getInstance()
                .getUserId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot journals : queryDocumentSnapshots) {
                                Document journal = journals.toObject(Document.class);
                                journalList.add(journal);
                            }

                            //Invoke recyclerview
                            journalRecyclerAdapter = new DocumentRecyclerAdapter(DocumentListActivity.this,
                                    journalList);
                            recyclerView.setAdapter(journalRecyclerAdapter);
                            journalRecyclerAdapter.notifyDataSetChanged();

                        }else {
                            noJournalEntry.setVisibility(View.VISIBLE);

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}