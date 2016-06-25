
```java
package ohnosequences.multisets.java;

import com.koloboke.compile.*;
import java.util.Map;
import java.util.function.*;

@KolobokeMap
@ConcurrentModificationUnchecked
public abstract class FastMap<K> {

  public static <K> FastMap<K> withExpectedSize(int expectedSize) {
      return new KolobokeFastMap<K>(expectedSize);
  }

  public abstract void justPut(K key, long value);

  public abstract long addValue(K key, long addition);

  public abstract long getLong(K key);

  public abstract int size();

  abstract long merge(
    K key,
    long value,
    LongBinaryOperator remappingFunction
  );

  abstract public void forEach(ObjLongConsumer<? super K> action);

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

```




[test/scala/Multisets.scala]: ../../test/scala/Multisets.scala.md
[main/java/FastMap.java]: FastMap.java.md
[main/scala/package.scala]: ../scala/package.scala.md
[main/scala/Multisets.scala]: ../scala/Multisets.scala.md