package zm.shakethatphone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Shake That Phone
 *
 * idéal entre deux matchs, simple rapide et efficace, le jeu parfait pour vous
 * affronter entre amis tout en restant amusant.
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        sprint = (Button) findViewById(R.id.sprint);
        sprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                Intent intent = new Intent(MainMenu.this, ModePuissance.class);
                intent.putExtra("autorised_time", 3);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }
}
