package lorma.ccse.ilearn.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import lorma.ccse.ilearn.Config;
import lorma.ccse.ilearn.R;
import lorma.ccse.ilearn.controller.PreferencesController;
import lorma.ccse.ilearn.model.Shape;

import java.util.ArrayList;

/**
 * Created by priyank on 23/12/15.
 * Activity to provide settings for the app
 */
public class SettingsShapeActivity extends AppCompatActivity implements View.OnClickListener{

    TextView textViewClearData;
    View layoutVibrationSetting;
    CheckBox checkBoxVibration;
    TextView textViewLicenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // finding views
        textViewClearData = (TextView) findViewById(R.id.textViewClearData);
        layoutVibrationSetting = findViewById(R.id.layoutVibrationSetting);
        checkBoxVibration = (CheckBox) findViewById(R.id.checkBoxVibration);
        textViewLicenses = (TextView) findViewById(R.id.textViewLicenses);
        textViewClearData.setOnClickListener(this);
        layoutVibrationSetting.setOnClickListener(this);
        textViewLicenses.setOnClickListener(this);
        // Get the vibration setting
        boolean status = PreferencesController.getVibrationPreference();
        checkBoxVibration.setChecked(status);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.textViewClearData:
                showConfirmationDialog();
                break;
            case R.id.layoutVibrationSetting:
                checkBoxVibration.toggle();
                boolean status = checkBoxVibration.isChecked();
                PreferencesController.setVibrationPreference(status);
                break;
            case R.id.textViewLicenses:
                showLicensesDialog();
                break;
        }
    }

    private void showConfirmationDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Are You Sure?")
                .setMessage("This will delete all data including scores and high scores.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Reset app data here
                        resetAppData();
                        Toast.makeText(getApplicationContext(), "App data reset. New beginning now!", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void resetAppData(){
        ArrayList<Integer> easyShapesIds;
        ArrayList<Integer> mediumShapesIds;
        ArrayList<Integer> hardShapesIds;
        // Load the bitmap paths
        easyShapesIds = new ArrayList<>();
        easyShapesIds.add(R.drawable.circle_1);
        easyShapesIds.add(R.drawable.triangle_1);
        easyShapesIds.add(R.drawable.oval_1);
        easyShapesIds.add(R.drawable.square_1);
        easyShapesIds.add(R.drawable.rectangle_1);
        easyShapesIds.add(R.drawable.diamond_1);
        // Write the value in the database.
        Shape.deleteAll(Shape.class);
        for (Integer resourceId : easyShapesIds){
            Shape shape = new Shape();
            shape.setMaxScore(0);
            shape.setLevel(Config.LEVEL_EASY);
            shape.setResourceId(resourceId);
            shape.save();
        }
        mediumShapesIds = new ArrayList<>();
        mediumShapesIds.add(R.drawable.pentagon_1);
        mediumShapesIds.add(R.drawable.hexagon_1);
        mediumShapesIds.add(R.drawable.star);
        mediumShapesIds.add(R.drawable.heart);
        mediumShapesIds.add(R.drawable.crescent);
        mediumShapesIds.add(R.drawable.lightning);
        for(Integer resourceId : mediumShapesIds){
            Shape shape = new Shape();
            shape.setMaxScore(0);
            shape.setLevel(Config.LEVEL_MEDIUM);
            shape.setResourceId(resourceId);
            shape.save();
        }
        hardShapesIds = new ArrayList<>();
        hardShapesIds.add(R.drawable.bell);
        hardShapesIds.add(R.drawable.bulb);
        hardShapesIds.add(R.drawable.cloud);
        hardShapesIds.add(R.drawable.flower);
        hardShapesIds.add(R.drawable.house);
        hardShapesIds.add(R.drawable.gear);
        for (Integer resourceId : hardShapesIds){
            Shape shape = new Shape();
            shape.setMaxScore(0);
            shape.setLevel(Config.LEVEL_HARD);
            shape.setResourceId(resourceId);
            shape.save();
        }
        PreferencesController.setDatabaseInitStatus(true);
    }

    private void showLicensesDialog(){
        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.dialog_licenses);
        dialog.show();
    }
}
