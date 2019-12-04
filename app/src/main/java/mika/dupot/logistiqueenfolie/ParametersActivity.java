package mika.dupot.logistiqueenfolie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Switch;

public class ParametersActivity extends AppCompatActivity {

    final String DISPLAY_PATH_DOT="DISPLAY_PATH_DOT";
    final String LEVEL_DIFFICULTY="LEVEL_DIFFICULTY";

    Switch displayPathDotSwitch;
    RadioButton radioFacile ;
    RadioButton radioNormal ;
    RadioButton radioDifficile ;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_parameters);

        displayPathDotSwitch = findViewById(R.id.switch1);
        radioFacile = findViewById(R.id.radioFacile);
        radioNormal = findViewById(R.id.radioNormal);
        radioDifficile = findViewById(R.id.radioDifficile);

        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        displayPathDotSwitch.setChecked(prefs.getBoolean(DISPLAY_PATH_DOT,true));

        switch (prefs.getInt(LEVEL_DIFFICULTY,2)){
            case 1:
                radioFacile.setChecked(true);
                break;
            case 2:
                radioNormal.setChecked(true);
                break;
            case 3:
                radioDifficile.setChecked(true);
                break;
        }


    }

    public void saveParameter(View view) {

        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor=prefs.edit();
        editor.putBoolean(DISPLAY_PATH_DOT, displayPathDotSwitch.isChecked()  );

        if(radioFacile.isChecked()){
            editor.putInt(LEVEL_DIFFICULTY,1);
        }else if(radioNormal.isChecked()){
            editor.putInt(LEVEL_DIFFICULTY,2);
        }else if(radioDifficile.isChecked()){
            editor.putInt(LEVEL_DIFFICULTY,3);
        }

        editor.commit();

        Intent oIntent = new Intent(this, MainActivity.class);
        startActivity(oIntent);

    }

    public void gotoMain(View view) {

        Intent oIntent = new Intent(this, MainActivity.class);
        startActivity(oIntent);

    }


}
