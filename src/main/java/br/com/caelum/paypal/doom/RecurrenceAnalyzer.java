package br.com.caelum.paypal.doom;

import java.math.BigDecimal;

public class RecurrenceAnalyzer {

	private Histogram all = new Histogram();
	private Histogram activeRecurrences = new Histogram();
	private Histogram canceledRecurrences = new Histogram();
	private BigDecimal amount = new BigDecimal("0.00");
	private BigDecimal expected = new BigDecimal("0.00");
	private BigDecimal expectedExceptCanceledWithoutPayments = new BigDecimal("0.00");
	
	public void add(Recurrence r) {
		amount = amount.add(r.getTotalPaid());
		expected = expected.add(r.getExpectedPayment());
		
		all.add(r.getNumberOfPayments());
		if(r.isCanceled()) {
			canceledRecurrences.add(r.getNumberOfPayments());
		} else {
			expectedExceptCanceledWithoutPayments = expectedExceptCanceledWithoutPayments.add(r.getExpectedPayment());
			activeRecurrences.add(r.getNumberOfPayments());
			
		}		
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	public BigDecimal getExpected() {
		return expected;
	}
	
	public BigDecimal getExpectedExceptCanceledWithoutPayments() {
		return expectedExceptCanceledWithoutPayments;
	}
	
	
	@Override
	public String toString() {
		return String.format("\n all recurrences: %s\nactive %s\ncanceled %s", all);
	}
	
}
