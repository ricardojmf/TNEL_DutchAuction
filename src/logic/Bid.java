package logic;

public class Bid {

	private int quantity;
	private String buyer;
	
	public Bid(int quantity, String buyer) {
		this.setQuantity(quantity);
		this.setBuyer(buyer);
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}
}
