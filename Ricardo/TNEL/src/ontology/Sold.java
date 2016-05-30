package ontology;

import jade.content.AgentAction;

public class Sold implements AgentAction {
	
	private static final long serialVersionUID = -303157063214382796L;
	
	private String itemName;
	private int price;
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}	
}
