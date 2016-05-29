package agents;

import java.util.Vector;

import agents.Auctioneer.AucioneerState;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import logic.Product;

public class AuctioneerBehaviour extends SimpleBehaviour {
	
	long startTime;
	protected boolean done = false;
	
	public AuctioneerBehaviour(Agent a) {
		super(a);
	}
	
	@Override
	public void action() {
		switch(getAuctioneer().getAucState()) {
			case START_CN:
				prepareCfps();
				break;
			case WAITING_FOR_RESPONSES:
				waitForResponses();
				break;
			case SELLING:
				break;
			case REDUCING_PRICE:
				reducePrice();
				break;
			case NEXT_ITEM:
				break;
			case END:
				System.out.println("Ending auction...");
				done = true;
				break;
			default:
				break;
		}
		
	}
	
	/*
	 * Returns myAgent casted to Auctioneer
	 */
	protected Auctioneer getAuctioneer() {
		return ((Auctioneer) myAgent);
	}
	
	/*
	 * Sends the initial message to sell an item
	 * Message in the format "fish name, price per kg, quantity in kg"
	 */
	protected void prepareCfps() {
		System.out.println("AUCTION sending messages");
		startTime = System.currentTimeMillis();
		DFAgentDescription[] buyers = getAuctioneer().getParticipants();
		int messageType = ACLMessage.CFP;
		Product p = getAuctioneer().getProductExample();
		String message = p.getName() + "," + p.getCurrentPrice() + "," + p.getQuantityAvailable();
		getAuctioneer().sendMessageToAgents(buyers, message, messageType);
		getAuctioneer().setAucState(AucioneerState.WAITING_FOR_RESPONSES);
	}
	
	protected void waitForResponses() {
		ACLMessage msg = myAgent.receive();
		if(msg != null) {
			System.out.println("AUCTION got a message...");
			if(msg.getPerformative() == ACLMessage.PROPOSE) {
				int unitsToSell = Integer.parseInt(msg.getContent());//.split(",");
				System.out.println("AUCTION got a propose from "+msg.getSender().getLocalName()+" for "+unitsToSell+" units");
				if(getAuctioneer().canSellCurrentProduct(unitsToSell)) {
					System.out.println("AUCTION accepting proposal from "+msg.getSender().getLocalName()+" for "+unitsToSell+" units");
					getAuctioneer().sellCurrentProduct(unitsToSell);
					String message = getAuctioneer().getProductExample().getCurrentPrice()+","+unitsToSell;
					getAuctioneer().replyBackToAgent(msg, message, ACLMessage.ACCEPT_PROPOSAL);
				}
				else {
					System.out.println("AUCTION rejecting proposal from "+msg.getSender().getLocalName());
					getAuctioneer().replyBackToAgent(msg, "", ACLMessage.REJECT_PROPOSAL);
				}
			}						
		}
		else {
			long elapsedTime = System.currentTimeMillis() - startTime;
			if(elapsedTime/1000F > 5.0) {
				getAuctioneer().setAucState(AucioneerState.REDUCING_PRICE);
			}
		}		
	}
	
	protected void reducePrice() {
		System.out.println("Reducing price...");
		if(getAuctioneer().reduceCurrentProductPrice()) {
			getAuctioneer().setAucState(AucioneerState.START_CN);
		}
		else {
			getAuctioneer().setAucState(AucioneerState.END);//next item
		}
	}

	@Override
	public boolean done() {
		return done;
	}

}
