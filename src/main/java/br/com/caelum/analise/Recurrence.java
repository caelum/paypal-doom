package br.com.caelum.analise;

import java.math.BigDecimal;

import org.joda.time.DateTime;

public interface Recurrence  extends Comparable<Recurrence> {

	public abstract String getState();

	public abstract String getPayerEmail();

	public abstract String getRecurrenceId();

	public abstract int getNumberOfPayments();

	public abstract int getNumberOfRealPayments();

	public abstract int getNumberOfRefunds();

	public abstract int getNumberOfSkips();

	public abstract int getNumberOfFailures();

	public abstract BigDecimal getTotalPaid();

	public abstract boolean isCanceled();

	public abstract boolean hasRealPayments();

	public abstract boolean hasSkips();

	public abstract DateTime getTimeCreated();

	public abstract BigDecimal getExpectedPayment();

	public abstract RecurrenceType getType();

	public abstract DateTime getTimeCanceled();

}