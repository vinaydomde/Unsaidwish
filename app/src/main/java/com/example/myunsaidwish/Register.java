package com.example.myunsaidwish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myunsaidwish.ui.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity  implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private TextView Tv3,Tv4;
    private EditText editTextfullname,editTextage,editTextemail,editTextpasswordd,editTextaddress,editTextdetails;
    private ProgressBar progressba;
    private Button regidterbt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        Tv3=findViewById(R.id.Tv3);
        Tv4=findViewById(R.id.Tv4);
        editTextfullname=findViewById(R.id.fullname);
        editTextage=findViewById(R.id.age);
        editTextemail=findViewById(R.id.email);
        editTextpasswordd=findViewById(R.id.passwordd);
        editTextaddress=findViewById(R.id.address);
        editTextdetails=findViewById(R.id.details);
        regidterbt=findViewById(R.id.regidterbt);
        regidterbt.setOnClickListener(this);
        progressba=findViewById(R.id.progressba);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.regidterbt:
                regidterbt();
                break;
        }

    }

    private void regidterbt() {
        String email= editTextemail.getText().toString().trim();
        String passwordd= editTextpasswordd.getText().toString().trim();
        String fullname= editTextfullname.getText().toString().trim();
        String age= editTextage.getText().toString().trim();
        String address= editTextaddress.getText().toString().trim();
        String details= editTextdetails.getText().toString().trim();

        if (fullname.isEmpty()){
            editTextfullname.setError("full name is required!");
            editTextfullname.requestFocus();
            return;
        }
        if (age.isEmpty()){
            editTextage.setError("age is required!");
            editTextage.requestFocus();
            return;
        }
        if (email.isEmpty()){
            editTextemail.setError("Email is required!");
            editTextemail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextemail.setError("Please provide valid email address");
            editTextemail.requestFocus();
            return;
        }
        if (passwordd.isEmpty()){
            editTextpasswordd.setError("password is required!");
            editTextpasswordd.requestFocus();
            return;
        }
        if (passwordd.length()<6){
            editTextpasswordd.setError("Min password length is 6 character");
            editTextpasswordd.requestFocus();
            return;
        }
        if (address.isEmpty()){
            editTextaddress.setError("Address is required!");
            editTextaddress.requestFocus();
            return;
        }
        if (details.isEmpty()){
            editTextdetails.setError("details is required!");
            editTextdetails.requestFocus();
            return;
        }

        progressba.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,passwordd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User user= new User(fullname,age,email,details,address);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(Register.this, "user has been register successfully!", Toast.LENGTH_SHORT).show();
                                        progressba.setVisibility(View.GONE);
                                        //
                                    }
                                    else{
                                        Toast.makeText(Register.this, "Failed to register. Try again!", Toast.LENGTH_SHORT).show();
                                        progressba.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(Register.this, "Failed to register.", Toast.LENGTH_SHORT).show();
                            progressba.setVisibility(View.GONE);
                        }
                    }
                });
    }

    }
