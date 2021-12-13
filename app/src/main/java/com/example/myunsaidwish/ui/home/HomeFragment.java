package com.example.myunsaidwish.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myunsaidwish.R;
import com.example.myunsaidwish.Register;
import com.example.myunsaidwish.ui.gallery.GalleryFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.widget.Toast.LENGTH_SHORT;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private TextView registerTV,forgetTV;
    private EditText editTextemail,editTextpass;
    private Button Loginbt;
    private FirebaseAuth mAuth;
    private ProgressBar progressbb;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        registerTV = root.findViewById(R.id.registerTV);
        registerTV.setOnClickListener(this);
        forgetTV= root.findViewById(R.id.forgetTV);
        Loginbt= root.findViewById(R.id.Loginbt);
        Loginbt.setOnClickListener(this);
        editTextemail= root.findViewById(R.id.email);
        editTextpass= root.findViewById(R.id.pass);
        progressbb= root.findViewById(R.id.progressbb);
        mAuth= FirebaseAuth.getInstance();

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerTV:
                Intent intent=new Intent(getActivity(), Register.class);
                startActivity(intent);
                break;
            case R.id.Loginbt:
                userLogin();
                break;

        }

    }

    private void userLogin() {
        String email= editTextemail.getText().toString().trim();
        String pass=editTextpass.getText().toString().trim();

        if (email.isEmpty()){
            editTextemail.setError("Email is required");
            editTextemail.requestFocus();
            return;
        }
//        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
//            editTextemail.setError("please enter valid email");
//            editTextemail.requestFocus();
//            return;
//        }
        if (pass.isEmpty()){
            editTextpass.setError("password is required");
            editTextpass.requestFocus();
            return;
        }
        if(pass.length()<6){
            editTextpass.setError("minimum password length is 6 character");
            editTextpass.requestFocus();
            return;
        }
        progressbb.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()){
                        //redirect to user profile
//                        Intent i=new Intent(getActivity(), GalleryFragment.class);
//                        startActivity(i);
                    }
                    else{
                        user.sendEmailVerification();
                        Toast.makeText(getActivity(),"check your email to verify your account", LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getActivity(),"Login Failed",LENGTH_SHORT).show();
                }
            }
        });
    }
}