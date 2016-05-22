package cli;

import logic.Auction;

public class Main {
	
	public static void main(String[] args) {
		boolean debugMode = true;
		Auction auction = new Auction(debugMode, "products.map");
		auction.launchJade();
		auction.initializeAuction();
		auction.createBuyerAgents();
		try {
			System.out.println("Registering buyers...");
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Registering auctioneer...");
		auction.createAuctioneerAgent();	
		//auction.printProducts();
	}
	
}