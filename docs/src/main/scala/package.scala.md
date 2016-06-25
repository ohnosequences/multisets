
```scala
package ohnosequences

import compat.java8.FunctionConverters._

package object multisets {

  type Multiset[X] = multisets.java.FastMap[X]

  case object Multiset {

    def apply[X,Y](f: X => Y): Multiset[X] => Multiset[Y] = {

      xs: Multiset[X] => {

        val m = java.FastMap.withExpectedSize[Y]( xs.size )

        xs forEach { (x:X, n: Long) => m.addValue(f(x), n): Unit }.asJava

        m
      }
    }
  }

  def multisetFrom[X](elementCounts: Seq[(X,Long)]): Multiset[X] = {

    val map = java.FastMap.withExpectedSize[X](elementCounts.size)

    elementCounts foreach { case (x,n) => map.addValue(x,n) }

    map
  }

  def unit[X]: Kleisli[X,X] = { x: X => { val xm = java.FastMap.withExpectedSize[X](1); xm.justPut(x,1L); xm } }

  def swap[A,B]: Kleisli[(A,B), (B,A)] = Kleisli { case (a,b) => unit( (b,a) ) }

  implicit def kleisli[X,Y](f: X => Multiset[Y]): Kleisli[X,Y] = Kleisli(f)

}

```




[test/scala/Multisets.scala]: ../../test/scala/Multisets.scala.md
[main/java/FastMap.java]: ../java/FastMap.java.md
[main/scala/package.scala]: package.scala.md
[main/scala/Multisets.scala]: Multisets.scala.md