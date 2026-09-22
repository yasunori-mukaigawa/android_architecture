# Step別Diff

このディレクトリには、`solution/01-sample-shell` の各Step Commitで追加・変更した内容をDiff形式で保存する。

Diffは手作業で作成せず、各Commitとその親Commitの差分から生成している。したがって、回答ソースとDiffの内容は一致する。

| ファイル | 対応Commit |
|---|---|
| `Step01-Routeと仮画面.diff` | `20c895b` |
| `Step02-NavigationGraph.diff` | `ae7fcfb` |
| `Step03-共通Navigation部品.diff` | `754c1b8` |
| `Step04-下部Navigation接続.diff` | `264f8a3` |
| `Step05-NavigationDrawer接続.diff` | `9e87b1c` |
| `Step06-共通レイアウト統合.diff` | `8deded2` |
| `Step07-BackStackと自己レビュー.diff` | `207e351` |
| `Step08-Template Starter整理.diff` | `0a5f949` |

## 確認方法

- Step単位の変更を確認する場合は、対応するDiffを開く
- 実際の回答ソースを確認する場合は、対応Commitへ移動する
- SampleAppとの一致タイミングは、Template側の`docs/ハンズオン/SampleApp対応表.md`を参照する

Diffディレクトリ自身はDiff生成対象から除外しているため、Diffファイルが別のDiffへ混入することはない。
