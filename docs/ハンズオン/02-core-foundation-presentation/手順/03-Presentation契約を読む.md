# Step 03 Presentation契約

## 目的

State、Event、Message、Effect、Reducerの役割を分離して確認する。

## 確認対象

- `UiState.kt`
- `UiEvent.kt`
- `UiMessage.kt`
- `UiEffect.kt`
- `Reducer.kt`

## 確認内容

1. 各Marker Interfaceの役割を確認する
2. EventをState更新の結果として使っていないことを確認する
3. MessageをReducerへ渡す理由を確認する
4. Navigation、Dialog、SnackbarをEffectで扱う理由を確認する
5. Reducerが副作用を持たないことを確認する

## 整理する表

| 型 | 発生元 | 役割 | 例 |
|---|---|---|---|
| UiState | Reducer | 画面が描画する状態 | `isLoading` |
| UiEvent | View / Route | ユーザー操作やライフサイクル入力 | `SaveClicked` |
| UiMessage | ViewModel / UseCase結果 | State更新理由 | `SaveSucceeded` |
| UiEffect | ViewModel | 単発処理の通知 | `ShowSnackbar` |

## 完了条件

- 表の4分類を自分の言葉で説明できる
- StateとEffectを混同していない
- Reducerに副作用を追加していない
