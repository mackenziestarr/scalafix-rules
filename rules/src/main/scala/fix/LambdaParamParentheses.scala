package fix

import scalafix.Diagnostic
import scalafix.Patch
import scalafix.lint.LintSeverity
import scalafix.v1.SyntacticDocument
import scalafix.v1.SyntacticRule
import scala.meta.Term
import scala.meta.Token
import scala.meta.Position

class LambdaParamParentheses extends SyntacticRule("LambdaParamParentheses") {
  override def isLinter = true

  override def fix(implicit doc: SyntacticDocument): Patch = {
    doc.tree.collect {
      case t1 @ Term.Function(List(param), _) if param.decltpe.nonEmpty && param.mods.isEmpty =>
        if (t1.tokens.find(_.is[Token.LeftParen]).exists(_.pos.start <= param.pos.start)) {
          Patch.empty
        } else {
          Patch.lint(
            LambdaParamParenthesesWarn(param.pos)
          )
        }
    }.asPatch.atomic
  }
}

case class LambdaParamParenthesesWarn(override val position: Position) extends Diagnostic {
  override def message = "add parentheses or remove explicit types for prepare Scala 3"
  override def severity = LintSeverity.Warning
}
