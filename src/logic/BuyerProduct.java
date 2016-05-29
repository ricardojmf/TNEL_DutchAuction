package logic;

public class BuyerProduct {
	private String name;
	private double valuation;	
	private double moneyAtStart;
	private double currentMoney;
	
	private int quantityToBuy;
	private int minimumQuantity;
	private int quantityBought;
	
	public BuyerProduct(String name, double valuation, double moneyAtStart, int quantityToBuy, int minimumQuantity) {
		this.name = name;
		this.valuation = valuation;
		this.moneyAtStart = moneyAtStart;
		this.currentMoney = moneyAtStart;
		this.quantityToBuy = quantityToBuy;
		this.minimumQuantity = minimumQuantity;
		this.quantityBought = 0;
	}
	
	public boolean buy(double price, int quantity) {
		if((currentMoney - price) >= 0) {			
			quantityBought -= quantity;
			currentMoney -= price;
			return true;
		}
		else {
			return false;
		}
	}
	
	public int getQuantityLeftToBuy() {
		return quantityToBuy - quantityBought;
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

	public double getMoneyAtStart() {
		return moneyAtStart;
	}

	public void setMoneyAtStart(double moneyAtStart) {
		this.moneyAtStart = moneyAtStart;
	}

	public double getCurrentMoney() {
		return currentMoney;
	}

	public void setCurrentMoney(double currentMoney) {
		this.currentMoney = currentMoney;
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
