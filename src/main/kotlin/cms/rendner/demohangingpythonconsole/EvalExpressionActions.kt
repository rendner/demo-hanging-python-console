package cms.rendner.demohangingpythonconsole

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.ui.Messages
import com.jetbrains.python.debugger.PyDebugValue
import com.intellij.xdebugger.XDebuggerUtil

sealed class BaseEvalExpressionActions : AnAction(), DumbAware {
    override fun actionPerformed(event: AnActionEvent) {
        getSelectedDebugValue(event)?.let {
            val expr = "__import__('math').ceil(1.1)"
            val evalResult = it.frameAccessor.evaluate(expr, false, false)
            Messages.showMessageDialog(
                event.project,
                "Result: ${evalResult?.value!!}",
                this.javaClass.simpleName,
                Messages.getInformationIcon(),
            )
        }
    }

    protected fun getSelectedDebugValue(event: AnActionEvent): PyDebugValue? {
        return XDebuggerUtil.getInstance().getValueContainer(event.dataContext)?.let {
            if (it is PyDebugValue) it else null
        }
    }
}
class EvalActionA: BaseEvalExpressionActions() {
    override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
class EvalActionB: BaseEvalExpressionActions() {
    override fun getActionUpdateThread() = ActionUpdateThread.EDT
}


sealed class BaseEvalExpressionWithUpdateCheck: BaseEvalExpressionActions() {
    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = hasRequiredPythonModules(event)
    }

    private fun hasRequiredPythonModules(event: AnActionEvent): Boolean {
        // val expr = "1 + 2" // <-- this also causes a hanging debugger
        val expr = "__import__('math')"
        return getSelectedDebugValue(event)?.frameAccessor?.evaluate(expr, false, false)?.let {
            it.value != "None"
        } ?: false
    }
}
class EvalActionC: BaseEvalExpressionWithUpdateCheck() {
    override fun getActionUpdateThread() = ActionUpdateThread.EDT
}
class EvalActionX: BaseEvalExpressionWithUpdateCheck() {
    override fun getActionUpdateThread() = ActionUpdateThread.BGT
}