# HANDS-002 Core Foundation / Presentation

## 目的

Templateにあらかじめ含まれているCore Foundation / Presentationの責務と利用規約を読み解く。

この課題では、SampleAppと同じCore実装をもう一度作り直さない。Templateは共通Coreを配布するための開始点であり、課題ではその契約をFeature実装で正しく利用できる状態を目指す。

## 対象

- `core/foundation`
- `core/presentation`
- `AppResult`
- `AppError` / `UiError` / `UiErrorAction`
- `FieldValidationResult` / `FieldValidationError`
- `UiState` / `UiEvent` / `UiMessage` / `UiEffect`
- `Reducer`
- `BaseViewModel`

## 前提資料

- [SampleApp対応表](../SampleApp対応表.md)
- [共通設計思想ガイド](../設計思想/共通設計思想ガイド.md)
- [Androidカスタマイズ設計書](../設計思想/Androidカスタマイズ設計書.md)
- [01 Sample Shellの回答](../01-sample-shell/README.md)

## 進め方

1. `手順/`を上から順に進める
2. 実装を変更する前に、Templateにすでに存在する契約を読む
3. SampleAppと比較し、実装差分がないことを確認する
4. 必要な補足コメントだけを追加する
5. Coreの責務を説明できる状態で自己レビューする

## 完了条件

- FoundationとPresentationの依存方向を説明できる
- `AppResult`、`AppError`、Validationの使い分けを説明できる
- Event、Message、State、Effectの違いを説明できる
- Reducerに副作用を入れない理由を説明できる
- `BaseViewModel`のChannelによるEvent Queue処理を説明できる
- ViewModelへ`NavController`や`Context`を渡さない理由を説明できる
- SampleAppとCore実装を比較し、コメント以外の差分がない
- `:core:foundation:test`、`:core:presentation:test`、`:app:compileDebugKotlin`が成功する
