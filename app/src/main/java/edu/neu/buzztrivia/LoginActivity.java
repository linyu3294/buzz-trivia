package edu.neu.buzztrivia;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import edu.neu.buzztrivia.model.User;
import edu.neu.buzztrivia.services.UserService;


public class LoginActivity extends AppCompatActivity {
    private static final String clientID = "1500462005314b4ab175dce1bf67f31e";
    private static final int REQUEST_CODE = 1337;
    private static final String REDIRECT_URI = "http://localhost/";
    private static final String SCOPES = ""
            + "streaming,"
            + "user-read-recently-played,"
            + "user-library-modify,"
            + "user-read-email,"
            + "user-read-private";
    private SharedPreferences.Editor editor;
    private SharedPreferences msharedPreferences;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart() {
        super.onStart();
        authenticateSpotify();
        msharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    editor = getSharedPreferences("SPOTIFY", 0).edit();
                    editor.putString("token", response.getAccessToken());
                    System.out.println("STARTING, GOT AUTH TOKEN");
                    editor.apply();
                    waitForUserInfo();
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;
                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        AuthorizationClient.clearCookies(this);
    }


    private void authenticateSpotify() {
        // Request code will be used to verify if result comes from the login activity.
        // Can be set to any integer.
        AuthorizationRequest.Builder builder =
                new AuthorizationRequest.Builder(
                        clientID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI
                );
        builder.setScopes(new String[]{SCOPES});
        AuthorizationRequest request = builder.build();
        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
    }


    private void waitForUserInfo() {
        UserService userService = new UserService(queue, msharedPreferences);
        userService.get(() -> {
            User user = userService.getSpotifyUser();
            editor = getSharedPreferences("SPOTIFY", 0).edit();
            editor.putString("userid", user.id);
            System.out.println("STARTING, GOT USER INFORMATION");
            // We use commit instead of apply because we need the information stored immediately
            editor.commit();
            goToSettings();
        });
    }


    public void goToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
//        intent.putExtra("trivia", Trivia);
        startActivity(intent);
    }

}



