package agents;

import agents.Buyer.BuyerState;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/*
 * "as the price decreases, when it reaches your valuation, buy the minimum quantity. 
 * Keep watching the auction, and before it closes, buy the remaining items 
 * (possibly at a smaller price that your valuation)."
*/
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
				System.out.println(myAgent.getLocalName()+" ending buying...");
				done = true;
				break;
			default:
				break;
		}		
	}
	
	protected void waitForResponses() {
		ACLMessage msg = myAgent.receive();
		
		if(msg != null) {
			if(msg.getPerformative() == ACLMessage.CFP) {
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
			else if(msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
				String[] product = msg.getContent().split(",");
				double price = Double.parseDouble(product[0]);
				int quantity = Integer.parseInt(product[1]);
				System.out.println(myAgent.getLocalName()+" bought "+getBuyer().getProductBeingBought().getName()+ " : "+quantity+" units for "+price);
				getBuyer().buy(price, quantity);
				if(getBuyer().getProductBeingBought().getQuantityLeftToBuy() == 0) {
					getBuyer().setCurrentProductBeingBought(-1);
				}
			}
			else if(msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {

			}
			else if(msg.getPerformative() == ACLMessage.INFORM) {
				//auction end
				String information = msg.getContent().toLowerCase();
				if(information.equals("auction_end")) {
					getBuyer().setBuyerState(BuyerState.END);
				}
			}
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
