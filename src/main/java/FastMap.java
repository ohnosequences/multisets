package ohnosequences.multisets.java;

import com.koloboke.compile.*;
// import com.koloboke.
import java.util.Map;
import java.util.function.*;

@KolobokeMap
@ConcurrentModificationUnchecked
public abstract class FastMap<K> {

  public static <K> FastMap<K> withExpectedSize(int expectedSize) {
      return new KolobokeFastMap<K>(expectedSize);
  }

  abstract void justPut(K key, long value);

  abstract long addValue(K key, long addition);

  abstract long getLong(K key);

  abstract int size();

  abstract long merge(
    K key,
    long value,
    LongBinaryOperator remappingFunction
  );

  abstract void forEach(ObjLongConsumer<? super K> action);

  public <X> FastMap<X> flatMap(Function<K,FastMap<X>> f) {

    // random number
    FastMap<X> xs = withExpectedSize( this.size() );

    this.forEach(
      (k,n) -> {
        FastMap<X> fk = f.apply(k);
        fk.forEach(
          (x,m) -> xs.addValue(x, m*n)
        );
      }
    );

    return xs;
  }
}
