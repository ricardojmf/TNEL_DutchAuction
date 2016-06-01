package agents;

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
				nextItem();
				break;
			case END:
				end();
				break;
			case STOP:
				stopAuctioneer();
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
	 * Message in the format "fish name, price per kg, quantity in kg, turn number"
	 */
	protected void prepareCfps() {
		System.out.println("AUCTION sending messages");
		startTime = System.currentTimeMillis();
		DFAgentDescription[] buyers = getAuctioneer().getParticipants();
		int messageType = ACLMessage.CFP;
		Product p = getAuctioneer().getProductBeingSold();
		System.out.println("AUCTION Product " + p.getName() + " Round "+getAuctioneer().getCurrentTurn());
		String message = p.getName() + "," + p.getCurrentPrice() + "," + p.getQuantityAvailable() + "," + getAuctioneer().getHowManyTurnsLeft();
		getAuctioneer().sendMessageToAgents(buyers, message, messageType);
		getAuctioneer().setAucState(AucioneerState.WAITING_FOR_RESPONSES);
	}
	
	protected void end() {
		System.out.println("AUCTION ending - sending messages");
		DFAgentDescription[] buyers = getAuctioneer().getParticipants();
		int messageType = ACLMessage.INFORM;
		String message = "auction_end";
		getAuctioneer().sendMessageToAgents(buyers, message, messageType);
		getAuctioneer().setAucState(AucioneerState.STOP);
	}
	
	protected void stopAuctioneer() {
		System.out.println("Ending auction, stopping agent...");
		getAuctioneer().printInformation();
		done = true;
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
					String message = getAuctioneer().getProductBeingSold().getCurrentPrice()+","+unitsToSell;
					getAuctioneer().getProductBeingSold().insertNewAcceptedBid(getAuctioneer().getProductBeingSold().getCurrentPrice(), unitsToSell, msg.getSender().getLocalName());
					getAuctioneer().replyBackToAgent(msg, message, ACLMessage.ACCEPT_PROPOSAL);
				}
				else {
					int proposeOtherQuantity = getAuctioneer().getCurrentProductQuantityLeft();
					System.out.println("AUCTION rejecting proposal from "+msg.getSender().getLocalName()+" suggesting quantity "+proposeOtherQuantity);
					String message = getAuctioneer().getProductBeingSold().getCurrentPrice()+","+proposeOtherQuantity;
					getAuctioneer().getProductBeingSold().insertNewRejectedBid(getAuctioneer().getProductBeingSold().getCurrentPrice(), unitsToSell, msg.getSender().getLocalName());
					getAuctioneer().replyBackToAgent(msg, message, ACLMessage.REJECT_PROPOSAL);				
				}
			}
		}
		else {
			long elapsedTime = System.currentTimeMillis() - startTime;
			if(elapsedTime/1000F > 2.0) {
				getAuctioneer().getProductBeingSold().insertNewRejectedBid(getAuctioneer().getProductBeingSold().getCurrentPrice(), 0, "none");
				getAuctioneer().setAucState(AucioneerState.REDUCING_PRICE);
			}
		}		
	}
	
	protected void reducePrice() {
		System.out.println("Reducing price...");
		
		if(getAuctioneer().getProductBeingSold().getQuantityAvailable() == 0){
			getAuctioneer().setAucState(AucioneerState.NEXT_ITEM);
		}
		else if(getAuctioneer().reduceCurrentProductPrice()) {
			getAuctioneer().setAucState(AucioneerState.START_CN);
		}
		else {
			getAuctioneer().setAucState(AucioneerState.NEXT_ITEM);//next item
		}
	}

	protected void nextItem() {
		System.out.println("Next item..");
		int next = getAuctioneer().nextProduct();
		
		if(next < 0){
			getAuctioneer().setAucState(AucioneerState.END);//no more items, end auction
		}
		else {
			getAuctioneer().setAucState(AucioneerState.START_CN);
		}
	}
	
	@Override
	public boolean done() {
		return done;
	}

}
