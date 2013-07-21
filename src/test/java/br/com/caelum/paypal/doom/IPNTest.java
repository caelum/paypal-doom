package br.com.caelum.paypal.doom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotSame;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.NoSuchElementException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

public class IPNTest {
	@Test
	public void should_extract_bigtest() {
		String bigtest = "residence_country=BR;&product_name=6 meses de cursos da Caelum no Caelum Online, R$699.99;&time_created=12:29:57 Jun 18, 2013 PDT;&next_payment_date=03:00:00 Jun 28, 2013 PDT;&outstanding_balance=0.00;&verify_sign=AzKelLP.up6.7xbc1bYZflpd0atKAWsAWqKgqR4-FR8FG5OxtcxPqbFF;&amount=699.99;&shipping=0.00;&payer_id=EFBQE9XK6K9EL;&first_name=Paulo;&payer_email=paulo@paulo.com.br;&receiver_email=billing@caelum.com.br;&period_type= Regular;&notify_version=3.7;&currency_code=BRL;&txn_type=recurring_payment_skipped;&payer_status=unverified;&rp_invoice_id=0a2258b4-2f6e-4bf7-bb86-f4605e54edc9;&initial_payment_amount=0.00;&charset=UTF-8;&product_type=1;&amount_per_cycle=699.99;&ipn_track_id=daed588c62a04;&recurring_payment_id=I-687AEEJ1D2JT;&tax=0.00;&payment_cycle=every 6 Months;&profile_status=Active;&last_name=Silveira;&";
		
		assertEquals("paulo@paulo.com.br", msgFor(bigtest).getPayerEmail());
		assertEquals(new BigDecimal("699.99"), msgFor(bigtest).getAmount());
		assertTrue(new DateTime(2013,6,18,12,29,57, DateTimeZone.forID("-07:00")).isEqual(msgFor(bigtest).getTimeCreated()));
		assertEquals(TransactionType.RECURRENCE_SKIPPED, msgFor(bigtest).getTransactionType());
	}
	
	@Test
	public void should_extract_refunded_transaction() {
		String bigtest = "residence_country=BR;&time_created=04:35:24 Apr 08, 2013 PDT;&product_name=Assinatura mensal dos cursos da Caelum Online R$ 99.99;&next_payment_date=03:00:00 Jun 08, 2013 PDT;&verify_sign=AWy5onB6REQ69mF6b0z9PVkmSoQCAWazI6ocF9rjCty4sdCuNrNMEgue;&outstanding_balance=0.00;&address_country=Brazil;&address_city=Samambaia;&payment_status=Refunded;&business=billing@caelum.com.br;&transaction_subject=Assinatura mensal dos cursos da Caelum Online R$ 99.99;&protection_eligibility=Eligible;&amount=99.99;&first_name=Leandro;&payer_id=E9X2HT2ZZ2ZXA;&shipping=0.00;&payer_email=paulo@paulo.com.br;&mc_fee=-6.00;&txn_id=6F326900UD8699639;&period_type= Regular;&receiver_email=billing@caelum.com.br;&notify_version=3.7;&currency_code=BRL;&mc_gross=-99.99;&mc_currency=BRL;&reason_code=refund;&payment_date=12:43:41 May 14, 2013 PDT;&rp_invoice_id=7a91cecc875d42de8a70b4c0015095fb;&payment_fee=;&initial_payment_amount=0.00;&charset=UTF-8;&address_country_code=BR;&product_type=1;&payment_gross=;&address_zip=72313-704;&amount_per_cycle=99.99;&ipn_track_id=6a9ce72923b1d;&address_state=Distrito Federal;&recurring_payment_id=I-SCPTA2P7HC1W;&payment_cycle=Monthly;&address_name=Leandro Pereira Brito;&last_name=Pereira Brito;&profile_status=Active;&parent_txn_id=48P98417PJ436153V;&payment_type=instant;&receiver_id=DM59MZNG5GJNN;&address_street=QUADRA QR 511 CONJUNTO 4\n23;&";
		
		assertEquals("paulo@paulo.com.br", msgFor(bigtest).getPayerEmail());
		assertEquals(new BigDecimal("99.99"), msgFor(bigtest).getAmount());
		assertNotNull((msgFor(bigtest).getTimeCreated()));
		assertEquals(TransactionType.REFUNDED, msgFor(bigtest).getTransactionType());
	}

	@Test
	public void should_extract_token() {
		String body = "TOKEN=guilherme;&TOKEN2=silveira;&";
		assertEquals("guilherme", msgFor(body).getToken());
	}

	@Test
	public void should_extract_random_field() {
		String body = "TOKEN=guilherme;&TOKEN2=silveira;&";
		assertEquals("silveira", msgFor(body).extract("TOKEN2"));
	}

	@Test(expected=NoSuchElementException.class)
	public void should_extract_random_field_as_empty_if_it_doesnt_exist() {
		String body = "TOKEN=guilherme;&TOKEN2=silveira;&";
		msgFor(body).extract("TOKEN3");
	}

	@Test
	public void should_check_the_request_was_a_success() {
		assertEquals(true, msgFor("RESULT=1;&ACK=SUCCESS;&").isSuccess());
		assertEquals(false, msgFor("RESULT=2;&ACK=FAILURE;&").isSuccess());
		assertEquals(false, msgFor("RESULT=3;&").isSuccess());
	}

	@Test
	public void should_decode_encoded_characters() throws UnsupportedEncodingException {
		String value = "guilherme.silveira@caelum.com.br";
		String encoded = URLEncoder.encode(value, "UTF-8");
		assertNotSame(value, encoded);
		assertEquals(value, msgFor("NAME=" + encoded).decode("NAME"));
	}
	


	private IPN msgFor(String body) {
		return new IPN(body);
	}

}
