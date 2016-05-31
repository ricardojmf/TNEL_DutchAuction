package agents;

import java.util.ArrayList;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import logic.BuyerProduct;

public class Buyer extends Agent {
	
	public enum BuyerState {
		WAIT,
		THINK,
		ACT,
		END
	}
	
	public enum Behavior {
		NORMAL,
		SAFE,
		PATIENT,
		PANIC
	}
	
	private BuyerState state;
	private ArrayList<BuyerProduct> productsToBuy;
	private BuyerProduct productExample = new BuyerProduct("sardinhas", 3.0, 50, 20);
	
	private String name;
	private double moneyAtStart;
	private double currentMoney;
	private int productBeingBought;
	private int waitTurn;
	private Behavior behavior;
	
	public Buyer(String name, double moneyAtStart, ArrayList<BuyerProduct> productsToBuy, int waitTurn) {
		this.name = name;
		this.moneyAtStart = moneyAtStart;
		this.currentMoney = moneyAtStart;
		this.productsToBuy = productsToBuy;
		this.productBeingBought = -1;
	}
	
	protected void setup() {
		System.out.println("Starting buyer agent..." + getAID() + " " + getLocalName() + " (" + behavior+")");
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
		switch(behavior) {
			case NORMAL:
				addBehaviour(new NormalBuyingBehaviour(this));
				break;
			case SAFE:
				addBehaviour(new SafeBuyingBehaviour(this));
				break;
			case PATIENT:
				addBehaviour(new SafeBuyingBehaviour(this));
				break;
			case PANIC:
				addBehaviour(new SafeBuyingBehaviour(this));
				break;
			default:
				addBehaviour(new SafeBuyingBehaviour(this));
				break;
		}
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
	
	public boolean hasReachedCriticalTurn(int turn) {
		return turn <= waitTurn;
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

	public int getWaitTurn() {
		return waitTurn;
	}

	public void setWaitTurn(int waitTurn) {
		this.waitTurn = waitTurn;
	}

	public Behavior getBehavior() {
		return behavior;
	}

	public void setBehavior(String b) {
		
		b = b.toLowerCase();
		
		if(b.equals("safe")) {
			this.behavior = Behavior.SAFE;
		}
		else if(b.equals("patient")) {
			this.behavior = Behavior.PATIENT;
		}
		else if(b.equals("panic")) {
			this.behavior = Behavior.PANIC;
		}
		else {
			this.behavior = Behavior.NORMAL;
		}

	}

	public BuyerProduct getProductExample() {
		return productExample;
	}

	public void setProductExample(BuyerProduct productExample) {
		this.productExample = productExample;
	}

}