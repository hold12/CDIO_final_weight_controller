package db.dto;

public class RecipeDTO {
	private int recipeId;
	private String recipeName;
    
	public RecipeDTO(int recipeId, String recipeName) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
    }

    public int getRecipeId() { return recipeId; }
	public void setRecipeId(int recipeId) { this.recipeId = recipeId; }
	public String getRecipeName() { return recipeName; }
	public void setRecipeName(String recipeName) { this.recipeName = recipeName; }
	public String toString() { 
		return recipeId + "\t" + recipeName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RecipeDTO that = (RecipeDTO) o;

		if (recipeId != that.recipeId) return false;
		return recipeName != null ? recipeName.equals(that.recipeName) : that.recipeName == null;
	}
}
