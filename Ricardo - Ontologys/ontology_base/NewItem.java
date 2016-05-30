package ontology_base;

import jade.content.AgentAction;

public class NewItem implements AgentAction{
	
	private static final long serialVersionUID = 5398570036720581755L;
	
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
