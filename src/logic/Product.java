package logic;

import java.util.HashMap;
import java.util.Map.Entry;

public class Product {

	private String name;
	private double actualValue;
	private double increment;
	private double minAcceptablePrice;
	private double startPrice;
	private double currentPrice;
	
	private int quantityAtStart;
	private int quantityAvailable;
	
	private HashMap<Double,Bid> acceptedBidsRecord;	
	private HashMap<Double,Bid> rejectedBidsRecord;
	
	public Product(String name, double actualValue, double startPrice, double increment, double minPrice, int quantity) {
		this.name = name;
		this.actualValue = actualValue;
		this.startPrice = startPrice;
		this.currentPrice = startPrice;
		this.increment = increment;
		this.minAcceptablePrice = minPrice;
		this.quantityAtStart = quantity;
		this.quantityAvailable = quantity;
		this.acceptedBidsRecord = new HashMap<Double,Bid>();
		this.rejectedBidsRecord = new HashMap<Double,Bid>();
	}
	
	public void reducePrice() {
		currentPrice = Math.round((currentPrice - increment) * 100.0) / 100.0;
		
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
	
	public void insertNewAcceptedBid(double price, int quantity, String buyer) {
		acceptedBidsRecord.put(price, new Bid(quantity, buyer));
	}
	
	public void insertNewRejectedBid(double price, int quantity, String buyer) {
		rejectedBidsRecord.put(price, new Bid(quantity, buyer));
	}
	
	public HashMap<Double,Bid> getAcceptedBids() {
		return acceptedBidsRecord;
	}
	
	public HashMap<Double,Bid> getRejectedBids() {
		return rejectedBidsRecord;
	}
	
	public HashMap<Double,Integer> getFilteredRejectedBids() {
		HashMap<Double,Integer> bids = new HashMap<Double,Integer>();
		
		for(Entry e : rejectedBidsRecord.entrySet()) {
			if(!acceptedBidsRecord.containsKey(e.getKey())) {
				bids.put((double)e.getKey(), ((Bid)e.getValue()).getQuantity());
			}
		}
		
		return bids;
	}
	
	public HashMap<Double,Integer> getBidsByAgent(String buyer) {
		HashMap<Double,Integer> bids = new HashMap<Double,Integer>();
		buyer = buyer.toLowerCase();
		for(Entry e : acceptedBidsRecord.entrySet()) {
			if(((Bid)e.getValue()).getBuyer().toLowerCase().equals(buyer)) {
				bids.put((double)e.getKey(), ((Bid)e.getValue()).getQuantity());
			}
		}
		
		for(Entry e : rejectedBidsRecord.entrySet()) {
			if(((Bid)e.getValue()).getBuyer().toLowerCase().equals(buyer)) {
				bids.put((double)e.getKey(), ((Bid)e.getValue()).getQuantity());
			}
		}
		
		return bids;
	}
	
	public double getEarnings() {
		double earnings = 0;
		
		for(Entry e : acceptedBidsRecord.entrySet()) {
			double price = (double)e.getKey();
			int quantity = ((Bid)e.getValue()).getQuantity();
			earnings += price * quantity;
		}
		
		return (earnings * 100) / 100;
	}
	
	public int getTotalAmountOfAcceptedBids() {
		return acceptedBidsRecord.size();
	}
	
	public int getTotalAmountOfRejectedBids() {
		int rej = 0;
		
		for(Entry e : rejectedBidsRecord.entrySet()) {
			if(((Bid)e.getValue()).getQuantity() > 0)
				rej++;
		}
		
		return rej;
	}
	
	public int getTotalAmountOfBids(int rej) {
		return acceptedBidsRecord.size() + rej;
	}
	
	public String printFinalInformation() {
		StringBuilder sb = new StringBuilder();
		sb.append("===\r\nProduct name: " + name.toLowerCase());
		sb.append("\r\nQuantity sold: " + (quantityAtStart - quantityAvailable) + " units out of " + quantityAtStart + " total units."+"\n");
		sb.append("\r\nInitial Price: " + startPrice);
		sb.append("\r\n# Accepted bids: " + getTotalAmountOfAcceptedBids());
		int rej = getTotalAmountOfRejectedBids();
		sb.append("\r\n# Rejected bids: " + rej);
		sb.append("\r\n# Total bids: " + getTotalAmountOfBids(rej));
		sb.append("\r\nEarnings: " + getEarnings() +"\r\n");
		
		sb.append("Accepted Bids: \r\n");
		if(acceptedBidsRecord.size() == 0)
			sb.append("\t No accepted bids. \r\n");
		
		int i = 0;
		for(Entry e : acceptedBidsRecord.entrySet()) {
			double price = (double)e.getKey();
			int quantity = ((Bid)e.getValue()).getQuantity();
			String buyer = ((Bid)e.getValue()).getBuyer();
			sb.append("\t Bid #"+i+": Units - "+quantity+" / Price - "+price+" / Buyer - "+buyer+"\r\n");
			i++;
		}
		
		sb.append("Rejected Bids: \r\n");
		
		if(rejectedBidsRecord.size() == 0)
			sb.append("\t No rejected bids. \r\n");
		
		i = 0;
		for(Entry e : rejectedBidsRecord.entrySet()) {
			double price = (double)e.getKey();
			int quantity = ((Bid)e.getValue()).getQuantity();
			String buyer = ((Bid)e.getValue()).getBuyer();
			sb.append("\t Bid #"+i+": Units - "+quantity+" / Price - "+price+" / Buyer - "+buyer+"\r\n");
			i++;
		}
		
		sb.append("===\n");
		return sb.toString();
	}
}
