package zm.shakethatphone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import zm.shakethatphone.ModesDeJeu.ModeEndurance;
import zm.shakethatphone.ModesDeJeu.ModePuissance;
import zm.shakethatphone.ModesDeJeu.ModeReflexe;
import zm.shakethatphone.ModesDeJeu.ModeSprint;

public class ResultatPartie extends Activity {
    TextView txtPartager;
    TextView txtQuitter;
    TextView txtRejouer;
    TextView txtGameMode;
    TextView txtScore;
    TextView txtBestScore;
    TextView txtMeilleur;
    LinearLayout linearLayout;

    BitmapDrawable bitmapDrawable;

    String mode;
    int autorisedTime;
    int score;
    int bestScore;

    /**
     * Preferences
     */
    SharedPreferences settings;
    /**
     * Editer les preferences
     */
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat_partie);

        recupViews();
        recupData();
        printData();
        changeColorDesign();
        playSoundIFNewBestScore();

        txtPartager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //takeScreenshot();
//                ImageView image = (ImageView) findViewById(R.id.screenshots);
//                image.setBackgroundDrawable(bitmapDrawable);
//                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//                Uri screenshotUri = Uri.parse();
//
//                sharingIntent.setType("image/png");
//                sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
//                startActivity(Intent.createChooser(sharingIntent, "Share image using"));


                // Now send it out to share
//                Intent share = new Intent(android.content.Intent.ACTION_SEND);
//                share.setType("image/*");
//                share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + sharefile));
//                try {
//                    startActivity(Intent.createChooser(share, "Share photo"));
//                } catch (Exception e) {
//
//                }

                String message = mode.split(" ")[1] + " Mode, " + score + " pts !!" +" Who can beat me in @ShakeThatPhone : " + " https://play.google.com/store/apps/details?id=" + getPackageName();
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);

                startActivity(Intent.createChooser(share, "Partager"));
            }
        });

        txtQuitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goMenu();
            }
        });

        txtRejouer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejouer();
            }
        });
    }

    private void takeScreenshot(){
        LinearLayout L1 = (LinearLayout) findViewById(R.id.linearLayoutResultatPartie);
        View v1 = L1.getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap bm = v1.getDrawingCache();
        bitmapDrawable = new BitmapDrawable(bm);
    }

    private void recupData(){
        mode = getIntent().getExtras().getString("game_mode");
        autorisedTime = getIntent().getExtras().getInt("autorised_time");
        score = getIntent().getExtras().getInt("score");
        settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        editor = settings.edit();
        recupBestScoreForThisGame();
    }

    private void recupBestScoreForThisGame(){
        if(mode.equals("Mode Sprint")) {
            bestScore = settings.getInt("bestScoreSprint", 0);
        } else if (mode.equals("Mode Endurance")){
            bestScore = settings.getInt("bestScoreEndurance", 0);
        } else if (mode.equals("Mode Reflexe")){
            bestScore = settings.getInt("bestScoreReflexe", 0);
        } else if (mode.equals("Mode Puissance")){
            bestScore = settings.getInt("bestScorePuissance", 0);
        }
    }

    private void printData(){
        printGameMode();
        printScore();
        printBestScore();
    }

    private void printGameMode(){
        txtGameMode.setText(mode);
    }

    private void printScore(){
        txtScore.setText(score+"");
    }

    private void printBestScore(){
        if(score > bestScore){
            txtBestScore.setText(score+"");
        } else {
            txtBestScore.setText(bestScore+"");
        }
    }

    private void recupViews(){
        txtScore = (TextView) findViewById(R.id.currentScore);
        txtBestScore = (TextView) findViewById(R.id.bestScore);
        txtMeilleur = (TextView) findViewById(R.id.txtMeilleur);
        txtRejouer = (TextView) findViewById(R.id.rejouer);
        txtQuitter = (TextView) findViewById(R.id.quitter);
        txtPartager = (TextView) findViewById(R.id.partager);
        txtGameMode = (TextView) findViewById(R.id.modeDeJeu);
        txtGameMode = (TextView) findViewById(R.id.modeDeJeu);
        linearLayout = (LinearLayout) txtGameMode.getParent().getParent();
    }

    private void changeColorDesign(){
        if(score >= bestScore){
            linearLayout.setBackgroundColor(Color.parseColor("#228b22"));
            txtBestScore.setTextColor(Color.parseColor("#ffd700"));
            txtScore.setTextColor(Color.parseColor("#ffd700"));
            txtGameMode.setTextColor(Color.parseColor("#ffd700"));
            txtPartager.setTextColor(Color.parseColor("#ffd700"));
            txtQuitter.setTextColor(Color.parseColor("#ffd700"));
            txtRejouer.setTextColor(Color.parseColor("#ffd700"));
            txtMeilleur.setTextColor(Color.parseColor("#ffd700"));
        } else {
            linearLayout.setBackgroundColor(Color.parseColor("#ffd700"));
            txtBestScore.setTextColor(Color.parseColor("#228b22"));
            txtScore.setTextColor(Color.parseColor("#228b22"));
            txtGameMode.setTextColor(Color.parseColor("#228b22"));
            txtPartager.setTextColor(Color.parseColor("#228b22"));
            txtQuitter.setTextColor(Color.parseColor("#228b22"));
            txtRejouer.setTextColor(Color.parseColor("#228b22"));
            txtMeilleur.setTextColor(Color.parseColor("#228b22"));
        }
    }

    private void playSoundIFNewBestScore(){
        if(score >= bestScore && DataGame.songNewBestScoreIsOn){
            MediaPlayer songNewBestScore = MediaPlayer.create(this, R.raw.song_new_best_score);
            songNewBestScore.start();
        }
    }

    private void rejouer(){
        if(mode.equals("Mode Sprint")){
            reLaunchSprint();
        } else if (mode.equals("Mode Endurance")){
            reLaunchEndurance();
        } else if (mode.equals("Mode Reflexe")){
            reLaunchReflexe();
        } else if (mode.equals("Mode Puissance")){
            reLaunchPuissance();
        }
    }

    private void reLaunchSprint(){
        Intent intent = new Intent(this, ModeSprint.class);
        intent.putExtra("autorised_time", autorisedTime);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void reLaunchEndurance(){
        Intent intent = new Intent(this, ModeEndurance.class);
        intent.putExtra("autorised_time", autorisedTime);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void reLaunchReflexe(){
        Intent intent = new Intent(this, ModeReflexe.class);
        intent.putExtra("autorised_time", autorisedTime);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void reLaunchPuissance(){
        Intent intent = new Intent(this, ModePuissance.class);
        intent.putExtra("autorised_time", autorisedTime);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        goMenu();
    }

    private void goMenu(){
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
        overridePendingTransition(R.anim.reverse_fade_in, R.anim.reverse_fade_out);
    }
}
