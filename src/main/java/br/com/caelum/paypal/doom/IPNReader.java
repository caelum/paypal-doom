package br.com.caelum.paypal.doom;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

public class IPNReader {
	static Logger logger = Logger.getLogger(IPNReader.class);

	public static void main(String[] args) throws FileNotFoundException {

		IPNAnalyzer analyzer = new IPNAnalyzer();
		Scanner s = new Scanner(new File(
				"/Users/peas/Desktop/Dropbox/tmp/ipns.txt"));

		int total = 0;
		while (s.hasNextLine()) {
			String line = s.nextLine();
			if (!line.startsWith("residence")) {
				if (line.contains("recurring_payment_id"))
					throw new IllegalStateException();
				continue;
			}

			IPN ipn = new IPN(line);

			if (!ipn.isRecurring())
				continue;

			// TODO cuidar desse:
			if (ipn.hasKey("payment_status")
					&& ipn.extract("payment_status").equals("Reversed")) {
				logger.info("Reversed? " + line);
				continue;
			}

			if (ipn.hasKey("payment_status")
					&& ipn.extract("payment_status")
							.equals("Canceled_Reversal")) {
				logger.info("Reversed? " + line);
				continue;
			}

			total++;
			analyzer.addIPN(ipn);
		}

		logger.info("carregado");

		List<Recurrence> recurrences = analyzer.getRecurrences();
		logger.info("IPNs " + total);
		logger.info(recurrences.size());

		List<? extends RecurrenceFilter> filters = Arrays.asList(
				new RecurrenceFilter() {
					public boolean filter(Recurrence r) {
						return r.getProductName().contains("R$99.99")
								|| r.getProductName().contains("R$ 99.99");
					}
				}, new PriceFilter("R$699.99"), new PriceFilter("R$149.99"),
				new PriceFilter("R$197.00"));

		for (RecurrenceFilter f : filters) {
			int novasComPagamentos = 0, novasRecuperadas = 0, novasSemPagamentos = 0, novasCanceladas = 0, novas = 0;

			System.out.println("novo filtro");
			System.out.println();
			for (Recurrence r : recurrences) {
				if (!f.filter(r))
					continue;
				System.out.println(r);

				novas++;
				if (r.hasPayments()) {
					novasComPagamentos++;
					if (r.hasSkips())
						novasRecuperadas++;
				} else
					novasSemPagamentos++;
				if (r.isCanceled())
					novasCanceladas++;

			}
			logger.info(String.format(
					"novas %d(%d canceladas) pagas: %d(%d) nao pagas: %d",
					novas, novasCanceladas, novasComPagamentos,
					novasRecuperadas, novasSemPagamentos));
		}

	}
}
