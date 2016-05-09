package agents;

import jade.core.Agent;

public class Seller extends Agent {
	
	protected void setup() {
		System.out.println("Starting seller agent..." + getAID() + " " + getLocalName());
	}
	
	protected void takeDown() {
		System.out.println("Ending seller agent..." + getAID() + " " + getLocalName());
	}

}