package ohnosequences.multisets

import compat.java8.FunctionConverters._

import annotation.unchecked.{ uncheckedVariance => uv }

trait AnyMultiset extends Any {

  def jMap: java.FastMap[Elements]

  type Elements
}

case class Multiset[+X] private[multisets] (val jMap: java.FastMap[X @uv]) extends AnyVal with AnyMultiset {

  type Elements = X @uv
}

case class MultisetOps[E](val jMap: java.FastMap[E]) {

  // NOTE this is safe as jMap will return 0 if not present
  def apply(elem: E): Long = jMap getLong elem

  def :+(elem: E, n: Long): Multiset[E] = Multiset( if ( n > 0 ) jMap.withAddedValue(elem, n) else jMap )

  def ++(other: Multiset[E]): Multiset[E] = Multiset {

    val (small, big) = if( other.jMap.size < jMap.size ) ( other.jMap, jMap ) else ( jMap, other.jMap )

    val union = java.FastMap.withExpectedSize[E]( big.size + ( (big.size - small.size) / 2 ) )

    small.forEach( { (e: E, n: Long) => big.addValue(e,n): Unit }.asJava )

    big
  }

  def fold[O](init: O)(f: (O,E) => O): O = if (jMap.size == 0) init else {

    var out = init

    @annotation.tailrec
    def nTimes(accum: O, e: E, times: Long): O = if (times == 0) accum else nTimes(f(accum, e), e, times - 1)

    jMap.forEach( { (e: E, n: Long) => { out = nTimes(out, e, n) } }.asJava )

    out
  }

  def flatMap[O](f: E => Multiset[O]): Multiset[O] = Multiset { jMap flatMap { e: E => f(e).jMap }.asJava }

  def size: Long = fold(0L){ (n,_) => n + 1 }
}

case object Multiset {

  implicit final def ops[X](multiset: Multiset[X]): MultisetOps[X] = MultisetOps(multiset.jMap)

  def apply[X,Y](f: X => Y): Multiset[X] => Multiset[Y] = {

    xs: Multiset[X] => {

      val m = java.FastMap.withExpectedSize[Y]( xs.jMap.size )

      xs.jMap forEach { (x:X, n: Long) => m.addValue(f(x), n): Unit }.asJava

      Multiset(m)
    }
  }

  def from[X](elementCounts: Seq[(X,Long)]): Multiset[X] = {

    val map = java.FastMap.withExpectedSize[X](elementCounts.size)

    elementCounts foreach { case (x,n) if (n > 0) => map.addValue(x,n) }

    Multiset(map)
  }

  def fromElements[X](elementCounts: Seq[X]): Multiset[X] = {

    val map = java.FastMap.withExpectedSize[X](elementCounts.size / 2)

    elementCounts foreach { map.addValue(_,1L) }

    Multiset(map)
  }

  private lazy val _empty: Multiset[Nothing] = Multiset(java.FastMap.withExpectedSize(0))

  def empty[X]: Multiset[X] = _empty
}

case class Kleisli[X,Y](val function: X => Multiset[Y]) extends AnyVal {

  def >=>[Z](g: Kleisli[Y,Z]): Kleisli[X,Z] =
    Kleisli { x: X => this(x) flatMap g.function }

  def apply(x: X): Multiset[Y] = function(x)

  def ⊕[Z,W](g: Kleisli[Z,W]): Kleisli[X Either Z, Y Either W] = Kleisli {

    case Left(x) => Multiset(Left[Y,W](_:Y): Y Either W)(function(x))
    case Right(y) => Multiset(Right[Y,W](_:W): Y Either W)(g(y))
  }

  def ⊗[Z,W](g: Kleisli[Z,W]): Kleisli[(X,Z), (Y,W)] = Kleisli {

    case (x,y) => {

      val fa: Multiset[Y] = function(x)
      val gb: Multiset[W] = g(y)

      val map = java.FastMap.withExpectedSize[(Y,W)](fa.jMap.size * gb.jMap.size)

      fa.jMap.forEach( { (y: Y, n: Long) => {
            gb.jMap.forEach( { (w: W, m: Long) => map.addValue((y,w), m*n): Unit }.asJava )
          }
        }.asJava
      )

      Multiset(map)
    }
  }
}
