organization := "com.example"

name := "scalatra-todo"

version := "1.0"

scalaVersion := "2.9.1"

sbtVersion := "0.11.2"

seq(webSettings: _*)

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % "2.1.0-SNAPSHOT",
  "org.scalatra" %% "scalatra-scalate" % "2.1.0-SNAPSHOT",
  "org.scalatra" %% "scalatra-auth" % "2.1.0-SNAPSHOT",
  "org.scalatra" %% "scalatra-akka" % "2.1.0-SNAPSHOT",
  "org.eclipse.jetty" % "jetty-webapp" % "8.0.0.v20110901" % "test;container",
  "javax.servlet" % "javax.servlet-api" % "3.0.1" % "provided",
  "net.liftweb" %% "lift-json" % "2.4-M5",
  "net.liftweb" %% "lift-mongodb-record" % "2.4-M5",
  "com.foursquare" %% "rogue" % "1.0.28" intransitive (),
  "org.slf4j" % "slf4j-simple" % "1.6.1" % "runtime"
)

resolvers += "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

resolvers += "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases"

resolvers += "Akka Repo" at "http://akka.io/repository"

resolvers += "FuseSource Snapshot Repository" at "http://repo.fusesource.com/nexus/content/groups/public"

resolvers += "FuseSource Snapshot Repository" at "http://repo.fusesource.com/nexus/content/repositories/snapshots"