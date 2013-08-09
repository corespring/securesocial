import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "securesocial"
    val baseVersion     = "master"

    lazy val appVersion = {
      val other = Process("git rev-parse --short HEAD").lines.head
      baseVersion + "-" + other
    }

    val appDependencies = Seq(
      "com.typesafe" %% "play-plugins-util" % "2.1.0",
      "com.typesafe" %% "play-plugins-mailer" % "2.1.0",
      "org.mindrot" % "jbcrypt" % "0.3m"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
    ).settings(
      publishMavenStyle := false,
      organization := "org.corespring",
      resolvers ++= Seq(
        "jBCrypt Repository" at "http://repo1.maven.org/maven2/org/",
        "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
      ),
      credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
      publishTo <<= version {
        (v: String) =>
          def isSnapshot = v.trim.contains("-")
          val base = "http://ec2-107-22-19-173.compute-1.amazonaws.com/artifactory"
          val repoType = if (isSnapshot) "snapshot" else "release"
          val finalPath = base + "/ivy-" + repoType + "s"
          Some( "Artifactory Realm" at finalPath )
      }
    )

}
