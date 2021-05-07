package edu.neu.buzztrivia.services;


import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.neu.buzztrivia.model.Track;

public class TriviaService {
    private ArrayList<Track> tracks = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    SpotifyAppRemote mSpotifyAppRemote;

    private static final String REDIRECT_URI = "http://localhost/";
    private static String clientID = "1500462005314b4ab175dce1bf67f31e";

    public TriviaService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
    }

    public ArrayList<Track> getSongs() {
        return tracks;
    }

    public void getRecentlyPlayedTracks(final IVolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/me/player/recently-played";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONArray("items");
                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            object = object.optJSONObject("track");
                            Track song = gson.fromJson(object.toString(), Track.class);
                            tracks.add(song);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    callBack.onSuccess();
                }, error -> {
                    // TODO: Handle error

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }



    public boolean playTrack(Context context, String trackID, final IVolleyCallBack callBack) {

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

                        connected(trackID);
                    }

                    public void onFailure(Throwable throwable) {
                        System.out.println("MyActivity" + throwable.getMessage() + throwable);
                        // TODO : Create onFailure Loggging and Handling
                    }
                });
        callBack.onSuccess();
        return true;
    }


    private void connected(String trackID) {
        String query = "spotify:track:" + trackID;
        // Play a playlist
        System.out.println(query);
        mSpotifyAppRemote.getPlayerApi().play(query);

        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final com.spotify.protocol.types.Track track = playerState.track;
                    if (track != null) {
//                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
    }

}
