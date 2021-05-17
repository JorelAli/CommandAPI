/*******************************************************************************
 * Copyright 2018, 2021 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
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