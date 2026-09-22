# Step 04 BaseViewModelのEvent Queue

## 目的

BaseViewModelがUiEventをChannelで受け取り、順番に処理する仕組みを確認する。

## 確認対象

`core/presentation/src/main/java/jp/co/nsco/basearchitecture/core/architecture/BaseViewModel.kt`

## 確認内容

1. `MutableStateFlow`が外部へ公開されていないことを確認する
2. `MutableSharedFlow`が外部へ公開されていないことを確認する
3. Event入力に`Channel.BUFFERED`を使っていることを確認する
4. `sendEvent`が通常関数として公開されていることを確認する
5. Channelから受け取ったEventを`handleEvent`へ渡す処理を確認する
6. `dispatch`がReducer経由でStateを更新することを確認する
7. `emitEffect`が単発Effectだけを発行することを確認する

## 考えること

- UiEventにSharedFlowではなくChannelを使う理由
- `sendEvent`をsuspend関数にしない理由
- Event処理の例外をBaseViewModelで自動変換しない理由
- BaseViewModelに便利機能を追加しすぎない理由

## 完了条件

- Event Queueの処理順序を説明できる
- ViewModelの責務とBaseViewModelの責務を分けて説明できる
- NavController、Context、UseCaseをBaseViewModelへ追加していない
