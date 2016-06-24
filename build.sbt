Nice.scalaProject

name          := "multisets"
organization  := "ohnosequences"
description   := "multisets project"

bucketSuffix  := "era7.com"

libraryDependencies ++= Seq(
  "com.koloboke"             % "koloboke-impl-common-jdk8"  % "1.0.0",
  "com.koloboke"             % "koloboke-compile"           % "0.5.1" % "provided",
  "org.scala-lang.modules"  %% "scala-java8-compat"         % "0.8.0-RC1"
) ++ testDependencies

val testDependencies = Seq("org.scalatest" %% "scalatest" % "2.2.6" % Test)
