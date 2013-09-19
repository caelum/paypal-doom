package br.com.caelum.analise.moip;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import br.com.caelum.analise.TransactionType;

public class MOIPMessage {

	static final DateTimeFormatter parser = DateTimeFormat
			.forPattern("yyyy-MM-dd\\THH:mm:ss'.000-02:00'");

	static final DateTimeFormatter formatter = DateTimeFormat
			.forPattern("HH:mm:ss dd/MM/yyyy");
	
	private TransactionType type;
	private String recurrenceId;
	private DateTime date;
	private BigDecimal amount = new BigDecimal("99.99");

	public MOIPMessage(String body) {
		String[] values = body.split("\\s+");
		this.type = TransactionType.MOIPtoType(values[0]);
		
		this.date = parser.parseDateTime(values[1]);
		
		this.recurrenceId = values[2].trim();
	}

	public DateTime getDate() {
		return date;
	}

	public TransactionType getType() {
		return type;
	}

	public String getRecurrenceId() {
		return recurrenceId;
	}

	public BigDecimal getAmout() {
		return amount;
	}

}
