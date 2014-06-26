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
    private TextView son;
    private TextView musique;
    private TextView evaluer;
    private TextView contacter;
    private TextView nothing;

    private MediaPlayer songTouch;
    private String songYes;
    private String songNo;
    private String musicYes;
    private String musicNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        songNo = this.getString(R.string.song_touchesNon);
        songYes = this.getString(R.string.song_touchesOui);
        musicYes = this.getString(R.string.musiqueOui);
        musicNo = this.getString(R.string.musiqueNon);

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

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSongTouch();
                goMenu();
            }
        });

        contacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSongTouch();
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "ShakeThatPhone@gmail.com", null));
                startActivity(Intent.createChooser(emailIntent, null));
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

        nothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSongTouch();
            }
        });
    }

    private void recupViews(){
        son = (TextView) findViewById(R.id.songTouches);
        musique = (TextView) findViewById(R.id.musique);
        contacter = (TextView) findViewById(R.id.contacter);
        evaluer = (TextView) findViewById(R.id.evaluer);
        menu = (TextView) findViewById(R.id.menu);
        nothing = (TextView) findViewById(R.id.nothing);
    }

    private void printData(){
        if(DataGame.songTouchIsOn){
            son.setText(songYes);
        } else {
            son.setText(songNo);
        }

        if(DataGame.songNewBestScoreIsOn){
            musique.setText(musicYes);
        } else {
            musique.setText(musicNo);
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
        finish();
    }
}
