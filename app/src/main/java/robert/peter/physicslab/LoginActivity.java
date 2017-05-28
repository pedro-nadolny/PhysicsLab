package robert.peter.physicslab;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passwordEditText;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        emailEditText = (EditText) findViewById(R.id.email_input);
        passwordEditText = (EditText) findViewById(R.id.password_input);

        View.OnFocusChangeListener hideKeyboardTouchOutside = new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    hideKeyboard((EditText) v);
                }
            }
        };

        emailEditText.setOnFocusChangeListener(hideKeyboardTouchOutside);
        passwordEditText.setOnFocusChangeListener(hideKeyboardTouchOutside);

        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        toast.setGravity(toast.getGravity(), toast.getXOffset(), toast.getYOffset() + 150);
    }

    public void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null) {
            presentHomeActivity(null);
        }
    }

    public void presentRegisterActivity (View view){
        Intent registerActivity = new Intent(this, RegisterActivity.class);
        startActivity(registerActivity);
    }

    public void presentHomeActivity (View view){
        Intent homeActivity = new Intent(this, HomeActivity.class);
        startActivity(homeActivity);
    }

    public void loginAction (View view) {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager.getActiveNetworkInfo() == null) {
            showToast("No internet connection.");
            return;
        }

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.length() == 0 || password.length() < 0) {
            showToast("Insert your email and password.");
            return;
        }

        findViewById(R.id.spinner).setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        findViewById(R.id.spinner).setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            if(mAuth.getCurrentUser() != null) {
                                presentHomeActivity(null);
                                return;
                            }
                        }

                        showToast("Login failed.");
                    }
                });
    }

    private void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void showToast(String s) {
        toast.setText(s);
        toast.show();
    }
}
