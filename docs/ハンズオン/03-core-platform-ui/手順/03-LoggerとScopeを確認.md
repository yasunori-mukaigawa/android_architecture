# 手順03 LoggerとScopeを確認

1. `AppLogger`と`AndroidAppLogger`の責務を読む。
2. Timberの初期化をApplicationから追う。
3. TAGの既定値と上書き方法を確認する。
4. `DispatcherProvider`と`CoroutineScopeProvider`の利用方法を読む。
5. CoroutineScopeをFeatureごとに乱立させない意図を説明する。

## 回答に含めること

- Loggerのinterfaceを利用側から見るメリット
- Application起動時にLoggerを初期化する理由
- Unit TestでDispatcherを差し替える理由
- ViewModelやFeature専用Scopeを追加する場合の判断基準
