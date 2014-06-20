package zm.shakethatphone;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class ModeReflexe extends Activity implements SensorEventListener {
    private float xCurrentValueGyroscope, yCurrentValueGyroscope, zCurrentValueGyroscope;
    private SensorManager sensorManager;
    private Sensor gyroscope;
    private int currentScore;
    private int autorisedTime;
    private TextView viewCurrentScore;
    private RelativeLayout relativeLayout;
    private Boolean userShouldShake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_reflexe);

        currentScore = 0;

        autorisedTime = getIntent().getExtras().getInt("autorised_time");

        viewCurrentScore = (TextView) findViewById(R.id.view_current_score);
        relativeLayout = (RelativeLayout) viewCurrentScore.getParent();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        userShouldShake = false;
        makeDesign();

        timer.start();
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        // mise a jour seulement quand on est dans le bon cas
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            // vitesse angulaire autour de chaque axes
            xCurrentValueGyroscope = event.values[0];
            yCurrentValueGyroscope = event.values[1];
            zCurrentValueGyroscope = event.values[2];

            incrementOrDecrementScore();
            updateTextCurrentScore();
        }
    }

    private void incrementOrDecrementScore(){
        if(userShouldShake){
            currentScore += (int) (Math.abs(xCurrentValueGyroscope) + Math.abs(yCurrentValueGyroscope)
                    + Math.abs(zCurrentValueGyroscope));
        } else {
            currentScore -= (int) (Math.abs(xCurrentValueGyroscope) + Math.abs(yCurrentValueGyroscope)
                    + Math.abs(zCurrentValueGyroscope));
        }
    }

    private void updateTextCurrentScore(){
        viewCurrentScore.setText(currentScore+"");
    }

    public void onBackPressed(){
        // nothing
    }

    Thread timer = new Thread(new Runnable() {
        @Override
        public void run() {
            incrementTime();
            unregisterListenerGyroscope();
            printScoreWithToast();
            goMenu();
        }
    });

    private void incrementTime(){
        int elapsedTime = 0;
        while(elapsedTime <= autorisedTime) {
            elapsedTime++;
            sleepOneSecond();
            makeChangementOrNot();
        }
    }

    private void sleepOneSecond(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void makeChangementOrNot(){
        Random r = new Random();
        if(r.nextInt(2) == 0){
            changeGameSituation();
        }
    }

    private void unregisterListenerGyroscope(){
        sensorManager.unregisterListener(this, gyroscope);
    }

    private void printScoreWithToast(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String finalScore = currentScore + "";
                Toast.makeText(ModeReflexe.this, finalScore, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goMenu(){
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
        overridePendingTransition(R.anim.reverse_fade_in, R.anim.reverse_fade_out);
    }

    private void makeDesign(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(userShouldShake){
                    // Fond vert, text jaune
                    viewCurrentScore.setTextColor(Color.parseColor("#ffd700"));
                    relativeLayout.setBackgroundColor(Color.parseColor("#228b22"));
                } else {
                    // Fond jaune, text vert
                    viewCurrentScore.setTextColor(Color.parseColor("#228b22"));
                    relativeLayout.setBackgroundColor(Color.parseColor("#ffd700"));
                }
            }
        });
    }

    private void changeGameSituation(){
        if(userShouldShake){
            userShouldShake = false;
            makeDesign();
        } else {
            userShouldShake = true;
            makeDesign();
        }
    }
}