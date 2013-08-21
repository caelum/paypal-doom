package br.com.caelum.analise.paypal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.caelum.analise.Recurrence;

public class IPNAnalyzer {

	private Map<String, List<IPN>> ipns = new HashMap<>();

	public void addIPN(IPN ipn) {
		if(!ipn.isRecurringPayment()) {
			throw new IllegalArgumentException("not a recurring IPN");
		}
		if (!ipns.containsKey(ipn.getRecurringPaymentId())) {
			ipns.put(ipn.getRecurringPaymentId(), new ArrayList<IPN>());
		}
		List<IPN> group = ipns.get(ipn.getRecurringPaymentId());

		group.add(ipn);
	}

	PaypalRecurrence getRecurrenceFor(String recurringId) {
		return new PaypalRecurrence(recurringId, ipns.get(recurringId));
	}

	Set<String> getRecurringPaymentIds() {
		return ipns.keySet();
	}

	public List<Recurrence> getRecurrences() {
		List<Recurrence> recurrences = new ArrayList<>();
		for (String id : getRecurringPaymentIds()) {
			recurrences.add(getRecurrenceFor(id));
		}
		Collections.sort(recurrences);
		return recurrences;
	}

}
