package dto;

public class IngredientDTO
{
    private int ingredientId;
    private String ingredientName;

	public IngredientDTO(int ingredientId, String ingredientName) {
		this.ingredientId = ingredientId;
		this.ingredientName = ingredientName;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IngredientDTO that = (IngredientDTO) o;

        if (ingredientId != that.ingredientId) return false;
        return ingredientName != null ? ingredientName.equals(that.ingredientName) : that.ingredientName == null;
    }

    public int getIngredientId() { return ingredientId; }
    public void setIngredientId(int ingredientId) { this.ingredientId = ingredientId; }
    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }
    public String toString() { 
		return ingredientId + "\t" + ingredientName;
	}
}
