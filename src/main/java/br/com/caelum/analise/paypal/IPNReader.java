package br.com.caelum.analise.paypal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import br.com.caelum.analise.RecurrenceTypeFilter;
import br.com.caelum.analise.Recurrence;
import br.com.caelum.analise.RecurrenceAnalyzer;
import br.com.caelum.analise.RecurrenceFilter;
import br.com.caelum.analise.RecurrenceType;

public class IPNReader {
	static Logger logger = Logger.getLogger(IPNReader.class);

	public static void main(String[] args) throws FileNotFoundException {

		IPNAnalyzer analyzer = new IPNAnalyzer();
		Scanner s = new Scanner(new File(
				"/Users/peas/Desktop/Dropbox/tmp/ipns/moip-gui.txt"));

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


		List<Recurrence> recurrences = analyzer.getRecurrences();
		logger.info("IPNs " + total);
		logger.info(recurrences.size());

		List<? extends RecurrenceFilter> filters = Arrays.asList(
				new RecurrenceTypeFilter(RecurrenceType.MOIP_99),
				new RecurrenceTypeFilter(RecurrenceType.MONTHLY_99),
				new RecurrenceTypeFilter(RecurrenceType.MONTHLY_149), new RecurrenceTypeFilter(
						RecurrenceType.SEMIANNUAL_699), new RecurrenceTypeFilter(
						RecurrenceType.MONTHLY_197));

		for (RecurrenceFilter f : filters) {
			RecurrenceAnalyzer rAnalyzer = new RecurrenceAnalyzer();
			
			
			for (Recurrence r : recurrences) {
				if (!f.filter(r))
					continue;
				
				rAnalyzer.add(r);
				System.out.println(r);
			}
			
			System.out.println("Resumo do filtro " + f);
			System.out.println();
			logger.info(rAnalyzer);
		}

	}
}
