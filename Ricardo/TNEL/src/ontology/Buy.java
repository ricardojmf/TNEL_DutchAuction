package ontology;

import jade.content.AgentAction;

public class Buy implements AgentAction{
	
	private static final long serialVersionUID = 1904954188924461661L;
	
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
