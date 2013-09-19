package br.com.caelum.analise.moip;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.DateTime;

import br.com.caelum.analise.Recurrence;
import br.com.caelum.analise.RecurrenceType;

public class MOIPRecurrence implements Recurrence {

	private String recurrenceId;

	public MOIPRecurrence(String recurrenceId, List<MOIPMessage> messages) {
		for (MOIPMessage m : messages) {
			
		}
	}
	
	@Override
	public int compareTo(Recurrence g) {
		return this.getTimeCreated()
				.compareTo(g.getTimeCreated());
	}

	@Override
	public String getPayerEmail() {
		return this.recurrenceId;
	}

	@Override
	public String getRecurrenceId() {	
		return this.recurrenceId;
	}

	@Override
	public int getNumberOfPayments() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfRealPayments() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfRefunds() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfSkips() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfFailures() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BigDecimal getTotalPaid() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCanceled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasRealPayments() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasSkips() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DateTime getTimeCreated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getExpectedPayment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RecurrenceType getType() {
		// TODO Auto-generated method stub
		return null;
	}

}
