package com.example.medcare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    GoogleSignInOptions gso;

    private LinearLayout linearLayout;
    private TextInputLayout nameTextEd,emailTextEd,passwordTextEd;
    private TextInputEditText nameSignUp,emailSignUp,passwordSignUp;
    private AppCompatButton signUpBtn;
    SignInButton googleSignInBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout=findViewById(R.id.linearLayout);
        nameSignUp=findViewById(R.id.nameSignUp);
        emailSignUp=findViewById(R.id.emailSignUp);
        passwordSignUp=findViewById(R.id.passwordSignUp);
        signUpBtn=findViewById(R.id.singUpBtn);
        nameTextEd=findViewById(R.id.nameTextEd);
        emailTextEd=findViewById(R.id.emailTextEd);
        passwordTextEd=findViewById(R.id.passwordTextED);
        googleSignInBtn=findViewById(R.id.googleSignInButton);

        googleSignInBtn.setSize(SignInButton.SIZE_WIDE);
        googleSignInBtn.setColorScheme(SignInButton.COLOR_DARK);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    // ToDo : volley in server add details in database and Shared Preference and email verification;
                    updateUI();
                }
            }
        });
        gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestProfile().build();
        final GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(MainActivity.this,gso);

        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.googleSignInButton:
                        signIn(googleSignInClient);
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123){
            Task<GoogleSignInAccount> task= GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try{
            GoogleSignInAccount account= completedTask.getResult(ApiException.class);
            updateUI(account);
        }catch (ApiException e){
            System.out.println("No Google Sign In");
            Snackbar snackbar= Snackbar.make(linearLayout, "Error In Google Sign In", Snackbar.LENGTH_LONG);
            snackbar.show();
            Log.w("[-] handleSignInResult :-->:", "signInResult:failed code=" + e.getStatusCode()+" [-] Message :-->:"+e.getMessage(),e.getCause());
        }
    }

    private void updateUI(GoogleSignInAccount account) {

        if(account!=null) {
            Intent i = new Intent(MainActivity.this, HomeActivity.class);
            i.putExtra("Code", 2);
            i.putExtra("Account Info", account);
            //System.out.println("@@@@@@@@@@@@@@@@@@@@@" + account.getEmail());
            startActivity(i);
            finish();
        }
    }


    private void updateUI(){
        String name=nameSignUp.getText().toString().trim();
        String email=emailSignUp.getText().toString().trim();
        Intent i = new Intent(MainActivity.this,HomeActivity.class);
        //System.out.println("Name "+name+" "+email);
        i.putExtra("Code",1);
        i.putExtra("Name",name);
        i.putExtra("Email",email);
        startActivity(i);
        finish();
    }
    private void signIn(GoogleSignInClient googleSignInClient) {
        Intent singInIntent= googleSignInClient.getSignInIntent();
        startActivityForResult(singInIntent,123);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private boolean validate() {
        String name,email,password;
        name=nameSignUp.getText().toString().trim();
        email=emailSignUp.getText().toString().trim();
        password=passwordSignUp.getText().toString().trim();

        return nameValidate(name) && emailValidate(email) && passwordValidate(password);
    }

    private boolean emailValidate(String email) {
        if(TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailTextEd.setError("Enter a valid email id");
            return false;
        }
        return true;
    }

    private boolean nameValidate(String name) {
        if(TextUtils.isEmpty(name)&& name.isEmpty()){
            nameTextEd.setError("Name Cannot be empty");
            return false;
        }
        return true;
    }

    private boolean passwordValidate(String password) {
        if(TextUtils.isEmpty(password) && password.length()>=6 && password.length()<12){
            passwordTextEd.setError("Password must be 6 character long");
            return false;
        }
        return true;
    }
}
