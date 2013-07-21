package br.com.caelum.paypal.doom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

public class Recurrence implements Comparable<Recurrence> {

	private String recurrenceId;
	private List<IPN> ipns;
	
	public Recurrence(String recurrenceId, List<IPN> ipns) {
		this.recurrenceId = recurrenceId;
		
		for (IPN ipn : ipns) {
			if(!ipn.getRecurringPaymentId().equals(recurrenceId)) {
				throw new IllegalArgumentException(ipn.toString());
			}
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
		int total = 0;
		for(IPN i : ipns) {
			if(i.getTransactionType().equals(TransactionType.RECURRENCE_PAYMENT))
				total++;
		}
		return total;
	}
	
	public boolean isCanceled() {
		for(IPN i : ipns) {
			if(i.getTransactionType().equals(TransactionType.RECURRENCE_CANCELED))
				return true;
		}
		return false;
	}

	@Override
	public int compareTo(Recurrence g) {
		return this.ipns.iterator().next().compareTo(g.getIpns().iterator().next());
	}
}
