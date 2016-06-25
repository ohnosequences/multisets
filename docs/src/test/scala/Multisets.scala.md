
```scala
package ohnosequences.multisets.test

import org.scalatest.FunSuite

import ohnosequences.multisets._

class Multisets extends FunSuite {

  test("Basic construction") {

    val wordCounts = multisetFrom( Seq("hola" -> 12L, "buh" -> 5L, "tralara" -> 7L) )
    
    val bookCopies: Unit => Multiset[String] = _ => multisetFrom( Seq("La Ilíada" -> 32L, "La Eneida" -> 421L ) )

    val bookWordCounts: String => Multiset[String] = {

      case "La Ilíada"  => multisetFrom( Seq("hola" -> 12L, "buh" -> 5L, "tralara" -> 7L) )

      case "La Eneida"  => multisetFrom( Seq("hola" -> 3L, "bien" -> 43L ) )

      case _            => multisetFrom(Seq())
    }

    println { (bookCopies >=> bookWordCounts)( () ) }

    val books = bookCopies( () )

    println { Multiset((_:String).length)(books) }
  }
}

```




[test/scala/Multisets.scala]: Multisets.scala.md
[main/java/FastMap.java]: ../../main/java/FastMap.java.md
[main/scala/package.scala]: ../../main/scala/package.scala.md
[main/scala/Multisets.scala]: ../../main/scala/Multisets.scala.md