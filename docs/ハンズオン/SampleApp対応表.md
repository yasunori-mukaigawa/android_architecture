# SampleApp 対応表

## 1. 目的

この資料は、TemplateからSampleApp相当の実装へ段階的に進める際に、各大枠Stepでどこまで実装するかを定義する。

学習用の中間実装は許容するが、最終的な実装コードはSampleAppと一致させる。SampleAppと異なる実装を独自判断で残さない。

KDocや学習用コメントは追加してよい。ただし、コメント以外のKotlin実装、XML、Resource、Gradle設定、Module構成は、最終的にSampleAppと一致させる。

## 2. Step対応表

| Step | 大枠 | 主な対象 | このStepの到達状態 | SampleAppとの一致方針 |
|---|---|---|---|---|
| 01 | Sample Shell | `app/navigation`、`feature/sample/presentation`、Legal / License / VersionInfoのRoute入口 | Sample FeatureのHome、履歴、設定、Drawer、Bottom Navigation、Navigation Graphを持つ起動可能な画面の殻を作る | `SampleRoutes`、`SampleGraph`、共通Navigation部品は追加時点で一致させる。画面RouteとScreenは後続Stepで発展するため中間実装を許容する |
| 02 | Core Foundation / Presentation | `core/foundation`、`core/presentation` | Templateに含まれるCore契約を読み解き、Module境界、Result / Error / Validation、State / Event / Message / Effect、Reducer、BaseViewModelの使い分けを確認する | Templateにすでに含まれるCore実装を再作成しない。SampleAppと実装比較し、差分があれば修正する。学習用KDoc以外の独自APIは追加しない |
| 03 | Core Platform / UI | `core/platform`、`core/ui`、アプリTheme | StringProvider、Coroutine、Time、Logger、Navigation補助、Dialog、Loading、ThemeなどAndroid依存の共通部品を整える | 後続Featureから利用される共通部品は、このStepでSampleAppと一致させる。画面固有の表示処理は追加しない |
| 04 | Storage / Network / License基盤 | `core/storage`、`core/network`、`core/license`、`app/database`、`app/datastore`、`app/network` | DataStore、Room、Retrofit、共通DB処理、API処理、OSS License取得・表示の基盤を追加する | Module、依存関係、DI、エラー変換をSampleAppと一致させる。Feature側の具体的なRepositoryは後続Stepで追加する |
| 05 | Sample Home / Settings | `feature/sample/home`、`feature/sample/settings`、Sample domain / application / data / di | Homeの時刻依存挨拶、日付表示、設定値の取得・保存、テーマモード・テーマプリセットを実装する | HomeとSettingsの最終実装をSampleAppと一致させる。`AppRoot`、Theme適用、文字列、DataStore接続も含めて確認する |
| 06 | Operation Log / History | `feature/operationlog`、`feature/sample/history` | 操作ログの保存・取得、設定変更や画面表示のログ記録、履歴一覧・検索・フィルタ表示を実装する | OperationLogのDomain、Repository、DAO、UseCase、HistoryのState / Event / Message / Effect / ReducerをSampleAppと一致させる |
| 07 | Supplementary Features | `feature/legal`、`feature/license`、`feature/versioninfo`、関連Resource・API・RawResource | 法務文書、OSS License一覧・詳細、バージョン情報、外部リンク、再試行・エラー表示を実装する | 画面、Route、Graph、UseCase、Mapper、Facade、Resource、依存関係をSampleAppと一致させる。`AppNavHost`もこのStepで最終形にする |
| 08 | Test / Design / Dokka | 各ModuleのUnit Test、Android Test、要件・基本・詳細設計書、Dokka、対応表・Diff | 実装、テスト、設計資料、KDoc、HTMLドキュメントを揃え、配布可能な状態にする | `BaseArchitecture`の実装とSampleAppを最終比較し、コメント以外の実装差分をゼロにする |

## 3. ファイルの変更予定を判定するルール

ファイルごとに、次の項目を管理する。

| 項目 | 内容 |
|---|---|
| 初回追加Step | ファイルが最初に追加されるStep |
| 最終一致Step | SampleAppと実装を一致させるStep |
| 後続変更 | 後続Stepで責務や依存が増えるか |
| 許容差分 | その時点で許容する中間実装の範囲 |
| 比較対象 | Kotlin、XML、Resource、Gradle、Moduleなどの対象 |

判定は次の順で行う。

1. 後続課題の手順とSampleAppの完成構成を確認する。
2. 後続課題で責務が変わらないファイルは、初回追加時点でSampleAppの実装を反映する。
3. ViewModel、State、Event、Message、Effect、UseCase、Repositoryなどが後続Stepで増えるファイルだけ、中間実装を許容する。
4. 中間実装を許容する場合は、必ず最終一致Stepと解消する差分を記録する。
5. 判断に迷う場合は独自実装を残さず、SampleAppに合わせる。

## 3.1 Templateにあらかじめ含まれるCore

Templateは、新規案件で毎回再実装するための空プロジェクトではない。共通利用するCore契約は、案件開始時点で提供する。

そのため、Step02では次の実装を新規追加しない。

- `core/foundation` の `AppResult`、`AppError`、Validation、共通Domain契約
- `core/presentation` の `UiState`、`UiEvent`、`UiMessage`、`UiEffect`、`Reducer`
- `BaseViewModel` の StateFlow、SharedFlow、Channel、Reducer接続

Step02の課題は、これらを読んで責務と利用規約を説明できるようにすること、およびSampleAppとの実装差分がないことを確認することである。Coreそのものを作り直す必要がある場合は、Templateの構成方針を見直す別作業として扱う。

## 4. 代表ファイルの判定例

| ファイル | 判定 | 最終一致Step | 理由 |
|---|---|---:|---|
| `SampleRoutes.kt` | 固定 | 01 | Home、履歴、設定のRouteは後続Featureで変更しない |
| `SampleGraph.kt` | 固定 | 01 | Sample Feature内のGraph構造は後続でも変わらない |
| `SampleNavigationComponents.kt` | 固定 | 01 | TopBar、Drawer、Bottom Navigationの共通表示部品 |
| `AppNavHost.kt` | 発展 | 07 | 後続FeatureのGraph登録が追加される |
| `strings.xml` | 発展 | 07 | Feature追加ごとに文言が増える |
| `SampleRoute.kt` | 発展 | 05 | ViewModel、State購読、Effect処理が後続で追加される |
| `SampleScreen.kt` | 発展 | 05 | Homeの表示内容が仮画面から実画面へ変わる |
| `SampleHistoryRoute.kt` | 発展 | 06 | 履歴ViewModel、Effect、検索条件が後続で追加される |
| `SampleSettingsRoute.kt` | 発展 | 05 | SettingsのState、DataStore、Theme接続が後続で追加される |
| `LegalDocumentRoute.kt` | 発展 | 07 | RawResource、UseCase、Error、Retryが後続で追加される |
| `LicenseRoute.kt` | 発展 | 07 | License一覧・詳細・OSSデータ取得が後続で追加される |
| `VersionInfoRoute.kt` | 発展 | 07 | AppInfo、API、外部リンク、Version状態が後続で追加される |
| `core/foundation` | 固定 | 02 | 共通契約のため、Feature実装前に完成させる |
| `core/presentation` | 固定 | 02 | 全Featureが同じPresentation契約を利用する |
| `core/platform` | 固定 | 03 | Featureから利用するAndroid依存部品を共通化する |
| `core/ui` | 固定 | 03 | 共通UI部品とThemeの契約を確定する |
| `core/storage` | 固定 | 04 | DataStore、Roomの共通基盤を確定する |
| `core/network` | 固定 | 04 | API通信の共通基盤を確定する |

## 5. Step完了時の比較

各Stepでは、次の2種類の比較を行う。

### 5.1 全差分比較

コメント、import、実装、Resource、Gradle設定を含む通常のDiffを確認する。

### 5.2 実装差分比較

KDocや学習用コメントを除外して、実装だけを比較する。この比較で差分が残る場合は、次のいずれかに分類する。

- 後続Stepで解消することが対応表に記録されている差分
- 学習用の一時的な中間実装
- 取り込み漏れ、または不要な独自実装

最後の分類は残さない。Step08完了時点では、後続Stepで解消予定だった差分も含めて、実装差分をゼロにする。

## 6. Diffファイルとの関係

Solution側では、各回答Commitの親Commitとの差分を次の場所へ保存する。

```text
docs/回答/<課題ID>/差分/
```

Diffファイルは手作業で作成せず、GitのCommit差分から生成する。対応表の判定変更やSampleApp一致のための修正も、どのStepのCommitに含めたかが分かるようにする。

## 7. 完了条件

- 各ファイルの初回追加Stepと最終一致Stepが対応表にある
- 中間実装の差分が未記録のまま残っていない
- StepごとのDiffが実際のCommit差分と一致している
- SampleAppに存在する実装がSolutionに不足していない
- Solutionだけに存在する業務実装がない
- Step08完了時点で、コメント以外の実装差分がない
