package robert.peter.physicslab;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        passwordConfirmEditText = (EditText) findViewById(R.id.confirm_password_input);
        passwordEditText = (EditText) findViewById(R.id.password_input);
        emailEditText = (EditText) findViewById(R.id.email_input);
    }

    public void createUser(View view) {

        String password = passwordEditText.getText().toString();
        String passwordConfirm = passwordConfirmEditText.getText().toString();

        if(!password.equals(passwordConfirm)) {
            Toast.makeText(getApplicationContext(), "Password dosen't match.",
                    Toast.LENGTH_SHORT).show();
            return;
        }


        String email = emailEditText.getText().toString();

        if(email.length() == 0 || password.length() == 0) {
            Toast.makeText(getApplicationContext(), "Registration failed.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if(email.startsWith("@") || email.endsWith("@") || !email.contains("@")) {
            Toast.makeText(getApplicationContext(), "Registration failed.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
