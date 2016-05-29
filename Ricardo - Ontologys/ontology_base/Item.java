package ontology_base;

import jade.content.Concept;

public class Item implements Concept{
	
	private static final long serialVersionUID = 2818147017918405271L;
	
	private String name;
	private float unitPrice;
	private Integer aparentQuality;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Integer getAparentQuality() {
		return aparentQuality;
	}
	public void setAparentQuality(Integer aparentQuality) {
		this.aparentQuality = aparentQuality;
	}	
}
