# Step 2: SampleGraphとAppNavHost

## 目的

Sample FeatureのRouteをGraphへ登録し、AppNavHostから呼び出せる状態にする。

## SampleAppとの対応

- `feature/sample/presentation/common/SampleGraph.kt`
- `app/navigation/AppNavHost.kt`

## 課題

- `NavGraphBuilder.sampleGraph(navController)`を作る
- Home / History / SettingsのRouteを登録する
- Home RouteをstartDestinationへ設定する
- AppNavHostからSample Graphを登録する
- Drawer遷移先のPlaceholder GraphもAppNavHostへ登録する

## 制約

- AppNavHostへ画面固有のComposable実装を書かない
- GraphはRoute登録と接続に集中させる
- NavControllerをViewModelやScreenへ渡さない

## 完了条件

- [ ] 起動時にSample Homeが表示される
- [ ] Route登録がSampleGraphへ集約されている
- [ ] AppNavHostがGraph登録中心になっている
- [ ] Graph単位で追加・削除できる構成になっている
