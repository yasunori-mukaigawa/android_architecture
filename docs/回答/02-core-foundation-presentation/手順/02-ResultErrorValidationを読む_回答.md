# 手順02回答: Result / Error / Validation

## 役割の分離

| 型 | 責務 |
|---|---|
| `AppResult<T>` | 処理の成功または失敗を表現する |
| `AppError` | 失敗の分類、コード、原因を保持する |
| `UiError` | 画面に表示するタイトル、メッセージ、行動候補を保持する |
| `FieldValidationResult` | 入力項目がValidかInvalidかを表現する |
| `FieldValidationError` | 入力項目、コード、理由を保持する |

処理層は`AppResult.Success`または`AppResult.Failure`を返し、失敗時は`AppError`で意味を表す。ViewModelやRouteは必要に応じて`AppError`を`UiError`や`UiMessage`へ変換する。

## Kotlin標準Resultを使わない理由

このアーキテクチャでは、アプリ全体の失敗契約を`AppError`へ統一する。Kotlin標準`Result<T>`を混在させると、処理層ごとに失敗表現が変わり、エラーコード、分類、原因の扱いが揺れるためである。

`AppResult`は成功値と`AppError`を明示的に持ち、`map`、`onSuccess`、`onFailure`、`getOrNull`、`errorOrNull`など、必要最小限の変換を提供する。

## AppErrorとUiError

`AppError`へ日本語の表示文言を直接入れない。`AppError`はログ、テスト、処理分岐で利用する処理側の情報であり、表示タイトルやRetryなどの行動候補は画面の都合で変わるためである。

```text
Repository / UseCase
    -> AppResult.Failure(AppError)
    -> ViewModelがUiMessageまたはUiEffectへ変換
    -> RouteがUiErrorや表示文言へ変換
```

入力検証は`FieldValidationResult.Valid`または`Invalid(FieldValidationError)`で返し、画面へ生の例外を渡さない。

