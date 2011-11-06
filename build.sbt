name := "scalapdf"

version := "0.1-SNAPSHOT"

scalaVersion := "2.9.1"

libraryDependencies ++= {
  val (itext,itextVersion) = ("com.itextpdf","5.1.1")
  Seq(
  // "com.lowagie" % "itext" % "2.1.7" //こっちは古いやつで、package名変わったらしい
     itext % "itextpdf" % itextVersion
    ,itext % "itext-asian" % itextVersion
    ,"com.github.jsuereth.scala-arm" %% "scala-arm" % "1.0"
    ,"org.scalaz" %% "scalaz-core" % "6.0.3"
  )
}

initialCommands in Compile in console := {
  Iterator(
     "scalaz._"
    ,"Scalaz.{resource => _,_}"
    ,"com.itextpdf.text.{List => _,_}"
    ,"com.itextpdf.text.pdf._"
    ,"com.github.xuwei_k.scalapdf._"
  ).map("import "+).mkString("\n")
}

