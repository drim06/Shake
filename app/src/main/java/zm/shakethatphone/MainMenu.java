package zm.shakethatphone;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import zm.shakethatphone.ModesDeJeu.ModeEndurance;
import zm.shakethatphone.ModesDeJeu.ModePuissance;
import zm.shakethatphone.ModesDeJeu.ModeReflexe;
import zm.shakethatphone.ModesDeJeu.ModeSprint;

/**
 * Shake That Phone
 *
 * idéal entre deux matchs, simple rapide et efficace, le jeu parfait pour vous
 * affronter entre amis tout en restant amusant. Et pourquoi ne pas perdre
 * quelques calories en même temps ? =p
 *
 * êtes vous un Sprinteur ou plûtot Endurant ?
 * Vous misez tout sur la Puissance ou sur vos Reflexes ?
 *
 * Rassurez vous il y a un mode de jeu pour chacun d'entre vous...
 *
 */
public class MainMenu extends Activity {
    private Button sprint;
    private Button endurance;
    private Button reflexe;
    private Button puissance;
    private Button profil;

    private MediaPlayer songTouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        songTouch = MediaPlayer.create(this, R.raw.song_touch);

        sprint = (Button) findViewById(R.id.sprint);
        sprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSongTouch();
                Intent intent = new Intent(MainMenu.this, ModeSprint.class);
                intent.putExtra("autorised_time", 4);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        endurance = (Button) findViewById(R.id.endurance);
        endurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSongTouch();
                Intent intent = new Intent(MainMenu.this, ModeEndurance.class);
                intent.putExtra("autorised_time", 21);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        reflexe = (Button) findViewById(R.id.reflexe);
        reflexe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSongTouch();
                Intent intent = new Intent(MainMenu.this, ModeReflexe.class);
                intent.putExtra("autorised_time", 10);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        puissance = (Button) findViewById(R.id.puissance);
        puissance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSongTouch();
                Intent intent = new Intent(MainMenu.this, ModePuissance.class);
                intent.putExtra("autorised_time", 3);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        profil = (Button) findViewById(R.id.profil);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSongTouch();
                Intent intent = new Intent(MainMenu.this, Profil.class);
                startActivity(intent);
                overridePendingTransition(R.anim.vertical_fade_in, R.anim.vertical_fade_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // nothing
    }

    private void playSongTouch(){
        if(DataGame.songTouchIsOn) {
            songTouch.start();
        }
    }
}
