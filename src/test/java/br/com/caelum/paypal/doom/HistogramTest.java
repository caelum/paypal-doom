package br.com.caelum.paypal.doom;

import org.junit.Assert;
import org.junit.Test;

import br.com.caelum.analise.Histogram;

public class HistogramTest {

	@Test
	public void testEmptyHistogram() {
		Histogram histogram = new Histogram();
		
		Assert.assertEquals(0, histogram.get(0));
		Assert.assertEquals(0, histogram.get(5));
		System.out.println(histogram);
	}
	
	@Test
	public void testSimpleHistogram() {
		Histogram histogram = new Histogram();
		histogram.add(0);
		histogram.add(0);
		histogram.add(1);
		histogram.add(3);
		
		Assert.assertEquals(2, histogram.get(0));
		Assert.assertEquals(1, histogram.get(3));
		System.out.println(histogram);
	}
}
