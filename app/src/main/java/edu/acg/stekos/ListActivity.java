package edu.acg.stekos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private List<Recipe> recipeList;

    @Override
    protected void attachBaseContext(android.content.Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("LocalePrefs", MODE_PRIVATE);
        String lang = prefs.getString("Language", "en");
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        super.attachBaseContext(newBase.createConfigurationContext(config));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setupUI();
    }

    private void setupUI() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample data
        recipeList = new ArrayList<>();
        recipeList.add(new Recipe(1, getString(R.string.pasta), getString(R.string.delicious_italian_dish), R.drawable.pasta, getString(R.string.ingredients_flour_water_salt), getString(R.string.instructions_mix_ingredients_and_bake)));
        recipeList.add(new Recipe(2, getString(R.string.pizza), getString(R.string.classic_italian_pizza), R.drawable.pizza, getString(R.string.ingredients_flour_water_salt), getString(R.string.instructions_mix_ingredients_and_bake)));
        recipeList.add(new Recipe(3, getString(R.string.salad), getString(R.string.healthy_and_refreshing), R.drawable.salad, getString(R.string.ingredients_lettuce_tomatoes_cucumber), getString(R.string.instructions_mix_ingredients_and_serve)));
        recipeList.add(new Recipe(4, getString(R.string.soup), getString(R.string.warm_and_comforting), R.drawable.soup, getString(R.string.ingredients_carrots_potatoes_onions), getString(R.string.instructions_boil_ingredients_and_blend)));

        adapter = new RecipeAdapter(recipeList, this, new Locale(getSavedLanguage()));
        recyclerView.setAdapter(adapter);

        ImageView englishFlag = findViewById(R.id.english_flag);
        ImageView greekFlag = findViewById(R.id.greek_flag);
        ImageView darkModeToggle = findViewById(R.id.dark_mode_toggle);

        englishFlag.setOnClickListener(v -> changeLanguage("en"));
        greekFlag.setOnClickListener(v -> changeLanguage("el"));
        darkModeToggle.setOnClickListener(v -> toggleDarkMode());
    }

    private String getSavedLanguage() {
        SharedPreferences prefs = getSharedPreferences("LocalePrefs", MODE_PRIVATE);
        return prefs.getString("Language", "en");
    }

    private void changeLanguage(String lang) {
        SharedPreferences prefs = getSharedPreferences("LocalePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Language", lang);
        editor.apply();

        // Restart activity to apply new language
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void toggleDarkMode() {
        ImageView darkModeToggle = findViewById(R.id.dark_mode_toggle);
        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        darkModeToggle.startAnimation(rotateAnimation);

        // Get current theme mode
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        boolean isDarkMode = currentNightMode == Configuration.UI_MODE_NIGHT_YES;

        // Get the root view for animation
        final View rootView = getWindow().getDecorView();
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        
        // Change theme immediately but let the animation smooth out the transition
        AppCompatDelegate.setDefaultNightMode(
            isDarkMode ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES
        );

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                // No need to recreate here as setDefaultNightMode will trigger recreation
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        // Start the fade out animation
        rootView.startAnimation(fadeOut);
    }
}
