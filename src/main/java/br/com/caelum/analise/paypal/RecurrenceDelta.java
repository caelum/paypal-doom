package br.com.caelum.analise.paypal;

import br.com.caelum.analise.Recurrence;

public class RecurrenceDelta {

	private int newRecurrences;
	private int canceledRecurrences;

	public RecurrenceDelta(int newRecurrences, int canceledRecurrences) {
		this.newRecurrences = newRecurrences;
		this.canceledRecurrences = canceledRecurrences;
	}

	public RecurrenceDelta() {
	}

	public int getCanceledRecurrences() {
		return canceledRecurrences;
	}

	public int getNewRecurrences() {
		return newRecurrences;
	}

	public void up() {
		newRecurrences++;
	}

	public void down() {
		canceledRecurrences++;
	}

}
