# BaseArchitecture

新規Android案件の開始点として利用する最小構成です。

Android / Kotlin / Jetpack Compose / Hilt を前提に、Sample FeatureのNavigation殻と、再利用するCore moduleを含みます。

## Module構成

| Module | 主な責務 |
| --- | --- |
| `:app` | Application、AppNavHost、Sample Feature、案件Feature |
| `:core:foundation` | `AppResult`、`AppError`、Validation、共通契約 |
| `:core:presentation` | `UiState`、`UiEvent`、`UiMessage`、`UiEffect`、`Reducer`、`BaseViewModel` |
| `:core:platform` | Resource、Coroutine、Logger、DateTime、AppInfoなどのAndroid補助 |
| `:core:ui` | Compose Theme、Dialog、Loading |

案件で必要になった場合だけ、DataStore / Room、API通信、OSS License、Markdown表示などのmoduleや実装を追加します。
TemplateへSampleAppの業務機能、Fake Repository、画面固有の永続化や通信実装は戻しません。

## Sample Feature

`app/src/main/java/jp/co/nsco/basearchitecture/feature/sample/presentation` に、Sample FeatureのHome、履歴、設定と共通Navigation部品を配置しています。

このブランチでは、01-sample-shellで作成したNavigation構成を引き継ぎ、02-core-foundation-presentationでCoreの共通契約を追加します。

## Coreの利用

```kotlin
implementation(project(":core:foundation"))
implementation(project(":core:presentation"))
implementation(project(":core:platform"))
implementation(project(":core:ui"))
```

依存方向は、共通契約を下位moduleへ置き、Android実装や画面実装がそれを利用する形にします。
`NavController`、`Context`、UseCase、Repositoryなどの責務をCoreへ過剰に集約しないでください。

## ドキュメント

設計思想とAndroidでの適用ルールは、親ディレクトリのREADMEから参照してください。

- `../README.md`: Template全体の適用手順
- `../docs/設計思想/共通設計思想ガイド.md`: 技術に依存しない設計判断
- `../docs/設計思想/Androidカスタマイズ設計書.md`: Androidでの具体的な構成と実装方針
- `../docs/Template適用チェックリスト.md`: 案件開始時の確認項目

## ビルドと確認

```powershell
.\gradlew.bat :app:assembleDebug
.\gradlew.bat test
.\gradlew.bat :app:dokkaHtml
```

Dokkaの出力先は `app/build/dokka/html/index.html` です。
