# BaseArchitecture

Android / Kotlin / Jetpack Compose / Hilt を利用したベースアーキテクチャのサンプルプロジェクトです。

## Core Module

Core は責務ごとの Android Library module に分割しています。必要な機能だけをアプリへ追加できます。

| Module | 主な責務 |
| --- | --- |
| `:core:foundation` | AppResult / AppError / Validation / 下位層向け契約 |
| `:core:presentation` | UiState / UiEvent / UiMessage / UiEffect / Reducer / BaseViewModel |
| `:core:platform` | StringProvider / DispatcherProvider / DateTimeProvider / Logger など |
| `:core:storage` | DataStore / Database / Cache / 永続化実装 |
| `:core:network` | API応答処理 / Network / Interceptor |
| `:core:ui` | Compose Theme / Dialog / Loading / Markdown |
| `:core:license` | OSS License情報 |

各moduleのpackage名は既存の `jp.co.nsco.basearchitecture.core.*` を維持しています。
そのため、Coreをmoduleへ分割してもFeature側のImportは原則変更ありません。

```kotlin
implementation(project(":core:foundation"))
implementation(project(":core:presentation"))
implementation(project(":core:platform"))
```

Theme の保存契約や認証トークン提供契約のように、複数の実装層から参照する型は
`:core:foundation` に配置しています。保存実装は `:core:storage`、Compose のテーマ適用や
UI部品は `:core:ui` が担当します。

依存方向は、下位の契約を上位の実装が利用する形に揃えています。特に `:core:storage` は
`:core:network` に依存せず、`:core:network` も保存方式を知りません。アプリ側で必要な
moduleを組み合わせて利用してください。

OSS Licenseを利用しないアプリでは `:core:license` を追加する必要はありません。

## KDoc / Dokka ドキュメント生成

本プロジェクトでは、KDoc コメントから HTML ドキュメントを生成するために Dokka を利用します。

### 生成コマンド

```powershell
.\gradlew.bat :app:dokkaHtml
```

### 出力先

```text
app/build/dokka/html/index.html
```

生成後、`index.html` をブラウザで開くことで、KDoc から生成されたドキュメントを確認できます。

### 位置づけ

このドキュメントは、ベースアーキテクチャの責務分離・状態管理・画面設計の意図を確認するための設計リファレンスです。
