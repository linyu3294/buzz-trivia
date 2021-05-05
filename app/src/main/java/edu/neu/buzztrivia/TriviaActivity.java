package edu.neu.buzztrivia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.ArrayList;

import edu.neu.buzztrivia.model.SingletonRemote;
import edu.neu.buzztrivia.model.Track;
import edu.neu.buzztrivia.model.TrackService;

public class TriviaActivity extends AppCompatActivity {
    private TextView userView;
    private TextView songView;
    private Button addBtn;
    private Track track;

    private TrackService trackService;
    private ArrayList<Track> recentlyPlayedTracks;
    SpotifyAppRemote mSpotifyAppRemote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_trivia);

        trackService = new TrackService(getApplicationContext());
        userView = (TextView) findViewById(R.id.user);
        songView = (TextView) findViewById(R.id.song);
        addBtn = (Button) findViewById(R.id.add);

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        userView.setText(sharedPreferences.getString("userid", "No User"));

        getTracks();
        SpotifyAppRemote mSpotifyAppRemote = SingletonRemote.getSpotifyRemote(this);
//        mSpotifyAppRemote.getContentApi().getRecommendedContentItems("");
    }


    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    private View.OnClickListener addListener = v -> {
        trackService.addTrackToLibrary(this.track);
        if (recentlyPlayedTracks.size() > 0) {
            recentlyPlayedTracks.remove(0);
        }
        updateSong();
    };

    private void getTracks() {
        trackService.getRecentlyPlayedTracks(() -> {
            recentlyPlayedTracks = trackService.getSongs();
            updateSong();
        });
    }

    private void updateSong() {
        if (recentlyPlayedTracks.size() > 0) {
            songView.setText(recentlyPlayedTracks.get(0).getName());
            track = recentlyPlayedTracks.get(0);
        }
    }
}