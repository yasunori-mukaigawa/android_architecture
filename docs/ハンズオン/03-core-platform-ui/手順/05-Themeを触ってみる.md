# 手順05 Themeを触ってみる

1. `AppRoot`からアプリ側Theme Composableを追う。
2. `BaseAppTheme`が`ThemeSettings`と`BaseThemeRegistry`を使う流れを確認する。
3. `BaseThemePreset`がColorScheme、Typography、Shapes、Spacingをまとめていることを確認する。
4. Previewまたはローカル確認用に、色・Shape・Spacing・ThemeSettingsのいずれかを一時的に変更する。
5. 変更が画面全体へ反映されることを確認する。
6. 実験用の変更を元へ戻し、SampleAppとの差分を残さない。

## 禁止事項

- Dashboardテーマなど後続Featureで扱う内容を独自に先行実装しない
- ScreenごとにMaterialThemeを個別設定しない
- Themeの選択状態をComposable内部で直接保存しない
