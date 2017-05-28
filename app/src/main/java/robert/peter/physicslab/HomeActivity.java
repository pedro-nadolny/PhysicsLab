package robert.peter.physicslab;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private final String[] titlesStr = new String[] {"Mecânica",
                                                    "Gravitação",
                                                    "Termodinâmica",
                                                    "Eletromagnetismo",
                                                    "Óptica",
                                                    "Fisica Moderna"};

    private final String[] subtitlesStr = new String[]{"Aula 1", "Aula 2","Aula 3"};

    private FirebaseAuth mAuth;

    private static ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAuth = FirebaseAuth.getInstance();
        configureExpandableListView();
    }

    private void configureExpandableListView() {
        expandableListView = (ExpandableListView) findViewById(R.id.list);

        List<String> titles = new ArrayList<>();
        for (String s: titlesStr) {
            titles.add(s);
        }

        List<String> subtitles = new ArrayList<>();
        for(String s: subtitlesStr) {
            subtitles.add(s);
        }

        HashMap<String, List<String>> subgroupsData = new HashMap<>();
        for(String s: titles) {
            subgroupsData.put(s,subtitles);
        }

        HomeExpandableListAdapter adapter = new HomeExpandableListAdapter(getApplicationContext(), titles, subgroupsData);
        expandableListView.setAdapter(adapter);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < titlesStr.length; i++) {
                    if (i != groupPosition) {
                        expandableListView.collapseGroup(i);
                    }
                }
            }
        });

        expandableListView.setGroupIndicator(null);
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



