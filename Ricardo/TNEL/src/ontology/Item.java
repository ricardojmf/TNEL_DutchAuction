package ontology;

import jade.content.Concept;

public class Item implements Concept{

	private static final long serialVersionUID = -4120602732690300547L;
	
	private String name;
	private int quantity;
	private float unitaryPrice;
	
	public Item(String name, int quantity, float unitaryPrice) {
		this.name = name;
		this.quantity = quantity;
		this.unitaryPrice = unitaryPrice;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getUnitaryPrice() {
		return unitaryPrice;
	}

	public void setUnitaryPrice(float unitaryPrice) {
		this.unitaryPrice = unitaryPrice;
	}
	
	public float getFullPrice()
	{
		return quantity*unitaryPrice;
	}
}
