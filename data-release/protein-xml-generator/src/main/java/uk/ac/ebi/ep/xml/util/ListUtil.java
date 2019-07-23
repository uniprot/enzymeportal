//package uk.ac.ebi.ep.xml.util;
//
//import com.oath.cyclops.react.collectors.lazy.MaxActive;
//import cyclops.futurestream.LazyReact;
//import java.time.Duration;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ForkJoinPool;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//import java.util.stream.Stream;
//import org.apache.commons.collections4.ListUtils;
//
///**
// *
// * @author joseph
// */
//public class ListUtil {
//
//    public static void main(String[] args) throws InterruptedException {
//        //final List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
//        final List<Integer> list = IntStream.rangeClosed(0, 1_000).boxed().collect(Collectors.toList());
//
////        System.out.println("By 2 : " + partition(list, 2));  // [[1, 2], [3, 4], [5, 6], [7]]
////        System.out.println(partition(list, 3));  // [[1, 2, 3], [4, 5, 6], [7]]
////
////        final AtomicInteger counter = new AtomicInteger(0);
////        final int size = 3;
////
////        final Collection<List<Integer>> partitioned = list.stream()
////                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / size))
////                .values();
////
////        partitioned.forEach(System.out::println);
////        System.out.println(" part " + partitioned.size());
////           System.out.println("By 3:");
////    batches(list, 3).forEach(index ->System.out.println("INDEX "+ index));
////
////    System.out.println("By 4:");
////    batches(list, 4).forEach(System.out::println);
////    
////    Map<Boolean, List<Integer>> groups = 
////      list.stream().collect(Collectors.partitioningBy(s -> s > size));
////    //List<List<Integer>> subSets = new ArrayList<List<Integer>>(groups.values());
////   
// System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "62");
// ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
//LazyReact builder = new LazyReact(forkJoinPool, true, MaxActive.CPU);
//builder.from(list)
//        .parallel().async()
//        //.grouped(50)
//        //.flatMap(x->x.stream().parallel())
//        //.parallel()
//        .map(ec -> process1(ec)).join();
//        //.forEach(ec -> process(ec));
//        System.out.println("POOL INFO "+ forkJoinPool);
////forkJoinPool.shutdownNow();
//
////        LazyReact.parallelCommonBuilder()
////                .async()
////                //.ofAsync(() ->enzyme.getEnzymePortalEcNumbersSet())
////                //.flatMap(x->x.stream())
////                .from(list)
////                .grouped(10).async()
////                .flatMap(x -> x.stream())
////                //.peek(data -> System.out.println(" CHECK "+ atom.getAndIncrement()))
////                .forEachAsync(ec -> process(ec));
////     
//
//    }
//    
//
//    private static void splitAndProcess(final List<Integer> list) {
//
//        List<List<Integer>> subSets = ListUtils.partition(list, list.size() / 10);
//
//        System.out.println("HOW MANY SUBSETS " + subSets.toArray().length);
//        System.out.println("SUBSETS " + subSets);
////        System.out.println("FIRST "+ subSets.get(0));
////        System.out.println("SECOND "+ subSets.get(1));
////         System.out.println("Third "+ subSets.get(2));
//
//        List<CompletableFuture<Void>> cfList = new ArrayList<>();
//        for (int x = 0; x < subSets.size(); x++) {
//
//            System.out.println("SUB " + subSets.get(x));
//            //final List<Integer> data = subSets.get(x);
//            final int index = x;
//            CompletableFuture<Void> cfOne
//                    = CompletableFuture.runAsync(() -> subSets.get(index).stream().forEach(d -> process(d)));
//
//            cfList.add(cfOne);
//
//            //subSets.get(index).parallelStream().forEach(d->process(d));
//        }
//
//        System.out.println(cfList.size() + " HOW MANY FUTURES " + cfList);
//        CompletableFuture<Void> allCfs = CompletableFuture.allOf(cfList.toArray(new CompletableFuture<?>[0]));
//        allCfs.join();
//
//    }
//
//    private static void process(int x) {
//        //System.nanoTime(); 
//        System.out.println(System.nanoTime() + " TIME : " + Duration.ofSeconds(System.currentTimeMillis()) + "  CALCULATE " + x);
//    }
//
//    private static int process1(int x) {
//        //System.nanoTime(); 
//        System.out.println(System.nanoTime() + " TIME : " + Duration.ofSeconds(System.currentTimeMillis()) + "  CALCULATE " + x);
//        return x;
//    }
//
//    private static <T> Collection<List<T>> partition(List<T> list, int size) {
//        final AtomicInteger counter = new AtomicInteger(0);
//
//        return list.stream()
//                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / size))
//                .values();
//    }
//
//    public static <T> Stream<List<T>> batches(List<T> source, int length) {
//        if (length <= 0) {
//            throw new IllegalArgumentException("length = " + length);
//        }
//        int size = source.size();
//        if (size <= 0) {
//            return Stream.empty();
//        }
//        int fullChunks = (size - 1) / length;
//        return IntStream.range(0, fullChunks + 1).mapToObj(
//                n -> source.subList(n * length, n == fullChunks ? size : (n + 1) * length));
//    }
//}
