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
	private ArrayList<Buyer> buyers; //buyers in auction
	
	public Auction(boolean debugMode, String productsFilePath, String agentsFilePath) {
		this.DEBUG_MODE = debugMode;
		pl = new ProductsLoader(productsFilePath, agentsFilePath);
		this.products = new ArrayList<Product>();
		this.buyers = new ArrayList<Buyer>();
	}
	
	public void launchJade() {
		Runtime rt = Runtime.instance();
		Profile p1 = new ProfileImpl();
		container = rt.createMainContainer(p1);
	}
	
	//TODO
	public boolean initializeAuction() {	
		products = pl.loadProducts();
		buyers = pl.loadAgents();
		
		if(products == null || buyers == null) {
			if(DEBUG_MODE) {
				System.out.println("error"+pl.getError());
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
		System.out.println("\tPRODUCTS");
		Product p;
		for(int i = 0; i < products.size(); i++) {
			p = products.get(i);
			System.out.println(i + ") " + p.getName() + " " + p.getActualValue() + " " + p.getStartPrice() + 
					" " + p.getIncrement() + " " + p.getMinPrice() + " " + p.getQuantityAtStart());
		}
	}
	
	public void printBuyers() {
		System.out.println("\tBUYERS");
		Buyer b;
		for(int i = 0; i < buyers.size(); i++) {
			BuyerProduct bp;
			b = buyers.get(i);
			System.out.println(i + ") " + b.getBuyerName() + " Money: " + b.getMoneyAtStart() + "\nProducts:");
			for(int j = 0; j < b.getProductsToBuy().size(); j++) {
				bp = b.getProductsToBuy().get(j);
				System.out.println("\t "+bp.getName()+ " "+bp.getValuation()+" "+bp.getQuantityToBuy()+" "+bp.getMinimumQuantity());
			}						
		}
	}
	
	//TODO
	public boolean launchBuyerAgents() {
		Object[] agentArgs = new Object[0];
		AgentController ac;
		
		for(int i = 0; i < buyers.size(); i++) {
			try {
				Buyer b = buyers.get(i);
				ac = container.acceptNewAgent(b.getBuyerName(), b);
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
			Auctioneer a = new Auctioneer(products);
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

	public ArrayList<Buyer> getBuyers() {
		return buyers;
	}

	public void setBuyers(ArrayList<Buyer> buyers) {
		this.buyers = buyers;
	}
}