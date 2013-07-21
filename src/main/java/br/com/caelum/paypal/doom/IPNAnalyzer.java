package br.com.caelum.paypal.doom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IPNAnalyzer {

	private Map<String, List<IPN>> ipns;

	public void addIPN(IPN ipn) {
		if (!ipns.containsKey(ipn.getPayerEmail())) {
			ipns.put(ipn.getRecurringPaymentId(), new ArrayList<IPN>());
		}
		List<IPN> group = ipns.get(ipn.getRecurringPaymentId());

		group.add(ipn);
	}

	Recurrence getRecurrenceFor(String recurringId) {
		return new Recurrence(recurringId, ipns.get(recurringId));
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
