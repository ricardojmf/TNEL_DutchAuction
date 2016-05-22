package logic;

import java.util.ArrayList;

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
	private ProductsLoader pl;
	private ArrayList<Product> products; // products in auction
	
	public Auction(boolean debugMode, String productsFilePath) {
		this.DEBUG_MODE = debugMode;
		pl = new ProductsLoader(productsFilePath);
		this.products = new ArrayList<Product>();
	}
	
	public void launchJade() {
		Runtime rt = Runtime.instance();
		Profile p1 = new ProfileImpl();
		container = rt.createMainContainer(p1);
	}
	
	//TODO
	public boolean initializeAuction() {	
		products = pl.loadProducts();
		
		if(products == null) {
			if(DEBUG_MODE) {
				System.out.println(pl.getError());
			}				
			else {
				System.out.println("Error launching Auction:\n " + pl.getError());
			}
			pl.setError(null);
			return false;
		}
		
		return true;
	}
	
	public void printProducts() {
		Product p;
		for(int i = 0; i < products.size(); i++) {
			p = products.get(i);
			System.out.println(i + ") " + p.getName() + " " + p.getActualValue() + " " + p.getStartPrice() + 
					" " + p.getIncrement() + " " + p.getMinPrice() + " " + p.getQuantityAtStart() + "\n");
		}
	}
	
	//TODO
	public boolean createBuyerAgents() {
		Object[] agentArgs = new Object[0];
		AgentController ac;
		
		for(int i = 0; i < 3; i++) {
			try {
				Buyer b = new Buyer();
				ac = container.acceptNewAgent("Buyer_" + i, b);
				ac.start();
			} catch (StaleProxyException e) {
				if(DEBUG_MODE) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}				
				else {
					System.out.println("Error launching Buyers.");
				}
					
				return false;
			}
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
				System.out.println(e.getMessage());
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