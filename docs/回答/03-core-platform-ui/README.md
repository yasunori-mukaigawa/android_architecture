# HANDS-003 回答: Core Platform / UI

## 回答の位置づけ

この回答は、`solution/02-core-foundation-presentation`を起点に、Android依存の共通部品とCompose UI基盤の責務を確認したものです。

このStepでは、`StringProvider`、Logger、Dispatcher、Theme、Dialogなどを新しく作り直していません。Templateにすでにある契約、Android実装、DI登録、AppRootからの接続を確認し、SampleAppにある差分を後続Stepへ分類しています。

## 手順別回答

- [手順01 ModuleとDIを読む](./手順/01-ModuleとDIを読む_回答.md)
- [手順02 Providerを追う](./手順/02-Providerを追う_回答.md)
- [手順03 LoggerとScopeを確認](./手順/03-LoggerとScopeを確認_回答.md)
- [手順04 Navigationと共通UIを確認](./手順/04-Navigationと共通UIを確認_回答.md)
- [手順05 Themeを触ってみる](./手順/05-Themeを触ってみる_回答.md)
- [手順06 SampleAppとの差分を分類](./手順/06-SampleAppとの差分を分類_回答.md)

## 回答の前提

- Coreの共通部品は、Featureが直接Android APIを使わないための境界である
- Coreは契約と標準実装を提供するが、Feature固有の業務判断を持たない
- AppRootはThemeとNavigationのアプリ共通入口であり、Feature処理を集約しない
- TemplateとSampleAppの差分は、すべて03で解消するのではなく、対応表の後続Stepへ分ける

## 実装変更について

この回答では、`BaseArchitecture`配下の本番ソースを変更していない。03の成果物は、既存実装の読み解き結果、差分分類、利用方法の説明である。

## 方針上の要確認事項

`AndroidExternalUriOpener`の公開APIは`AppResult<Unit>`を返すが、内部のprivate関数ではKotlin標準`Result<Unit>`と`runCatching`を使用している。SampleAppと実装は一致しているものの、「Kotlin標準Resultを使わない」というプロジェクト方針には抵触するため、Step08までに扱いを決める必要がある。
