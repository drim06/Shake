package zm.shakethatphone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

public class Profil extends Activity {
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    int bestScoreSprint;
    int bestScoreEndurance;
    int bestScoreReflexe;
    int bestScorePuissance;
    int caloriesBrulees;

    TextView txtBestScoreSprint;
    TextView txtBestScoreEndurance;
    TextView txtBestScoreReflexe;
    TextView txtBestScorePuissance;
    TextView txtCaloriesBrulees;
    TextView txtPartager;
    TextView txtReinitialiser;

    MediaPlayer songTouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        songTouch = MediaPlayer.create(this, R.raw.song_touch);

        recupData();
        recupViews();
        updateViews();

        txtPartager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSongTouch();
                // need partage face...
            }
        });

        txtReinitialiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSongTouch();
                resetGameScores();
                recupData();
                updateViews();
            }
        });
    }

    private void resetGameScores(){
        editor.putInt("bestScoreSprint", 0);
        editor.putInt("bestScoreEndurance", 0);
        editor.putInt("bestScoreReflexe", 0);
        editor.putInt("bestScorePuissance", 0);
        editor.putInt("caloriesBrulees", 0);
        editor.commit();
    }

    private void recupData(){
        settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        editor = settings.edit();
        bestScoreSprint = settings.getInt("bestScoreSprint",0);
        bestScoreEndurance = settings.getInt("bestScoreEndurance",0);
        bestScoreReflexe = settings.getInt("bestScoreReflexe",0);
        bestScorePuissance = settings.getInt("bestScorePuissance",0);
        caloriesBrulees = settings.getInt("caloriesBrulees", 0);
    }

    private void recupViews(){
        txtBestScoreSprint = (TextView) findViewById(R.id.bestSprint);
        txtBestScoreEndurance = (TextView) findViewById(R.id.bestEndurance);
        txtBestScoreReflexe = (TextView) findViewById(R.id.bestReflexe);
        txtBestScorePuissance = (TextView) findViewById(R.id.bestPuissance);
        txtCaloriesBrulees = (TextView) findViewById(R.id.caloriesBrulees);
        txtPartager = (TextView) findViewById(R.id.partager);
        txtReinitialiser = (TextView) findViewById(R.id.reinitialiser);
    }

    private void updateViews(){
        txtBestScoreSprint.setText(bestScoreSprint+"");
        txtBestScoreEndurance.setText(bestScoreEndurance + "");
        txtBestScoreReflexe.setText(bestScoreReflexe+"");
        txtBestScorePuissance.setText(bestScorePuissance+"");
        txtCaloriesBrulees.setText(caloriesBrulees+"");
    }

    private void fakeDataViews(){
        txtBestScoreSprint.setText(812+"");
        txtBestScoreEndurance.setText(2312+ "");
        txtBestScoreReflexe.setText(326+"");
        txtBestScorePuissance.setText(3020+"");
        txtCaloriesBrulees.setText(246+"");
    }

    @Override
    public void onBackPressed() {
        playSongTouch();
        goMenu();
    }

    private void playSongTouch(){
        if(DataGame.songTouchIsOn) {
            songTouch.start();
        }
    }

    private void goMenu(){
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
        overridePendingTransition(R.anim.vertical_reverse_fade_in, R.anim.vertical_reverse_fade_out);
    }
}
