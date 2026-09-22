# 手順04回答: BaseViewModelのEvent Queue

## BaseViewModelの責務

`BaseViewModel`は次の共通処理だけを持つ。

- `MutableStateFlow`を内部に保持し、`StateFlow`として公開する
- `MutableSharedFlow`を内部に保持し、`SharedFlow`として公開する
- `Channel<E>(Channel.BUFFERED)`でEventを受け取る
- `sendEvent(event)`からEventをChannelへ積む
- Channelを順番に受信し、`handleEvent(event)`へ委譲する
- `dispatch(message)`からReducerを呼んでStateを更新する
- `emitEffect(effect)`からEffectを発行する
- `stateValue`または`currentState()`で現在Stateを参照できるようにする

## 処理フロー

```text
Screen / Route
    -> sendEvent(event)
    -> Channel<E>
    -> handleEvent(event)
    -> UseCase
    -> dispatch(message) または emitEffect(effect)
    -> Reducer / SharedFlow
    -> StateFlow / RouteのEffect処理
```

ScreenやRouteはViewModelの処理本体を直接呼ばず、`sendEvent`で通知する。Feature ViewModelは`handleEvent`の中でUseCaseを呼び、結果をMessageやEffectへ変換する。

## 保証範囲

Channelによって、ViewModelが受信したEventは順次`handleEvent`へ渡される。ただし、複数スレッドから同時に送信した場合の呼び出し開始順まで保証する契約ではない。

BaseViewModelへ多重クリック抑制、Busy中制御、自動Loading、自動Error変換を追加しない。必要な制御はFeatureの要件に応じて明示的に実装する。

## ViewModelへ渡さないもの

ViewModelへ`NavController`や`Context`を渡さない。NavigationはEffectをRouteが処理し、Androidの文字列やResourceはProviderなどの契約を経由する。

