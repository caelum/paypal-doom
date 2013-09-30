package br.com.caelum.analise.paypal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import br.com.caelum.analise.Recurrence;
import br.com.caelum.analise.RecurrenceType;
import br.com.caelum.analise.TransactionType;

public class PaypalRecurrence implements Recurrence {

	static final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");

	private String recurrenceId;
	private String productName;
	private List<IPN> ipns;

	private int numberOfPayments;
	private int numberOfRefunds;
	private int numberOfSkips;
	private int numberOfFailures;
	private RecurrenceType type;

	private boolean canceled;

	private IPN cancelIPN;

	public PaypalRecurrence(String recurrenceId, List<IPN> ipns) {
		this.recurrenceId = recurrenceId;
		this.productName = ipns.iterator().next().getProductName();
		this.type = RecurrenceType.toType(ipns.iterator().next().getProductName());

		for (IPN ipn : ipns) {
			if (!ipn.getRecurringPaymentId().equals(recurrenceId)) {
				throw new IllegalArgumentException(ipn.toString());
			}

			TransactionType transaction = ipn.getTransactionType();

			if (transaction.equals(TransactionType.REFUND)
					|| transaction.equals(TransactionType.RECURRENCE_CREATED_WITH_CYCLES)) {
				numberOfRefunds += ipn.getRefunds();
			}

			if (transaction.equals(TransactionType.RECURRENCE_CANCELED)) {
				this.canceled = true;
				this.cancelIPN = ipn;
			}
			if (transaction.equals(TransactionType.RECURRENCE_PAYMENT)
					|| transaction.equals(TransactionType.RECURRENCE_OUTSTANDING_PAYMENT)
					|| transaction.equals(TransactionType.RECURRENCE_CREATED_WITH_FIRST_PAYMENT)
					|| transaction.equals(TransactionType.RECURRENCE_CREATED_WITH_CYCLES))
				numberOfPayments += ipn.getPaidCycles();
			if (transaction.equals(TransactionType.RECURRENCE_SKIPPED)
					|| transaction.equals(TransactionType.RECURRENCE_CREATED_BUT_SKIPPED))
				numberOfSkips++;
			if (transaction.equals(TransactionType.RECURRENCE_FAILED)
					|| transaction.equals(TransactionType.RECURRENCE_OUTSTANDING_FAILED))
				numberOfFailures++;
		}
		// linked hash set to remove repeated elements
		this.ipns = new ArrayList<>(new LinkedHashSet<>(ipns));
		// Collections.sort(this.ipns);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.caelum.paypal.doom.Recurrence#getPayerEmail()
	 */
	@Override
	public String getPayerEmail() {
		return ipns.iterator().next().getPayerEmail();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.caelum.paypal.doom.Recurrence#getRecurrenceId()
	 */
	@Override
	public String getRecurrenceId() {
		return recurrenceId;
	}

	public List<IPN> getIpns() {
		return ipns;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.caelum.paypal.doom.Recurrence#getNumberOfPayments()
	 */
	@Override
	public int getNumberOfPayments() {
		return numberOfPayments;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.caelum.paypal.doom.Recurrence#getNumberOfRealPayments()
	 */
	@Override
	public int getNumberOfRealPayments() {
		return numberOfPayments - numberOfRefunds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.caelum.paypal.doom.Recurrence#getNumberOfRefunds()
	 */
	@Override
	public int getNumberOfRefunds() {
		return numberOfRefunds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.caelum.paypal.doom.Recurrence#getNumberOfSkips()
	 */
	@Override
	public int getNumberOfSkips() {
		return numberOfSkips;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.caelum.paypal.doom.Recurrence#getNumberOfFailures()
	 */
	@Override
	public int getNumberOfFailures() {
		return numberOfFailures;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.caelum.paypal.doom.Recurrence#getTotalPaid()
	 */
	@Override
	public BigDecimal getTotalPaid() {
		BigDecimal total = new BigDecimal("0.00");

		for (IPN i : ipns) {
			if (i.getTransactionType().equals(TransactionType.RECURRENCE_PAYMENT)
					|| i.getTransactionType().equals(TransactionType.RECURRENCE_CREATED_WITH_FIRST_PAYMENT))
				total = total.add(i.getAmount());
			if (i.getTransactionType().equals(TransactionType.REFUND))
				total = total.subtract(i.getAmount());
		}
		return total;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.caelum.paypal.doom.Recurrence#isCanceled()
	 */
	@Override
	public boolean isCanceled() {
		return this.canceled;
	}

	@Override
	public int compareTo(Recurrence g) {
		return this.getTimeCreated().compareTo(g.getTimeCreated());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.caelum.paypal.doom.Recurrence#hasRealPayments()
	 */
	@Override
	public boolean hasRealPayments() {
		return getNumberOfRealPayments() > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.caelum.paypal.doom.Recurrence#hasSkips()
	 */
	@Override
	public boolean hasSkips() {
		return getNumberOfSkips() > 0;
	}

	@Override
	public String toString() {
		List<TransactionType> transactions = new ArrayList<>();

		for (IPN i : getIpns()) {
			transactions.add(i.getTransactionType());
		}
		return String.format("[REC %s %8s paid:%2d skip:%2d refunds:%2d R$%7s %25s %s %s %s]",
				formatter.print(getTimeCreated()), isCanceled() ? "CANCELED" : "VALID", getNumberOfRealPayments(),
				getNumberOfSkips(), getNumberOfRefunds(), getTotalPaid(),
				getPayerEmail().substring(0, Math.min(24, getPayerEmail().length())), getType(), recurrenceId,
				transactions);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.caelum.paypal.doom.Recurrence#getTimeCreated()
	 */
	@Override
	public DateTime getTimeCreated() {
		return ipns.get(0).getTimeCreated();
	}

	public String getProductName() {
		return productName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.caelum.paypal.doom.Recurrence#getExpectedPayment()
	 */
	@Override
	public BigDecimal getExpectedPayment() {
		return new BigDecimal("0.0");
	}

	
	
	@Override
	public RecurrenceType getType() {
		return type;
	}

	@Override
	public DateTime getTimeCanceled() {
		if(!this.isCanceled()) {
			throw new IllegalStateException("not canceled");
		}
		return this.cancelIPN.getTimeCreated();
	}
}
