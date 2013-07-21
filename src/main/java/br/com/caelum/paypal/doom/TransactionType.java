package br.com.caelum.paypal.doom;

public enum TransactionType {

	RECURRENCE_SKIPPED("recurring_payment_skipped"),
	RECURRENCE_CANCELED("recurring_payment_profile_cancel"),
	RECURRENCE_FAILED("recurring_payment_failed"),
	RECURRENCE_CREATED("recurring_payment_profile_created"),
	RECURRENCE_PAYMENT("recurring_payment"), REFUND("xxxxxx");

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
	
}
