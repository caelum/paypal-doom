package br.com.caelum.analise.moip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import br.com.caelum.analise.TransactionType;

public class MOIPMessageTest {

	@Test
	public void testMoipCreatedMessage() {
		String created = "CREATED	2013-01-18T01:01:00.000-02:00	1358519415284guilhermedeazevedosilveira222407638091358519415284	9999";

		MOIPMessage message = new MOIPMessage(created);
		assertEquals(TransactionType.RECURRENCE_CREATED, message.getType());
		assertTrue(new DateTime(2013, 6, 18, 12, 29, 57,
				DateTimeZone.forID("-07:00")).isEqual(message.getDate()));
		assertEquals(
				"1358519415284guilhermedeazevedosilveira222407638091358519415284",
				message.getRecurrenceId());
	}
	

	@Test
	public void testMoipPaymentMessage() {
		String created = "PAID	2013-01-18T01:01:00.000-02:00	1358519415284guilhermedeazevedosilveira222407638091358519415284	9999";

		MOIPMessage message = new MOIPMessage(created);
		assertEquals(TransactionType.RECURRENCE_PAYMENT, message.getType());
		assertTrue(new DateTime(2013, 6, 18, 12, 29, 57,
				DateTimeZone.forID("-07:00")).isEqual(message.getDate()));
		assertEquals(
				"1358519415284guilhermedeazevedosilveira222407638091358519415284",
				message.getRecurrenceId());
		
		assertEquals(
				new BigDecimal("99.99"),
				message.getAmout());
	}
	
	@Test
	public void testMoipSuspendedMessage() {
		String created = "SUSPENDED	2013-01-18T01:01:00.000-02:00	1358519415284guilhermedeazevedosilveira222407638091358519415284	9999";

		MOIPMessage message = new MOIPMessage(created);
		assertEquals(TransactionType.RECURRENCE_CANCELED, message.getType());
	}
	
	@Test
	public void testMoipCanceledMessage() {
		String created = "CANCELED	2013-01-18T01:01:00.000-02:00	1358519415284guilhermedeazevedosilveira222407638091358519415284	9999";

		MOIPMessage message = new MOIPMessage(created);
		assertEquals(TransactionType.RECURRENCE_CANCELED, message.getType());
	}
	
}
