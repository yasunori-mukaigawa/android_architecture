# Step 02 Result / Error / Validation

## 目的

処理結果、エラー、入力検証を混ぜずに扱うためのCore契約を確認する。

## 確認対象

- `AppResult.kt`
- `AppError.kt`
- `UiError.kt`
- `UiErrorAction.kt`
- `FieldValidationResult.kt`
- `FieldValidationError.kt`

## 確認内容

1. 成功と失敗を`AppResult`で表現する流れを追う
2. `AppError`の分類と保持情報を確認する
3. `UiError`が表示用情報だけを保持することを確認する
4. Domain / ApplicationのErrorとUI表示用Errorを分離する理由を整理する
5. `FieldValidationResult`と`AppResult`の使い分けを整理する

## 禁止事項

- ExceptionのmessageをそのままUIへ渡さない
- `UiError`をRepositoryやUseCaseの戻り値にしない
- Validationの結果を画面表示用文字列だけで表現しない

## 完了条件

- `AppResult`、`AppError`、`UiError`、Validationの境界を説明できる
- SampleAppの実装と比較し、独自のError型を追加していない
