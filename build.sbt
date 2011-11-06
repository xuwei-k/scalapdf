name := "scalapdf"

version := "0.1-SNAPSHOT"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
   // "com.itextpdf" % "itextpdf" % "5.1.2" // これとどういう関係なのだろうか？
   "com.lowagie" % "itext" % "2.1.7"
  ,"com.github.jsuereth.scala-arm" %% "scala-arm" % "1.0"
)

initialCommands in Compile in console := {
  Iterator(
     "com.lowagie.text._"
    ,"com.lowagie.text.pdf._"
  ).map("import "+).mkString("\n")
}

