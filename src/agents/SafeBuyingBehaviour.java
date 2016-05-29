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
public class SafeBuyingBehaviour extends SimpleBehaviour {

	public SafeBuyingBehaviour(Agent a, MessageTemplate mt) {
		super(a);
	}
	
	protected boolean done = false;
	
	@Override
	public void action() {
		switch(getBuyer().getBuyerState()) {
			case WAIT:
				waitForResponses();
				break;
			case THINK:
				break;
			case ACT:
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
				String[] product = msg.getContent().split(",");
				if(product[0].equals(getBuyer().getProductExample().getName())) {
					System.out.println(myAgent.getLocalName()+" it's a product I want to buy...");
					double price = Double.parseDouble(product[1]);
					int qt = Integer.parseInt(product[2]);
					if(price <= getBuyer().getProductExample().getValuation() && getBuyer().getProductExample().getQuantityLeftToBuy() > 0) {
						int toBuy = 0;
						if(qt >= getBuyer().getProductExample().getMinimumQuantity()) {
							toBuy = getBuyer().getProductExample().getMinimumQuantity();
						}
						else {
							toBuy = qt;
						}
						
						if(toBuy > getBuyer().getProductExample().getQuantityLeftToBuy()) {
							toBuy = getBuyer().getProductExample().getQuantityLeftToBuy();
						}
						
						System.out.println(myAgent.getLocalName()+" proposing a price...");
						getBuyer().replyBackToAgent(msg, ""+toBuy, ACLMessage.PROPOSE);
					}
				}
			}
			else if(msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
				String[] product = msg.getContent().split(",");
				double price = Double.parseDouble(product[0]);
				int quantity = Integer.parseInt(product[1]);
				System.out.println(myAgent.getLocalName()+" bought "+quantity+" for "+price);
				getBuyer().getProductExample().buy(price, quantity);
				if(getBuyer().getProductExample().getQuantityLeftToBuy() == 0) {
					getBuyer().setBuyerState(BuyerState.END);
				}
			}
		}		
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
