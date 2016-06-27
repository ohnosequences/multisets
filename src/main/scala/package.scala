package ohnosequences

import compat.java8.FunctionConverters._

package object multisets {

  def unit[X]: Kleisli[X,X] = { x: X => { val xm = java.FastMap.withExpectedSize[X](1); xm.justPut(x,1L); Multiset(xm) } }

  def swap[A,B]: Kleisli[(A,B), (B,A)] = Kleisli { case (a,b) => unit( (b,a) ) }

  implicit def kleisli[X,Y](f: X => Multiset[Y]): Kleisli[X,Y] = Kleisli(f)
}
