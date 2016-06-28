
```java
package ohnosequences.multisets.java;

import com.koloboke.compile.*;
import com.koloboke.function.ObjLongPredicate;
import java.util.Map;
import java.util.function.*;

@KolobokeMap
@ConcurrentModificationUnchecked
public abstract class FastMap<K> {

  public static <K> FastMap<K> withExpectedSize(int expectedSize) {

    return new KolobokeFastMap<K>(expectedSize);
  }

  public abstract int size();

  public abstract void justPut(K key, long value);
  public abstract long addValue(K key, long addition);

  public abstract long getLong(K key);

  abstract public void    forEach(ObjLongConsumer<? super K> action);
  abstract public boolean forEachWhile(ObjLongPredicate<? super K> predicate);

  abstract long merge(
  K key,
  long value,
  LongBinaryOperator remappingFunction
  );

  public final boolean equals(Object other) {

    if ( !(other instanceof FastMap) ) return false; else if( size() != ((FastMap<K>) other).size() ) return false; else
      return this.forEachWhile( (k,v) -> v == ((FastMap<K>)other).getLong(k) );
  }


  public final FastMap<K> withAddedValue(K key, long addition) {

    addValue(key, addition);
    return this;
  }

  public <X> FastMap<X> flatMap(Function<K,FastMap<X>> f) {

    // random number
    FastMap<X> xs = withExpectedSize( this.size() );

    this.forEach(
      (k,n) -> {
        FastMap<X> fk = f.apply(k);
        fk.forEach(
          (x,m) -> xs.addValue(x, m * n)
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