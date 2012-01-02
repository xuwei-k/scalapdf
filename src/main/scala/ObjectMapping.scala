package com.github.xuwei_k.scalapdf

import com.tristanhunt.knockoff.{Chunk=>_,Paragraph=>KnockPara,_}
import com.itextpdf.text.{
  List=>_,Paragraph=>_,
  Chunk,Element,Font,Phrase
}


object ObjectMapping{
  import Sample1.f
  
  def C(str:String) = new Chunk(str,f())

  def getContent(s:Span):String = s match{
    case Text(str)     => str
    case HTMLSpan(str) => str
    case CodeSpan(str) => str
  }

  def convertSpan(s:Span):Seq[Element] = s match{
    case Text(str)     => new Chunk(str) :: Nil
    case HTMLSpan(str) => new Chunk(str) :: Nil
    case CodeSpan(str) => new Chunk(str) :: Nil
    case Strong(Seq(Text(str))) => new Chunk(str,f(style = Font.BOLD)) :: Nil
    case Emphasis(seq) => seq.flatMap{convertSpan}
    case Link(List(Text(str)),url,title) => 
      new Chunk(str).setAnchor(url) :: Nil
    case IndirectLink(seq,link) => seq.flatMap{convertSpan}
    case ImageLink(seq,url,title) => seq.flatMap{convertSpan}
    case IndirectImageLink(seq,link) => seq.flatMap{convertSpan}
  }

  def convertBlock(b:Block):Seq[Element] = b match{
    case KnockPara(seq,p)     => seq.flatMap{convertSpan}
    case Header(level,seq,p)  => 
      new Phrase(seq.map{getContent}.mkString(" "),f(size = (20+(5*(7-level))))) :: Nil
    case LinkDefinition(id,url,title,p) => Nil
    case Blockquote(seq,p)    => seq.flatMap{convertBlock}
    case CodeBlock(str,p)     => new Chunk(str.content) :: Nil
    case HorizontalRule(p)    => Nil
    case OrderedItem(seq,p)   => seq.flatMap{convertBlock}
    case UnorderedItem(seq,p) => seq.flatMap{convertBlock}
    case OrderedList(seq)     => seq.flatMap{convertBlock}
    case UnorderedList(seq)   => seq.flatMap{convertBlock}
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
 
