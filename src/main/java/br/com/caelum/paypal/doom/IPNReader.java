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
				new PriceFilter(RecurrenceType.MONTHLY_99),
				new PriceFilter(RecurrenceType.MONTHLY_149), new PriceFilter(
						RecurrenceType.SEMIANNUAL_699), new PriceFilter(
						RecurrenceType.MONTHLY_197));

		for (RecurrenceFilter f : filters) {
			int novasComPagamentos = 0, novasRecuperadas = 0, novasSemPagamentos = 0, novasCanceladas = 0, novas = 0;
			RecurrenceAnalyzer rAnalyzer = new RecurrenceAnalyzer();
			
			System.out.println("novo filtro");
			System.out.println();
			for (Recurrence r : recurrences) {
				if (!f.filter(r))
					continue;
				
				rAnalyzer.add(r);
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
			logger.info(rAnalyzer);
		}

	}
}
