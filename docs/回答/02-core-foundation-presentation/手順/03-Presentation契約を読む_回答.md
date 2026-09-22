# 手順03回答: Presentation契約

## 4つの契約

| 契約 | 意味 |
|---|---|
| `UiState` | 再描画されても同じ意味になる現在の画面状態 |
| `UiEvent` | View / Screen / RouteからViewModelへ渡す入力 |
| `UiMessage` | Stateを更新する理由や処理結果 |
| `UiEffect` | Navigation、Dialog、Snackbarなど一度だけ扱う処理 |

例えば保存処理では、`SaveClicked`をEventとして受け取り、`SaveStarted`や`SaveSucceeded`をMessageとしてdispatchする。保存完了Snackbarは`ShowSnackbar`のようなEffectとして発行する。

```text
SaveClicked          UiEvent
SaveStarted          UiMessage
isLoading = true     UiState
ShowSnackbar(...)    UiEffect
```

## Reducerの責務

Reducerは現在StateとMessageから次のStateを生成する純粋関数である。API、DB、Navigation、Dialog、ログ出力などの副作用は持たせない。

この分離により、Reducerは入力と出力だけをUnit Testできる。UseCase呼び出しや単発UI処理はViewModel、Routeなどの責務として別に確認できる。

## StateとEffectを分ける理由

SnackbarやNavigationをStateへ入れると、Stateの再購読や画面再生成時に同じ処理を再実行する可能性がある。画面の現在値はStateに保持し、再実行したくない処理はEffectとして通知する。

