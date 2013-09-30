package br.com.caelum.analise;

public enum TransactionType {

	RECURRENCE_SKIPPED("recurring_payment_skipped"),
	RECURRENCE_CANCELED("recurring_payment_profile_cancel"),
	RECURRENCE_FAILED("recurring_payment_failed"),
	RECURRENCE_OUTSTANDING_FAILED("recurring_payment_outstanding_payment_failed"),
	RECURRENCE_SUSPENDED("recurring_payment_suspended"),
	RECURRENCE_CREATED("recurring_payment_profile_created"),
	RECURRENCE_CREATED_WITH_CYCLES("gui"),
	RECURRENCE_PAYMENT("recurring_payment"),
	RECURRENCE_OUTSTANDING_PAYMENT("recurring_payment_outstanding_payment"),
	REFUND("xxxxxx"),
	RECURRENCE_CREATED_WITH_FIRST_PAYMENT("yyyyyyy"),
	RECURRENCE_CREATED_BUT_SKIPPED("zzzz");

	private String paypalCode;
	
	TransactionType(String paypalCode) {
		this.paypalCode = paypalCode;
	}
	
	public static TransactionType toType(String code) {
		for(TransactionType t : values()) {
			if(t.paypalCode.equals(code)) {
				return t;
			}
		}
		throw new IllegalArgumentException(code);
	}

	public static TransactionType MOIPtoType(String value) {
		if(value.equals("CREATED")) return RECURRENCE_CREATED;
		if(value.equals("PAID")) return RECURRENCE_PAYMENT;
		if(value.equals("SUSPENDED") || value.equals("CANCELED")) return RECURRENCE_CANCELED;
		throw new IllegalArgumentException(value);
	}
	
}
