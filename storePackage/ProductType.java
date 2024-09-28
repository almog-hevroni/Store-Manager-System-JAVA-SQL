package storePackage;

public enum ProductType {
	STORE("ST-","ORD_ST_"),
	WHOLESALERS("SW-","ORD_SW_"),
	WEB("WB-","ORD_WB_");

	private String prefixProductCode, prefixOrderCode;
	
	ProductType(String productCode, String orderCode) {
		this.prefixProductCode = productCode;
		this.prefixOrderCode = orderCode;
	}

	public String getPrefixProductCode() {
		return prefixProductCode;
	}

	public String getPrefixOrderCode() {
		return prefixOrderCode;
	}
	
}
