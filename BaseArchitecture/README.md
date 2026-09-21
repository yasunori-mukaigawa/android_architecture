# BaseArchitecture

新規Android案件の開始点として利用する最小構成です。

Android / Kotlin / Jetpack Compose / Hilt を前提に、業務機能を持たないStarter画面と、再利用するCore moduleだけを含みます。

## Module構成

| Module | 主な責務 |
| --- | --- |
| `:app` | Application、AppNavHost、Starter、案件Feature |
| `:core:foundation` | `AppResult`、`AppError`、Validation、共通契約 |
| `:core:presentation` | `UiState`、`UiEvent`、`UiMessage`、`UiEffect`、`Reducer`、`BaseViewModel` |
| `:core:platform` | Resource、Coroutine、Logger、DateTime、AppInfoなどのAndroid補助 |
| `:core:ui` | Compose Theme、Dialog、Loading |

案件で必要になった場合だけ、DataStore / Room、API通信、OSS License、Markdown表示などのmoduleや実装を追加します。
TemplateへSampleAppの業務機能、Fake Repository、画面固有の永続化や通信実装は戻しません。

## 初期画面

`app/src/main/java/jp/co/nsco/basearchitecture/feature/starter/presentation` に、起動確認用のStarter画面を配置しています。

StarterはViewModelやUseCaseを持たない最小画面です。案件開始時に、要件に応じたFeatureのRoute / Screen / ViewModelへ置き換えてください。

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
