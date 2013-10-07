package br.com.caelum.analise;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import br.com.caelum.analise.paypal.RecurrenceDelta;

public class RecurrenceAnalyzer {

	static final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
	
	private Map<DateMidnight, RecurrenceDelta> deltas = new TreeMap<>();
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
		if (r.isCanceled()) {
			canceledRecurrences.add(r.getNumberOfRealPayments());
		} else {
			expectedExceptCanceledWithoutPayments = expectedExceptCanceledWithoutPayments.add(r.getExpectedPayment());
			activeRecurrences.add(r.getNumberOfRealPayments());
		}

		RecurrenceDelta recurrenceDelta = getDeltaFor(r.getTimeCreated());
		recurrenceDelta.up();

		if (r.isCanceled()) {
			RecurrenceDelta deltaCanceled = getDeltaFor(r.getTimeCanceled());
			deltaCanceled.down();
		}
	}

	private RecurrenceDelta getDeltaFor(DateTime time) {
		DateMidnight date = time.toDateMidnight();
		if (!deltas.containsKey(date)) {
			deltas.put(date, new RecurrenceDelta());
		}
		return deltas.get(date);
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

	
	public void showDeltas() {
		int total = 0;
		for(DateMidnight date : deltas.keySet()) {
			RecurrenceDelta delta = deltas.get(date);
			total = total + delta.getNewRecurrences() - delta.getCanceledRecurrences();
			System.out.printf("%s - %3d(+%2d, -%2d)\n", formatter.print(date), total, delta.getNewRecurrences(), delta.getCanceledRecurrences());
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("[");
		int total = this.total;

		for (int i = 0; i <= all.getMaxKey(); i++) {
			int canceled = canceledRecurrences.get(i);

			builder.append(String.format("%d:%d (%.1f%%), ", canceled, total, ((double) canceled) * 100 / total));

			total -= all.get(i);
		}
		builder.append("]").toString();
		return String.format("\nall: \t\t%s\nactive: \t%s\ncanceled: \t%s\nfunil: \t%s\n", all, activeRecurrences,
				canceledRecurrences, builder);
	}

}
