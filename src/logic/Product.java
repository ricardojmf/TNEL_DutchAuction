package logic;

public class Product {

	private String name;
	private double actualValue;
	private double increment;
	private double minAcceptablePrice;
	private double startPrice;
	private double currentPrice;
	
	private int quantityAtStart;
	private int quantityAvailable;
	
	public Product(String name, double actualValue, double startPrice, double increment, double minPrice, int quantity) {
		this.name = name;
		this.actualValue = actualValue;
		this.startPrice = startPrice;
		this.currentPrice = startPrice;
		this.increment = increment;
		this.minAcceptablePrice = minPrice;
		this.quantityAtStart = quantity;
		this.quantityAvailable = quantity;
	}
	
	public void reducePrice() {
		currentPrice = currentPrice - increment;
		
		if(currentPrice < 0)
			currentPrice = 0;
	}
	
	public boolean isAcceptablePrice() {
		return ((currentPrice - increment) >= minAcceptablePrice);
	}
	
	public boolean isPossibleToSell(int quantity) {
		return ((quantityAvailable - quantity) >= 0);
	}
	
	public void sell(int quantity) {
		quantityAvailable = quantityAvailable - quantity;
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

	public int getQuantityAtStart() {
		return quantityAtStart;
	}

	public void setQuantityAtStart(int quantityAtStart) {
		this.quantityAtStart = quantityAtStart;
	}

	public int getQuantityAvailable() {
		return quantityAvailable;
	}

	public void setQuantityAvailable(int quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}

	public double getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}
	
	
}
