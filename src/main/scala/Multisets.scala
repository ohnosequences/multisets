package ohnosequences.multisets

import compat.java8.FunctionConverters._

case class Kleisli[X,Y](val f: X => Multiset[Y]) extends AnyVal {

  def andThen[Z](g: Y => Multiset[Z]): X => Multiset[Z] = { x: X => f(x) flatMap g.asJava }
}
