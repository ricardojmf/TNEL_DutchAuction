package cli;

import agents.Auctioneer;
import agents.Buyer;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Auction {
	
	private ContainerController container;
	private boolean DEBUG_MODE = true;
	
	public Auction(boolean debugMode) {
		this.DEBUG_MODE = debugMode;
	}
	
	public void launchJade() {
		Runtime rt = Runtime.instance();
		Profile p1 = new ProfileImpl();
		container = rt.createMainContainer(p1);
	}
	
	//TODO
	public boolean createBuyerAgents() {
		Object[] agentArgs = new Object[0];
		AgentController ac;
		try {
			Buyer b = new Buyer();
			ac = container.acceptNewAgent("Buyer_" + 1, b);
			ac.start();
		} catch (StaleProxyException e) {
			if(DEBUG_MODE) {
				e.printStackTrace();
			}				
			else {
				System.out.println("Error launching Buyers.");
			}
				
			return false;
		}
		
		return true;
	}
	
	//TODO
	public boolean createAuctioneerAgent() {
		Object[] agentArgs = new Object[0];
		AgentController ac;
		try {
			Auctioneer a = new Auctioneer();
			ac = container.acceptNewAgent("Auctioneer", a);
			ac.start();
		} catch (StaleProxyException e) {
			if(DEBUG_MODE) {
				e.printStackTrace();
			}				
			else {
				System.out.println("Error launching Buyers.");
			}
				
			return false;
		}
		
		return true;
	}
}