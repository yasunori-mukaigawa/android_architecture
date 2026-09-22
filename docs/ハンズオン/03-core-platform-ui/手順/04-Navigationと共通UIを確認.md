# 手順04 Navigationと共通UIを確認

次の部品を確認する。

- `RouteArgumentEncoder`
- `ExternalUriOpener`
- `DialogUiState`
- `AppDialog`
- `LoadingContent`

それぞれについて、Coreが提供する範囲と、Route / Screen / ViewModelが担当する範囲を分けて記録する。

## 確認すること

- Route引数のencodeだけをCoreで補助する理由
- Navigation実行をRouteに置く理由
- Dialog表示条件をViewModelやRouteが判断する理由
- Loadingの自動表示をCoreへ入れない理由
- Compose UI部品をFeature固有の業務処理から分離する理由
