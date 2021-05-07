package edu.neu.buzztrivia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.ArrayList;

import edu.neu.buzztrivia.model.Track;
import edu.neu.buzztrivia.services.TriviaService;

public class TriviaActivity extends AppCompatActivity {
    private TextView userView;
    private TextView songView;
    private Button addBtn;
    private int trackIndex = 0;
    private TriviaService triviaService;
    private ArrayList<Track> recentlyPlayedTracks;
    SpotifyAppRemote mSpotifyAppRemote;
    private Track track;
    boolean endOfPlaylist;

    private static final String REDIRECT_URI = "http://localhost/";
    private static String clientID = "1500462005314b4ab175dce1bf67f31e";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        triviaService = new TriviaService(getApplicationContext());
        userView = (TextView) findViewById(R.id.user);
        songView = (TextView) findViewById(R.id.song);
        addBtn = (Button) findViewById(R.id.add);


        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        userView.setText(sharedPreferences.getString("userid", "No User"));

        endOfPlaylist =false;
        getTracks();
        addBtn.setOnClickListener(addTrackListener);
    }


    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }


    private View.OnClickListener addTrackListener = v -> {
        updateSong();
        playTrack(this.track.getId());
        if (recentlyPlayedTracks.size() > 0) {
            recentlyPlayedTracks.remove(0);
        }else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    };


    private void updateSong() {
        if (recentlyPlayedTracks.size() > 0) {
            songView.setText(recentlyPlayedTracks.get(0).getName());
            track = recentlyPlayedTracks.get(0);
        }
    }



    private void getTracks() {
        triviaService.getRecentlyPlayedTracks(() -> {
            recentlyPlayedTracks = triviaService.getSongs();
            updateSong();
            System.out.println("Number of Tracks in Recently Played : " + recentlyPlayedTracks.size());
            for (int i = 0; i < recentlyPlayedTracks.size(); i++) {
                System.out.println("--------------------");
                System.out.println("Track index : " + i);
                System.out.println("Track Name : " + recentlyPlayedTracks.get(i).getName());
                System.out.println("Track ID : " + recentlyPlayedTracks.get(i).getId());
                System.out.println("--------------------");
            }
        });
    }


    private boolean playTrack(String trackID) {
        return triviaService.playTrack(
                this,
                trackID,
                () -> {
                });
    }


}