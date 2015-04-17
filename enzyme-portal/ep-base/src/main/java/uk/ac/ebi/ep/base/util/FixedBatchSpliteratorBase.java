/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.base.util;
import java.util.Comparator;
import java.util.Spliterator;
import static java.util.Spliterators.spliterator;
import java.util.function.Consumer;

/**
 *
 * @author joseph
 * @param <T>
 */
public abstract class FixedBatchSpliteratorBase<T> implements Spliterator<T> {
  private final int batchSize;
  private final int characteristics;
  private long est;

  public FixedBatchSpliteratorBase(int characteristics, int batchSize, long est) {
    characteristics |= ORDERED;
   
    if ((characteristics & SIZED) != 0) characteristics |= SUBSIZED;
    this.characteristics = characteristics;
    this.batchSize = batchSize;
    this.est = est;
  }
  public FixedBatchSpliteratorBase(int characteristics, int batchSize) {
    this(characteristics, batchSize, Long.MAX_VALUE);
  }
  public FixedBatchSpliteratorBase(int characteristics) {
    this(characteristics, 64, Long.MAX_VALUE);
    //this(characteristics, 128, Long.MAX_VALUE);
  }

  @Override public Spliterator<T> trySplit() {
    final HoldingConsumer<T> holder = new HoldingConsumer<>();
    if (!tryAdvance(holder)) return null;
    final Object[] a = new Object[batchSize];
    int j = 0;
    do a[j] = holder.value; while (++j < batchSize && tryAdvance(holder));
    if (est != Long.MAX_VALUE) est -= j;
    return spliterator(a, 0, j, characteristics());
  }
  @Override public Comparator<? super T> getComparator() {
    if (hasCharacteristics(SORTED)) return null;
    throw new IllegalStateException();
  }
  @Override public long estimateSize() { return est; }
  @Override public int characteristics() { return characteristics; }

  static final class HoldingConsumer<T> implements Consumer<T> {
    Object value;
    @Override public void accept(T value) { this.value = value; }
  }
}
