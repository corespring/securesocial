import sbt._
import Keys._
import play.Project._
import scala.Some

object ApplicationBuild extends Build {

  val appName = "securesocial"
  val baseVersion = "master"

  lazy val appVersion = {
    val other = Process("git rev-parse --short HEAD").lines.head
    baseVersion + "-" + other
  }

  val appDependencies = Seq(
    cache,
    "com.typesafe" %% "play-plugins-util" % "2.2.0",
    "com.typesafe" %% "play-plugins-mailer" % "2.2.0",
    "org.mindrot" % "jbcrypt" % "0.3m"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
  ).settings(
      publishMavenStyle := true,
      organization := "org.corespring",
      resolvers ++= Seq(
        "jBCrypt Repository" at "http://repo1.maven.org/maven2/org/",
        "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
      ),
      credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
      publishTo <<= version {
        (v: String) =>
          def isSnapshot = v.trim.contains("-")
          val base = "http://repository.corespring.org/artifactory"
          val repoType = if (isSnapshot) "snapshot" else "release"
          val finalPath = base + "/ivy-" + repoType + "s"
          Some("Artifactory Realm" at finalPath)
      }
    )

}
