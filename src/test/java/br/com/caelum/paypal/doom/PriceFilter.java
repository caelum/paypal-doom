package br.com.caelum.paypal.doom;

public class PriceFilter implements RecurrenceFilter {

	private RecurrenceType type;

	public PriceFilter(RecurrenceType type) {
		this.type = type;
	}

	@Override
	public boolean filter(Recurrence r) {
		return r.getType().equals(type);
	}

}
