package agents;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Buyer extends Agent {
	
	protected void setup() {
		System.out.println("Starting buyer agent..." + getAID() + " " + getLocalName());
		
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
		addBehaviour(new SafeBuyingBehviour(this, MessageTemplate.MatchPerformative(ACLMessage.CFP)));
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

}