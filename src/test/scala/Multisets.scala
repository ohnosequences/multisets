package ohnosequences.multisets.test

import org.scalatest.FunSuite

import ohnosequences.multisets._

class Multisets extends FunSuite {

  test("Basic construction") {

    val wordCounts = Multiset.from( Seq("hola" -> 12L, "buh" -> 5L, "tralara" -> 7L) )

    val bookCopies: Unit => Multiset[String] = _ => Multiset.from( Seq("La Ilíada" -> 32L, "La Eneida" -> 421L ) )

    val bookWordCounts: String => Multiset[String] = {

      case "La Ilíada"  => Multiset.from( Seq("hola" -> 12L, "buh" -> 5L, "tralara" -> 7L) )

      case "La Eneida"  => Multiset.from( Seq("hola" -> 3L, "bien" -> 43L ) )

      case _            => Multiset.from(Seq())
    }

    println { (bookCopies >=> bookWordCounts)( () ) }

    val books = bookCopies( () )

    println { Multiset((_:String).length)(books) }
  }

  test("Covariance") {

    val zz = Multiset.from[String]( Seq("hola" -> 12L, "buh" -> 5L) )

    val zzAny: Multiset[Any] = zz

    val uu = Multiset.from[Boolean]( Seq(true -> 12L, false -> 1L, true -> 5L) )

    val fun = zzAny ++ uu
    // val funString: Multiset[String] = fun

    val uuAgain = Multiset.from( Seq("hola" -> 12L, "buh" -> 5L) ++ Seq(true -> 12L, false -> 1L, true -> 5L) )

    assert { uuAgain == uu }
  }

  test("cardinality") {

    val u = Multiset.fromElements( Seq("hola", "hola", "buh", "feo", "lalala", "hola", "buh") )

    assert { u.size == 7 }

    assert { u === u.fold(Multiset.empty[String])( _ :+ (_, 1L) ) }
  }
}
