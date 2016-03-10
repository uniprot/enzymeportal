/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.util;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class StreamUtils {

    public static <T> Stream<T> stream(Iterator<T> sourceIterator) {
        return compute(sourceIterator, false);
    }

    public static <T> Stream<T> parallelStream(Iterator<T> sourceIterator) {

        return compute(sourceIterator, true);
    }

    private static <T> Stream<T> compute(Iterator<T> sourceIterator, boolean parallel) {
        Iterable<T> iterable = () -> sourceIterator;
        return StreamSupport.stream(iterable.spliterator(), parallel);
    }
}
