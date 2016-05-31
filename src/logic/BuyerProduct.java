package logic;

public class BuyerProduct {
	private String name;
	private double valuation;
	
	private int quantityToBuy;
	private int minimumQuantity;
	private int quantityBought;
	
	public BuyerProduct(String name, double valuation, int quantityToBuy, int minimumQuantity) {
		this.name = name;
		this.valuation = valuation;
		this.quantityToBuy = quantityToBuy;
		this.minimumQuantity = minimumQuantity;
		this.quantityBought = 0;
	}
	
	public boolean buy(int quantity) {
		if((quantityBought + quantity) <= quantityToBuy) {			
			quantityBought += quantity;
			return true;
		}
		else {
			return false;
		}
	}
	
	public int getQuantityLeftToBuy() {
		return quantityToBuy - quantityBought;
	}
	
	public boolean obtainedMinimumQuantity() {
		return quantityBought >= minimumQuantity;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getValuation() {
		return valuation;
	}

	public void setValuation(double valuation) {
		this.valuation = valuation;
	}

	public int getQuantityToBuy() {
		return quantityToBuy;
	}

	public void setQuantityToBuy(int quantityToBuy) {
		this.quantityToBuy = quantityToBuy;
	}

	public int getMinimumQuantity() {
		return minimumQuantity;
	}

	public void setMinimumQuantity(int minimumQuantity) {
		this.minimumQuantity = minimumQuantity;
	}

	public int getQuantityBought() {
		return quantityBought;
	}

	public void setQuantityBought(int quantityBought) {
		this.quantityBought = quantityBought;
	}
}
