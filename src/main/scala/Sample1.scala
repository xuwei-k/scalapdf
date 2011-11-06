package com.github.xuwei_k.scalapdf

import scalaz._
import Scalaz.{resource => _,_}
import resource.{managed}
import com.itextpdf.text.{List => _,_}
import com.itextpdf.text.pdf._
import java.io.{File,FileOutputStream,BufferedOutputStream}
import java.net.URI

object Sample1{

  val f = new Font(
    BaseFont.createFont("HeiseiKakuGo-W5","UniJIS-UCS2-H",BaseFont.NOT_EMBEDDED)
    ,20
  )

  val FILE_NAME = "test.pdf"

  def main(args:Array[String] ){

    val func = for{
      out    <- managed(new FileOutputStream(FILE_NAME))
      bout   <- managed(new BufferedOutputStream(out))
      doc    <- managed(new Document(new Rectangle(0,0,500,500)))
      writer <- managed(PdfWriter.getInstance(doc, bout))
    }yield{
      doc.open()
      doc.add(new Paragraph("geso geso あばばばば",f))
    }
  
    {(_:List[_]).foreach(println)} <-: func.acquireFor(identity) :-> {_ => open(FILE_NAME)}
  }

  def open(file:String){
    val dsk = Class.forName("java.awt.Desktop")
    dsk.getMethod("browse", classOf[URI]).invoke(
      dsk.getMethod("getDesktop").invoke(null), 
      new URI("file://" + new File("").getAbsolutePath + "/" + file)
    )
  }
}

