package br.com.caelum.analise.paypal;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.NoSuchElementException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import br.com.caelum.analise.TransactionType;

public class IPN {

	static final DateTimeFormatter parser = DateTimeFormat.forPattern("HH:mm:ss MMM dd, yyyy zzz");

	static final DateTimeFormatter moipParser = DateTimeFormat.forPattern("yyyy-MM-dd");

	static final DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss dd/MM/yyyy");

	private final String body;

	public IPN(String body) {
		this.body = body;
	}

	public String getEmail() {
		return extract("TOKEN");
	}

	public String getToken() {
		return extract("TOKEN");
	}

	public boolean isRecurring() {
		return hasKey("recurring_payment_id");
	}

	public String extract(String key) {
		int start = body.indexOf(key + "=");
		if (start == -1)
			throw new NoSuchElementException(key + " in " + body);

		start += 1 + key.length();
		int end = body.indexOf(";&", start);
		if (end == -1)
			end = body.length(); // perigoso

		return body.substring(start, end);
	}

	public boolean hasKey(String key) {
		int start = body.indexOf(key + "=");
		return start != -1;
	}

	public String getBody() {
		return body;
	}

	public boolean isSuccess() {
		if (!hasKey("ACK"))
			return false;
		return extract("ACK").equalsIgnoreCase("SUCCESS");
	}

	public String decode(String key) {
		try {
			return URLDecoder.decode(extract(key), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public String getPayerEmail() {
		return extract("payer_email");
	}

	public String getRecurringPaymentId() {
		return extract("recurring_payment_id");
	}

	public boolean isRecurringPayment() {
		return hasKey("recurring_payment_id");
	}

	public TransactionType getTransactionType() {
		if (hasKey("payment_status") && 
				(extract("payment_status").equals("Refunded") 
						|| extract("payment_status").equals("Reversed"))) {
			return TransactionType.REFUND;
		}
		if (TransactionType.toType(decode("txn_type")).equals(TransactionType.RECURRENCE_CREATED)
				&& hasKey("paidCycles")) {
			return TransactionType.RECURRENCE_CREATED_WITH_CYCLES;
		}
		if (TransactionType.toType(decode("txn_type")).equals(TransactionType.RECURRENCE_CREATED)
				&& hasKey("initial_payment_status")) {
			if (extract("initial_payment_status").equals("Completed")) {
				return TransactionType.RECURRENCE_CREATED_WITH_FIRST_PAYMENT;
			}
			if (extract("initial_payment_status").equals("Failed")) {
				return TransactionType.RECURRENCE_CREATED_BUT_SKIPPED;
			}
			throw new IllegalStateException();
		}
		return TransactionType.toType(decode("txn_type"));
	}

	public BigDecimal getAmount() {
		if (getTransactionType().equals(TransactionType.RECURRENCE_CREATED_WITH_FIRST_PAYMENT)
				|| getTransactionType().equals(TransactionType.RECURRENCE_CREATED_BUT_SKIPPED))
			return new BigDecimal(extract("initial_payment_amount"));
		return new BigDecimal(extract("amount"));
	}

	public DateTime getTimeCreated() {
		DateTime dt;
		try {
			dt = parser.parseDateTime(extract("time_created"));
		} catch (IllegalArgumentException e) {
			dt = moipParser.parseDateTime(extract("time_created").substring(0, 10));
		}

		return dt;
	}

	public String getVerifySign() {
		return extract("verify_sign");
	}

	public String getProductName() {
		return extract("product_name");
	}

	@Override
	public String toString() {
		if (!isRecurring())
			return String.format("[IPN notrecurring %s]", body);
		return String.format("[IPN %s %s %s]", getVerifySign(), formatter.print(this.getTimeCreated()),
				this.getPayerEmail());
	}

	@Override
	public boolean equals(Object obj) {
		IPN i = (IPN) obj;
		return body.equals(i.body);
	}

	@Override
	public int hashCode() {
		return this.body.hashCode();
	}

	public int getPaidCycles() {
		if (getTransactionType().equals(TransactionType.RECURRENCE_CREATED_WITH_FIRST_PAYMENT)
				|| getTransactionType().equals(TransactionType.RECURRENCE_PAYMENT)
				|| getTransactionType().equals(TransactionType.RECURRENCE_OUTSTANDING_PAYMENT))
			return 1;
		// formato do GUI
		if (getTransactionType().equals(TransactionType.RECURRENCE_CREATED_WITH_CYCLES))
			return Integer.parseInt(extract("paidCycles"));
		throw new IllegalStateException();
	}

	public int getRefunds() {
		if (getTransactionType().equals(TransactionType.REFUND))
			return 1;
		// formato do GUI
		if (getTransactionType().equals(TransactionType.RECURRENCE_CREATED_WITH_CYCLES)) {
			String value = extract("reimbursements").replace(";", "");
			return Integer.parseInt(value);
		}
		throw new IllegalStateException();
	}

	public String getState() {
		if(!hasKey("address_state")) {
			return "none";
		}
		return extract("address_state");
	}

}