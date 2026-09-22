# 手順01 ModuleとDIを読む

1. `core:platform`と`core:ui`の`build.gradle.kts`を読む。
2. `settings.gradle.kts`でModule登録を確認する。
3. `CoreModule`と`ThemeUiModule`の提供内容を一覧化する。
4. interface、Android実装、DI登録、利用側の関係を図にする。
5. `core:platform`がFeatureへ直接依存していないことを確認する。

## 確認すること

- Android依存をどこへ閉じ込めているか
- Hiltが何を生成・提供しているか
- Featureが直接実装をnewしていないか
