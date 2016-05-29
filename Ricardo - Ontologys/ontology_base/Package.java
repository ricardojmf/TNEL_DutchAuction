package ontology_base;

import jade.util.leap.List;

public class Package{
	
	private String packageName;
	private List items;
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public List getItems() {
		return items;
	}
	public void setItems(List items) {
		this.items = items;
	}
}
