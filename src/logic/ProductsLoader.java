package logic;

import java.io.File;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import agents.Buyer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ProductsLoader {

	String productsFilePath;
	String agentsFilePath;
	private String error;
	
	public ProductsLoader(String productsFilePath, String agentsFilePath) {
		this.productsFilePath = productsFilePath;
		this.agentsFilePath = agentsFilePath;
	}
	
	public ArrayList<Product> loadProducts() {
		try {
			File file = new File(productsFilePath);
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
	
	public ArrayList<Buyer> loadAgents() {
		try {
			File file = new File(agentsFilePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();
			
			NodeList agentNodes = doc.getElementsByTagName("Agent");
			ArrayList<Buyer> buyers = new ArrayList<Buyer>();
			for(int i = 0; i < agentNodes.getLength(); i++) {
				Element agent = (Element)agentNodes.item(i);
				String id = agent.getAttribute("id");
				double money = Double.parseDouble(agent.getElementsByTagName("money").item(0).getTextContent());
				String behavior = agent.getElementsByTagName("behavior").item(0).getTextContent();
				NodeList wt = agent.getElementsByTagName("waitTurn");
				int waitTurn;
				if(wt.getLength() > 0) {
					waitTurn = Integer.parseInt(wt.item(0).getTextContent());
				}
				else {
					waitTurn = 0;
				}
				
				ArrayList<BuyerProduct> productsToBuy = new ArrayList<BuyerProduct>();				
				NodeList productNodes = agent.getElementsByTagName("product");
				
				for(int j = 0; j < productNodes.getLength(); j++) {
					Element product = (Element)productNodes.item(j);
					String name = product.getAttribute("id");
					double valuation = Double.parseDouble(product.getAttribute("valuation"));
					int quantityToBuy = Integer.parseInt(product.getAttribute("quantityToBuy"));
					int minimumQuantity = Integer.parseInt(product.getAttribute("minimumQuantity"));
					BuyerProduct bp = new BuyerProduct(name, valuation, quantityToBuy, minimumQuantity);
					productsToBuy.add(bp);
				}
				
				Buyer buyer = new Buyer(id, money, productsToBuy, waitTurn);
				buyer.setBehavior(behavior);
				buyers.add(buyer);
			}
			
			return buyers;
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
