package br.com.caelum.analise;

import java.math.BigDecimal;

public enum RecurrenceType {
	MONTHLY_99_MOIP(new BigDecimal("99.00"), 1), MONTHLY_99(new BigDecimal("99.99"), 1), MONTHLY_149(
			new BigDecimal("149.99"), 1), MONTHLY_197(new BigDecimal("197.00"),
			1), SEMIANNUAL_699(new BigDecimal("699.99"), 1);

	private BigDecimal value;
	private int months;

	RecurrenceType(BigDecimal value, int months) {
		this.value = value;
		this.months = months;
	}

	public BigDecimal getValue() {
		return value;
	}

	public int getMonths() {
		return months;
	}

	public static RecurrenceType toType(String paypalDescription) {
		if (paypalDescription.contains("R$99.99")
				|| paypalDescription.contains("R$ 99.99")
				|| paypalDescription.contains("R$ 99.00"))
			return MONTHLY_99;
		if (paypalDescription.contains("R$699.99"))
			return SEMIANNUAL_699;
		if (paypalDescription.contains("R$149.99"))
			return MONTHLY_149;
		if (paypalDescription.contains("R$197.00"))
			return MONTHLY_197;

		throw new IllegalArgumentException(paypalDescription);
	}
}
