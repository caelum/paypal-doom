package br.com.caelum.paypal.doom;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.NoSuchElementException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class IPN implements Comparable<IPN> {

	static final DateTimeFormatter parser = 
			DateTimeFormat.forPattern("HH:mm:ss MMM dd, yyyy zzz");

	static final DateTimeFormatter formatter = 
			DateTimeFormat.forPattern("HH:mm:ss dd/MM/yyyy");

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

	String extract(String key) {
		int start = body.indexOf(key + "=");
		if (start == -1)
			throw new NoSuchElementException(key + " in " + body);

		start += 1 + key.length();
		int end = body.indexOf(";&", start);
		if (end == -1)
			end = body.length(); //perigoso

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
		if(!hasKey("ACK")) return false;
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
		if(hasKey("payment_status") && extract("payment_status").equals("Refunded")) {
			return TransactionType.REFUND;
		}
		return TransactionType.toType(decode("txn_type"));
	}

	public BigDecimal getAmount() {
		return new BigDecimal(extract("amount"));
	}


	public DateTime getTimeCreated() {
		// TODO cache
		return parser.parseDateTime(extract("time_created"));
	}

	@Override
	public int compareTo(IPN o) {
		return this.getTimeCreated().compareTo(o.getTimeCreated());
	}
	
	public String getVerifySign() {
		return extract("verify_sign");
	}
	
	@Override
	public String toString() {
		if(!isRecurring()) return String.format("[IPN notrecurring %s]", body);
		return String.format("[IPN %s %s %s]", getVerifySign(), formatter.print(this.getTimeCreated()), this.getPayerEmail());
	}
	
	@Override
	public boolean equals(Object obj) {
		IPN i = (IPN) obj;
		return getVerifySign().equals(i.getVerifySign());
	}
	
	@Override
	public int hashCode() {
		return this.getVerifySign().hashCode();
	}
}