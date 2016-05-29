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
	private BuyerProduct productExample = new BuyerProduct("sardinhas", 3.0, 1000, 50, 20);
	
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

	public BuyerProduct getProductExample() {
		return productExample;
	}

	public void setProductExample(BuyerProduct productExample) {
		this.productExample = productExample;
	}

}