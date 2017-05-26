package robert.peter.physicslab;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    String[] titles = new String[] {"Mecânica",
                                    "Gravitação",
                                    "Termodinâmica",
                                    "Eletromagnetismo",
                                    "Óptica",
                                    "Fisica Moderna"};
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ListView listaDeCursos = (ListView) findViewById(R.id.list);

        final ArrayList<String> list = new ArrayList<String>();

        for (int i = 0; i < titles.length; ++i) {
            String str = titles[i];
            list.add(titles[i]);
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);

        listaDeCursos.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
    }

    public void presentLoginActivity (){
        Intent loginActivity = new Intent(this, LoginActivity.class);
        startActivity(loginActivity);
    }

    public void onBackPressed() {
        AlertDialog alert = new AlertDialog.Builder(HomeActivity.this).create();

        alert.setTitle("Logout");
        alert.setMessage("Would you like to logut?");

        alert.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        dialog.dismiss();
                        presentLoginActivity();
                    }
                }
        );

        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }
}
