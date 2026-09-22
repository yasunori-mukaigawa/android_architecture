# ヒント02 Themeの追跡

Themeを確認するときは、色定義だけでなく次の順番で追う。

```text
AppRoot
  -> app側Theme Composable
  -> BaseAppTheme
  -> ThemeSettings
  -> BaseThemeRegistry
  -> BaseThemePreset
  -> MaterialTheme / CompositionLocal
```

テーマ選択を保存するDataStoreやSettings ViewModelは、03ではなく後続Stepの責務である。
