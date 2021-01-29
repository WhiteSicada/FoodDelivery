package com.example.easyeat.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyeat.MainActivity;
import com.example.easyeat.R;
import com.example.easyeat.model.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;


public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    EditText usernameLogin, mPassword;
    Button mLoginBtn;
    TextView linkToRegister;
    TextView forgot;
    ProgressBar progressBar;
    private GoogleApiClient googleApiClient;

    private static final int RC_SIGN_IN = 1;


    Button btnGoogle,btnFacebook;
    private DataBaseHelper myDb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        forgot = findViewById(R.id.forgotPassword);
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        btnGoogle=(Button)findViewById(R.id.btnGoogle);
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });


        usernameLogin = (EditText) findViewById(R.id.usernameLogin);
        mPassword = (EditText) findViewById(R.id.passwordLogin);
        mLoginBtn = (Button) findViewById(R.id.btnLogin);
        linkToRegister = (TextView) findViewById(R.id.linkToRegister);
        progressBar = findViewById(R.id.progressBar);

        linkToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });
        myDb = new DataBaseHelper(this);

        loginUser();
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PasswordActivity.class);
                startActivity(intent);
            }
        });



    }
    /*public void openDialog() {
        PasswordActivity pass = new PasswordActivity();
        pass.show(getSupportFragmentManager(), "example dialog");
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
           // gotoProfile();
            Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();

        }else{
            Intent intent=new Intent(Login.this,MainActivity.class);
            startActivity(intent);
           // Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
        }
    }
    private void gotoProfile(){
        Intent intent=new Intent(Login.this,MainActivity.class);
        startActivity(intent);
    }

    private void loginUser(){
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = myDb.checkUser(usernameLogin.getText().toString() , mPassword.getText().toString());
                if (user == null){
                    Toast.makeText(Login.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Login.this, R.string.login_successfully, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this , MainActivity.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

   /* @Override
    public void applyTexts(String username) {

    }*/
}

