package dto;

public class ProductBatchDTO {
	private int productbatchId;
	private int status;
	private int recipeId;
	
	public ProductBatchDTO(int productbatchId, int status, int recipeId) {
		this.productbatchId = productbatchId;
		this.status = status;
		this.recipeId = recipeId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ProductBatchDTO that = (ProductBatchDTO) o;

		if (productbatchId != that.productbatchId) return false;
		if (status != that.status) return false;
		return recipeId == that.recipeId;
	}
	
	public int getProductbatchId() { return productbatchId; }
	public void setProductbatchId(int productbatchId) { this.productbatchId = productbatchId; }
	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }
	public int getRecipeId() { return recipeId; }
	public void setRecipeId(int recipeId) { this.recipeId = recipeId; }
	public String toString() { return productbatchId + "\t" + status + "\t" + recipeId; }
}

