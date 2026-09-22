# HANDS-001 Sample Featureの共通部品

Templateから、SampleAppの主要画面群に相当するSample Featureを段階的に作成する課題です。

## 課題の対象

この課題では、SampleAppの次の構成を再現する。

```text
feature/sample/
  presentation/
    common/
      SampleGraph.kt
      SampleRoutes.kt
      SampleNavigationComponents.kt
    home/
    history/
    settings/
```

Drawerから遷移する別Featureは、Placeholderとして次の構成を用意する。

```text
feature/legal/presentation/
feature/license/presentation/common/
feature/versioninfo/presentation/
```

業務データ、ViewModel、UseCase、DataStore、Roomは後続課題で追加する。

## ファイル構成

| ファイル | 内容 |
|---|---|
| `課題.md` | 親課題の目的、共通制約、最終完了条件 |
| `手順/` | 実装を段階化した7つの小課題 |
| `ヒント/` | 必要な場合だけ開く段階ヒント |
| `レビュー確認項目.md` | SampleApp実体との整合性を確認する項目 |
| `回答チェックポイント.md` | 回答ブランチのCommit分割案 |

## 想定ブランチ

```text
handson/01-sample-shell
solution/01-sample-shell
```
