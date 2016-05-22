package agents;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;

/*
 * "as the price decreases, when it reaches your valuation, buy the minimum quantity. 
 * Keep watching the auction, and before it closes, buy the remaining items 
 * (possibly at a smaller price that your valuation)."
*/
public class SafeBuyingBehviour extends ContractNetResponder {

	public SafeBuyingBehviour(Agent a, MessageTemplate mt) {
		super(a, mt);
	}
	
	
	protected ACLMessage handleCfp(ACLMessage cfp) {
		System.out.println("Got a CFP");
		ACLMessage reply = cfp.createReply();
		reply.setPerformative(ACLMessage.PROPOSE);
		reply.setContent("I will do: it for free!!!");
		// ...
		return reply;
	}
	
	protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
		System.out.println(myAgent.getLocalName() + " got a reject...");
	}

	protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) {
		System.out.println(myAgent.getLocalName() + " got an accept!");
		ACLMessage result = accept.createReply();
		result.setPerformative(ACLMessage.INFORM);
		result.setContent("this is the result");
		
		return result;
	}

}
