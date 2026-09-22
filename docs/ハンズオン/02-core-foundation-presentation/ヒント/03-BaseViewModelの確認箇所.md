# ヒント 03 BaseViewModelの確認箇所

次の処理を順に確認する。

```text
sendEvent
  -> eventChannel.send
  -> handleEvent
  -> dispatch(message)
  -> reducer.reduce
  -> uiState
```

単発処理は次の経路になる。

```text
handleEvent
  -> emitEffect(effect)
  -> uiEffect
  -> Route
```
