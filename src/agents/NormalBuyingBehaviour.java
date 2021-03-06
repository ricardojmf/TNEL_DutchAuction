package agents;

import agents.Buyer.BuyerState;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class NormalBuyingBehaviour extends SimpleBehaviour {

	public NormalBuyingBehaviour(Agent a) {
		super(a);
	}
	
	protected boolean done = false;
	
	@Override
	public void action() {
		switch(getBuyer().getBuyerState()) {
			case WAIT:
				waitForResponses();
				break;
			case END:
				end();
				break;
			default:
				break;
		}		
	}
	
	protected void waitForResponses() {
		ACLMessage msg = myAgent.receive();
		
		if(msg != null) {
			if(msg.getPerformative() == ACLMessage.CFP) {
				dealWithCfp(msg);
			}
			else if(msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
				dealWithAccept(msg);
			}
			else if(msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
				dealWithReject(msg);
			}
			else if(msg.getPerformative() == ACLMessage.INFORM) {
				dealWithInform(msg);
			}
		}		
	}
	
	protected void dealWithCfp(ACLMessage msg) {
		System.out.println(myAgent.getLocalName()+" got a product message...");
		
		/* Format - ProductName, ProductPrice, ProductQuantity */
		String[] product = msg.getContent().split(",");
		String pname = product[0];
		
		if(getBuyer().currentlyBuyingItem(pname) || getBuyer().wantsToBuyItem(pname)) {					
			System.out.println(myAgent.getLocalName()+" it's a product ("+pname+") I want to buy...");
			
			double price = Double.parseDouble(product[1]);
			int qtt = Integer.parseInt(product[2]);
			int howMany = decideToBuy(price, qtt);
			System.out.println(myAgent.getLocalName()+" how many i will buy: "+howMany+ " price: "+ price+" quantity: "+qtt);
			
			if(howMany > 0) {			
				System.out.println(myAgent.getLocalName()+" proposing a quantity for item "+pname+" : "+howMany+" out of "+qtt+" total being sold.");
				getBuyer().replyBackToAgent(msg, ""+howMany, ACLMessage.PROPOSE);
			}
		}
	}
	
	protected void dealWithInform(ACLMessage msg) {
		//auction end
		String information = msg.getContent().toLowerCase();
		if(information.equals("auction_end")) {
			getBuyer().setBuyerState(BuyerState.END);
		}
	}

	protected void dealWithAccept(ACLMessage msg) {
		String[] product = msg.getContent().split(",");
		double price = Double.parseDouble(product[0]);
		int quantity = Integer.parseInt(product[1]);
		System.out.println(myAgent.getLocalName()+" bought "+getBuyer().getProductBeingBought().getName()+ " : "+quantity+" units for "+price);
		getBuyer().buy(price, quantity);
		getBuyer().getProductBeingBought().insertNewAcceptedBid(price, quantity);
		if(getBuyer().getProductBeingBought().getQuantityLeftToBuy() == 0) {
			getBuyer().setCurrentProductBeingBought(-1);
		}
	}
	
	protected void dealWithReject(ACLMessage msg) {
		String[] product = msg.getContent().split(",");
		double price = Double.parseDouble(product[0]);
		int quantity = Integer.parseInt(product[1]);
		
		if(quantity != 0 && thinkAcceptNewQuantity(quantity)) {
			System.out.println(myAgent.getLocalName()+" proposing a quantity for item : "+quantity+" out of "+quantity+" total being suggested.");
			getBuyer().getProductBeingBought().insertNewAcceptedBid(price, quantity);
			getBuyer().replyBackToAgent(msg, ""+quantity, ACLMessage.PROPOSE);
		}
		else {
			getBuyer().getProductBeingBought().insertNewRejectedBid(price, quantity);
		}
	}
	
	/**
	 * Decides if it wants to propose a new value, if the auctioneer isn't selling enough units anymore
	 */
	protected boolean thinkAcceptNewQuantity(int quantity) {
		switch(getBuyer().getProposalBehavior()){
			case NEVER:
				return false;
			case ALWAYS:
				return true;
			case MINIMUM:
				if(getBuyer().getProductBeingBought().obtainedMinimumQuantity())
					return false;
				return true;
			default:
				return false;
		}
	}
	
	protected int decideToBuy(double price, int quantity) {
		int toBuy = 0;
		
		if(!getBuyer().canBuy(price))
			return 0;
		
		if(!getBuyer().wantsToBuy())
			return 0;
		
		if(getBuyer().getProductBeingBought().obtainedMinimumQuantity()) {
			if(quantity >= getBuyer().getProductBeingBought().getQuantityLeftToBuy()) {
				toBuy = getBuyer().getProductBeingBought().getQuantityLeftToBuy();
			}
			else {
				toBuy = quantity;
			}
			
			if(toBuy > getBuyer().getProductBeingBought().getQuantityLeftToBuy()) {
				toBuy = getBuyer().getProductBeingBought().getQuantityLeftToBuy();
			}
			
			if(toBuy < 0) {
				toBuy = 0;
			}
		}
		else {
			if(price <= getBuyer().getProductBeingBought().getValuation()) {
				
				if(quantity >= getBuyer().getProductBeingBought().getMinimumQuantity()) {
					toBuy = getBuyer().getProductBeingBought().getMinimumQuantity();
				}
				else {
					toBuy = quantity;
				}
				
				if((toBuy + getBuyer().getProductBeingBought().getQuantityBought()) >= getBuyer().getProductBeingBought().getMinimumQuantity()) { 
					toBuy = getBuyer().getProductBeingBought().getMinimumQuantity() - getBuyer().getProductBeingBought().getQuantityBought();
				}
				
				if(toBuy > getBuyer().getProductBeingBought().getQuantityLeftToBuy()) {
					toBuy = getBuyer().getProductBeingBought().getQuantityLeftToBuy();
				}
				
				if(toBuy < 0) {
					toBuy = 0;
				}
			}
		}
		
		return toBuy;		
	}
	
	public void end() {
		System.out.println(myAgent.getLocalName()+" ending buying...");
		getBuyer().printInformation();
		done = true;
	}
	
	/*
	 * Returns myAgent casted to Buyer
	 */
	protected Buyer getBuyer() {
		return ((Buyer) myAgent);
	}

	@Override
	public boolean done() {
		return done;
	}

}
