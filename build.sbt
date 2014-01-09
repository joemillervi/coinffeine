name := "bitmarket"

version := "0.1-SNAPSHOT"

organization in ThisBuild := "com.bitwise"

scalaVersion in ThisBuild := "2.10.3"

scalacOptions in ThisBuild ++= Seq("-deprecation", "-feature")

javacOptions in ThisBuild ++= Seq("-source", "1.7")

resolvers in ThisBuild ++= Seq(
  "nexus-releases" at "http://bitmarket.no-ip.biz:8086/nexus/content/repositories/releases",
  "bitcoinj" at "http://distribution.bitcoinj.googlecode.com/git/releases/",
  "sonatype-releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
)

libraryDependencies in ThisBuild ++= Seq(
  Dependencies.commonsConfig,
  Dependencies.guava,
  Dependencies.hamcrest,
  Dependencies.jodaTime,
  Dependencies.junit,
  Dependencies.junitInterface,
  Dependencies.logbackClassic,
  Dependencies.logbackCore,
  Dependencies.mockito,
  Dependencies.scalatest,
  Dependencies.slf4j
)
