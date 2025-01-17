package fix

import scalafix.Patch
import scalafix.v1.SyntacticDocument
import scalafix.v1.SyntacticRule
import scala.meta.Defn
import scala.meta.Template

class RemoveEmptyObject extends SyntacticRule("RemoveEmptyObject") {
  override def fix(implicit doc: SyntacticDocument): Patch = {
    doc.tree.collect { case t @ Defn.Object(Nil, _, Template.After_4_4_0(Nil, Nil, _, Nil, Nil)) =>
      Patch.removeTokens(t.tokens)
    }.asPatch
  }
}
