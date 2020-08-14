
public class ProductDTO {
	
	private String productCode;
	private String productImgPath;
	private String productName;
	private String productPrice;
	private String productCategory;
	private String productDescription;
	private int productImageLength;
	
	public ProductDTO() {}

	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductImgPath() {
		return productImgPath;
	}
	public void setProductImgPath(String productImgPath) {
		this.productImgPath = productImgPath;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}
	public int getProductImageLength() {
		return productImageLength;
	}

	public void setProductImageLength(int productImageLength) {
		this.productImageLength = productImageLength;
	}

	public String getQuery() {
		StringBuilder builder = new StringBuilder();
		builder.append("INSERT INTO matchesfashion_item (MF_ITEM_NAME, MF_ITEM_IMGS, MF_ITEM_DESCRIPTION, MF_ITEM_PRICE, MF_ITEM_CATEGORY, MF_ITEM_CODE, MF_ITEM_IMAGE_LENGTH) VALUES (");
		builder.append("\""+getProductName()+"\", ");
		builder.append("\""+getProductImgPath()+"\", ");
		builder.append("\""+getProductDescription()+"\", ");
		builder.append("\""+getProductPrice()+"\", ");
		builder.append("\""+getProductCategory()+"\", ");
		builder.append("\""+getProductCode()+"\", ");
		builder.append(getProductImageLength()+");");
		return builder.toString();
	}
}
