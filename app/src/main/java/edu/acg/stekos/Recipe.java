package edu.acg.stekos;

public class Recipe {
    private int id;
    private String title;
    private String description;
    private int imageResourceId;
    private String ingredients; // Add ingredients field
    private String instructions; // Add instructions field

    public Recipe(int id, String title, String description, int imageResourceId, String ingredients, String instructions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageResourceId = imageResourceId;
        this.ingredients = ingredients; // Initialize ingredients
        this.instructions = instructions; // Initialize instructions
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getIngredients() { // Add getter for ingredients
        return ingredients;
    }

    public String getInstructions() { // Add getter for instructions
        return instructions;
    }
}