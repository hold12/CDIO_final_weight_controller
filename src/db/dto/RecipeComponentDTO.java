package db.dto;

public class RecipeComponentDTO {
	private int recipeId;
	private int ingredientId;
	private double nominatedNetWeight;
	private double tolerance;
	
	public RecipeComponentDTO(int recipeId, int ingredientId, double nominatedNetWeight, double tolerance) {
		this.recipeId = recipeId;
		this.ingredientId = ingredientId;
		this.nominatedNetWeight = nominatedNetWeight;
		this.tolerance = tolerance;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RecipeComponentDTO that = (RecipeComponentDTO) o;

		if (recipeId != that.recipeId) return false;
		if (ingredientId != that.ingredientId) return false;
		if (Double.compare(that.nominatedNetWeight, nominatedNetWeight) != 0) return false;
		return Double.compare(that.tolerance, tolerance) == 0;
	}


	public int getRecipeId() { return recipeId; }
	public void setRecipeId(int recipeId) { this.recipeId = recipeId; }
	public int getIngredientId() { return ingredientId; }
	public void setIngredientId(int ingredientId) { this.ingredientId = ingredientId; }
	public double getNominatedNetWeight() { return nominatedNetWeight; }
	public void setNominatedNetWeight(double nominatedNetWeight) { this.nominatedNetWeight = nominatedNetWeight; }
	public double getTolerance() { return tolerance; }
	public void setTolerance(double tolerance) { this.tolerance = tolerance; }
	public String toString() { 
		return recipeId + "\t" + ingredientId + "\t" + nominatedNetWeight + "\t" + tolerance;
	}
}
