package br.com.caelum.paypal.doom;

import java.math.BigDecimal;
import java.util.Arrays;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Test;

import br.com.caelum.analise.Recurrence;
import br.com.caelum.analise.TransactionType;
import br.com.caelum.analise.paypal.IPN;
import br.com.caelum.analise.paypal.PaypalRecurrence;

public class RecurrenceTest {

	@Test
	public void test_ipns_create_skip() {
		// ERA MELHOR COM MOCK
		IPN ipn1 = new IPN(
				"residence_country=BR;&product_name=1 mes de cursos da Caelum no Caelum Online, R$99.99;&time_created=09:43:34 May 07, 2013 PDT;&next_payment_date=03:00:00 May 07, 2013 PDT;&verify_sign=ARQQWyIGI-sJY3gS4LaZ3ocTBTTtA2r9lqlwM2I6C1kd09oHY7Up7rNT;&outstanding_balance=0.00;&amount=99.99;&first_name=EDUARDO;&payer_id=REFM3XJMNJUHN;&shipping=0.00;&payer_email=xpto@gmail.com;&period_type= Regular;&receiver_email=billing@caelum.com.br;&notify_version=3.7;&txn_type=recurring_payment_profile_created;&currency_code=BRL;&payer_status=unverified;&rp_invoice_id=c211221d-e227-452e-9e42-0a25de1cc446;&initial_payment_amount=0.00;&charset=UTF-8;&product_type=1;&amount_per_cycle=99.99;&ipn_track_id=8970106126f7d;&recurring_payment_id=I-RN98NF4PXWFL;&tax=0.00;&payment_cycle=Monthly;&last_name=xpto;&profile_status=Active;&");
		IPN ipn2 = new IPN(
				"residence_country=BR;&product_name=1 mes de cursos da Caelum no Caelum Online, R$99.99;&time_created=09:43:34 May 07, 2013 PDT;&next_payment_date=03:00:00 May 12, 2013 PDT;&verify_sign=Au36HP-e56fdwcxUdwBvYXPvRdLcAaGFYO0O1rGrQSCbXPKq3oHuoBIX;&outstanding_balance=0.00;&amount=99.99;&first_name=EDUARDO;&payer_id=REFM3XJMNJUHN;&shipping=0.00;&payer_email=xpto@gmail.com;&period_type= Regular;&receiver_email=billing@caelum.com.br;&notify_version=3.7;&txn_type=recurring_payment_skipped;&currency_code=BRL;&payer_status=unverified;&rp_invoice_id=c211221d-e227-452e-9e42-0a25de1cc446;&initial_payment_amount=0.00;&charset=UTF-8;&product_type=1;&amount_per_cycle=99.99;&ipn_track_id=a62b548961632;&recurring_payment_id=I-RN98NF4PXWFL;&tax=0.00;&payment_cycle=Monthly;&last_name=xpto;&profile_status=Active;&");
		IPN ipn3 = new IPN(
				"residence_country=BR;&product_name=1 mes de cursos da Caelum no Caelum Online, R$99.99;&time_created=09:43:34 May 07, 2013 PDT;&next_payment_date=N/A;&verify_sign=ABZ8SWDjZvuwvTb6gqiguyiUaT0PASphXiABVKxIFw2G0XoQ9K8HbYv3;&outstanding_balance=99.99;&amount=99.99;&first_name=EDUARDO;&payer_id=REFM3XJMNJUHN;&shipping=0.00;&payer_email=xpto@gmail.com;&period_type= Regular;&receiver_email=billing@caelum.com.br;&notify_version=3.7;&txn_type=recurring_payment_profile_cancel;&currency_code=BRL;&payer_status=unverified;&rp_invoice_id=c211221d-e227-452e-9e42-0a25de1cc446;&initial_payment_amount=0.00;&charset=UTF-8;&product_type=1;&amount_per_cycle=99.99;&ipn_track_id=a47af2be6b7a;&recurring_payment_id=I-RN98NF4PXWFL;&tax=0.00;&payment_cycle=Monthly;&last_name=xpto;&profile_status=Cancelled;&");

		// repetido:
		IPN ipn4 = new IPN(
				"residence_country=BR;&product_name=1 mes de cursos da Caelum no Caelum Online, R$99.99;&time_created=09:43:34 May 07, 2013 PDT;&next_payment_date=N/A;&verify_sign=ABZ8SWDjZvuwvTb6gqiguyiUaT0PASphXiABVKxIFw2G0XoQ9K8HbYv3;&outstanding_balance=99.99;&amount=99.99;&first_name=EDUARDO;&payer_id=REFM3XJMNJUHN;&shipping=0.00;&payer_email=xpto@gmail.com;&period_type= Regular;&receiver_email=billing@caelum.com.br;&notify_version=3.7;&txn_type=recurring_payment_profile_cancel;&currency_code=BRL;&payer_status=unverified;&rp_invoice_id=c211221d-e227-452e-9e42-0a25de1cc446;&initial_payment_amount=0.00;&charset=UTF-8;&product_type=1;&amount_per_cycle=99.99;&ipn_track_id=a47af2be6b7a;&recurring_payment_id=I-RN98NF4PXWFL;&tax=0.00;&payment_cycle=Monthly;&last_name=xpto;&profile_status=Cancelled;&");

		PaypalRecurrence g = new PaypalRecurrence("I-RN98NF4PXWFL", Arrays.asList(ipn1,
				ipn2, ipn3, ipn4));

		Assert.assertEquals(3, g.getIpns().size());
		
		Assert.assertTrue(g.isCanceled());
		Assert.assertEquals(0, g.getNumberOfRealPayments());
		Assert.assertFalse(g.hasRealPayments());
		
		Assert.assertEquals(new BigDecimal("0.00"), g.getTotalPaid());
		
		Assert.assertEquals(ipn1, g.getIpns().get(0));
		Assert.assertEquals(ipn3, g.getIpns().get(2));
		Assert.assertEquals(TransactionType.RECURRENCE_CREATED, g.getIpns().get(0).getTransactionType());
		Assert.assertEquals(TransactionType.RECURRENCE_SKIPPED, g.getIpns().get(1).getTransactionType());
		Assert.assertEquals(TransactionType.RECURRENCE_CANCELED, g.getIpns().get(2).getTransactionType());
	}
	



	@Test
	public void test_create_and_skip() {
		// ERA MELHOR COM MOCK
		IPN ipn1 = new IPN(
				"residence_country=BR;&product_name=1 mes de cursos da Alura, R$197.00;&time_created=04:51:08 Jul 05, 2013 PDT;&next_payment_date=03:00:00 Aug 05, 2013 PDT;&outstanding_balance=197.00;&verify_sign=Ay8bfb6mVh3W9pUfPeDjXiGc9uGhAwuRgp0dEEJXQxMQ96JGChg-ThHT;&amount=197.00;&first_name=paulooo;&payer_id=9WBQL476Q2YNN;&shipping=0.00;&payer_email=paulo@paulo.com.br;&period_type= Regular;&receiver_email=billing@caelum.com.br;&notify_version=3.7;&txn_type=recurring_payment_profile_created;&currency_code=BRL;&payer_status=verified;&rp_invoice_id=ecc30fb5-fa5d-4872-b05f-03f182875899;&initial_payment_status=Failed;&initial_payment_amount=197.00;&charset=UTF-8;&product_type=1;&amount_per_cycle=197.00;&ipn_track_id=30f5b5a4d06d;&recurring_payment_id=XPTO;&tax=0.00;&payment_cycle=Monthly;&last_name=silveira;&profile_status=Active;&");
	
		Recurrence g = new PaypalRecurrence("XPTO", Arrays.asList(ipn1));
		Assert.assertFalse(g.isCanceled());
		Assert.assertFalse(g.hasRealPayments());
		Assert.assertEquals(0, g.getNumberOfRealPayments());

		Assert.assertTrue(g.hasSkips());
		Assert.assertEquals(1, g.getNumberOfSkips());

		Assert.assertEquals(new BigDecimal("0.00"), g.getTotalPaid());
	}
	
	@Test
	public void test_create_with_init_pay() {
		// ERA MELHOR COM MOCK
		IPN ipn1 = new IPN(
				"residence_country=BR;&product_name=1 mes de cursos da Alura, R$197.00;&time_created=04:54:35 Jul 05, 2013 PDT;&next_payment_date=03:00:00 Aug 05, 2013 PDT;&outstanding_balance=0.00;&verify_sign=AiY17Nwogeh9-6FflLZWEJgQDzSwAYhToznEAiLbwwo3g1NaqqWYZJRf;&amount=197.00;&first_name=Paulo;&payer_id=T762N3ESUS5XU;&shipping=0.00;&payer_email=paulo@paulo.com.br;&period_type= Regular;&receiver_email=billing@caelum.com.br;&notify_version=3.7;&txn_type=recurring_payment_profile_created;&currency_code=BRL;&payer_status=verified;&rp_invoice_id=1af27359-abe1-4aae-9c5c-638644fa401d;&initial_payment_txn_id=1JA96445U1675612S;&initial_payment_status=Completed;&initial_payment_amount=197.00;&charset=UTF-8;&product_type=1;&amount_per_cycle=197.00;&ipn_track_id=2d3fbc4cddf3b;&recurring_payment_id=XPTO;&tax=0.00;&payment_cycle=Monthly;&last_name=Fim;&profile_status=Active;&");
	
		Recurrence g = new PaypalRecurrence("XPTO", Arrays.asList(ipn1));
		Assert.assertFalse(g.isCanceled());
		Assert.assertTrue(g.hasRealPayments());
		Assert.assertEquals(1, g.getNumberOfRealPayments());

		Assert.assertFalse(g.hasSkips());
		Assert.assertEquals(0, g.getNumberOfSkips());

		Assert.assertEquals(new BigDecimal("197.00"), g.getTotalPaid());
	}
	
	
	public void test_another_series_of_ipns() {
		IPN ipn4 = new IPN(
				"residence_country=BR;&product_name=1 mes de cursos da Caelum no Caelum Online, R$99.99;&time_created=09:59:55 May 07, 2013 PDT;&next_payment_date=03:00:00 May 07, 2013 PDT;&verify_sign=AE51dwHG0dC1.9oQNCAAVtfBML1eAyA8rLAnzelJtePsvi6HWy3uUUnr;&outstanding_balance=0.00;&amount=99.99;&first_name=EDUARDO;&payer_id=REFM3XJMNJUHN;&shipping=0.00;&payer_email=xpto@gmail.com;&period_type= Regular;&receiver_email=billing@caelum.com.br;&notify_version=3.7;&txn_type=recurring_payment_profile_created;&currency_code=BRL;&payer_status=unverified;&rp_invoice_id=2fdbecc0-e971-4511-95c4-029bc07ab57c;&initial_payment_amount=0.00;&charset=UTF-8;&product_type=1;&amount_per_cycle=99.99;&ipn_track_id=df0bd280baf3c;&recurring_payment_id=I-3FDBDM0F6WHY;&tax=0.00;&payment_cycle=Monthly;&last_name=xpto;&profile_status=Active;&");
		IPN ipn5 = new IPN(
				"residence_country=BR;&product_name=1 mes de cursos da Caelum no Caelum Online, R$99.99;&time_created=09:59:55 May 07, 2013 PDT;&next_payment_date=03:00:00 May 12, 2013 PDT;&verify_sign=A8luagT5oP09U7wl0BW9cI5oPcBvAVSQGWS.j71uniRV-X91AKfyll.o;&outstanding_balance=0.00;&amount=99.99;&first_name=EDUARDO;&payer_id=REFM3XJMNJUHN;&shipping=0.00;&payer_email=xpto@gmail.com;&period_type= Regular;&receiver_email=billing@caelum.com.br;&notify_version=3.7;&txn_type=recurring_payment_skipped;&currency_code=BRL;&payer_status=unverified;&rp_invoice_id=2fdbecc0-e971-4511-95c4-029bc07ab57c;&initial_payment_amount=0.00;&charset=UTF-8;&product_type=1;&amount_per_cycle=99.99;&ipn_track_id=d2176433ea4c0;&recurring_payment_id=I-3FDBDM0F6WHY;&tax=0.00;&payment_cycle=Monthly;&last_name=xpto;&profile_status=Active;&");
		IPN ipn6 = new IPN(
				"residence_country=BR;&product_name=1 mes de cursos da Caelum no Caelum Online, R$99.99;&time_created=09:59:55 May 07, 2013 PDT;&next_payment_date=N/A;&verify_sign=A4-DEGdLv4x5ZzrbVdtm6aIirwguAZmb0Ht6iCsyq-PUm09FDfOksHU2;&outstanding_balance=99.99;&amount=99.99;&first_name=EDUARDO;&payer_id=REFM3XJMNJUHN;&shipping=0.00;&payer_email=xpto@gmail.com;&period_type= Regular;&receiver_email=billing@caelum.com.br;&notify_version=3.7;&txn_type=recurring_payment_profile_cancel;&currency_code=BRL;&payer_status=unverified;&rp_invoice_id=2fdbecc0-e971-4511-95c4-029bc07ab57c;&initial_payment_amount=0.00;&charset=UTF-8;&product_type=1;&amount_per_cycle=99.99;&ipn_track_id=c8f536e8d8b04;&recurring_payment_id=I-3FDBDM0F6WHY;&tax=0.00;&payment_cycle=Monthly;&last_name=xpto;&profile_status=Cancelled;&");
		// repetida
		IPN ipn7 = new IPN(
				"residence_country=BR;&product_name=1 mes de cursos da Caelum no Caelum Online, R$99.99;&time_created=09:43:34 May 07, 2013 PDT;&next_payment_date=N/A;&verify_sign=ABZ8SWDjZvuwvTb6gqiguyiUaT0PASphXiABVKxIFw2G0XoQ9K8HbYv3;&outstanding_balance=99.99;&amount=99.99;&first_name=EDUARDO;&payer_id=REFM3XJMNJUHN;&shipping=0.00;&payer_email=xpto@gmail.com;&period_type= Regular;&receiver_email=billing@caelum.com.br;&notify_version=3.7;&txn_type=recurring_payment_profile_cancel;&currency_code=BRL;&payer_status=unverified;&rp_invoice_id=c211221d-e227-452e-9e42-0a25de1cc446;&initial_payment_amount=0.00;&charset=UTF-8;&product_type=1;&amount_per_cycle=99.99;&ipn_track_id=a47af2be6b7a;&recurring_payment_id=I-RN98NF4PXWFL;&tax=0.00;&payment_cycle=Monthly;&last_name=xpto;&profile_status=Cancelled;&");
	}

	
	@Test
	public void test_ipns_with_gui_format() {
		IPN ipn1 = new IPN(
				"residence_country=BR;&time_created=2013-03-08T03:00:00Z;&initial_payment_amount=0.00;&product_name=PAYPAL_99;&payment_status=Created;&payer_email=I-F7BNNY4H7J35;&txn_type=recurring_payment_profile_created;&format=GUI;&recurring_payment_id=I-F7BNNY4H7J35;&paidCycles=3;&paidFailed=0;&reimbursements=1;");
	
		Recurrence g = new PaypalRecurrence("I-F7BNNY4H7J35", Arrays.asList(ipn1));
		Assert.assertFalse(g.isCanceled());
		Assert.assertTrue(g.hasRealPayments());
		Assert.assertEquals(2, g.getNumberOfRealPayments());
		Assert.assertEquals(1, g.getNumberOfRefunds());
	}
	
	@Test
	public void test_ipn_with_gui_format_and_time_canceled() {
		IPN ipn1 = new IPN(
				"residence_country=BR;&time_created=2013-03-04T03:00:00Z;&initial_payment_amount=0.00;&product_name=PAYPAL_99;&payment_status=Canceled;&payer_email=I-KL59BPEW6LGX;&txn_type=recurring_payment_profile_cancel;&format=GUI;&recurring_payment_id=I-KL59BPEW6LGX;&paidCycles=0;&paidFailed=0;&reimbursements=0;");
	
		Recurrence g = new PaypalRecurrence("I-KL59BPEW6LGX", Arrays.asList(ipn1));
		Assert.assertTrue(g.isCanceled());
		DateTime time = new DateTime(2013, 3, 4, 0, 0, 0, 0, DateTimeZone.forID("-03:00"));
		Assert.assertTrue(time.isEqual(g.getTimeCanceled()));
	}
	
	@Test
	public void test_ipns_with_valid_payments() {
		// ERA MELHOR COM MOCK
		IPN ipn1 = new IPN("residence_country=BR;&next_payment_date=03:00:00 Jun 08, 2013 PDT;&address_city=Samambaia;&amount=99.99;&shipping=0.00;&payer_id=E9X2HT2ZZ2ZXA;&first_name=Leandro;&mc_fee=6.00;&txn_id=48P98417PJ436153V;&receiver_email=billing@caelum.com.br;&period_type= Regular;&currency_code=BRL;&payment_date=06:57:12 May 08, 2013 PDT;&payment_fee=;&initial_payment_amount=0.00;&address_country_code=BR;&charset=UTF-8;&payment_gross=;&address_zip=72313-704;&ipn_track_id=362fcf44ee97;&tax=0.00;&address_name=Leandro xpto;&payment_cycle=Monthly;&profile_status=Active;&last_name=xpto;&receiver_id=DM59MZNG5GJNN;&time_created=04:35:24 Apr 08, 2013 PDT;&product_name=Assinatura mensal dos cursos da Caelum Online R$ 99.99;&verify_sign=Aa1RUf-mwyvMLOx-r1ndoMLS96HXAh8-mmyFKL6Mp8zsNFOc7AdFL4tv;&outstanding_balance=0.00;&address_country=Brazil;&business=billing@caelum.com.br;&payment_status=Completed;&address_status=unconfirmed;&transaction_subject=Assinatura mensal dos cursos da Caelum Online R$ 99.99;&protection_eligibility=Eligible;&payer_email=paulo@gmail.com;&notify_version=3.7;&txn_type=recurring_payment;&mc_currency=BRL;&mc_gross=99.99;&payer_status=unverified;&rp_invoice_id=7a91cecc875d42de8a70b4c0015095fb;&product_type=1;&amount_per_cycle=99.99;&address_state=Distrito Federal;&recurring_payment_id=I-SCPTA2P7HC1W;&payment_type=instant;&address_street=QUADRA QR 511 CONJUNTO 4\n23;&");
		IPN ipn2 = new IPN("residence_country=BR;&time_created=04:35:24 Apr 08, 2013 PDT;&product_name=Assinatura mensal dos cursos da Caelum Online R$ 99.99;&next_payment_date=03:00:00 Jun 08, 2013 PDT;&verify_sign=AWy5onB6REQ69mF6b0z9PVkmSoQCAWazI6ocF9rjCty4sdCuNrNMEgue;&outstanding_balance=0.00;&address_country=Brazil;&address_city=Samambaia;&payment_status=Refunded;&business=billing@caelum.com.br;&transaction_subject=Assinatura mensal dos cursos da Caelum Online R$ 99.99;&protection_eligibility=Eligible;&amount=99.99;&first_name=Leandro;&payer_id=E9X2HT2ZZ2ZXA;&shipping=0.00;&payer_email=paulo@gmail.com;&mc_fee=-6.00;&txn_id=6F326900UD8699639;&period_type= Regular;&receiver_email=billing@caelum.com.br;&notify_version=3.7;&currency_code=BRL;&mc_gross=-99.99;&mc_currency=BRL;&reason_code=refund;&payment_date=12:43:41 May 14, 2013 PDT;&rp_invoice_id=7a91cecc875d42de8a70b4c0015095fb;&payment_fee=;&initial_payment_amount=0.00;&charset=UTF-8;&address_country_code=BR;&product_type=1;&payment_gross=;&address_zip=72313-704;&amount_per_cycle=99.99;&ipn_track_id=6a9ce72923b1d;&address_state=Distrito Federal;&recurring_payment_id=I-SCPTA2P7HC1W;&payment_cycle=Monthly;&address_name=Leandro xpto;&last_name=xpto;&profile_status=Active;&parent_txn_id=48P98417PJ436153V;&payment_type=instant;&receiver_id=DM59MZNG5GJNN;&address_street=QUADRA QR 511 CONJUNTO 4\n23;&");
		IPN ipn3 = new IPN("residence_country=BR;&next_payment_date=03:00:00 Jul 08, 2013 PDT;&address_city=Samambaia;&amount=99.99;&shipping=0.00;&first_name=Leandro;&payer_id=E9X2HT2ZZ2ZXA;&mc_fee=6.00;&txn_id=2TG76408E1226930S;&receiver_email=billing@caelum.com.br;&period_type= Regular;&currency_code=BRL;&payment_date=06:43:57 Jun 08, 2013 PDT;&payment_fee=;&initial_payment_amount=0.00;&address_country_code=BR;&charset=UTF-8;&payment_gross=;&address_zip=72313-704;&ipn_track_id=410b5b77bb22e;&tax=0.00;&address_name=Leandro xpto;&payment_cycle=Monthly;&last_name=xpto;&profile_status=Active;&receiver_id=DM59MZNG5GJNN;&time_created=04:35:24 Apr 08, 2013 PDT;&product_name=Assinatura mensal dos cursos da Caelum Online R$ 99.99;&verify_sign=A5qTmJSd.qwDwa8HzaIAfZInmiuVAaCB1VPrv2WtivxAc565XZtmH0jJ;&outstanding_balance=0.00;&address_country=Brazil;&business=billing@caelum.com.br;&payment_status=Completed;&address_status=unconfirmed;&transaction_subject=Assinatura mensal dos cursos da Caelum Online R$ 99.99;&protection_eligibility=Eligible;&payer_email=paulo@gmail.com;&notify_version=3.7;&txn_type=recurring_payment;&mc_currency=BRL;&payer_status=unverified;&mc_gross=99.99;&rp_invoice_id=7a91cecc875d42de8a70b4c0015095fb;&product_type=1;&amount_per_cycle=99.99;&address_state=Distrito Federal;&recurring_payment_id=I-SCPTA2P7HC1W;&payment_type=instant;&address_street=QUADRA QR 511 CONJUNTO 4\n23;&");
		IPN ipn4 = new IPN("residence_country=BR;&product_name=Assinatura mensal dos cursos da Caelum Online R$ 99.99;&time_created=04:35:24 Apr 08, 2013 PDT;&next_payment_date=03:00:00 Jul 13, 2013 PDT;&verify_sign=AswkGSvIs1JsSQxNQRfw08GzIq8.A5nKfZwcIUmqJ6A-Cm.cjAHdRQ5v;&outstanding_balance=0.00;&amount=99.99;&first_name=Leandro;&payer_id=E9X2HT2ZZ2ZXA;&shipping=0.00;&payer_email=paulo@gmail.com;&period_type= Regular;&receiver_email=billing@caelum.com.br;&notify_version=3.7;&txn_type=recurring_payment_skipped;&currency_code=BRL;&payer_status=unverified;&rp_invoice_id=7a91cecc875d42de8a70b4c0015095fb;&initial_payment_amount=0.00;&charset=UTF-8;&product_type=1;&amount_per_cycle=99.99;&ipn_track_id=43cd50f19188;&recurring_payment_id=I-SCPTA2P7HC1W;&tax=0.00;&payment_cycle=Monthly;&last_name=xpto;&profile_status=Active;&");
		IPN ipn5 = new IPN("residence_country=BR;&product_name=Assinatura mensal dos cursos da Caelum Online R$ 99.99;&time_created=04:35:24 Apr 08, 2013 PDT;&next_payment_date=03:00:00 Jul 18, 2013 PDT;&outstanding_balance=0.00;&verify_sign=ALyuCjdMmzGa1iYyz7HFdlKp0geLALj.8lUt7eqy02BPsKwyseNIO0Pz;&amount=99.99;&shipping=0.00;&payer_id=E9X2HT2ZZ2ZXA;&first_name=Leandro;&payer_email=paulo@gmail.com;&receiver_email=contato@alura.com.br;&period_type= Regular;&notify_version=3.7;&txn_type=recurring_payment_skipped;&currency_code=BRL;&payer_status=unverified;&rp_invoice_id=7a91cecc875d42de8a70b4c0015095fb;&initial_payment_amount=0.00;&charset=UTF-8;&product_type=1;&amount_per_cycle=99.99;&ipn_track_id=c4127ac948739;&recurring_payment_id=I-SCPTA2P7HC1W;&tax=0.00;&payment_cycle=Monthly;&profile_status=Active;&last_name=xpto;&");
		IPN ipn6 = new IPN("residence_country=BR;&product_name=Assinatura mensal dos cursos da Caelum Online R$ 99.99;&time_created=04:35:24 Apr 08, 2013 PDT;&next_payment_date=03:00:00 Aug 08, 2013 PDT;&outstanding_balance=99.99;&verify_sign=Ajf9cVdXUXO5zD0hsqV13PuuUpbVAWNkU5jWcGemtmA1VnXPHKLaOsnJ;&amount=99.99;&shipping=0.00;&payer_id=E9X2HT2ZZ2ZXA;&first_name=Leandro;&payer_email=paulo@gmail.com;&receiver_email=contato@alura.com.br;&period_type= Regular;&notify_version=3.7;&currency_code=BRL;&txn_type=recurring_payment_failed;&payer_status=unverified;&rp_invoice_id=7a91cecc875d42de8a70b4c0015095fb;&initial_payment_amount=0.00;&charset=UTF-8;&product_type=1;&amount_per_cycle=99.99;&ipn_track_id=e7bf2dbd2ed2e;&recurring_payment_id=I-SCPTA2P7HC1W;&tax=0.00;&payment_cycle=Monthly;&profile_status=Active;&last_name=xpto;&");

		
		PaypalRecurrence g = new PaypalRecurrence("I-SCPTA2P7HC1W", Arrays.asList(ipn1,
				ipn2, ipn3, ipn4, ipn5, ipn6));
		
		Assert.assertEquals(TransactionType.RECURRENCE_PAYMENT, g.getIpns().get(0).getTransactionType());
		Assert.assertEquals(TransactionType.REFUND, g.getIpns().get(1).getTransactionType());
		Assert.assertEquals(TransactionType.RECURRENCE_PAYMENT, g.getIpns().get(2).getTransactionType());
		Assert.assertEquals(TransactionType.RECURRENCE_SKIPPED, g.getIpns().get(3).getTransactionType());
		Assert.assertEquals(TransactionType.RECURRENCE_SKIPPED, g.getIpns().get(4).getTransactionType());
		Assert.assertEquals(TransactionType.RECURRENCE_FAILED, g.getIpns().get(5).getTransactionType());
		
		Assert.assertFalse(g.isCanceled());
		Assert.assertEquals(1, g.getNumberOfRealPayments());
		Assert.assertEquals(2, g.getNumberOfPayments());
		Assert.assertEquals(1, g.getNumberOfRefunds());
		Assert.assertEquals(new BigDecimal("99.99"), g.getTotalPaid());
		
		System.out.println(g);

	}
}
