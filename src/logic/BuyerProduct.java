package logic;

import java.util.HashMap;
import java.util.Map.Entry;

public class BuyerProduct {
	private String name;
	private double valuation;
	private double initialValuation;
	
	private int quantityToBuy;
	private int minimumQuantity;
	private int quantityBought;
	
	private HashMap<Double,Integer> acceptedBidsRecord;
	private HashMap<Double,Integer> rejectedBidsRecord;
	
	public BuyerProduct(String name, double valuation, int quantityToBuy, int minimumQuantity) {
		this.name = name;
		this.valuation = valuation;
		this.initialValuation = valuation;
		this.quantityToBuy = quantityToBuy;
		this.minimumQuantity = minimumQuantity;
		this.quantityBought = 0;
		this.acceptedBidsRecord = new HashMap<Double,Integer>();
		this.rejectedBidsRecord = new HashMap<Double,Integer>();
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

	public double getInitialValuation() {
		return initialValuation;
	}

	public void setInitialValuation(double initialValuation) {
		this.initialValuation = initialValuation;
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
	
	public void insertNewAcceptedBid(double price, int quantity) {
		acceptedBidsRecord.put(price, quantity);
	}
	
	public void insertNewRejectedBid(double price, int quantity) {
		rejectedBidsRecord.put(price, quantity);
	}
	
	public HashMap<Double,Integer> getAcceptedBids() {
		return acceptedBidsRecord;
	}
	
	public HashMap<Double,Integer> getRejectedBids() {
		return rejectedBidsRecord;
	}
		
	public int getTotalAmountOfAcceptedBids() {
		return acceptedBidsRecord.size();
	}
	
	public int getTotalAmountOfRejectedBids() {
		return rejectedBidsRecord.size();
	}
	
	public int getTotalAmountOfBids() {
		return acceptedBidsRecord.size() + rejectedBidsRecord.size();
	}
	
	public String printFinalInformation() {
		StringBuilder sb = new StringBuilder();
		sb.append("===\r\nProduct name: " + name.toLowerCase()+"\r\n");
		sb.append("Quantity bought: " + quantityBought + " units out of " + quantityToBuy + " total units."+"\r\n");
		sb.append("Initial valuation: " + initialValuation +"\r\n");
		sb.append("Final valuation: " + valuation +"\r\n");
		sb.append("# Accepted bids: " + getTotalAmountOfAcceptedBids() +"\r\n");
		sb.append("# Rejected bids: " + getTotalAmountOfRejectedBids() +"\r\n");
		sb.append("# Total bids: " + getTotalAmountOfBids() +"\r\n");
		
		sb.append("Accepted Bids: \r\n");
		if(acceptedBidsRecord.size() == 0)
			sb.append("\t No accepted bids. \r\n");
		
		int i = 0;
		for(Entry e : acceptedBidsRecord.entrySet()) {
			double price = (double)e.getKey();
			int quantity = (int)e.getValue();
			sb.append("\t Bid #"+i+": Units - "+quantity+" / Price - "+price+"\r\n");
			i++;
		}
		
		sb.append("Rejected Bids: \r\n");
		
		if(rejectedBidsRecord.size() == 0)
			sb.append("\t No rejected bids. \r\n");
		
		i = 0;
		for(Entry e : rejectedBidsRecord.entrySet()) {
			double price = (double)e.getKey();
			int quantity = (int)e.getValue();
			sb.append("\t Bid #"+i+": Units - "+quantity+" / Price - "+price+"\r\n");
			i++;
		}
		
		return sb.toString();
	}
}
