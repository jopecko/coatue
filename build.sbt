lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.7"
    )),
    name := "scalatest-example"
  )

libraryDependencies += "org.specs2" %% "specs2-core" % "4.3.4" % Test
libraryDependencies += "org.specs2" %% "specs2-mock" % "4.3.4" % Test

scalacOptions in Test ++= Seq("-Yrangepos")