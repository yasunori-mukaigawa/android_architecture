# Step 1: RouteとPlaceholder画面

## 目的

Sample FeatureとDrawer遷移先の最小Route構成を作る。Navigation部品はまだ作らない。

## SampleAppとの対応

- `feature/sample/presentation/common/SampleRoutes.kt`
- `feature/sample/presentation/home/SampleRoute.kt`
- `feature/sample/presentation/history/SampleHistoryRoute.kt`
- `feature/sample/presentation/settings/SampleSettingsRoute.kt`
- `feature/legal/presentation/LegalDocumentRoutes.kt`
- `feature/license/presentation/common/LicenseRoutes.kt`
- `feature/versioninfo/presentation/VersionInfoRoutes.kt`

## 課題

- Sample Home / History / SettingsのRouteを定義する
- 各画面にタイトルとPlaceholder文言を表示する
- Drawer遷移先のRoute定義とPlaceholder画面を用意する
- 文言はResourceから取得する
- Previewを追加する

## 制約

- `NavController`はScreenへ渡さない
- ViewModel、UseCase、Repositoryは作らない
- Route文字列をScreenへ直書きしない

## 完了条件

- [ ] すべてのPlaceholder画面を単独表示できる
- [ ] Route定義が各Featureの責務として配置されている
- [ ] Previewを確認できる
- [ ] コンパイルが成功する

## 次Stepへの状態

この時点では、画面間のNavigation接続は未完成でよい。
