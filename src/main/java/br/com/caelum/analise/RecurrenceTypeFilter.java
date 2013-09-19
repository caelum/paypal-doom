package br.com.caelum.analise;


public class RecurrenceTypeFilter implements RecurrenceFilter {

	private RecurrenceType type;

	public RecurrenceTypeFilter(RecurrenceType type) {
		this.type = type;
	}

	@Override
	public boolean filter(Recurrence r) {
		return r.getType().equals(type);
	}
	
	@Override
	public String toString() {
		return type.toString();
	}

}
