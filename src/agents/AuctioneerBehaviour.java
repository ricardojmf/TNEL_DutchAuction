package agents;

import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

public class AuctioneerBehaviour extends ContractNetInitiator {
	
	public AuctioneerBehaviour(Agent a, ACLMessage msg) {
		super(a, msg);
	}
	
	protected Vector prepareCfps(ACLMessage cfp) {
		Vector v = new Vector();
		DFAgentDescription[] buyers = ((Auctioneer)myAgent).getParticipants();
		for(DFAgentDescription buyer : buyers) {
			cfp.addReceiver(new AID(buyer.getName().getLocalName(), false));
		}
		
		cfp.setContent("this is a call...");
		
		v.add(cfp);
		
		return v;
	}

	protected void handleAllResponses(Vector responses, Vector acceptances) {
		
		System.out.println("got " + responses.size() + " responses!");
		
		for(int i=0; i<responses.size(); i++) {
			System.out.println(((ACLMessage) responses.get(i)).getContent());
			ACLMessage msg = ((ACLMessage) responses.get(i)).createReply();
			msg.setPerformative(ACLMessage.ACCEPT_PROPOSAL); // OR NOT!
			acceptances.add(msg);
		}
	}
	
	protected void handleAllResultNotifications(Vector resultNotifications) {
		System.out.println("got " + resultNotifications.size() + " result notifs!");
	}

}
