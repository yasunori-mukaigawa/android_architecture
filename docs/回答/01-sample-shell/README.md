# HANDS-001 回答: Sample Featureの共通部品

`handson/01-sample-shell` の課題に対する回答です。

## 回答の進め方

回答は、課題の手順に対応するコミット単位で積み上げます。

| Step | 内容 | 状態 |
|---|---|---|
| 01 | Routeと仮画面の追加 | 完了 |
| 02 | SampleGraphとAppNavHost | 完了 |
| 03 | 共通ナビゲーション部品 | 完了 |
| 04 | 下部ナビゲーション接続 | 完了 |
| 05 | ナビゲーションドロワー接続 | 完了 |
| 06 | 共通レイアウト統合 | 未着手 |
| 07 | BackStack確認と自己レビュー | 未着手 |

## 実装方針

- 回答ソースは `BaseArchitecture` 配下に配置する
- ScreenはViewModelやNavControllerを直接知らない構成にする
- NavControllerを知る責務はGraphとRouteに限定する
- この課題では業務データ、ViewModel、UseCase、DataStore、Roomは追加しない
- SampleAppの実装と異なる判断をした場合は、このREADMEまたは該当コミットに理由を残す
