package edu.neu.buzztrivia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View goToTriviaBtn = findViewById(R.id.btn_nav_to_trivia);
        goToTriviaBtn.setOnClickListener(goToTriviaListener);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }



    private View.OnClickListener goToTriviaListener = v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
//        intent.putExtra("trivia", Trivia);
            startActivity(intent);
    };



    public void goToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
//        intent.putExtra("trivia", Trivia);
        startActivity(intent);
    }

}