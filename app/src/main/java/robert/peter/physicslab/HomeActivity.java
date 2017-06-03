package robert.peter.physicslab;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private final String[] titlesStr = new String[] {"Mechanics",
                                                     "Gravitation",
                                                     "Thermodynamics",
                                                     "Electromagnetism",
                                                     "Optics",
                                                     "Modern Physics"};

    private final String[] subtitlesStr = new String[]{ "Class 1",
                                                        "Class 2",
                                                        "Class 3",
                                                        "Class 4"};

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

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                presentMechanicsActivity();
                return true;
            }
        });

        expandableListView.setGroupIndicator(null);
    }

    public void presentMechanicsActivity (){
        startActivity(new Intent(this, MechanicsActivity.class));
    }

    public void presentLoginActivity (){
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void onBackPressed() {
        AlertDialog alert = new AlertDialog.Builder(HomeActivity.this).create();

        alert.setTitle(getString(R.string.logout_alert_title));
        alert.setMessage(getString(R.string.logout_alert_message));

        alert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.logout_alert_button_positive), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        dialog.dismiss();
                        presentLoginActivity();
                    }
                }
        );

        alert.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.logout_alert_button_negative), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }
}



