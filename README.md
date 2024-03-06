# Problem

Complete IDE freezes when using `PyDebugValue::frameEvaluator` inside the `AnAction::update` method.

# Steps to Reproduce

1. start gradle task `runIde`

2. setup a local Python Interpreter (Python 3, no additional Python modules required)

3. enter `a = 1` into the active `Python Console`

4. select variable `a` in the variable view

5. use key binding `alt x` to trigger the action which causes the IDE to freeze

# Expected Result

The IDE should be able to execute the executed action without freezing the IDE.

# Actual Result

The IDE freezes and no interaction with the IDE is possible.

# Additional Information

The plugin contains four different actions (`A`, `B`, `C` and `X`) in the linked plugin example to demo the problem. Only action `X` results into a freezing IDE if executed via the mapped key binding.

## Action A

* key binding `alt a`
* does *not* evaluate in the `AnAction::update` method
* action update thread is `ActionUpdateThread.BGT`

## Action B

* key binding `alt b`
* does *not* evaluate in the `AnAction::update` method
* action update thread is `ActionUpdateThread.EDT`

## Action C

* key binding `alt c`
* does evaluate in the `AnAction::update` method
* action update thread is `ActionUpdateThread.EDT`

## Action X

* key binding `alt x`
* does evaluate in the `AnAction::update` method
* action update thread is `ActionUpdateThread.BGT`

All actions, `A`, `B`, `C` and `X`, run successfully if:

* the action is executed on a variable from the `Python Console` via the context menu (right click on variable in variable view) instead of the mapped key binding
* the action is executed on a variable from a debugger session no matter if the key binding or context menu was used