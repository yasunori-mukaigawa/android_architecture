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
| 06 | 共通レイアウト統合 | 完了 |
| 07 | BackStack確認と自己レビュー | 完了 |

## 最終整理

Sample Shellを起動入口として接続したため、Templateの起動確認用`feature/starter`とStarter専用Resourceを削除した。Starter画面はSampleAppの構成に含まれないため、01の完了時点で残さない。

## 実装方針

- 回答ソースは `BaseArchitecture` 配下に配置する
- ScreenはViewModelやNavControllerを直接知らない構成にする
- NavControllerを知る責務はGraphとRouteに限定する
- この課題では業務データ、ViewModel、UseCase、DataStore、Roomは追加しない
- SampleAppの実装と異なる判断をした場合は、このREADMEまたは該当コミットに理由を残す

全体のStepとSampleAppとの一致タイミングは、Template側の`docs/ハンズオン/SampleApp対応表.md`を基準にする。

## Step 07の確認結果

- Drawer表示中のBack操作は、各RouteでDrawerを閉じる処理として扱う
- Bottom Navigationの切り替えは、`launchSingleTop`、`restoreState`、`popUpTo`を使用する
- Screenは`NavController`を参照せず、RouteがNavigationを担当する
- 共通Navigation部品は、表示とクリック通知だけを担当し、遷移処理を持たない

詳細な確認項目と、実機で未実施の確認範囲は[レビュー結果](./レビュー結果.md)に記載する。

各StepのCommit差分は[Step別Diff](./差分/README.md)から確認できる。
