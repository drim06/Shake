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

import zm.shakethatphone.R;
import zm.shakethatphone.ResultatPartie;

public class ModeEndurance extends Activity implements SensorEventListener {
    private float xCurrentValueGyroscope, yCurrentValueGyroscope, zCurrentValueGyroscope;
    private SensorManager sensorManager;
    private Sensor gyroscope;
    private int currentScore;
    private int autorisedTime;
    private TextView viewCurrentScore;
    private TextView viewTutoEndurance;
    private ImageView imageTutoEndurance;
    /**
     * Preferences
     */
    private SharedPreferences settings;
    /**
     * Editer les preferences
     */
    private SharedPreferences.Editor editor;
    private int memoireBestScoreEndurance;
    private int memoireCaloriesBrulees;
    private boolean afficherTuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_endurance);

        currentScore = 0;
        settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        editor = settings.edit();
        memoireBestScoreEndurance = settings.getInt("bestScoreEndurance",0);
        memoireCaloriesBrulees = settings.getInt("caloriesBrulees", 0);
        afficherTuto = settings.getBoolean("tutoEndurance", true);

        autorisedTime = getIntent().getExtras().getInt("autorised_time");
        viewCurrentScore = (TextView) findViewById(R.id.view_current_score);
        viewTutoEndurance = (TextView) findViewById(R.id.tutoEndurance);
        imageTutoEndurance = (ImageView) findViewById(R.id.imageTutoEndurance);
        imageTutoEndurance.setAlpha(0.8f);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if(afficherTuto){
            viewTutoEndurance.setVisibility(View.VISIBLE);
            imageTutoEndurance.setVisibility(View.VISIBLE);
            viewCurrentScore.setVisibility(View.INVISIBLE);
        }
        else{
            timer.start();
        }
        imageTutoEndurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewTutoEndurance.setVisibility(View.INVISIBLE);
                imageTutoEndurance.setVisibility(View.INVISIBLE);
                viewCurrentScore.setVisibility(View.VISIBLE);
                editor.putBoolean("tutoEndurance", false);
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
                if(currentScore > memoireBestScoreEndurance){
                    editor.putInt("bestScoreEndurance", currentScore);
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
        intent.putExtra("game_mode", "Mode Endurance");
        intent.putExtra("score", currentScore);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in_opacity, R.anim.fade_out_opacity);
    }
}