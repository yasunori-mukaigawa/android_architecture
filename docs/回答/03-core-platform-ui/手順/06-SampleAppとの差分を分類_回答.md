# 手順06 SampleAppとの差分を分類: 回答

## 比較結果

Core Platformの契約・Android実装は、TemplateとSampleAppで対応している。Core UIにはDefaultテーマを中心に共通する実装があるが、SampleAppにはDashboardテーマ用のPreset、Color、Shape、Spacing、Registry分岐が追加されている。

SampleAppにあってTemplateにない主なファイルは、次のように分類する。

| 差分 | 判定 | 対応Step | 理由 |
|---|---|---:|---|
| `core/ui/markdown/MarkdownDocumentView.kt` | 03対象外 | 07 | Legal / Licenseの文書表示で利用するUIだから |
| `core/ui/markdown/MarkdownRenderOptions.kt` | 03対象外 | 07 | Markdown表示のFeature要件に属するから |
| `app/AppViewModel.kt` | 構造だけ確認 | 05 | ThemeSettingsなどアプリ共通状態を扱うから |
| `app/auth/AuthModule.kt` | 03対象外 | 後続 | 認証固有の依存だから |
| `app/database/*` | 03対象外 | 04 | Room / Database基盤だから |
| `app/datastore/*` | 03対象外 | 04・05 | DataStore基盤とSettings保存だから |
| `app/network/*` | 03対象外 | 04 | Network基盤だから |
| Dashboard ThemePreset / Registry分岐 | 構造を確認 | 05 | SettingsからのTheme選択と結びつくから |
| SampleAppの大量のstring resource | 03対象外 | 05以降 | Featureの表示文言だから |

## 03で確認すべき差分

03で確認するのは、差分を見つけたらすぐ実装することではなく、次の接続である。

```text
Coreの契約
    -> Android / Composeの標準実装
    -> Hilt ModuleまたはTheme Module
    -> AppRoot / Application
    -> 後続Featureからの利用
```

Providerの利用がTemplateのShell画面にまだ少ないのは、Providerが不要だからではない。Homeの日付、Settingsの文字列・保存、LegalのRawResource、VersionInfoのAppInfo、Operation Logの時刻など、後続Featureで利用する前提を先にCoreへ置いているためである。

## 実装差分の判定

- Core Platformの共通契約・Android実装: SampleAppと一致
- Core UIのDefault Theme基盤: SampleAppと共通
- Dashboard Theme: 05でSettings接続とあわせて扱う
- Markdown UI: 07で扱う
- DataStore / Database / Network: 04以降で扱う
- Feature固有の文言、State、UseCase: 05以降で扱う

したがって、03でCore Platform / UIを別実装に置き換える必要はない。後続Stepで利用が発生した時点で、既存契約へ接続する。

## 方針上の要確認事項

`AndroidExternalUriOpener`内部の標準`Result`利用は、TemplateとSampleAppで一致しているが、プロジェクトの独自`AppResult`方針とは整合しない。このような「SampleAppと一致しているが設計ルールには疑義がある」項目は、差分なしとして黙って終わらせず、Step08までの確認事項として残す。

## 結論

TemplateとSampleAppの差分は、03のCore理解、04のPlatform基盤、05のSettings / Theme接続、07のLegal / License / VersionInfoへ分割される。03の完了条件は、SampleAppを一気にコピーすることではなく、差分を責務の境界に沿って分類できることである。
