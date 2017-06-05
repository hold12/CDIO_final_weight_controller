package dto;

public class ProductBatchComponentDTO {
	private int productbatchId;
	private int ingredientbatchId;
	private double tare;
	private double netWeight;
	private int userId;


	public ProductBatchComponentDTO(int productbatchId, int ingredientbatchId, double tare, double netWeight, int userId) {
		this.productbatchId = productbatchId;
		this.ingredientbatchId = ingredientbatchId;
		this.tare = tare;
		this.netWeight = netWeight;
		this.userId = userId;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductBatchComponentDTO that = (ProductBatchComponentDTO) o;

        if (productbatchId != that.productbatchId) return false;
        if (ingredientbatchId != that.ingredientbatchId) return false;
        if (tare != that.tare) return false;
        if (netWeight != that.netWeight) return false;
        return userId == that.userId;
    }

	public int getProductbatchId() { return productbatchId; }
	public void setProductbatchId(int productbatchId) { this.productbatchId = productbatchId; }
	public int getIngredientbatchId() { return ingredientbatchId; }
	public void setIngredientbatchId(int ingredientbatchId) { this.ingredientbatchId = ingredientbatchId; }
	public double getTare() { return tare; }
	public void setTare(double tare) { this.tare = tare; }
	public double getNetWeight() { return netWeight; }
	public void setNetWeight(double netWeight) { this.netWeight = netWeight; }
	public int getUserId() { return userId; }
	public void setUserId(int operatorId) { this.userId = userId; }

    public String toString() {
        return productbatchId + "\t" + ingredientbatchId +"\t" + tare +"\t" + netWeight + "\t" + userId;
	}
}
