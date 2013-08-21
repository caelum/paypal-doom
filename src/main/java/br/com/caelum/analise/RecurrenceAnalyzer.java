package br.com.caelum.analise;

import java.math.BigDecimal;


public class RecurrenceAnalyzer {

	private Histogram all = new Histogram();
	private Histogram activeRecurrences = new Histogram();
	private Histogram canceledRecurrences = new Histogram();
	private BigDecimal amount = new BigDecimal("0.00");
	private BigDecimal expected = new BigDecimal("0.00");
	private BigDecimal expectedExceptCanceledWithoutPayments = new BigDecimal("0.00");
	private int total = 0;
	
	public void add(Recurrence r) {
		total++;
		amount = amount.add(r.getTotalPaid());
		expected = expected.add(r.getExpectedPayment());
		
		all.add(r.getNumberOfRealPayments());
		if(r.isCanceled()) {
			canceledRecurrences.add(r.getNumberOfRealPayments());
		} else {
			expectedExceptCanceledWithoutPayments = expectedExceptCanceledWithoutPayments.add(r.getExpectedPayment());
			activeRecurrences.add(r.getNumberOfRealPayments());
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
		StringBuilder builder = new StringBuilder("[");
		int total = this.total - all.get(0);
		//int parcial = total;
		
		for (int i = 1; i <= canceledRecurrences.getMaxKey(); i++) {
			int canceled = canceledRecurrences.get(i);
			
			
			//parcial -= canceled;
			builder.append(String.format("%d:%d (%.1f%%), ", canceled, total,
					((double) canceled) * 100 / total));
			
			total -= all.get(i);
		}
		builder.append("]").toString();
		return String.format("\nall: \t\t%s\nactive: \t%s\ncanceled: \t%s\nfunil: \t%s\n", all, activeRecurrences, canceledRecurrences, builder);
	}
	
}
