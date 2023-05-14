package model;

public class Cart {
	private int cartBookID;
	private String cartBookName;
	private String cartBookAuthor;
	private Integer cartBookPrice;
	private Integer cartBookQuantity;
	
	public Cart(int cartBookID, String cartBookName, String cartBookAuthor, int cartBookPrice, int cartBookQuantity) {
		super();
		this.cartBookID = cartBookID;
		this.cartBookName = cartBookName;
		this.cartBookAuthor = cartBookAuthor;
		this.cartBookPrice = cartBookPrice;
		this.cartBookQuantity = cartBookQuantity;
	}

	public int getCartBookID() {
		return cartBookID;
	}

	public void setCartBookID(int cartBookID) {
		this.cartBookID = cartBookID;
	}

	public String getCartBookName() {
		return cartBookName;
	}

	public void setCartBookName(String cartBookName) {
		this.cartBookName = cartBookName;
	}

	public String getCartBookAuthor() {
		return cartBookAuthor;
	}

	public void setCartBookAuthor(String cartBookAuthor) {
		this.cartBookAuthor = cartBookAuthor;
	}

	public int getCartBookPrice() {
		return cartBookPrice;
	}

	public void setCartBookPrice(int cartBookPrice) {
		this.cartBookPrice = cartBookPrice;
	}

	public int getCartBookQuantity() {
		return cartBookQuantity;
	}

	public void setCartBookQuantity(int cartBookQuantity) {
		this.cartBookQuantity = cartBookQuantity;
	}
	
}
