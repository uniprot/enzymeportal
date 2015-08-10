/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.base.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 *
 * @author joseph
 */
public class BaseUtil {

    /**
     * creates a list of lists where each list.size() <= <code>length</code>
     *
     * @param <T>
     * @param list
     * @param length
     * @return
     */
    public <T> List<List<T>> split(List<T> list, final int length) {
        List<List<T>> parts = new ArrayList<>();
        final int size = list.size();
        for (int i = 0; i < size; i += length) {
            parts.add(new ArrayList<>(
                    list.subList(i, Math.min(size, i + length)))
            );
        }
        return parts;
    }

    public static <T extends Object> List<List<T>> splitList(List<T> list, int targetSize) {
        List<List<T>> lists = new ArrayList<>();
        for (int i = 0; i < list.size(); i += targetSize) {
            lists.add(list.subList(i, Math.min(i + targetSize, list.size())));
        }
        return lists;
    }

    private static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allDoneFuture
                = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDoneFuture.thenApply(v
                -> futures.stream().
                map(future -> future.join()).
                collect(Collectors.<T>toList())
        );
    }

    private <T> CompletableFuture<T> transfromToCompletableFuture(Future<T> future) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
