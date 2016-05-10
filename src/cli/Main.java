package cli;

import logic.Auction;

public class Main {
	
	public static void main(String[] args) {
		boolean debugMode = true;
		Auction auction = new Auction(debugMode, "products.map");
		auction.launchJade();
		auction.createAuctioneerAgent();
		auction.createBuyerAgents();
		auction.initializeAuction();
		auction.printProducts();		
	}
	
}