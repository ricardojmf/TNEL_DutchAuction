package agents;

import java.util.ArrayList;

import agents.Auctioneer.AucioneerState;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import logic.BuyerProduct;
import logic.Product;

public class Buyer extends Agent {
	
	public enum BuyerState {
		WAIT,
		THINK,
		ACT,
		END
	}
	
	private BuyerState state;
	private ArrayList<BuyerProduct> productsToBuy;
	private BuyerProduct productExample = new BuyerProduct("sardinhas", 3.0, 50, 20);
	
	private String name;
	private double moneyAtStart;
	private double currentMoney;
	private int productBeingBought;
	
	public Buyer(String name, double moneyAtStart, ArrayList<BuyerProduct> productsToBuy) {
		this.name = name;
		this.moneyAtStart = moneyAtStart;
		this.currentMoney = moneyAtStart;
		this.productsToBuy = productsToBuy;
		this.productBeingBought = -1;
	}
	
	protected void setup() {
		System.out.println("Starting buyer agent..." + getAID() + " " + getLocalName());
		state = BuyerState.WAIT;
		
		//register service
		ServiceDescription sd = new ServiceDescription();
		sd.setType("buyer");
		sd.setName(getLocalName());
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch(FIPAException fe) {
			fe.printStackTrace();
		}
				
		//add contract net behaviour 
		addBehaviour(new SafeBuyingBehaviour(this, MessageTemplate.MatchPerformative(ACLMessage.CFP)));
	}
	
	protected void takeDown() {
		System.out.println("Ending buyer agent..." + getAID() + " " + getLocalName());
		//deregister service
		try {
			DFService.deregister(this);  
		} catch(FIPAException e) {
			e.printStackTrace();
		}
	}

	public void replyBackToAgent(ACLMessage originalMsg, String message, int messageType) {
		ACLMessage msg = originalMsg.createReply();
		msg.setPerformative(messageType);
		if(!message.isEmpty()) {
			msg.setContent(message);
		}
		this.send(msg);
	}
	
	public BuyerProduct getProductBeingBought() {
		if(productBeingBought >= 0 && productBeingBought < productsToBuy.size()) {
			return productsToBuy.get(productBeingBought);
		}		
		
		return null;
	}
	
	public boolean buy(double price, int quantity) {
		if((currentMoney - price) >= 0 && productBeingBought >= 0) {			
			if(productsToBuy.get(productBeingBought).buy(quantity)) {
				currentMoney -= price;
				return true;
			}
			return false;			
		}
		return false;
	}
	
	public boolean canBuy(double price) {
		return (currentMoney - price) >= 0;
	}
	
	public boolean wantsToBuyItem(String name) {
		String productName;
		
		for(int i = 0; i < productsToBuy.size(); i++) {
			productName = productsToBuy.get(i).getName().toLowerCase();
			if(productName.equals(name.toLowerCase())) { //item matches
				if(productsToBuy.get(i).getQuantityLeftToBuy() > 0) { //we still want to buy it
					this.productBeingBought = i;
					return true;
				}			
			}
		}
		
		return false;
	}
	
	public void resetItemBeingBought() {
		this.productBeingBought = -1;
	}
	
	public BuyerState getBuyerState() {
		return state;
	}

	public void setBuyerState(BuyerState state) {
		this.state = state;
	}

	public ArrayList<BuyerProduct> getProductsToBuy() {
		return productsToBuy;
	}

	public void setProductsToBuy(ArrayList<BuyerProduct> productsToBuy) {
		this.productsToBuy = productsToBuy;
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

	public int getCurrentProductBeingBought() {
		return productBeingBought;
	}

	public void setCurrentProductBeingBought(int productBeingBought) {
		this.productBeingBought = productBeingBought;
	}

	public String getBuyerName() {
		return name;
	}

	public void setBuyerName(String name) {
		this.name = name;
	}

	public BuyerProduct getProductExample() {
		return productExample;
	}

	public void setProductExample(BuyerProduct productExample) {
		this.productExample = productExample;
	}

}