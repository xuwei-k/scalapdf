package com.github.xuwei_k.scalapdf

import resource.{managed}
import com.lowagie.text._
import com.lowagie.text.pdf._
import java.io.{FileOutputStream,BufferedOutputStream}

object Sample1{

  def main(args:Array[String] ){

    val func = for{
      out    <- managed(new FileOutputStream("test.pdf"))
      bout   <- managed(new BufferedOutputStream(out))
      doc    <- managed(new Document(new Rectangle(0,0,500,500)))
      writer <- managed(PdfWriter.getInstance(doc, bout))
    }yield{
      doc.open()
      doc.add(new Paragraph("ababa"))
      doc.add(new Paragraph("geso geso"))
    }
  
    func.acquireFor(identity).left.foreach{_.foreach(println)}
  }
}

