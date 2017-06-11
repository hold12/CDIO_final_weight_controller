package db.dto;

import java.sql.Timestamp;

public class ProductBatchDTO {
	private int productbatchId;
	private Timestamp createdTime;
	private Timestamp finishedTime;
	private int status;
	private int recipeId;
	private int userId;

	public ProductBatchDTO(int productbatchId, Timestamp createdTime, Timestamp finishedTime, int status, int recipeId, int userId) {
		this.productbatchId = productbatchId;
		this.createdTime = createdTime;
		this.finishedTime = finishedTime;
		this.status = status;
		this.recipeId = recipeId;
		this.userId = userId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ProductBatchDTO that = (ProductBatchDTO) o;

		if (productbatchId != that.productbatchId) return false;
		if (createdTime != null ? !createdTime.equals(that.createdTime) : that.createdTime != null) return false;
		if (finishedTime != null ? !finishedTime.equals(that.finishedTime) : that.finishedTime != null) return false;
		if (status != that.status) return false;
		if (userId != that.userId) return false;
		return recipeId == that.recipeId;
	}
	
	public int getProductbatchId() { return productbatchId; }
	public void setProductbatchId(int productbatchId) { this.productbatchId = productbatchId; }
	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }
	public int getRecipeId() { return recipeId; }
	public void setUserId(int userId) { this.userId = userId; }
	public int getUserId() { return userId; }
	public void setRecipeId(int recipeId) { this.recipeId = recipeId; }
	public Timestamp getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}
	public Timestamp getFinishedTime() {
		return finishedTime;
	}
	public void setFinishedTime(Timestamp finishedTime) {
		this.finishedTime = finishedTime;
	}
	public String toString() { return productbatchId + "\t" + createdTime + "\t" + finishedTime + "\t" + status + "\t" + recipeId + "\t" + userId; }
}

