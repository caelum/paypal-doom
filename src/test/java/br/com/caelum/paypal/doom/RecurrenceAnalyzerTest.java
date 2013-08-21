package br.com.caelum.paypal.doom;

import static org.junit.Assert.*;

import org.junit.Test;

import br.com.caelum.analise.paypal.IPN;

public class RecurrenceAnalyzerTest {

	@Test
	public void test() {
		IPN ipn1 = new IPN(
				"residence_country=BR;&product_name=1 mes de cursos da Caelum no Caelum Online, R$99.99;&time_created=09:43:34 May 07, 2013 PDT;&next_payment_date=03:00:00 May 07, 2013 PDT;&verify_sign=ARQQWyIGI-sJY3gS4LaZ3ocTBTTtA2r9lqlwM2I6C1kd09oHY7Up7rNT;&outstanding_balance=0.00;&amount=99.99;&first_name=EDUARDO;&payer_id=REFM3XJMNJUHN;&shipping=0.00;&payer_email=eduardocar83@gmail.com;&period_type= Regular;&receiver_email=billing@caelum.com.br;&notify_version=3.7;&txn_type=recurring_payment_profile_created;&currency_code=BRL;&payer_status=unverified;&rp_invoice_id=c211221d-e227-452e-9e42-0a25de1cc446;&initial_payment_amount=0.00;&charset=UTF-8;&product_type=1;&amount_per_cycle=99.99;&ipn_track_id=8970106126f7d;&recurring_payment_id=I-RN98NF4PXWFL;&tax=0.00;&payment_cycle=Monthly;&last_name=REGINATTO;&profile_status=Active;&");
		IPN ipn2 = new IPN(
				"residence_country=BR;&product_name=1 mes de cursos da Caelum no Caelum Online, R$99.99;&time_created=09:43:34 May 07, 2013 PDT;&next_payment_date=03:00:00 May 12, 2013 PDT;&verify_sign=Au36HP-e56fdwcxUdwBvYXPvRdLcAaGFYO0O1rGrQSCbXPKq3oHuoBIX;&outstanding_balance=0.00;&amount=99.99;&first_name=EDUARDO;&payer_id=REFM3XJMNJUHN;&shipping=0.00;&payer_email=eduardocar83@gmail.com;&period_type= Regular;&receiver_email=billing@caelum.com.br;&notify_version=3.7;&txn_type=recurring_payment_skipped;&currency_code=BRL;&payer_status=unverified;&rp_invoice_id=c211221d-e227-452e-9e42-0a25de1cc446;&initial_payment_amount=0.00;&charset=UTF-8;&product_type=1;&amount_per_cycle=99.99;&ipn_track_id=a62b548961632;&recurring_payment_id=I-RN98NF4PXWFL;&tax=0.00;&payment_cycle=Monthly;&last_name=REGINATTO;&profile_status=Active;&");
		IPN ipn3 = new IPN(
				"residence_country=BR;&product_name=1 mes de cursos da Caelum no Caelum Online, R$99.99;&time_created=09:43:34 May 07, 2013 PDT;&next_payment_date=N/A;&verify_sign=ABZ8SWDjZvuwvTb6gqiguyiUaT0PASphXiABVKxIFw2G0XoQ9K8HbYv3;&outstanding_balance=99.99;&amount=99.99;&first_name=EDUARDO;&payer_id=REFM3XJMNJUHN;&shipping=0.00;&payer_email=eduardocar83@gmail.com;&period_type= Regular;&receiver_email=billing@caelum.com.br;&notify_version=3.7;&txn_type=recurring_payment_profile_cancel;&currency_code=BRL;&payer_status=unverified;&rp_invoice_id=c211221d-e227-452e-9e42-0a25de1cc446;&initial_payment_amount=0.00;&charset=UTF-8;&product_type=1;&amount_per_cycle=99.99;&ipn_track_id=a47af2be6b7a;&recurring_payment_id=I-RN98NF4PXWFL;&tax=0.00;&payment_cycle=Monthly;&last_name=REGINATTO;&profile_status=Cancelled;&");

		IPN ipn4 = new IPN(
				"residence_country=BR;&product_name=1 mes de cursos da Caelum no Caelum Online, R$99.99;&time_created=09:59:55 May 07, 2013 PDT;&next_payment_date=03:00:00 May 07, 2013 PDT;&verify_sign=AE51dwHG0dC1.9oQNCAAVtfBML1eAyA8rLAnzelJtePsvi6HWy3uUUnr;&outstanding_balance=0.00;&amount=99.99;&first_name=EDUARDO;&payer_id=REFM3XJMNJUHN;&shipping=0.00;&payer_email=eduardocar83@gmail.com;&period_type= Regular;&receiver_email=billing@caelum.com.br;&notify_version=3.7;&txn_type=recurring_payment_profile_created;&currency_code=BRL;&payer_status=unverified;&rp_invoice_id=2fdbecc0-e971-4511-95c4-029bc07ab57c;&initial_payment_amount=0.00;&charset=UTF-8;&product_type=1;&amount_per_cycle=99.99;&ipn_track_id=df0bd280baf3c;&recurring_payment_id=I-3FDBDM0F6WHY;&tax=0.00;&payment_cycle=Monthly;&last_name=REGINATTO;&profile_status=Active;&");
		IPN ipn5 = new IPN(
				"residence_country=BR;&product_name=1 mes de cursos da Caelum no Caelum Online, R$99.99;&time_created=09:59:55 May 07, 2013 PDT;&next_payment_date=03:00:00 May 12, 2013 PDT;&verify_sign=A8luagT5oP09U7wl0BW9cI5oPcBvAVSQGWS.j71uniRV-X91AKfyll.o;&outstanding_balance=0.00;&amount=99.99;&first_name=EDUARDO;&payer_id=REFM3XJMNJUHN;&shipping=0.00;&payer_email=eduardocar83@gmail.com;&period_type= Regular;&receiver_email=billing@caelum.com.br;&notify_version=3.7;&txn_type=recurring_payment_skipped;&currency_code=BRL;&payer_status=unverified;&rp_invoice_id=2fdbecc0-e971-4511-95c4-029bc07ab57c;&initial_payment_amount=0.00;&charset=UTF-8;&product_type=1;&amount_per_cycle=99.99;&ipn_track_id=d2176433ea4c0;&recurring_payment_id=I-3FDBDM0F6WHY;&tax=0.00;&payment_cycle=Monthly;&last_name=REGINATTO;&profile_status=Active;&");
		IPN ipn6 = new IPN(
				"residence_country=BR;&product_name=1 mes de cursos da Caelum no Caelum Online, R$99.99;&time_created=09:59:55 May 07, 2013 PDT;&next_payment_date=N/A;&verify_sign=A4-DEGdLv4x5ZzrbVdtm6aIirwguAZmb0Ht6iCsyq-PUm09FDfOksHU2;&outstanding_balance=99.99;&amount=99.99;&first_name=EDUARDO;&payer_id=REFM3XJMNJUHN;&shipping=0.00;&payer_email=eduardocar83@gmail.com;&period_type= Regular;&receiver_email=billing@caelum.com.br;&notify_version=3.7;&txn_type=recurring_payment_profile_cancel;&currency_code=BRL;&payer_status=unverified;&rp_invoice_id=2fdbecc0-e971-4511-95c4-029bc07ab57c;&initial_payment_amount=0.00;&charset=UTF-8;&product_type=1;&amount_per_cycle=99.99;&ipn_track_id=c8f536e8d8b04;&recurring_payment_id=I-3FDBDM0F6WHY;&tax=0.00;&payment_cycle=Monthly;&last_name=REGINATTO;&profile_status=Cancelled;&");
		// repetida
		IPN ipn7 = new IPN(
				"residence_country=BR;&product_name=1 mes de cursos da Caelum no Caelum Online, R$99.99;&time_created=09:43:34 May 07, 2013 PDT;&next_payment_date=N/A;&verify_sign=ABZ8SWDjZvuwvTb6gqiguyiUaT0PASphXiABVKxIFw2G0XoQ9K8HbYv3;&outstanding_balance=99.99;&amount=99.99;&first_name=EDUARDO;&payer_id=REFM3XJMNJUHN;&shipping=0.00;&payer_email=eduardocar83@gmail.com;&period_type= Regular;&receiver_email=billing@caelum.com.br;&notify_version=3.7;&txn_type=recurring_payment_profile_cancel;&currency_code=BRL;&payer_status=unverified;&rp_invoice_id=c211221d-e227-452e-9e42-0a25de1cc446;&initial_payment_amount=0.00;&charset=UTF-8;&product_type=1;&amount_per_cycle=99.99;&ipn_track_id=a47af2be6b7a;&recurring_payment_id=I-RN98NF4PXWFL;&tax=0.00;&payment_cycle=Monthly;&last_name=REGINATTO;&profile_status=Cancelled;&");

	}

}
