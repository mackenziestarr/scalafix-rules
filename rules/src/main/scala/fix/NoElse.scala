package fix

import scalafix.Diagnostic
import scalafix.Patch
import scalafix.lint.LintSeverity
import scalafix.v1.SyntacticDocument
import scalafix.v1.SyntacticRule
import scala.meta.Lit
import scala.meta.Term
import scala.meta.inputs.Position

class NoElse extends SyntacticRule("NoElse") {
  override def fix(implicit doc: SyntacticDocument): Patch = {
    doc.tree.collect { case t @ Term.If(_, _, Lit.Unit()) =>
      Patch.lint(
        NoElseWarn(t.pos)
      )
    }.asPatch.atomic
  }
}

case class NoElseWarn(override val position: Position) extends Diagnostic {
  override def message = "add `else`"
  override def severity = LintSeverity.Warning
}
