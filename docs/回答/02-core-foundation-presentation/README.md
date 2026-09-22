# HANDS-002 回答: Core Foundation / Presentation

## 回答の位置づけ

この回答は、`solution/01-sample-shell`を起点に、Templateへあらかじめ含まれているCore Foundation / Presentationの責務と利用契約を確認したものです。

Starter画面とStarter専用ResourceはHANDS-001の完了時に削除済みです。このStepではCore実装を再作成せず、Feature実装者が既存契約を正しく利用できるように、手順ごとに確認結果と設計意図を整理しています。

## 手順別回答

- [手順01 Module境界と依存方向](./手順/01-Module境界と依存方向_回答.md)
- [手順02 Result / Error / Validation](./手順/02-ResultErrorValidationを読む_回答.md)
- [手順03 Presentation契約](./手順/03-Presentation契約を読む_回答.md)
- [手順04 BaseViewModelのEvent Queue](./手順/04-BaseViewModelのEventQueueを確認_回答.md)
- [手順05 SampleAppとの差分と自己レビュー](./手順/05-SampleAppとの差分確認と自己レビュー_回答.md)

## 共通結論

- `core:foundation`はAndroid UIやNavigationを知らない共通契約を保持する
- `core:presentation`はFoundationの契約を利用して画面状態と入力処理の基盤を提供する
- `UiEvent`は入力、`UiMessage`はState更新理由、`UiState`は現在状態、`UiEffect`は単発処理として分離する
- Reducerは副作用を持たず、StateとMessageから次のStateだけを生成する
- BaseViewModelはEventをChannelで順次処理するが、LoadingやError変換を自動化しない
- ViewModelへ`NavController`や`Context`を渡さない

## 実装変更について

このStepでは、`core:foundation`と`core:presentation`の本番ソースを変更していません。HANDS-001でSampleAppに存在しないStarterを整理済みであり、ここでは既存CoreとSampleAppの一致を確認しています。

各回答CommitとDiffは[Step別Diff](./差分/README.md)から確認できます。

