# 手順05回答: SampleAppとの差分確認と自己レビュー

## 比較対象

Template / Solution側とSampleApp側について、次の実装を比較した。

- `core/foundation`のResult、Error、Validation
- `core/presentation`のState、Event、Message、Effect、Reducer、BaseViewModel

比較時は、学習用KDocやコメントを除外し、型、公開API、処理内容、依存方向を確認した。

## 比較結果

| 対象 | コメント以外の実装差分 | 判定 |
|---|---:|---|
| AppResult / AppError / UiError / Validation | なし | 一致 |
| UiState / UiEvent / UiMessage / UiEffect | なし | 一致 |
| Reducer | なし | 一致 |
| BaseViewModel | なし | 一致 |

Starter画面とStarter専用Resourceは、SampleAppに存在しないTemplate専用コードとしてHANDS-001の完了時に削除済みである。したがって、このStepで不要なソース整理を追加する必要はない。

## 自己レビューの結論

- Kotlin標準`Result<T>`をCore契約へ追加していない
- `AppError`と`UiError`を混同していない
- Event、Message、State、Effectの役割を分離している
- Reducerへ副作用を追加していない
- BaseViewModelへ自動Loading、自動Error変換、UseCase実行ラッパーを追加していない
- ViewModelへ`NavController`や`Context`を渡していない
- Coreの実装はSampleAppと一致している

## 理解確認

1. `AppResult`とKotlin標準`Result`を分ける理由を説明できるか
2. `AppError`をそのまま画面へ渡さず、`UiError`へ変換する理由を説明できるか
3. `UiEvent`と`UiMessage`の違いを説明できるか
4. SnackbarやNavigationを`UiEffect`で扱う理由を説明できるか
5. ReducerにAPIやDBアクセスを入れてはいけない理由を説明できるか
6. `sendEvent`、`handleEvent`、`dispatch`、`emitEffect`の役割を説明できるか

この確認に答えられれば、型名だけでなく、後続Featureで契約を使い分ける意図まで理解できたと判断する。

