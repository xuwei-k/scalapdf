package com.github.xuwei_k.scalapdf

import com.tristanhunt.knockoff.{Chunk=>_,Paragraph=>KnockPara,_}
import com.itextpdf.text.{
  List=>_,
  Chunk,Element,Font,Phrase,Paragraph
}


object ObjectMapping{
  import Sample1.f

  val scalaWords = Set(
    "abstract ","case ","catch","class ","def ",
    "do","else","extends","false","final ",
    "finally","for","forSome"," if","implicit",
    "import ","lazy val","match ","new ","null",
    "object ","override ","package ","private ","protected",
    "return ","sealed ","super","this",
    "throw","trait ","try ","true"," type ",
    "val ","var ","while"," with "," yield"
  )
  
  def C(str:String) = new Chunk(str,f())

  def getContent(s:Span):String = s match{
    case Text(str)     => str
    case HTMLSpan(str) => str
    case CodeSpan(str) => str
  }

  def convertSpan(s:Span):Seq[Element] = s match{
    case Text(str)     => C(str) :: Nil
    case HTMLSpan(str) => C(str) :: Nil
    case CodeSpan(str) => C(str) :: Nil
/*      scalaWords.foldLeft(str){(r,k) => 
        if(! r.contains(k)) r
        else{
          r
        }
      } 
*/

    case Strong(Seq(Text(str))) => new Chunk(str,f(style = Font.BOLD)) :: Nil
    case Emphasis(seq) => seq.flatMap{convertSpan}
    case Link(List(Text(str)),url,title) => 
      C(str).setAnchor(url) :: Nil
    case IndirectLink(seq,link) => seq.flatMap{convertSpan}
    case ImageLink(seq,url,title) => seq.flatMap{convertSpan}
    case IndirectImageLink(seq,link) => seq.flatMap{convertSpan}
  }

  def convertBlock(b:Block):Seq[Element] = b match{
    case KnockPara(seq,p)     => seq.flatMap{convertSpan}
    case Header(level,seq,p)  => 
      new Paragraph(seq.map{getContent}.mkString(" "),f(size = (20+(5*(7-level))))) :: Nil
    case LinkDefinition(id,url,title,p) => Nil
    case Blockquote(seq,p)    => 
      assert(seq.forall{_.isInstanceOf[KnockPara]})
      seq.asInstanceOf[Seq[KnockPara]].map{
        case KnockPara(Seq(e),_) =>
        new Chunk(getContent(e),f(style = Font.ITALIC))
      } 
    case CodeBlock(str,p)     => C(str.content) :: Nil
    case HorizontalRule(p)    => Nil
//    case OrderedItem(seq,p)   => seq.flatMap{convertBlock}
//    case UnorderedItem(seq,p) => seq.flatMap{convertBlock}
    case OrderedList(seq)     => 
      assert(seq.forall{_.isInstanceOf[OrderedItem]})
      seq.asInstanceOf[Seq[OrderedItem]].zipWithIndex.map{
        case (OrderedItem(Seq(KnockPara(Seq(e),_)),_),i) =>
        C(i + " " + getContent(e))
      } 
    case UnorderedList(seq)   =>
      assert(seq.forall{_.isInstanceOf[UnorderedItem]})
      seq.asInstanceOf[Seq[UnorderedItem]].map{
        case UnorderedItem(Seq(KnockPara(Seq(e),_)),_) =>
        C("* " + getContent(e))
      } 
  }
  
}

/*

Text
HTMLSpan
CodeSpan
Strong
Emphasis
Link
IndirectLink
ImageLink
IndirectImageLink
Block
Paragraph
Header
LinkDefinition
Blockquote
CodeBlock
HorizontalRule
OrderedItem
UnorderedItem
OrderedList
UnorderedList

*/
 
