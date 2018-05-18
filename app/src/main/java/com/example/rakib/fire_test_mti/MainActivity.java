package com.example.rakib.fire_test_mti;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.internal.FallbackServiceBroker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1 ; //activity Result tag

    FirebaseAuth mFireBaseAuth;

    FirebaseAuth.AuthStateListener mAuthStateListener; //for sign in auth state like showing sign in option or closing options




    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            mContext=this;
        mFireBaseAuth=FirebaseAuth.getInstance();

        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){
                    //user is signed in

                    Toast.makeText(MainActivity.this, "User is Signed in", Toast.LENGTH_SHORT).show();
                }else{
                    //User is not signed in

                    //Todo: We have to set the mechanism for sign in the user

                    // Choose authentication providers
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.FacebookBuilder().build(), //this is the only line needed for facebook sign in
                            new AuthUI.IdpConfig.GoogleBuilder().build()

                         );

// Create and launch sign-in intent
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false) //setting this true adds a lot complexity
                                    .setAvailableProviders(providers)
                                    .setLogo(android.R.drawable.btn_star_big_on) //for setting our logo, Optional

                                    .setTheme(R.style.FireBaseGreenTheme) //optional
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };


        //Fign out Button
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AuthUI.getInstance()
                        .signOut(mContext)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                                Toast.makeText(mContext, "you are successfully Log out", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

       }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                finish(); //closing App
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mFireBaseAuth.addAuthStateListener(mAuthStateListener); //showing sign in options
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mAuthStateListener!=null)
        mFireBaseAuth.removeAuthStateListener(mAuthStateListener); //closing sign in options
    }
}
