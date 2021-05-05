package edu.neu.buzztrivia.model;

import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Trivia implements Serializable {
    private Playlist playlist;
    private SpotifyAppRemote mSpotifyAppRemote;

    private void play() {
        // Play a playlist
        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");

        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        System.out.println("LoginActivity" + track.name + " by " + track.artist.name);
                    }
                });
    }


}
