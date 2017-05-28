package robert.peter.physicslab;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText passwordEditText;
    private EditText passwordConfirmEditText;
    private EditText emailEditText;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_register);

        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        mAuth = FirebaseAuth.getInstance();

        passwordConfirmEditText = (EditText) findViewById(R.id.confirm_password_input);
        passwordEditText = (EditText) findViewById(R.id.password_input);
        emailEditText = (EditText) findViewById(R.id.email_input);

        View.OnFocusChangeListener hideKeyboardTouchOutside = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    hideKeyboard((EditText) v);
                }
            }
        };

        passwordConfirmEditText.setOnFocusChangeListener(hideKeyboardTouchOutside);
        passwordEditText.setOnFocusChangeListener(hideKeyboardTouchOutside);
        emailEditText.setOnFocusChangeListener(hideKeyboardTouchOutside);
    }

    public void createUser(View view) {
        String password = passwordEditText.getText().toString();
        String passwordConfirm = passwordConfirmEditText.getText().toString();

        if(!password.equals(passwordConfirm)) {
            showToast("Password confirm do not match");
            return;
        }

        String email = emailEditText.getText().toString();

        if(email.length() == 0 || password.length() == 0) {
            showToast("Invalid email or password");
            return;
        }

        if(email.startsWith("@") || email.endsWith("@") || !email.contains("@")) {
            showToast("Invalid email or password");
            return;
        }

        findViewById(R.id.spinner).setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        findViewById(R.id.spinner).setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                        } else {

                            showToast("Registration failed");
                        }
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
