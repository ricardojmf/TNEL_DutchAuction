package agents;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Auctioneer extends Agent {
	
	private DFAgentDescription[] participants;
	
	protected void setup() {
		System.out.println("Starting auctioneer agent..." + getAID() + " " + getLocalName());
		initialize();
	}
	
	protected void takeDown() {
		System.out.println("Ending auctioneer agent..." + getAID() + " " + getLocalName());
	}
	
	public void initialize() {
		
		int i = 0;
		while(i < 9999999) {i++;}
		
		//add contract net behaviour 
		addBehaviour(new AuctioneerBehaviour(this, new ACLMessage(ACLMessage.CFP)));

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

	public void setParticipants(DFAgentDescription[] participants) {
		this.participants = participants;
	}

}