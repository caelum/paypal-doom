package br.com.caelum.paypal.doom;

public class PriceFilter implements RecurrenceFilter {

	private String price;

	public PriceFilter(String price) {
		this.price = price;
	}

	@Override
	public boolean filter(Recurrence r) {

		return r.getProductName().contains(price);
	}

}
