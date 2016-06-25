
```scala
package ohnosequences.multisets

import compat.java8.FunctionConverters._

case class Kleisli[X,Y](val function: X => Multiset[Y]) extends AnyVal {

  def >=>[Z](g: Kleisli[Y,Z]): Kleisli[X,Z] = Kleisli { x: X => function(x) flatMap g.function.asJava }

  def apply(x: X): Multiset[Y] = function(x)

  def ⊕[Z,W](g: Kleisli[Z,W]): Kleisli[X Either Z, Y Either W] = Kleisli {

    case Left(x) => Multiset(Left[Y,W](_:Y): Y Either W)(function(x))
    case Right(y) => Multiset(Right[Y,W](_:W): Y Either W)(g(y))
  }

  def ⊗[Z,W](g: Kleisli[Z,W]): Kleisli[(X,Z), (Y,W)] = Kleisli {

    case (x,y) => {

      val fa: Multiset[Y] = function(x)
      val gb: Multiset[W] = g(y)

      val map = java.FastMap.withExpectedSize[(Y,W)](fa.size * gb.size)

      fa.forEach( { (y: Y, n: Long) => {
            gb.forEach( { (w: W, m: Long) => map.addValue((y,w), m*n): Unit }.asJava )
          }
        }.asJava
      )

      map
    }
  }
}

```




[test/scala/Multisets.scala]: ../../test/scala/Multisets.scala.md
[main/java/FastMap.java]: ../java/FastMap.java.md
[main/scala/package.scala]: package.scala.md
[main/scala/Multisets.scala]: Multisets.scala.md