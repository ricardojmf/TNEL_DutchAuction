package agents;

import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import logic.Product;

public class Auctioneer extends Agent {
	
	public enum AucioneerState {
		START_CN, /* Starting the contract net - initial message */
		WAITING_FOR_RESPONSES,
		SELLING,
		REDUCING_PRICE,
		NEXT_ITEM,
		END
	}
	
	private AucioneerState state;
	private DFAgentDescription[] participants;
	private ArrayList<Product> productsToSell;
	private Product productExample = new Product("sardinhas", 2.0, 4.0, 0.5, 2.5, 200);
	
	protected void setup() {
		System.out.println("Starting auctioneer agent..." + getAID() + " " + getLocalName());
		state = AucioneerState.START_CN;
		initialize();
	}
	
	protected void takeDown() {
		System.out.println("Ending auctioneer agent..." + getAID() + " " + getLocalName());
		state = AucioneerState.END;
	}
	
	public void initialize() {
	
		//add contract net behaviour 
		addBehaviour(new AuctioneerBehaviour(this));

	}

	public DFAgentDescription[] getParticipants() {
		DFAgentDescription[] result = null;
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("buyer");
		dfd.addServices(sd);
		
		try {
			result = DFService.search(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		participants = result;
		
		return participants;
	}
	
	public void replyBackToAgent(ACLMessage originalMsg, String message, int messageType) {
		ACLMessage msg = originalMsg.createReply();
		msg.setPerformative(messageType);
		if(!message.isEmpty()) {
			msg.setContent(message);
		}
		this.send(msg);
	}
	
	public void sendMessageToAgents(DFAgentDescription[] receivers, String message, int messageType) {
		ACLMessage msg = new ACLMessage(messageType);		
		for(DFAgentDescription receiver : receivers) {
			msg.addReceiver(new AID(receiver.getName().getLocalName(), false));
		}
		//msg.setLanguage("PT");
		msg.setContent(message);
		this.send(msg);
	}
	
	public boolean canSellCurrentProduct(int quantity) {
		return productExample.isPossibleToSell(quantity);
	}
	
	public void sellCurrentProduct(int quantity) {
		productExample.sell(quantity);
	}
	
	public boolean reduceCurrentProductPrice() {
		if(productExample.isAcceptablePrice()) {
			productExample.reducePrice();
			return true;
		}
		return false;
	}

	public void setParticipants(DFAgentDescription[] participants) {
		this.participants = participants;
	}
	

	public AucioneerState getAucState() {
		return state;
	}

	public void setAucState(AucioneerState state) {
		this.state = state;
	}

	public Product getProductExample() {
		return productExample;
	}
	
	public ArrayList<Product> getProductsToSell() {
		return productsToSell;
	}

	public void setProductsToSell(ArrayList<Product> productsToSell) {
		this.productsToSell = productsToSell;
	}

}