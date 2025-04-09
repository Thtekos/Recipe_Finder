package edu.acg.stekos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    private List<Recipe> recipeList;
    private Toolbar toolbar;
    private ImageView darkModeToggle;

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


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Add dark mode toggle to toolbar
        darkModeToggle = new ImageView(this);
        darkModeToggle.setImageResource(R.drawable.ic_dark_mode);
        darkModeToggle.setContentDescription(getString(R.string.dark_mode_toggle_description));
        
        // Set proper layout parameters for the dark mode toggle
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(
            Toolbar.LayoutParams.WRAP_CONTENT,
            Toolbar.LayoutParams.WRAP_CONTENT
        );
        layoutParams.width = (int) (48 * getResources().getDisplayMetrics().density);
        layoutParams.height = (int) (48 * getResources().getDisplayMetrics().density);
        layoutParams.gravity = android.view.Gravity.END;
        layoutParams.rightMargin = (int) (16 * getResources().getDisplayMetrics().density);
        darkModeToggle.setLayoutParams(layoutParams);
        
        darkModeToggle.setClickable(true);
        darkModeToggle.setOnClickListener(v -> toggleDarkMode());
        toolbar.addView(darkModeToggle);

        // Initialize the list of recipes
        recipeList = new ArrayList<>();
        recipeList.add(new Recipe(1,
                getString(R.string.pasta),
                getString(R.string.delicious_italian_dish),
                R.drawable.pasta,
                getString(R.string.ingredients_flour_water_salt),
                getString(R.string.instructions_mix_ingredients_and_bake)
        ));

        recipeList.add(new Recipe(2,
                getString(R.string.pizza),
                getString(R.string.classic_italian_pizza),
                R.drawable.pizza,
                getString(R.string.ingredients_flour_water_salt),
                getString(R.string.instructions_mix_ingredients_and_bake)
        ));

        recipeList.add(new Recipe(3,
                getString(R.string.salad),
                getString(R.string.healthy_and_refreshing),
                R.drawable.salad,
                getString(R.string.ingredients_lettuce_tomatoes_cucumber),
                getString(R.string.instructions_mix_ingredients_and_serve)
        ));

        recipeList.add(new Recipe(4,
                getString(R.string.soup),
                getString(R.string.warm_and_comforting),
                R.drawable.soup,
                getString(R.string.ingredients_carrots_potatoes_onions),
                getString(R.string.instructions_boil_ingredients_and_blend)
        ));

        // Get recipe ID from intent
        int recipeId = getIntent().getIntExtra("RECIPE_ID", -1);

        // Find the recipe by ID
        Recipe selectedRecipe = findRecipeById(recipeId);

        // Populate UI
        ImageView imageView = findViewById(R.id.imageView);
        TextView title = findViewById(R.id.title);
        TextView description = findViewById(R.id.description);
        TextView ingredients = findViewById(R.id.ingredients);
        TextView instructions = findViewById(R.id.instructions);
        Button shareButton = findViewById(R.id.shareButton);

        imageView.setImageResource(selectedRecipe.getImageResourceId());
        title.setText(selectedRecipe.getTitle());
        description.setText(selectedRecipe.getDescription());
        ingredients.setText(selectedRecipe.getIngredients());
        instructions.setText(selectedRecipe.getInstructions());

        // Set up share button
        shareButton.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Recipe: " + selectedRecipe.getTitle());
            shareIntent.putExtra(Intent.EXTRA_TEXT, selectedRecipe.getIngredients() + "\n" + selectedRecipe.getInstructions());
            startActivity(Intent.createChooser(shareIntent, "Share Recipe"));
        });
    }

    private Recipe findRecipeById(int id) {
        for (Recipe recipe : recipeList) {
            if (recipe.getId() == id) {
                return recipe;
            }
        }
        return null; // Return null if no recipe is found with the given ID
    }

    private void toggleDarkMode() {
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

    private void shareRecipe() {
        Recipe selectedRecipe = findRecipeById(getIntent().getIntExtra("RECIPE_ID", -1));
        if (selectedRecipe != null) {
            String shareText = String.format("%s\n\n%s\n\n%s\n\n%s",
                    selectedRecipe.getTitle(),
                    selectedRecipe.getDescription(),
                    selectedRecipe.getIngredients(),
                    selectedRecipe.getInstructions());

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_recipe)));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}