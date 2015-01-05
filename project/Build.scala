import com.ebiznext.sbt.plugins.CxfWsdl2JavaPlugin
import com.ebiznext.sbt.plugins.CxfWsdl2JavaPlugin.cxf._
import io.gatling.sbt.GatlingPlugin
import sbt.Keys._
import sbt._
import sbtprotobuf.{ProtobufPlugin => PB}
import sbtscalaxb.Plugin.ScalaxbKeys._
import sbtscalaxb.Plugin.scalaxbSettings
import scoverage.ScoverageSbtPlugin

object Build extends sbt.Build {

  lazy val root = (Project(id = "coinffeine", base = file("."))
    aggregate(headless, peer, protocol, model, common, commonAkka, commonTest, gui)
    settings(ScoverageSbtPlugin.instrumentSettings: _*)
  )

  lazy val peer = (Project(id = "peer", base = file("coinffeine-peer"))
    settings(scalaxbSettings: _*)
    settings(
      sourceGenerators in Compile <+= scalaxb in Compile,
      packageName in (Compile, scalaxb) := "coinffeine.peer.payment.okpay.generated",
      dispatchVersion in (Compile, scalaxb) := Dependencies.Versions.dispatch,
      async in (Compile, scalaxb) := true
    )
    settings(ScoverageSbtPlugin.instrumentSettings: _*)
    dependsOn(
      model % "compile->compile;test->test",
      protocol % "compile->compile;test->test",
      commonAkka % "compile->compile;test->test",
      commonTest % "test->compile"
    )
  )

  lazy val protocol = (Project(id = "protocol", base = file("coinffeine-protocol"))
    settings(PB.protobufSettings: _*)
    settings(ScoverageSbtPlugin.instrumentSettings: _*)
    dependsOn(
      common,
      model % "compile->compile;test->test",
      commonAkka % "compile->compile;test->test",
      commonTest % "test->compile"
    )
  )

  lazy val model = (Project(id = "model", base = file("coinffeine-model"))
    settings(ScoverageSbtPlugin.instrumentSettings: _*)
    dependsOn(commonTest % "test->compile")
  )

  lazy val common = (Project(id = "common", base = file("coinffeine-common"))
    settings(ScoverageSbtPlugin.instrumentSettings: _*)
    dependsOn commonTest
  )

  lazy val commonAkka = (Project(id = "common-akka", base = file("coinffeine-common-akka"))
    settings(ScoverageSbtPlugin.instrumentSettings: _*)
    dependsOn commonTest
  )

  lazy val commonTest = (Project(id = "common-test", base = file("coinffeine-common-test"))
    settings(PB.protobufSettings: _*)
    settings(ScoverageSbtPlugin.instrumentSettings: _*)
  )

  lazy val headless = (Project(id = "headless", base = file("coinffeine-headless"))
    settings(ScoverageSbtPlugin.instrumentSettings: _*)
    settings(Assembly.settings("coinffeine.headless.Main"): _*)
    dependsOn(peer % "compile->compile;test->test", commonTest)
  )

  lazy val gui = (Project(id = "gui", base = file("coinffeine-gui"))
    configs IntegrationTest
    settings(Defaults.itSettings: _*)
    settings(ScoverageSbtPlugin.instrumentSettings: _*)
    dependsOn(peer % "compile->compile;test,it->test", commonTest)
  )

  lazy val benchmark = (Project(id = "benchmark", base = file("coinffeine-benchmark"))
    dependsOn(
      peer % "compile->compile;test->test",
      commonAkka % "compile->compile;test->test",
      commonTest % "compile->compile;test->compile"
    )
    enablePlugins GatlingPlugin
  )
}
