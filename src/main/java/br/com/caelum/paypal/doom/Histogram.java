package br.com.caelum.paypal.doom;

import java.util.HashMap;
import java.util.Map;

public class Histogram {

	private int total;
	private int maxKey = 0;
	private Map<Integer, Integer> values = new HashMap<>();

	public void add(int key) {
		if (key > maxKey)
			maxKey = key;

		total++;
		if (!values.containsKey(key)) {
			values.put(key, 0);
		}
		values.put(key, values.get(key) + 1);
	}

	public int getTotal() {
		return total;
	}
	
	public int getMaxKey() {
		return maxKey;
	}

	public int get(int key) {
		if (!values.containsKey(key)) {
			return 0;
		}
		return values.get(key);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("[Histogram: ");
		for (int i = 0; i <= maxKey; i++) {
			builder.append(String.format("%d:%d (%.1f%%), ", i, get(i),
					((double) get(i)) * 100 / total));
		}
		return builder.append("]").toString();
	}

}
