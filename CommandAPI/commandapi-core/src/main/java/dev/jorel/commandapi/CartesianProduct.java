package dev.jorel.commandapi;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * A class to compute the cartesian product of a number of lists. From Rosetta
 * Code:
 * https://rosettacode.org/wiki/Cartesian_product_of_two_or_more_lists#Java
 */
public class CartesianProduct {

	public static List<?> product(List<?>... a) {
		if (a.length >= 2) {
			List<?> product = a[0];
			for (int i = 1; i < a.length; i++) {
				product = product(product, a[i]);
			}
			return product;
		}

		return emptyList();
	}

	private static <A, B> List<?> product(List<A> a, List<B> b) {
		return of(a.stream().map(e1 -> of(b.stream().map(e2 -> asList(e1, e2)).collect(toList())).orElse(emptyList()))
				.flatMap(List::stream).collect(toList())).orElse(emptyList());
	}
	
	public static void flatten(List<List<?>> list) {
		ListIterator<List<?>> listIterator = list.listIterator();
		while(listIterator.hasNext()) {
			List<?> innerList = listIterator.next();
			listIterator.set(flattenL(innerList));
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<?> flattenL(List<?> list) {
		List returnVal = new ArrayList<>();
		for(Object o : list) {
			if(o instanceof List) {
				returnVal.addAll(flattenL((List) o));
			} else {
				returnVal.add(o);
			}
		}
		return returnVal;
	}
}