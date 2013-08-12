package br.com.caelum.paypal.doom;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Recurrence implements Comparable<Recurrence> {

	static final DateTimeFormatter formatter = DateTimeFormat
			.forPattern("dd/MM/yyyy");

	private String recurrenceId;
	private String productName;
	private List<IPN> ipns;

	private int numberOfPayments;
	private int numberOfRefunds;
	private int numberOfSkips;
	private int numberOfFailures;

	public Recurrence(String recurrenceId, List<IPN> ipns) {
		this.recurrenceId = recurrenceId;
		this.productName = ipns.iterator().next().getProductName();
		
		for (IPN ipn : ipns) {
			if (!ipn.getRecurringPaymentId().equals(recurrenceId)) {
				throw new IllegalArgumentException(ipn.toString());
			}
			if (ipn.getTransactionType().equals(TransactionType.REFUND))
				numberOfRefunds++;
			if (ipn.getTransactionType().equals(
					TransactionType.RECURRENCE_PAYMENT)
					|| ipn.getTransactionType().equals(
							TransactionType.RECURRENCE_OUTSTANDING_PAYMENT) ||
							ipn.getTransactionType().equals(
									TransactionType.RECURRENCE_CREATED_WITH_PAYMENT) )
				numberOfPayments++;
			if (ipn.getTransactionType().equals(
					TransactionType.RECURRENCE_SKIPPED) || ipn.getTransactionType().equals(
							TransactionType.RECURRENCE_CREATED_BUT_SKIPPED) )
				numberOfSkips++;
			if (ipn.getTransactionType().equals(
					TransactionType.RECURRENCE_FAILED)
					|| ipn.getTransactionType().equals(
							TransactionType.RECURRENCE_OUTSTANDING_FAILED))
				numberOfFailures++;
		}
		// linked hash set to remove repeated elements
		this.ipns = new ArrayList<>(new LinkedHashSet<>(ipns));
		Collections.sort(this.ipns);
	}

	public String getPayerEmail() {
		return ipns.iterator().next().getPayerEmail();
	}

	public String getRecurrenceId() {
		return recurrenceId;
	}

	public List<IPN> getIpns() {
		return ipns;
	}

	public int getNumberOfPayments() {
		return numberOfPayments;
	}

	public int getNumberOfRefunds() {
		return numberOfRefunds;
	}

	public int getNumberOfSkips() {
		return numberOfSkips;
	}

	public int getNumberOfFailures() {
		return numberOfFailures;
	}

	public BigDecimal getTotalPaid() {
		BigDecimal total = new BigDecimal("0.00");

		for (IPN i : ipns) {
			if (i.getTransactionType().equals(
					TransactionType.RECURRENCE_PAYMENT)
					|| i.getTransactionType().equals(
							TransactionType.RECURRENCE_CREATED_WITH_PAYMENT))
				total = total.add(i.getAmount());
			if (i.getTransactionType().equals(TransactionType.REFUND))
				total = total.subtract(i.getAmount());
		}
		return total;
	}

	public boolean isCanceled() {
		for (IPN i : ipns) {
			if (i.getTransactionType().equals(
					TransactionType.RECURRENCE_CANCELED))
				return true;
		}
		return false;
	}

	@Override
	public int compareTo(Recurrence g) {
		return this.ipns.iterator().next()
				.compareTo(g.getIpns().iterator().next());
	}

	public boolean hasPayments() {
		return getNumberOfPayments() > 0;
	}

	public boolean hasSkips() {
		return getNumberOfSkips() > 0;
	}

	@Override
	public String toString() {
		List<TransactionType> transactions = new ArrayList<>();

		for (IPN i : getIpns()) {
			transactions.add(i.getTransactionType());
		}
		return String.format(
				"[REC %s %8s paid:%2d skip:%2d refunds:%2d R$%7s %25s %s %s]",
				formatter.print(getTimeCreated()), isCanceled() ? "CANCELED"
						: "VALID", getNumberOfPayments(), getNumberOfSkips(),
				getNumberOfRefunds(), getTotalPaid(), getPayerEmail()
						.substring(0, Math.min(24, getPayerEmail().length())),
				transactions, getProductName());
	}

	public DateTime getTimeCreated() {
		return ipns.get(0).getTimeCreated();
	}
	
	public String getProductName() {
		return productName;
	}
}
