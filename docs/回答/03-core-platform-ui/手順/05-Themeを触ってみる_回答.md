# 手順05 Themeを触ってみる: 回答

## TemplateのTheme適用経路

Templateでは、`MainActivity`が`AppRoot`を表示し、`AppRoot`が`BaseArchitectureTheme`で`AppNavHost`を包む。

```text
MainActivity
    -> AppRoot
    -> BaseArchitectureTheme
    -> BaseAppTheme
    -> MaterialTheme / LocalBaseSpacing
    -> AppNavHostと配下のComposable
```

`BaseArchitectureTheme`は現在、`ThemeSettings()`と`DefaultBaseThemeRegistry()`を渡している。アプリ側Theme Composableを一段置くことで、後からThemeSettingsの取得元をDataStoreやAppViewModelへ変更しても、各ScreenやPreviewの呼び出し方を変えずに済む。

## Core UI Themeの責務

- `BaseAppTheme`: ThemeSettingsをもとにDark / Lightを判定し、Presetを解決してMaterialThemeへ適用する
- `BaseThemePreset`: ColorScheme、Typography、Shapes、Spacingをテーマ単位で保持する
- `BaseThemeRegistry`: Theme IDからPresetを解決し、利用可能なTheme IDを提供する
- `ThemeModeResolver`: System / Light / Darkの表示モードを解決する
- `CompositionLocal`: Themeに紐づくSpacingを配下Composableへ提供する

各Screenが個別にMaterialThemeやSpacingを定義せず、AppRootから一つのTheme入口を使うことで、画面間の見た目の不整合を防ぐ。

## 実験結果として確認すること

ThemeのColorScheme、Shape、Spacing、またはThemeSettingsを一時的に変更すると、`BaseAppTheme`配下のComposableへ反映される。これはTheme値が個別Screenではなく、AppRootからCompositionLocalとMaterialThemeを通じて伝播しているためである。

実験用の変更は課題ブランチへ残さない。課題完了後はTemplateのDefault ThemeとSampleAppの対応する実装を基準に戻す。

## SampleAppとの差分

SampleAppでは、TemplateのDefaultテーマに加えてDashboardテーマがあり、`BaseThemePreset`と`BaseThemeRegistry`が複数Theme IDを扱う。またSampleAppの`AppRoot`はAppViewModelからThemeSettingsを購読し、Hilt EntryPoint経由でThemeRegistryを取得している。

ただし、Theme設定の保存・選択とAppViewModelはDataStoreやSettings Featureと結びつくため、この差分を03で完成させない。03ではTheme契約と適用経路を理解し、05でSettingsと永続化を接続する。

## 結論

Themeは単なる色定義ではなく、保存されたThemeSettings、Theme IDを解決するRegistry、Preset、MaterialTheme、Spacing提供をつなぐアプリ共通の表示基盤である。FeatureはThemeの解決や保存を担当せず、適用済みのMaterialThemeと共通UI契約を利用する。
