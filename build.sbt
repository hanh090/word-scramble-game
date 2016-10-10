name := """word-scramble-game"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "de.tudarmstadt.ukp.jwktl" % "jwktl" % "1.0.1",
  "net.sf.extjwnl" % "extjwnl" % "1.9.1",
  "net.sf.extjwnl" % "extjwnl-data-wn31" % "1.2"
)


fork in run := false

fork in Test := false
EclipseKeys.preTasks := Seq(compile in Compile)
EclipseKeys.projectFlavor := EclipseProjectFlavor.Java           // Java project. Don't expect Scala IDE
EclipseKeys.createSrc := EclipseCreateSrc.ValueSet(EclipseCreateSrc.ManagedClasses, EclipseCreateSrc.ManagedResources)