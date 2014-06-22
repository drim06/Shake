package zm.shakethatphone.ModesDeJeu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import zm.shakethatphone.DataGame;
import zm.shakethatphone.R;
import zm.shakethatphone.ResultatPartie;

public class ModeSprint extends Activity implements SensorEventListener {
    private float xCurrentValueGyroscope, yCurrentValueGyroscope, zCurrentValueGyroscope;
    private SensorManager sensorManager;
    private Sensor gyroscope;
    private int currentScore;
    private int autorisedTime;
    private TextView viewCurrentScore;
    private TextView viewTutoSprint;
    private ImageView imageTutoSprint;
    /**
     * Preferences
     */
    private SharedPreferences settings;
    /**
     * Editer les preferences
     */
    private SharedPreferences.Editor editor;
    private boolean afficherTuto;
    private int memoireBestScoreSprint;
    private int memoireCaloriesBrulees;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_sprint);


        currentScore = 0;
        settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        editor = settings.edit();
        memoireBestScoreSprint = settings.getInt("bestScoreSprint",0);
        memoireCaloriesBrulees = settings.getInt("caloriesBrulees", 0);
        afficherTuto = settings.getBoolean("tutoSprint", true);

        autorisedTime = getIntent().getExtras().getInt("autorised_time");
        viewCurrentScore = (TextView) findViewById(R.id.view_current_score);
        viewTutoSprint = (TextView) findViewById(R.id.tutoSprint);
        imageTutoSprint = (ImageView) findViewById(R.id.imageTutoSprint);
        imageTutoSprint.setAlpha(0.8f);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if(afficherTuto){
            viewTutoSprint.setVisibility(View.VISIBLE);
            imageTutoSprint.setVisibility(View.VISIBLE);
            viewCurrentScore.setVisibility(View.INVISIBLE);
        }
        else{
            timer.start();
        }
        viewTutoSprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewTutoSprint.setVisibility(View.INVISIBLE);
                imageTutoSprint.setVisibility(View.INVISIBLE);
                viewCurrentScore.setVisibility(View.VISIBLE);
                editor.putBoolean("tutoSprint", false);
                editor.commit();
                afficherTuto = false;
                timer.start();
            }
        });

    }

    @Override
    protected void onPause() {
        unregisterListenerGyroscope();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // Abonnement au capteur
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_UI);
        super.onResume();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if(!afficherTuto){
            final String LOG_TAG = "SensorsGyroscope";
            if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                String accuracyStr;
                if (SensorManager.SENSOR_STATUS_ACCURACY_HIGH == accuracy) {
                    accuracyStr = "SENSOR_STATUS_ACCURACY_HIGH";
                } else if (SensorManager.SENSOR_STATUS_ACCURACY_LOW == accuracy) {
                    accuracyStr = "SENSOR_STATUS_ACCURACY_LOW";
                } else if (SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM == accuracy) {
                    accuracyStr = "SENSOR_STATUS_ACCURACY_MEDIUM";
                } else {
                    accuracyStr = "SENSOR_STATUS_UNRELIABLE";
                }
                Log.d(LOG_TAG, "Sensor's values (" + xCurrentValueGyroscope + ","
                        + yCurrentValueGyroscope + "," + zCurrentValueGyroscope + ") and accuracy : "
                        + accuracyStr);
            }
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(!afficherTuto){
            // mise a jour seulement quand on est dans le bon cas
            if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                // vitesse angulaire autour de chaque axes
                xCurrentValueGyroscope = event.values[0];
                yCurrentValueGyroscope = event.values[1];
                zCurrentValueGyroscope = event.values[2];

                currentScore += (int) (Math.abs(xCurrentValueGyroscope) + Math.abs(yCurrentValueGyroscope)
                        + Math.abs(zCurrentValueGyroscope));

                viewCurrentScore.setText(currentScore+"");
            }
        }
    }

    public void onBackPressed(){
        // nothing
    }

    Thread timer = new Thread(new Runnable() {
        @Override
        public void run() {
            incrementTime();
            unregisterListenerGyroscope();
            updateNewScore();
            updateCaloriesBrulees();
            goResultatPartie();
        }
    });

    private void incrementTime(){
        int elapsedTime = 0;
        while(elapsedTime <= autorisedTime) {
            elapsedTime++;
            sleepOneSecond();
        }
    }

    private void sleepOneSecond(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void unregisterListenerGyroscope(){
        sensorManager.unregisterListener(this, gyroscope);
    }

    private void updateNewScore(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(currentScore > memoireBestScoreSprint){
                    editor.putInt("bestScoreSprint",currentScore);
                    editor.commit();
                }
            }
        });
    }

    private void updateCaloriesBrulees(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int caloriesBruleesDuringThisGame = currentScore / 100;
                int totalCaloriesBrulees = memoireCaloriesBrulees + caloriesBruleesDuringThisGame;
                editor.putInt("caloriesBrulees", totalCaloriesBrulees);
                editor.commit();
            }
        });
    }

    private void goResultatPartie(){
        Intent intent = new Intent(this, ResultatPartie.class);
        intent.putExtra("autorised_time", autorisedTime);
        intent.putExtra("game_mode", "Mode Sprint");
        intent.putExtra("score", currentScore);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in_opacity, R.anim.fade_out_opacity);
    }
}