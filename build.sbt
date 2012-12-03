name := "BNA2 Web"

version := "0.0.1"

organization := "de.schwetschke.bna2"

scalaVersion := "2.9.1"

resolvers ++= Seq("snapshots"     at "http://oss.sonatype.org/content/repositories/snapshots",
                "releases"        at "http://oss.sonatype.org/content/repositories/releases"
                )

// Add WAR file building and integrated Jetty
// Start/stop Jetty with "container:start"/"container:stop"
// Reload WAR with "container:reload ,context path>"
// Example: "~;container:start; container:reload /"

seq(com.github.siasia.WebPlugin.webSettings :_*)

port in container.Configuration := 8081

//javaOptions in (Test, run) += "-Xmx200M"

scalacOptions ++= Seq("-deprecation", "-unchecked")

libraryDependencies ++= {
  val liftVersion = "2.5-SNAPSHOT"
  Seq(
    "net.liftweb"       %% "lift-webkit"        % liftVersion        % "compile",
    "net.liftmodules"   %% "lift-jquery-module" % (liftVersion + "-1.0-SNAPSHOT"),
    "org.eclipse.jetty" % "jetty-webapp"        % "7.5.4.v20111024"  % "container; test; compile",
    "ch.qos.logback"    % "logback-classic"     % "1.0.6",
    "org.specs2"        %% "specs2"             % "1.11"             % "test"
  )
}
