package com.github.xuwei_k.scalapdf

import scalaz._
import Scalaz.{resource => _,_}
import resource.{managed}
import com.itextpdf.text.{List => _,_}
import com.itextpdf.text.pdf._
import com.tristanhunt.knockoff.DefaultDiscounter.knockoff
import java.io.{File,FileOutputStream,BufferedOutputStream}
import java.net.URI

object Sample1{

  def f(size:Int = 20,style:Int = Font.NORMAL,color:BaseColor = BaseColor.BLACK) = new Font(
     BaseFont.createFont("HeiseiKakuGo-W5","UniJIS-UCS2-H",BaseFont.NOT_EMBEDDED)
    ,size
    ,style
    ,color
  )

  val FILE_NAME = "test.pdf"

  def main(args:Array[String] ){

    val func = for{
      out    <- managed(new FileOutputStream(FILE_NAME))
      bout   <- managed(new BufferedOutputStream(out))
      doc    <- managed(new Document(PageSize.A4,50,50,50,50))
      writer <- managed(PdfWriter.getInstance(doc, bout))
    }yield{
      doc.open()
      f2(doc)
    }
  
    {(_:List[_]).foreach(println)} <-: func.acquireFor(identity) :-> {_ => open(FILE_NAME)}
  }

  val data =
    """# head 1
      |## head 2
      |### head 3
      |#### head 4
      |##### head 5
      |###### head 6
      |
      |[GitHub](http://github.com)
      |
      |* Item1 111
      |* Item2 222
      |
      |> hoge
      |> abababa
      |
      |`
      |def sum(a:Int,b:Int) = {
      |  a + b
      |}
      |`
      |
      |1. hoge1
      |2. hoge2""".stripMargin


  def f2(doc:Document){
       val list = knockoff(data)
    list.foreach(println)
    val a = list.flatMap{ObjectMapping.convertBlock}
    a.foreach(println) 
    a.foreach(doc.add)
  }

  def f1(doc:Document){
    doc.add(new Paragraph("geso geso あばばばば",f()))
    doc.add(new Chunk("github",f(14)).setAnchor("http://github.com").setTextRise(10))
    doc.newPage()
    doc.add(new Paragraph("iiii",f()))
  }

  def open(file:String){
    val dsk = Class.forName("java.awt.Desktop")
    dsk.getMethod("browse", classOf[URI]).invoke(
      dsk.getMethod("getDesktop").invoke(null), 
      new URI("file://" + new File("").getAbsolutePath + "/" + file)
    )
  }
}

