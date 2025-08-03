package diet;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Represents and order issued by an {@link Customer} for a {@link Restaurant}.
 *
 * When an order is printed to a string is should look like:
 * <pre>
 *  RESTAURANT_NAME, USER_FIRST_NAME USER_LAST_NAME : DELIVERY(HH:MM):
 *  	MENU_NAME_1->MENU_QUANTITY_1
 *  	...
 *  	MENU_NAME_k->MENU_QUANTITY_k
 * </pre>
 */
public class Order {

	/**
	 * Possible order statuses
	 */
	public enum OrderStatus {
		ORDERED, READY, DELIVERED
	}

	/**
	 * Accepted payment methods
	 */
	public enum PaymentMethod {
		PAID, CASH, CARD
	}

	private final Customer customer;
	private final Restaurant restaurant;
	private PaymentMethod paymentMethod;
	private OrderStatus status;
	private final Time deliveryTime;
	private final SortedMap<Menu, Integer> menuOrder;

	Order(Restaurant restaurant, Customer customer, String time) {
		this.restaurant = restaurant;
		this.customer = customer;
		this.paymentMethod = PaymentMethod.CASH; // Default payment method
		this.status = OrderStatus.ORDERED; // Default status
		this.deliveryTime = restaurant.checkTime(new Time(time));
		this.menuOrder = new TreeMap<>(Comparator.comparing(Menu::getName));

	}

	/**
	 * Set payment method
	 * @param pm the payment method
	 */
	public void setPaymentMethod(PaymentMethod pm) {
		this.paymentMethod = pm;
	}

	/**
	 * Retrieves current payment method
	 * @return the current method
	 */
	public PaymentMethod getPaymentMethod() {
		return this.paymentMethod;
	}

	/**
	 * Set the new status for the order
	 * @param os new status
	 */
	public void setStatus(OrderStatus os) {
		this.status = os;
	}

	/**
	 * Retrieves the current status of the order
	 *
	 * @return current status
	 */
	public OrderStatus getStatus() {
		return this.status;
	}

	/**
	 * Add a new menu to the order with a given quantity
	 *
	 * @param menu	menu to be added
	 * @param quantity quantity
	 * @return the order itself (allows method chaining)
	 */
	public Order addMenus(String menu, int quantity) {
		Menu m = restaurant.getMenu(menu);
		menuOrder.put(m, quantity);
		return this;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(restaurant.getName()).append(", ").append(customer).append(" : (");
		b.append(deliveryTime).append("):\n");
		for (Map.Entry<Menu, Integer> m : menuOrder.entrySet()) {
			b.append("\t").append(m.getKey().getName()).append("->").append(m.getValue()).append("\n");
		}
		return b.toString();
	}

	Customer getUser() {
		return customer;
	}
	
	Time getDeliveryTime() {
		return this.deliveryTime;
	}

	Restaurant getRestaurant() {
		return this.restaurant;
	}
	
}