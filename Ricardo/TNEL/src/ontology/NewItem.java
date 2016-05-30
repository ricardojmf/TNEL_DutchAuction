package ontology;

import jade.content.AgentAction;

public class NewItem implements AgentAction{
	
	private static final long serialVersionUID = 5398570036720581755L;
	
	private String itemName;
	private float price;
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float f) {
		this.price = f;
	}	
}
