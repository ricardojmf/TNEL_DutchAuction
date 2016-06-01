package agents;

import agents.Buyer.BuyerState;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/*
 * "the following "patient buying" tactic: "if the price drops fast, the buying rate is low,
 * and you reach your valuation, then lower your valuation". This tactic might save the buyer 
 * from the winner’s curse as it helps get the items at a lower price. There is however a risk 
 * that someone else buys all the items before the agent makes its move."
*/
public class PatientBuyingBehaviour extends SimpleBehaviour {

	public PatientBuyingBehaviour(Agent a) {
		super(a);
	}
	
	protected boolean done = false;
	protected int initialQuantity;
	protected double initialPrice;
	
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
		
		/* Format - ProductName, ProductPrice, ProductQuantity, TurnsLeft */
		String[] product = msg.getContent().split(",");
		String pname = product[0];
		
		//currently buying
		if(getBuyer().currentlyBuyingItem(pname)) {
			System.out.println(myAgent.getLocalName()+" it's still a product ("+pname+") I want to buy...");
			
			double price = Double.parseDouble(product[1]);
			int qtt = Integer.parseInt(product[2]);
			int turnsLeft = Integer.parseInt(product[3]);
			think(price, qtt, turnsLeft);
			int howMany = decideToBuy(price, qtt, turnsLeft);
			System.out.println(myAgent.getLocalName()+" how many i will buy: "+howMany+ " price: "+ price+" quantity: "+qtt);
			
			if(howMany > 0) {			
				System.out.println(myAgent.getLocalName()+" proposing a quantity for item "+pname+" : "+howMany+" out of "+qtt+" total being sold.");
				getBuyer().replyBackToAgent(msg, ""+howMany, ACLMessage.PROPOSE);
			}
		}
		//not currently buying, but want to buy
		else if(getBuyer().wantsToBuyItem(pname)) {
			System.out.println(myAgent.getLocalName()+" it's a new product ("+pname+") I want to buy...");
			
			double price = Double.parseDouble(product[1]);
			int qtt = Integer.parseInt(product[2]);
			int turnsLeft = Integer.parseInt(product[3]);
			this.initialPrice = price;
			this.initialQuantity = qtt;
			
			int howMany = decideToBuy(price, qtt, turnsLeft);
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
		
		System.out.println(myAgent.getLocalName()+" got a reject : "+quantity+" is being suggested.");
		
		if(quantity != 0 && thinkAcceptNewQuantity(quantity)) {
			System.out.println(myAgent.getLocalName()+" proposing a quantity suggested : "+quantity);
			getBuyer().replyBackToAgent(msg, ""+quantity, ACLMessage.PROPOSE);
		}
		else {
			getBuyer().getProductBeingBought().insertNewRejectedBid(price, quantity);
		}
	}
	
	/**
	 * Update valuation, based on current price and how quickly people are buying items
	 */
	protected void think(double price, int quantity, int turnsLeft) {
		
		if(getBuyer().hasReachedCriticalTurn(turnsLeft)) {
			return;
		}
		
		if(price <= getBuyer().getProductBeingBought().getValuation()) {
			double quantitydrop = initialQuantity - (getBuyer().getPercentageQuantityDrop() * initialQuantity);
			quantitydrop = Math.round(quantitydrop * 100.0) / 100.0;
			System.out.println("QUANTITY: "+initialQuantity+" "+quantitydrop);
			if(quantity >= quantitydrop) {
				double newvaluation = price - (getBuyer().getPercentageValuationDrop() * price);
				newvaluation = Math.round(newvaluation * 100.0) / 100.0;
				System.out.println(myAgent.getLocalName()+" reduced valuation of item from "+getBuyer().getProductBeingBought().getValuation() + " to " + newvaluation);
				getBuyer().getProductBeingBought().setValuation(newvaluation);
			}
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
	
	protected int decideToBuy(double price, int quantity, int turnsLeft) {
		int toBuy = 0;
		
		if(!getBuyer().canBuy(price))
			return 0;
		
		if(price <= getBuyer().getProductBeingBought().getValuation()) {
			if(quantity >= getBuyer().getProductBeingBought().getQuantityLeftToBuy()) {
				toBuy = getBuyer().getProductBeingBought().getQuantityLeftToBuy();
			}
			else {
				toBuy = quantity;
			}
		}	
		
		if(toBuy < 0) {
			toBuy = 0;
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