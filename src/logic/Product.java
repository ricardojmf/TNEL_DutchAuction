package logic;

public class Product {

	private String name;
	private double actualValue;
	private double startPrice;
	private double increment;
	private double minAcceptablePrice;
	
	public Product(String name, double actualValue, double startPrice, double increment, double minPrice) {
		this.name = name;
		this.actualValue = actualValue;
		this.startPrice = startPrice;
		this.increment = increment;
		this.minAcceptablePrice = minPrice;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getActualValue() {
		return actualValue;
	}
	public void setActualValue(double actualValue) {
		this.actualValue = actualValue;
	}
	public double getStartPrice() {
		return startPrice;
	}
	public void setStartPrice(double startPrice) {
		this.startPrice = startPrice;
	}
	public double getIncrement() {
		return increment;
	}
	public void setIncrement(double increment) {
		this.increment = increment;
	}
	public double getMinPrice() {
		return minAcceptablePrice;
	}
	public void setMinPrice(double minPrice) {
		this.minAcceptablePrice = minPrice;
	}
	
	
}
