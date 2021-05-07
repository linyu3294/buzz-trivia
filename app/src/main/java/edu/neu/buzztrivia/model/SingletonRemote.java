package edu.neu.buzztrivia.model;

import android.content.Context;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.io.Serializable;

public class SingletonRemote implements Serializable {
    private static final String REDIRECT_URI = "http://localhost/";
    private static SpotifyAppRemote mSpotifyAppRemote = null;
    private static String clientID = "1500462005314b4ab175dce1bf67f31e";

    public static SpotifyAppRemote getSpotifyRemote (Context context) {

        if (mSpotifyAppRemote == null){
            ConnectionParams connectionParams =
                    new ConnectionParams.Builder(clientID)
                            .setRedirectUri(REDIRECT_URI)
                            .showAuthView(true)
                            .build();


            SpotifyAppRemote.connect(context, connectionParams,
                    new Connector.ConnectionListener() {
                        public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                            mSpotifyAppRemote = spotifyAppRemote;
                            System.out.println("LoginActivity Connected! Yay!");
                            // Now you can start interacting with App Remote
                        }

                        public void onFailure(Throwable throwable) {
                            System.out.println("MyActivity" + throwable.getMessage() + throwable);
                            // TODO : Create onFailure Loggging and Handling
                        }
                    });
        }
        System.out.println("checkpoint" + mSpotifyAppRemote);
        return mSpotifyAppRemote;
    }
}
