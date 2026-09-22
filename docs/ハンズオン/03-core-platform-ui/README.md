# HANDS-003 Core Platform / UIを読み解き、使ってみる

## 課題の位置づけ

この課題では、Templateにあらかじめ含まれているAndroid依存の共通部品を読み解き、アプリ側からどのように利用するかを確認する。

`StringProvider`や`AppLogger`などを別名で作り直す課題ではない。既存の契約、Android実装、DI登録、利用側の接続を追跡し、後続Featureで同じ部品を使える状態にする。

## 対象

- `core:platform`
- `core:ui`
- `app`のTheme接続
- `AppRoot`
- `CoreModule`
- `ThemeUiModule`

## 進め方

1. [課題](./課題.md)で目的と対象外を確認する
2. [手順](./手順)を上から順番に進める
3. 必要な場合だけ[ヒント](./ヒント)を開く
4. Themeや共通UIを一度小さく変更して挙動を確認する
5. 実験用の変更をSampleAppと一致する状態へ戻す
6. [レビュー確認項目](./レビュー確認項目.md)で自己確認する
7. [回答チェックポイント](./回答チェックポイント.md)を自分の言葉で説明する

## 開始時点

この課題は、`solution/02-core-foundation-presentation`の回答済みソースと、Handson 01・02の課題資料を引き継いだ状態から開始する。

## 完了条件

- Core Platform / UIの責務と依存方向を説明できる
- Providerのinterface、Android実装、DI登録、利用側を追跡できる
- AppRootからThemeが適用される流れを説明できる
- Dialog / LoadingをFeatureへ組み込む責務の位置を説明できる
- TemplateとSampleAppの差分を、03で扱うものと後続Stepで扱うものに分類できる
- 実験用の変更を残さず、ビルド・テスト可能な状態に戻している
