package cli;

public class Main {
	
	public static void main(String[] args) {
		boolean debugMode = true;
		Auction auction = new Auction(debugMode);
		auction.launchJade();
		auction.createAuctioneerAgent();
		auction.createBuyerAgents();
	}
	
}