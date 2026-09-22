# ハンズオン課題

TemplateからSampleApp相当のアプリを段階的に構築するための課題定義です。

このディレクトリには課題、ヒント、レビュー観点を置きます。開始ソースは `handson` ブランチ、回答ソースは `solution` ブランチで管理します。

## 進め方

1. 親課題の目的と完了条件を確認する
2. `手順/`を上から順番に進める
3. 各Stepの設計資料を読む
4. 必要な場合だけヒントを開く
5. Stepごとにビルドと自己レビューを行う
6. 回答ブランチの対応Commit、またはSampleAppと比較する

## ブランチ方針

```text
handson/01-sample-shell
solution/01-sample-shell

handson/02-core-foundation-presentation
solution/02-core-foundation-presentation
```

大きな課題単位で課題ブランチと回答ブランチを分けます。Step 1.1、1.2のような小課題は、回答ブランチ内のCommitまたはタグで区切ります。

学習者は課題ブランチから自分の作業ブランチを作成します。

```text
handson/01-sample-shell
  -> user/<name>/01-sample-shell
```

## 課題一覧

| ID | 課題 | 状態 |
|---|---|---|
| HANDS-001 | [Sample Featureの共通部品を作成する](01-sample-shell/課題.md) | 作成済み |
| HANDS-002 | [Core Foundation / Presentationを読み解く](02-core-foundation-presentation/課題.md) | 作成済み |
| HANDS-002 | [Core Foundation / Presentationを読み解く](02-core-foundation-presentation/課題.md) | 作成済み |
