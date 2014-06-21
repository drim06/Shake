package zm.shakethatphone;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

public class Options extends Activity {

    MediaPlayer songTouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        songTouch = MediaPlayer.create(this, R.raw.song_touch);
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
