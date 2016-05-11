package logic;

import java.io.File;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ProductsLoader {

	String filePath;
	private String error;
	
	public ProductsLoader(String filePath) {
		this.filePath = filePath;
	}
	
	public ArrayList<Product> loadProducts() {
		try {
			File file = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();
			
			NodeList productNodes = doc.getElementsByTagName("Product");
			ArrayList<Product> products = new ArrayList<Product>();
			
			for(int i = 0; i < productNodes.getLength(); i++) {
				String id = ((Element)productNodes.item(i)).getAttribute("id");
				double value = Double.parseDouble(((Element)productNodes.item(i)).getElementsByTagName("value").item(0).getTextContent());
				double startPrice = Double.parseDouble(((Element)productNodes.item(i)).getElementsByTagName("startPrice").item(0).getTextContent());
				double minAcceptablePrice = Double.parseDouble(((Element)productNodes.item(i)).getElementsByTagName("minAcceptablePrice").item(0).getTextContent());
				double increment = Double.parseDouble(((Element)productNodes.item(i)).getElementsByTagName("increment").item(0).getTextContent());
				int quantity = Integer.parseInt(((Element)productNodes.item(i)).getElementsByTagName("quantity").item(0).getTextContent());
				
				Product p = new Product(id, value, startPrice, increment, minAcceptablePrice, quantity);
				products.add(p);
			}
			
			return products;
		}
		catch (Exception e) {
			error = e.getMessage();
			return null;
		}
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
