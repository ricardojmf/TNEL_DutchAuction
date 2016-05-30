package ontology_base;

import jade.content.AgentAction;

public class NewPrice implements AgentAction{
	
	private static final long serialVersionUID = -8446670338877259265L;
	
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
