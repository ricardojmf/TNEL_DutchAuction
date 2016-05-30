package agents;

import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames.Ontology;
import jade.domain.introspection.ACLMessage;
import jade.lang.acl.*;
import ontology.DutchAuctionOntology;
import ontology.Item;
import ontology.NewItem;

public class Auctioneer extends Agent {

	private static final long serialVersionUID = 4084101276942950877L;
	
	private ContentManager cm = (ContentManager) getContentManager();
	private Codec codec = new SLCodec();
	private Ontology ontology = (Ontology) DutchAuctionOntology.getInstance();
	
	//aletar isto para o que está realmente a ser usado!
	private int state = 0;
	
	//resto das variáveis do auctioneer
	
	//setup do agent
	//setup do behaviour
	//ver se acabaram as paletes e ou agentes
	
	
	//Criação dos vários tipos de mensagens
	
	public void sendmessage(AID agent, int state){
		
		//preparação inicial da mensagem, usar contractNet
		ACLMessage msg = new ACLMessage();
		
		Item item_exemplo = new Item("exemplo", 2, 2);
		
		try {
			ContentManager cm = getContentManager();
			
			switch (state) {
			
			//Exemplo
			case 0:
				
				NewItem nt = new NewItem();
				nt.setItemName(item_exemplo.getName());
				nt.setPrice(item_exemplo.getFullPrice());
				Action a = new Action(agent, nt);
				//cm.fillContent(msg, a);
				break;
			default:
				break;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	

}
