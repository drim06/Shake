package zm.shakethatphone;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Options extends Activity {
    private TextView menu;
    private TextView tutoriel;
    private TextView son;
    private TextView musique;
    private TextView evaluer;
    private TextView aPropos;

    private MediaPlayer songTouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        recupViews();
        printData();

        songTouch = MediaPlayer.create(this, R.raw.song_touch);

        son.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DataGame.songTouchIsOn){
                    DataGame.songTouchIsOn = false;
                } else {
                    DataGame.songTouchIsOn = true;
                }
                printData();
                playSongTouch();
            }
        });

        musique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DataGame.songNewBestScoreIsOn){
                    DataGame.songNewBestScoreIsOn = false;
                } else {
                    DataGame.songNewBestScoreIsOn = true;
                }
                printData();
                playSongTouch();
            }
        });

        tutoriel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DataGame.tutorielIsOn){
                    DataGame.tutorielIsOn = false;
                } else {
                    DataGame.tutorielIsOn = true;
                }
                printData();
                playSongTouch();
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSongTouch();
                goMenu();
            }
        });

        aPropos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSongTouch();
            }
        });

        evaluer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSongTouch();
                String packageName = getPackageName();
                String uri ="https://play.google.com/store/apps/details?id=" + packageName;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
            }
        });
    }

    private void recupViews(){
        son = (TextView) findViewById(R.id.songTouches);
        musique = (TextView) findViewById(R.id.musique);
        tutoriel = (TextView) findViewById(R.id.tutoriel);
        aPropos = (TextView) findViewById(R.id.aPropos);
        evaluer = (TextView) findViewById(R.id.evaluer);
        menu = (TextView) findViewById(R.id.menu);
    }

    private void printData(){
        if(DataGame.songTouchIsOn){
            son.setText("Son: OUI");
        } else {
            son.setText("Son: NON");
        }

        if(DataGame.songNewBestScoreIsOn){
            musique.setText("Musique: OUI");
        } else {
            musique.setText("Musique: NON");
        }

        if(DataGame.tutorielIsOn){
            tutoriel.setText("Tutoriel: OUI");
        } else {
            tutoriel.setText("Tutoriel: NON");
        }
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
