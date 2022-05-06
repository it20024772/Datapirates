package com.example.datapirates;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datapirates.model.Progress;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;

public class progressShow extends AppCompatActivity {
    private EditText bookName, totalPages, currentPage;
    private FloatingActionButton savebtn;
    private ProgressBar progressBar;
    private TextView progressPercentTxt;
    private ImageView backArrow, addProgress, deleteProgress, editBookName, editTotalPages, editCurrentPage;
    private ProgressDialog dialog;

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String userId;
    Progress progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = new ProgressDialog(progressShow.this);
        dialog.setMessage("loading...");
        dialog.show();

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        userId = user.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Progress").child(userId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    // got to add progress
                    Intent progressAdd = new Intent(progressShow.this,progressAdd.class);
                    startActivity(progressAdd);
                    finish();
                }
                else{
                    progress = snapshot.getValue(Progress.class);
                    setContentView(R.layout.progress_show);

                    dialog.hide();

                    savebtn = findViewById(R.id.saveProgressUpdateBtn);
                    backArrow = findViewById(R.id.backArrow);
                    addProgress = findViewById(R.id.addProgressBtn);
                    deleteProgress = findViewById(R.id.deleteProgressBtn);
                    editBookName = findViewById(R.id.progressBookNameEditImg);
                    editTotalPages = findViewById(R.id.totalPagesEditImg);
                    editCurrentPage = findViewById(R.id.currentPageditImg);

                    bookName = findViewById(R.id.edtProgressBookNameUpdate);
                    totalPages = findViewById(R.id.edtTotalPagesUpdate);
                    currentPage = findViewById(R.id.edtCurrentPageUpdate);
                    progressBar = findViewById(R.id.progressBar);
                    progressPercentTxt = findViewById(R.id.progressPercentTxt);


                    bookName.setText(progress.getBookName());
                    bookName.setEnabled(false);

                    int savedTotalPages = progress.getTotalPages();
                    int savedCurrentPage = progress.getCurrentPage();
                    totalPages.setText(String.valueOf(savedTotalPages));
                    totalPages.setEnabled(false);
                    currentPage.setText(String.valueOf(savedCurrentPage));
                    currentPage.setEnabled(false);

                    int progressPercentage = calcPercentage(savedCurrentPage,savedTotalPages);

                    progressBar.setProgress(progressPercentage);
                    progressPercentTxt.setText(String.valueOf(progressPercentage));

