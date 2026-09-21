# 🚀 Android Architecture

Android / Kotlin / Jetpack Compose / Hilt を利用した、社内向けAndroidベースアーキテクチャのサンプルリポジトリです。

このREADMEはリポジトリの入口と資料の案内を目的とします。設計思想、要件、実装責務の詳細は、各設計書を参照してください。

**対象:** `BaseArchitecture` を利用してAndroidアプリを実装・レビューするエンジニア向け

**位置づけ:** 設計思想と実装サンプルを確認するためのリポジトリ入口

共通設計思想、Android実装ルール、要件定義、基本設計、詳細設計、ソースコードを次の関係で整理しています。

```text
共通設計思想ガイド
    ↓
Androidカスタマイズ設計書
    ↓
要件定義 / 基本設計 / 詳細設計
    ↓
BaseArchitectureの実装
```

> [!IMPORTANT]
> READMEは、詳細な設計内容を重複して説明する場所ではありません。
> 「どの資料を、どの場面で読むか」と「ソースがどこにあるか」を確認するために使用します。

---

## 🧭 このREADMEの使い方

### 🎯 このREADMEの役割

- リポジトリ全体の構成を確認する
- 設計資料の使い分けを確認する
- ソースコードの読み始める場所を確認する
- 新しいFeatureを追加するときの参照先を確認する

### 📌 こういうときに見る

- 初めてこのリポジトリを読むとき
- 設計思想とAndroid実装ルールの違いを確認したいとき
- 要件定義、基本設計、詳細設計のどれを参照すべきか迷ったとき
- Core moduleやSample Featureの配置を確認したいとき
- 設計書とソースコードの更新範囲を確認したいとき

## 🎯 このリポジトリの役割

本リポジトリでは、複数のAndroidアプリで再利用できる以下の考え方と実装例を扱います。

- Composeによる画面構築
- MVVM + MVI要素による状態管理
- UiState / UiEvent / UiMessage / UiEffect / Reducer
- BaseViewModelによるEvent Queue処理
- UseCase、Repository、Gateway、Providerの責務分離
- HiltによるDependency Injection
- DataStore / Roomによる端末内データ保存
- AppResult / AppError / UiErrorによるエラー構造化
- Navigation Composeによる画面遷移
- Core moduleの分割と再利用

本プロジェクトは、特定案件の完成品ではありません。Sample FeatureやFake実装を含むため、責務分離と処理フローを確認するためのベースアーキテクチャとして利用します。

## 🗂 ディレクトリ構成

```text
android_architecture/
├─ BaseArchitecture/       # Android Studioプロジェクトとソースコード
│  ├─ app/                  # アプリ固有のFeature、Navigation、DI
│  ├─ core/                 # 再利用可能なAndroid Library module
│  ├─ gradle/               # Version CatalogとGradle設定
│  └─ README.md             # Core moduleの利用補足
├─ docs/
│  ├─ 設計思想/              # 技術をまたいだ共通思想とAndroid適用ルール
│  ├─ 要件定義/              # 利用者の目的、振る舞い、要件
│  ├─ 基本設計/              # 機能構成、画面遷移、共通処理
│  ├─ 詳細設計/              # 実装責務、状態遷移、具体値、画面画像
│  └─ API仕様書/             # Dokkaで生成したKDoc HTML
└─ README.md                # このリポジトリの入口
```

## 🔎 資料の使い分け

| 確認したいこと | 参照する資料 |
|---|---|
| 技術に依存しない設計の考え方を確認したい | [共通設計思想ガイド](docs/設計思想/共通設計思想ガイド.md) |
| Androidでの具体的な実装ルールを確認したい | [Androidカスタマイズ設計書](docs/設計思想/Androidカスタマイズ設計書.md) |
| 利用者の目的や必要な振る舞いを確認したい | [要件定義書](docs/要件定義/要件定義書.md) |
| 画面一覧、画面遷移、機能全体の流れを確認したい | [基本設計書](docs/基本設計/基本設計書.md) |
| クラス責務、DI、State遷移、実装との対応を確認したい | [詳細設計書](docs/詳細設計/詳細設計書.md) |
| 画面レイアウトや実画面キャプチャを確認したい | [画面レイアウト設計書](docs/詳細設計/画面レイアウト設計書.md) |
| SDK、Plugin、Libraryの具体的なバージョンを確認したい | [実装基盤・バージョン一覧](docs/詳細設計/実装基盤・バージョン一覧.md) |
| UI文字列の名前、文言、用途を確認したい | [文字列定義一覧](docs/詳細設計/文字列定義一覧.md) |
| DataStore、Room、保存キー、テーブルを確認したい | [永続化仕様書](docs/詳細設計/永続化仕様書.md) |
| KDocから生成されたAPI仕様を確認したい | [API仕様書](docs/API仕様書/index.html) |

## 🧭 どこから読むか

新しく参加した場合は、次の順番で読むことを推奨します。

1. [共通設計思想ガイド](docs/設計思想/共通設計思想ガイド.md)で、責務分離の考え方を確認する
2. [Androidカスタマイズ設計書](docs/設計思想/Androidカスタマイズ設計書.md)で、Android上の対応関係を確認する
3. [要件定義書](docs/要件定義/要件定義書.md)で、サンプルアプリの対象範囲を確認する
4. [基本設計書](docs/基本設計/基本設計書.md)で、画面と機能の全体像を確認する
5. [詳細設計書](docs/詳細設計/詳細設計書.md)で、実装と設計要素の対応を確認する
6. `BaseArchitecture/app` と `BaseArchitecture/core` のソースを読む

## 🔄 実装を見るときの対応関係

```text
Screen
  -> UiEventを通知
Route
  -> ViewModel、State、Effect、Navigationを接続
ViewModel
  -> Eventを処理し、UseCaseを呼び出す
UseCase
  -> Repository / Gateway / Providerを利用する
Reducer
  -> UiMessageからUiStateを生成する
```

基本ルールとして、ScreenはViewModelやNavControllerを直接保持しません。ViewModelはContextやNavControllerを保持せず、NavigationやSnackbarなどの単発処理はUiEffectでRouteへ通知します。

## 🧱 Core module

| Module | 主な責務 |
|---|---|
| `:core:foundation` | AppResult、AppError、Validation、下位層向け契約 |
| `:core:presentation` | UiState、UiEvent、UiMessage、UiEffect、Reducer、BaseViewModel |
| `:core:platform` | StringProvider、DispatcherProvider、DateTime、Loggerなど |
| `:core:storage` | DataStore、Room、Cache、永続化実装 |
| `:core:network` | API応答処理、Network、Interceptor |
| `:core:ui` | Compose Theme、Dialog、Loading、Markdown表示 |
| `:core:license` | OSS License情報 |

必要な機能だけをアプリへ追加できるよう、Coreは責務ごとに分割しています。各moduleの利用方法は [BaseArchitecture/README.md](BaseArchitecture/README.md) を参照してください。

## 🧩 Sample Feature

`app`には、ベースアーキテクチャの利用例として以下のFeatureを配置しています。

| Feature | 主な内容 |
|---|---|
| Sample Home | 時刻に応じた挨拶、日付、テーマ反映 |
| Sample History | 操作ログの検索、カテゴリ絞り込み、一覧表示 |
| Sample Settings | 通知、ログイン時確認、キャッシュ保持、Theme設定 |
| Legal | プライバシーポリシー、利用規約の表示 |
| License | OSS License一覧と詳細表示 |
| Version Info | 現在バージョン、最新バージョン、更新導線 |

## 🛠 ビルドとドキュメント

```powershell
cd BaseArchitecture
.\gradlew.bat :app:assembleDebug
.\gradlew.bat test
.\gradlew.bat dokkaHtmlMultiModule
```

生成されたAPI仕様書は、通常 `docs/API仕様書/index.html` から確認します。生成タスクや出力先を変更した場合は、[実装基盤・バージョン一覧](docs/詳細設計/実装基盤・バージョン一覧.md) と合わせて更新します。

## ➕ 新しいFeatureを追加するとき

- 要件定義書へ利用者の目的と振る舞いを追加する
- 基本設計書へ画面、Route、機能フローを追加する
- FeatureのDomain / Application / Data / Presentationを定義する
- UiState、UiEvent、UiMessage、UiEffect、Reducerを定義する
- 詳細設計書へDI構成とState遷移を追加する
- Screen、Route、ViewModel、UseCase、Repositoryを実装する
- Unit Test、Preview、必要な画面キャプチャを追加する

UI文言、保存値、バージョンなどの具体値を変更した場合は、対応する個別設計書も更新します。

## 🔁 更新時の考え方

このリポジトリでは、ソースコードだけでなく、設計書との対応を維持することを重視します。

- 設計思想を変更した場合は、設計思想ガイドを更新する
- 機能の振る舞いを変更した場合は、要件定義書を更新する
- 画面遷移や処理フローを変更した場合は、基本設計書を更新する
- クラス責務やState遷移を変更した場合は、詳細設計書を更新する
- 文字列、保存値、バージョンを変更した場合は、個別一覧を更新する
- レイアウトを変更した場合は、画面レイアウト設計書とキャプチャを更新する

案件へ適用する際は、Sampleの文言、Fake実装、法務文書、固定値を案件仕様へ置き換えてください。
