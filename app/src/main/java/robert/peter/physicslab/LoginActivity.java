package robert.peter.physicslab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
    }

    public void presentRegisterActivity (View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
