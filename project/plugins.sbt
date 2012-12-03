// Plugin for building web apps
// Se https://github.com/siasia/xsbt-web-plugin/wiki
libraryDependencies <+= sbtVersion(v => v match {
case "0.11.0" => "com.github.siasia" %% "xsbt-web-plugin" % "0.11.0-0.2.8"
case "0.11.1" => "com.github.siasia" %% "xsbt-web-plugin" % "0.11.1-0.2.10"
case "0.11.2" => "com.github.siasia" %% "xsbt-web-plugin" % "0.11.2-0.2.11"
case "0.11.3" => "com.github.siasia" %% "xsbt-web-plugin" % "0.11.3-0.2.11.1"
case x if (x.startsWith("0.12")) => "com.github.siasia" %% "xsbt-web-plugin" % "0.12.0-0.2.11.1"
})

//Create project files for IntelliJ IDEA with "sbt gen-idea"
addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.1.0")

//Create Eclipse project file with "sbt eclipse"
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.1.0")
