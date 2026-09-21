# 🚀 Android Architecture Template

Android / Kotlin / Jetpack Compose / Hilt を利用した、新規Android案件開始用の最小Templateです。

**対象:** 新規案件の初期構成を作成するAndroid実装者・レビュー担当者

**位置づけ:** Core基盤とStarter画面だけを持つ開始点

このTemplateには、SampleAppの業務機能やサンプルデータは含めません。必要なFeature、永続化、API通信、OSS License表示などを案件の要件に応じて追加します。

> [!IMPORTANT]
> このTemplateは完成アプリではありません。
> `BaseArchitecture` をコピーした後、package、applicationId、アプリ名、Starter画面を案件用へ変更して使用します。

---

## 🧭 このREADMEの使い方

### 🎯 ここで確認すること

- Templateに含まれる最小構成
- 追加するOptional moduleの判断
- 設計思想・適用チェックリストの参照先
- 新規案件を開始するときの初期作業

### 📌 詳細設計を確認したいとき

実装の責務分離やState / Event / Message / Effectの考え方は、READMEへ重複して記載しません。次の資料を参照してください。

```text
共通設計思想ガイド
    ↓
Androidカスタマイズ設計書
    ↓
案件用の要件定義 / 基本設計 / 詳細設計
    ↓
BaseArchitectureの実装
```

## 🗂 ディレクトリ構成

```text
android_architecture/
├─ BaseArchitecture/
│  ├─ app/                  # Application、Navigation、Starter、案件Feature
│  ├─ core/
│  │  ├─ foundation/        # Result、Error、Validation、共通契約
│  │  ├─ presentation/      # UiState、UiEvent、UiMessage、UiEffect、Reducer
│  │  ├─ platform/          # Resource、Coroutine、Logger、Timeなど
│  │  └─ ui/                # Theme、Dialog、Loadingなど
│  └─ gradle/               # Version CatalogとGradle設定
├─ docs/
│  ├─ 設計思想/              # 共通思想とAndroid適用ルール
│  └─ Template適用チェックリスト.md
└─ README.md
```

## 🧱 含まれるCore module

| Module | 主な責務 |
|---|---|
| `:core:foundation` | `AppResult`、`AppError`、Validation、Themeなどの共通契約 |
| `:core:presentation` | `UiState`、`UiEvent`、`UiMessage`、`UiEffect`、`Reducer`、`BaseViewModel` |
| `:core:platform` | `StringProvider`、Coroutine、Logger、DateTime、AppInfoなど |
| `:core:ui` | Compose Theme、Dialog、Loading |

`BaseViewModel`はUiEventをChannelで順番に処理します。Screenはイベントを通知し、RouteがViewModelとNavigationを接続します。

```text
Screen
  → UiEvent
Route
  → ViewModel / State / Effect / Navigation
ViewModel
  → UseCase / dispatch / emitEffect
Reducer
  → UiMessageからUiStateを生成
```

## 🧩 Optional module

案件で必要になった時点で、SampleAppまたは実装基盤から追加します。

| 要件 | 追加候補 |
|---|---|
| DataStore / Roomを利用する | `:core:storage` |
| Retrofit / API通信を利用する | `:core:network` |
| OSS License画面を提供する | `:core:license` |
| Markdown文書を表示する | `core:ui`へMarkdown部品と依存を追加 |

不要なmoduleを最初から追加しないことで、案件開始時の依存関係とビルド時間を抑えます。

## 📚 設計資料

| 確認したいこと | 資料 |
|---|---|
| 技術に依存しない設計判断 | [共通設計思想ガイド](docs/設計思想/共通設計思想ガイド.md) |
| Androidでの具体的な実装ルール | [Androidカスタマイズ設計書](docs/設計思想/Androidカスタマイズ設計書.md) |
| Template適用時の初期作業 | [Template適用チェックリスト](docs/Template適用チェックリスト.md) |

SampleAppに含まれる要件定義書、基本設計書、詳細設計書、画面キャプチャは、Templateへは持ち込みません。案件開始後に案件用の資料として作成します。

## ➕ 新規案件の開始手順

1. `BaseArchitecture`を案件用の作業場所へコピーする
2. namespace、applicationId、rootProject.name、アプリ名を変更する
3. Starter画面と初期Routeを案件の初期画面へ置き換える
4. 必要なOptional moduleを選択する
5. 要件定義書、基本設計書、詳細設計書を作成する
6. Featureを画面単位で追加する
7. [Template適用チェックリスト](docs/Template適用チェックリスト.md)を完了する

## 🛠 ビルドと確認

```powershell
cd BaseArchitecture
.\gradlew.bat :app:assembleDebug
.\gradlew.bat test
.\gradlew.bat :app:dokkaHtml
```

DokkaのHTMLは `BaseArchitecture/app/build/dokka/html/index.html` に生成されます。生成物はTemplateリポジトリへ含めず、必要なときに生成します。

## 🔁 更新時のルール

- Coreの契約を変更した場合は、Androidカスタマイズ設計書を確認する
- Featureの振る舞いを追加した場合は、案件の要件定義・基本設計・詳細設計を更新する
- State / Event / Message / Effectを変更した場合は、画面ごとの遷移表を更新する
- 文字列、保存値、バージョンなどの具体値は、案件側の個別資料へ記録する
- Templateへ案件固有の業務ルールやFake実装を戻さない
