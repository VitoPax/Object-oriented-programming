package diet;

import java.util.LinkedList;

public class Customer implements Comparable<Customer> {
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private final LinkedList<Order> orderList;
	
	Customer(String firstName, String lastName, String email, String phone) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.orderList = new LinkedList<>();

	
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void SetEmail(String email) {
		this.email = email;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void addOrder(Order order) {
		orderList.add(order);
	}


	@Override
	public String toString() {
		return this.firstName + " " + this.lastName;
	}

	@Override
    public int compareTo(Customer other) {
        int cmp = this.lastName.compareTo(other.getLastName());
        if (cmp == 0) {
            cmp = this.firstName.compareTo(other.getFirstName());
        }
        return cmp;
    }

	
	
}
