package fix

import scalafix.Patch
import scalafix.v1.SyntacticDocument
import scalafix.v1.SyntacticRule
import scala.meta.Term

class FilterSize extends SyntacticRule("FilterSize") {
  override def fix(implicit doc: SyntacticDocument): Patch = {
    doc.tree.collect {
      case t @ Term.Select(
            Term.Apply.Initial(
              Term.Select(
                obj,
                Term.Name("filter")
              ),
              f :: Nil
            ),
            Term.Name("size" | "length")
          ) =>
        Patch.replaceTree(t, s"${obj}.count($f)")
    }.asPatch
  }
}
