package br.com.caelum.analise.paypal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import br.com.caelum.analise.Recurrence;
import br.com.caelum.analise.RecurrenceAnalyzer;
import br.com.caelum.analise.RecurrenceFilter;
import br.com.caelum.analise.RecurrenceType;
import br.com.caelum.analise.RecurrenceTypeFilter;

/**
 * Transacao I-RN98NF4PXWFL é a que  começou a ser logada por completo, no dia 7/5/2013
 * 
 * @author peas
 *
 */
public class IPNReader {
	static Logger logger = Logger.getLogger(IPNReader.class);

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		IPNAnalyzer analyzer = new IPNAnalyzer();
		Scanner s = new Scanner(new InputStreamReader(new FileInputStream(
				//"/Users/peas/Desktop/Dropbox/tmp/ipns/ipns-plus-gui.txt"), "ISO-8859-1"));
				"/Users/peas/Desktop/Dropbox/tmp/ipns/paulo-aniche-paypal.txt"), "ISO-8859-1"));
				
		while (s.hasNextLine()) {
			String line = s.nextLine();
			
			if (!line.startsWith("residence")) {
				if (line.contains("recurring_payment_id"))
					throw new IllegalStateException();
				continue;
			}
			
			if(line.contains("silveira@caelum") ||  line.contains("aniche") || line.contains("paulo@paulo.com.br")) {
				continue;
			}
			
			IPN ipn = new IPN(line);

			if (!ipn.isRecurring()) {
				continue;
			}


			if (ipn.hasKey("payment_status")
					&& ipn.extract("payment_status")
							.equals("Canceled_Reversal")) {
				logger.info("Reversed? " + line);
				continue;
			}
			analyzer.addIPN(ipn);
		}
		
		System.out.println(s.ioException());
		List<Recurrence> recurrences = analyzer.getRecurrences();

		List<? extends RecurrenceFilter> filters = Arrays.asList(
				new RecurrenceTypeFilter(RecurrenceType.MOIP_99),
				new RecurrenceTypeFilter(RecurrenceType.MONTHLY_99),
				new RecurrenceTypeFilter(RecurrenceType.MONTHLY_149), new RecurrenceTypeFilter(
						RecurrenceType.SEMIANNUAL_699), new RecurrenceTypeFilter(
						RecurrenceType.MONTHLY_197));

		System.out.println(recurrences.size());
		for (RecurrenceFilter f : filters) {
			RecurrenceAnalyzer rAnalyzer = new RecurrenceAnalyzer();
			
			
			for (Recurrence r : recurrences) {
				if (!f.filter(r))
					continue;
				
				rAnalyzer.add(r);
			}
			
			System.out.println("Resumo do filtro " + f);
			System.out.println();
			logger.info(rAnalyzer);
			rAnalyzer.showDeltas();
		}

	}
}
