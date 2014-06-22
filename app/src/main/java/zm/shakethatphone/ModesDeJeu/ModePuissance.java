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

import java.util.Random;

import zm.shakethatphone.R;
import zm.shakethatphone.ResultatPartie;

/**
 * Created by Marc on 21/06/2014.
 */
public class ModePuissance extends Activity implements SensorEventListener {
    private float xCurrentValueGyroscope, yCurrentValueGyroscope, zCurrentValueGyroscope;
    private SensorManager sensorManager;
    private Sensor gyroscope;
    private int autorisedTime;
    private TextView viewCurrentScore;
    private int currentPuissanceValue;
    private int bestPuissanceValue;
    private TextView viewTutoPuissance;
    private ImageView imageTutoPuissance;
    /**
     * Preferences
     */
    private SharedPreferences settings;
    /**
     * Editer les preferences
     */
    private SharedPreferences.Editor editor;
    private int memoireBestScorePuissance;
    private int memoireCaloriesBrulees;
    private boolean afficherTuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_puissance);

        currentPuissanceValue = 0;
        bestPuissanceValue = 0;

        settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        editor = settings.edit();
        memoireBestScorePuissance = settings.getInt("bestScorePuissance",0);
        memoireCaloriesBrulees = settings.getInt("caloriesBrulees", 0);
        afficherTuto = settings.getBoolean("tutoPuissance", true);

        autorisedTime = getIntent().getExtras().getInt("autorised_time");
        viewCurrentScore = (TextView) findViewById(R.id.view_current_score);
        viewTutoPuissance = (TextView) findViewById(R.id.tutoPuissance);
        imageTutoPuissance = (ImageView) findViewById(R.id.imageTutoPuissance);
        imageTutoPuissance.setAlpha(0.8f);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if(afficherTuto){
            viewTutoPuissance.setVisibility(View.VISIBLE);
            imageTutoPuissance.setVisibility(View.VISIBLE);
            viewCurrentScore.setVisibility(View.INVISIBLE);
        }
        else{
            timer.start();
        }
        imageTutoPuissance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewTutoPuissance.setVisibility(View.INVISIBLE);
                imageTutoPuissance.setVisibility(View.INVISIBLE);
                viewCurrentScore.setVisibility(View.VISIBLE);
                editor.putBoolean("tutoPuissance", false);
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

                Random r = new Random();
                currentPuissanceValue = (int) (Math.abs(xCurrentValueGyroscope) + Math.abs(yCurrentValueGyroscope)
                        + Math.abs(zCurrentValueGyroscope))*(95 + r.nextInt(10));

                if(currentPuissanceValue > bestPuissanceValue){
                    bestPuissanceValue = currentPuissanceValue;
                    viewCurrentScore.setText(bestPuissanceValue+"");
                }
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
                if(bestPuissanceValue > memoireBestScorePuissance){
                    editor.putInt("bestScorePuissance",bestPuissanceValue);
                    editor.commit();
                }
            }
        });
    }

    private void goResultatPartie(){
        Intent intent = new Intent(this, ResultatPartie.class);
        intent.putExtra("autorised_time", autorisedTime);
        intent.putExtra("game_mode", "Mode Puissance");
        intent.putExtra("score", bestPuissanceValue);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in_opacity, R.anim.fade_out_opacity);
    }

    private void updateCaloriesBrulees(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int caloriesBruleesDuringThisGame = bestPuissanceValue / 1000;
                int totalCaloriesBrulees = memoireCaloriesBrulees + caloriesBruleesDuringThisGame;
                editor.putInt("caloriesBrulees", totalCaloriesBrulees);
                editor.commit();
            }
        });
    }
}
