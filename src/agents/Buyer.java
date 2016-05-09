package agents;

import jade.core.Agent;

public class Buyer extends Agent {
	
	protected void setup() {
		System.out.println("Starting buyer agent..." + getAID() + " " + getLocalName());
	}
	
	protected void takeDown() {
		System.out.println("Ending buyer agent..." + getAID() + " " + getLocalName());
	}

}