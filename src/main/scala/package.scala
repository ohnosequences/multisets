package ohnosequences

package object multisets {

  type Multiset[X] = multisets.java.FastMap[X]

  def multisetFrom[X](elementCounts: Seq[(X,Long)]): Multiset[X] = {

    val map = java.FastMap.withExpectedSize[X](elementCounts.size)

    elementCounts foreach { case (x,n) => map.addValue(x,n) }

    map
  }
}
