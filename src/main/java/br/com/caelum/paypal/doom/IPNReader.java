package br.com.caelum.paypal.doom;

import java.io.File;
import java.io.FileNotFoundException;
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

		int antigasRecuperadas = 0, novasRecuperadas = 0;
		int antigasComPagamentos = 0, antigasSemPagamentos = 0, antigasCanceladas = 0, antigas = 0;
		int novasComPagamentos = 0, novasSemPagamentos = 0, novasCanceladas = 0, novas = 0;

		for (Recurrence r : recurrences) {

			System.out.println(r);
			if (r.getTimeCreated().isBefore(new DateTime(2013, 5, 7, 0, 0, 0))) {
				//continue;
			}
			if (r.getTimeCreated().isBefore(new DateTime(2013, 7, 5, 0, 0, 0))) {
				antigas++;
				if (r.hasPayments()) {
					antigasComPagamentos++;
					if (r.hasSkips())
						antigasRecuperadas++;
				} else
					antigasSemPagamentos++;
				if (r.isCanceled())
					antigasCanceladas++;
			} else {
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

		}

		logger.info(String.format(
				"antigas %d(%d canceladas) pagas: %d(%d) nao pagas: %d",
				antigas, antigasCanceladas, antigasComPagamentos,
				antigasRecuperadas, antigasSemPagamentos));
		logger.info(String.format(
				"novas %d(%d canceladas) pagas: %d(%d) nao pagas: %d", novas,
				novasCanceladas, novasComPagamentos, novasRecuperadas,
				novasSemPagamentos));
	}
}
