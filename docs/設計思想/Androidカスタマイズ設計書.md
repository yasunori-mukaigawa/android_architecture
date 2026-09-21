# 🚀 Androidアプリ カスタマイズ設計書

**対象:** `BaseArchitecture` を利用してAndroidアプリを実装・レビューするエンジニア向け

**実装同期:** 2026-07-28時点の `E:\_work\_project\ProtoType\Android\BaseArchitecture`

このガイドは、Androidアプリにおける実装ルールをまとめたものです。  
迷ったときはここに戻り、共通設計思想ガイドの考え方をAndroid実装へ落とし込んで作業を進めてください。

---

## 🧭 1. この文書の使い方

### 🎯 この文書の役割

本ドキュメントは、Androidアプリにおける  
**実装手順・命名・構成・具体コードの書き方** を定義するものです。

共通設計思想ガイドでは、技術スタックに依存しない  
プレゼンテーション層の責務分離・状態管理・副作用制御の考え方を定義しています。

一方、本書では、その思想を Android / Jetpack Compose / ViewModel / StateFlow / SharedFlow / Hilt などの  
Android実装へ落とし込みます。

```text
プレゼンテーション層 共通設計思想ガイド
    ↓
Android カスタマイズ設計書
    ↓
各機能の詳細設計 / 実装
```

> [!IMPORTANT]
> **「この文書は、Android実装のための作業ガイドである」**
>
> 共通設計思想ガイドで定義している責務分離・状態管理・副作用制御の考え方は変えず、  
> Androidではどのクラス・どの仕組み・どの書き方で実現するかを定義します。

### 📌 この章はこういうときに見る

- Androidアプリの実装を始める前に、全体の作法を確認したいとき
- Composeで画面を作るときに、どこまでComposableに書いてよいか迷ったとき
- ViewModelに処理を書きすぎていないか確認したいとき
- UiState / UiEvent / UiMessage / UiEffect の使い分けに迷ったとき
- Hilt / Repository / Gateway / UseCase の接続方法に迷ったとき
- レビュー時に、Android実装としての観点を確認したいとき
- 実装中に動かなくなり、どこから確認すべきか迷ったとき

### 🔁 共通設計思想ガイドとの使い分け

#### 共通設計思想ガイド

共通設計思想ガイドは、技術スタックを問わない設計判断の基準です。

```text
なぜその責務に分けるのか
なぜその依存方向にするのか
なぜその情報をStateに持つのか
なぜその処理をEffectにするのか
```

を判断するために使います。

#### Android カスタマイズ設計書

Android カスタマイズ設計書は、Androidでの具体的な実装方法を定義します。

```text
Composableではどう書くか
ViewModelではどう受けるか
UiStateはどう定義するか
UiMessageはどう定義するか
UiEffectはどう流すか
Hiltではどう登録するか
Navigation Composeではどう遷移するか
Room / DataStore / Retrofitではどう接続するか
```

を判断するために使います。

> [!IMPORTANT]
> **「共通思想は変えない。Androidでの実装方法だけを定義する」**
>
> AndroidにはAndroidの書き方があります。  
> ただし、責務・状態・副作用・依存方向の考え方まで変えてはいけません。

### 🧭 作業の基本フロー

Androidアプリで新しい画面や機能を追加する場合は、次の順で考えます。

```text
1. 画面に残る状態をUiStateとして定義する
2. ユーザー操作・初期表示・再試行をUiEventとして定義する
3. 処理結果をUiMessageとして定義する
4. Navigation / Dialog / SnackbarをUiEffectとして定義する
5. ReducerでUiMessageから次のUiStateを作る
6. ScreenはuiStateを描画しonEventを呼ぶ
7. RouteはonEventへviewModel::sendEventを接続する
8. BaseViewModelのChannelがEventをhandleEventへ順次渡す
9. ViewModelはUseCaseを呼びAppResultをMessage / Effectへ変換する
10. Repository / Gateway / ProviderがAndroid・外部I/Oの詳細を隠す
```

依存が増えた画面では、UseCase群を `UseCaseFacade`、
Mapper / Formatter / Factory / Provider群を `PresentationFacade` にまとめます。

> [!TIP]
> **迷ったら「状態・入力・処理結果・副作用」と「判断・取得・表示変換」に分けます。**
>
> 業務ルールはPolicy、現在時刻はProvider、表示形式はMapper / Formatterです。
> ViewModelのprivate関数や拡張関数へ再利用可能なルールを置きません。

### 🔎 やりたいこと別・逆引きインデックス

- **Android全体の構造を知りたい**  
  → 第2章：Androidにおける基本構造

- **共通思想とAndroid実装の対応を知りたい**  
  → 第3章：用語対応表 / 命名規約

- **画面やFeatureを追加したい**  
  → 第4章：画面 / Feature を追加したいとき

- **入力内容によって画面表示を変えたい**  
  → 第5章：入力内容によって画面表示を変えたいとき

- **UseCaseを呼び出したい**  
  → 第6章：UseCaseを呼び出したいとき

- **Room / DataStore / 設定値を扱いたい**  
  → 第7章：ローカル永続化を扱いたいとき

- **API通信を追加したい**  
  → 第8章：API通信をしたいとき

- **エラー処理を追加したい**  
  → 第9章：エラーハンドリング

- **画面遷移・ダイアログ・Snackbarを出したい**  
  → 第10章：Navigation / Dialog / Snackbar

- **Hiltの登録を追加したい**  
  → 第11章：Hilt

- **UiEvent / UiMessage / UiEffect の分け方に迷った**  
  → 第12章：Event / Message / Effect 分割の判断基準

- **Unit Testを書きたい**  
  → 第13章：テスト方針

- **レビュー観点を確認したい**  
  → 第14章：レビュー観点

- **設計としてやってはいけないパターンを確認したい**  
  → 第15章：アンチパターン

- **実装時にハマりやすい症状を確認したい**  
  → 第16章：ハマるポイントランキング

- **動かないときにどこを見るか確認したい**  
  → 第17章：動かないときのチェックフロー

- **ひな形をコピーしたい**  
  → 第18章：コピー用テンプレート

### 📝 ソースコードの参照ヒント

実装に迷った場合は、概念説明だけでなく現行ソースを次の順で確認します。

| 確認したい内容 | 参照先 |
|---|---|
| Event Queue / State / Effect | `core/architecture/BaseViewModel.kt` |
| Screen / Route分離 | `feature/sample/presentation/home/` |
| 複数UseCaseとFacade | `feature/sample/presentation/settings/` |
| Flow購読とJob管理 | `feature/sample/presentation/history/` |
| Feature内の複数画面 | `feature/license/presentation/list/`, `detail/`, `common/` |
| DataStore | `core/datastore/`, `feature/sample/data/` |
| Room / 操作ログ | `core/database/`, `feature/operationlog/` |
| Retrofit / AppResult変換 | `core/network/`, `feature/versioninfo/data/` |
| Themeの全画面適用 | `app/AppRoot.kt`, `core/ui/theme/` |
| Navigationの束ね方 | `app/navigation/AppNavHost.kt`, 各Feature Graph |
| 法務文書 / Markdown | `feature/legal/`, `core/ui/markdown/` |
| OSSライセンス | `core/license/`, `feature/license/` |

本書の `SampleRepository` や `SaveSampleUseCase` など一部のコードは、
責務を説明するための汎用例です。  
現在のSample画面そのものを示す箇所は、明示的に現行パスまたは実クラス名を記載します。

> [!CAUTION]
> **やってはいけないこと**
>
> - 一般的なAndroid記事だけを見て、本アプリの責務分離を無視すること
> - Composeのサンプルをそのまま流用し、ViewModel / UseCase / State の境界を崩すこと
> - `mutableStateOf` や `remember` に業務状態を逃がすこと
> - Navigation / Dialog / Snackbar をStateのboolで管理すること
> - Retrofit / Room / DataStore をViewModelやComposableから直接扱うこと

### 🎯 1章まとめ

この文書は、Androidアプリを実装するときの  
**具体的な作業ガイド** です。

```text
共通設計思想ガイド
  → なぜそう分けるか

Androidカスタマイズ設計書
  → Androidではどう書くか

各機能の詳細設計書
  → 対象機能では何を作るか
```

> [!IMPORTANT]
> **「思想は共通、実装はAndroid向けに具体化する」**
>
> 責務分離・状態管理・副作用制御・依存方向は共通思想に従い、  
> AndroidではCompose / ViewModel / StateFlow / Hiltなどを使って実現します。

---

## 🏗 2. Androidにおける基本構造

### 🎯 この章の目的

この章では、共通設計思想ガイドで定義している構造を、  
Androidアプリの実装要素へ対応付けます。

Androidでは、主に以下の構成で実装します。

```text
Composable
    ↓ UiEvent
ViewModel
    ↓ UseCase呼び出し
UseCase
    ↓ Repository / Gateway
External Resource

UseCase
    ↓ AppResult
ViewModel
    ↓ UiMessage / Reducer
UiState
    ↓ collectAsStateWithLifecycle
Composable

ViewModel
    ↓ UiEffect
Effect Handler
    ↓ Navigation / Dialog / Snackbar / Toast
```

> [!IMPORTANT]
> **「Composableは表示と入力、ViewModelは司令塔、UseCaseは処理手順」**
>
> Androidでも、共通思想の責務分離は変えません。  
> 変わるのは、XAMLではなくComposableで書く、BindingではなくStateFlowで購読する、という実装方法だけです。

### 🧱 レイヤ構成

Androidアプリでは、以下のレイヤ構成を基本とします。

```text
Presentation
  - Composable
  - Route
  - ViewModel
  - UiState
  - UiEvent
  - UiMessage
  - UiEffect
  - Reducer

Application
  - UseCase
  - Input
  - AppResult
  - AppError

Domain
  - Domain Model
  - ValueObject
  - Domain Policy
  - Domain Service

Data / Infrastructure
  - RepositoryImpl
  - GatewayImpl
  - DAO
  - Entity
  - DTO
  - Mapper
  - Retrofit API
  - DataStore

DI
  - Hilt Module
```

依存方向は、以下を基本とします。

```text
Presentation
  ↓
Application
  ↓
Domain

Application
  ↓ interface
Data / Infrastructure
```

> [!IMPORTANT]
> **「上位層は具体技術を知らない」**
>
> ViewModelはRoom / Retrofit / DataStoreを知りません。  
> UseCaseはRepository / Gatewayのinterfaceだけを知ります。  
> 具体技術はData / Infrastructure層へ閉じ込めます。

### 🗂 パッケージ構成例

現行プロジェクトでは、アプリ全体の入口、横断Core、Featureを次のように分けています。

```text
app/src/main/java/jp/co/nsco/basearchitecture/
  MainActivity.kt

  app/
    AppRoot.kt
    AppViewModel.kt
    navigation/
      AppNavHost.kt
    auth/
      AuthModule.kt
    database/
      AppDatabase.kt
      DatabaseProvideModule.kt
      DatabaseBindModule.kt
    datastore/
      PreferenceDataStoreModule.kt
    network/
      NetworkModule.kt

  core/
    architecture/
      BaseViewModel.kt
      UiState.kt
      UiEvent.kt
      UiMessage.kt
      UiEffect.kt
      Reducer.kt
    result/
    error/
    validation/
    resource/
    coroutine/
    logging/
    navigation/
    datastore/
    database/
    network/
    time/
    theme/
    appinfo/
    license/
    external/
    auth/
    cache/
    ui/
      dialog/
      loading/
      markdown/
      theme/
    di/
      CoreModule.kt

  feature/
    sample/
      application/
      domain/
      data/
      di/
      presentation/
        common/
        home/
        history/
        settings/

    operationlog/
      application/
      domain/
      data/

    license/
      application/
      presentation/
        common/
        list/
        detail/

    legal/
      application/
      domain/
      data/
      presentation/
      ui/

    versioninfo/
      application/
      domain/
      data/
      di/
      presentation/
      ui/
```

Feature内で複数画面を持つ場合は、`presentation` を画面単位に分けます。  
画面間で共有するRoute定義、Graph、TopBar、Bottom Navigation、ErrorMapperなどは
`presentation/common` に置きます。

### 🧱 Gradle Module構成

論理パッケージの責務に加えて、Gradle Moduleも責務単位で分割します。
各Moduleは必要な契約だけを参照し、保存・通信・Compose UIの実装詳細を横断させません。

| Gradle Module | 主な責務 | 主な依存先 |
|---|---|---|
| `:core:foundation` | AppResult / AppError / Validation / 下位層向け契約 | なし。Kotlin Coroutinesの型のみ利用 |
| `:core:presentation` | UiState / UiEvent / UiMessage / UiEffect / Reducer / BaseViewModel | `:core:foundation` |
| `:core:platform` | StringProvider / DispatcherProvider / DateTimeProvider / Timber Logger | `:core:foundation` |
| `:core:storage` | DataStore / Room / DatabaseExecutor / Theme保存実装 | `:core:foundation` |
| `:core:network` | Retrofit / OkHttp / API応答処理 / Interceptor | `:core:foundation` |
| `:core:ui` | Compose Theme / Dialog / Loading / Markdown | `:core:foundation` |
| `:core:license` | OSS License情報の取得 | `:core:foundation` |

アプリ側では、必要なCore Moduleを組み合わせて利用します。現在の依存方向は次のとおりです。

```text
core:foundation
  ↑
  ├─ core:presentation
  ├─ core:platform
  ├─ core:storage
  ├─ core:network
  ├─ core:ui
  └─ core:license

app
  └─ 必要なCore ModuleとFeature実装を利用
```

特に、以下の逆方向依存は作りません。

- `:core:storage` は `:core:network` に依存しない
- `:core:ui` は `:core:storage` に依存しない
- Compose UIはDataStoreやRoomの保存実装を直接知らない
- Network Interceptorはトークン保存方式を直接知らない

共通契約の配置は次のように分けます。

| 契約 / 実装 | 配置 |
|---|---|
| `AuthTokenProvider` | `:core:foundation` |
| `DataStoreAuthTokenStore` | `:core:storage` |
| `AuthHeaderInterceptor` | `:core:network` |
| `ThemeRepository`, `ThemeSettings`, `AppThemeMode` | `:core:foundation` |
| `ThemeDataStore`, `ThemeModule` | `:core:storage` |
| `BaseThemeRegistry`, `ThemeUiModule` | `:core:ui` |

> [!IMPORTANT]
> **`common` はFeature内共有、`core` はアプリ横断共有です。**
>
> 1画面だけが使うMapperやFormatterを `core` に上げません。  
> 複数画面で使うがFeature外へ公開する必要がないものは、Featureの `common` に閉じます。

### 🧩 Androidで使用する主要要素

| 要素 | 役割 |
|---|---|
| `MainActivity` | Composeの起点として `AppRoot` を表示する |
| `AppRoot` | アプリ共通ThemeとNavigationを接続する |
| `Screen` | `UiState` の描画と `onEvent` による操作通知 |
| `Route` | ViewModel取得、State/Effect購読、Navigation・Dialog・Snackbar実行 |
| `BaseViewModel` | StateFlow / SharedFlow / Event Channel / dispatch / emitEffectを統一する |
| `UiState` | 画面が保持すべき表示状態 |
| `UiEvent` | ユーザー操作・画面ライフサイクル入力 |
| `UiMessage` | State変更の理由・処理結果 |
| `Reducer` | 現在StateとMessageから次Stateを生成する純粋関数 |
| `UiEffect` | 一度だけ実行するUI副作用 |
| `UseCase` | アプリケーションとしての処理手順 |
| `Repository` | 業務データの取得・保存契約 |
| `Gateway` | 外部API・SDK・デバイス・OS境界の契約 |
| `Provider` | 時刻、文字列、アプリ情報など環境値の取得契約 |
| `UseCaseFacade` | 画面が使うUseCase群を束ねる依存グループ |
| `PresentationFacade` | Mapper / Formatter / Factory / Provider群を束ねる依存グループ |
| Hilt Module | interfaceと実装、生成処理、Scopeを登録する |

### 🖼 現在のアプリ共通レイアウト

`MainActivity` は `AppRoot` だけを表示し、`AppRoot` がテーマ設定を購読して
`BaseAppTheme` の内側に `AppNavHost` を配置します。  
このため、設定画面で選択したThemePresetはHomeだけでなく全画面へ反映されます。

Sample Featureの主要画面は、次の構成です。

```text
Sample Home / History / Settings
  ├─ TopAppBar
  │   ├─ Hamburger menu
  │   └─ Notification icon
  ├─ Screen content
  └─ Bottom Navigation
      ├─ 履歴
      ├─ Home
      └─ 設定
```

Navigation Drawerには、プライバシーポリシー、利用規約、ライセンス、
バージョン情報への導線を置きます。  
法務文書、ライセンス一覧・詳細、バージョン情報は独立した戻る導線を持つ画面です。

| 機能 | 現行実装の要点 |
|---|---|
| Home | `DateTimeProvider` で端末日時を取得し、`SampleGreetingPolicy` で時間帯を判定、Formatterで挨拶・日付を表示する |
| Settings | 通知、ログイン時確認、キャッシュ保持をDataStoreへ保存し、ThemePreset / ThemeModeも変更する |
| History | Roomの操作ログをFlow購読し、検索・種別フィルタを画面Stateへ反映する |
| License | OSS License Gradle Pluginの生成情報を `LicenseProvider` 経由で一覧・詳細表示する |
| Legal | raw resourceのMarkdownをRepository / UseCase経由で読み、Markdown表示部品で描画する |
| VersionInfo | `AppInfoProvider`、最新Version Repository、外部リンクProviderを組み合わせて表示する |

画面固有の一時状態はCompose側へ置けます。現在もDrawer開閉、Dialog表示中状態、
`SnackbarHostState` はRouteの `remember` で保持しています。  
業務上意味のある状態や画面再生成後も必要な状態は `UiState` に置きます。

### 🔄 標準データフロー

画面操作から状態更新までを、次の流れで統一します。

```text
User
  ↓
Screen.onEvent(UiEvent)
  ↓ callback
Route
  ↓ viewModel.sendEvent(event)
BaseViewModel.eventChannel (Channel.BUFFERED)
  ↓ 順次処理
FeatureViewModel.handleEvent(event)
  ↓
UseCaseFacade / UseCase
  ↓ AppResult
FeatureViewModel
  ↓ dispatch(UiMessage)
Reducer
  ↓
UiState
  ↓ collectAsStateWithLifecycle
Screen
```

`Screen` の引数名は `onEvent` のままです。  
これは表示部品から見たイベント通知callbackであり、ViewModelの処理入口ではありません。
Routeはこのcallbackへ `viewModel::sendEvent` を渡します。

Navigation、Dialog、Snackbar、外部URI起動などは次の流れです。

```text
FeatureViewModel
  ↓ emitEffect(UiEffect)
BaseViewModel.uiEffect (SharedFlow)
  ↓ collect
Route
  ↓
NavController / AppDialog / SnackbarHostState / ExternalUriOpener
```

> [!IMPORTANT]
> **画面表示はState、単発処理はEffect、ユーザー入力はChannelです。**
>
> Event Channelは入力順を扱うためのキューです。  
> `handleEvent` 内で別のJobを起動した場合、そのJob同士の完了順までは保証されません。
> 長時間購読が必要な処理は、History画面のようにJobを明示的に保持・cancelします。

### 🖼 Composableの責務

Composableは、画面表示とユーザー操作の検知に責務を限定します。

#### やること

- UiStateを表示する
- ユーザー操作を検知する
- ユーザー操作をUiEventとしてViewModelへ通知する
- 表示上のレイアウト・余白・色・フォーカスなどを扱う

#### やらないこと

- UseCaseを直接呼ぶ
- Repository / Gateway / API Client を直接呼ぶ
- 保存可否・登録可否などの業務判断を行う
- 処理結果を判断する
- エラー文言を組み立てる
- Navigationを業務判断込みで直接実行する

> [!CAUTION]
> **Composableに業務判断を書かない。**
>
> Composableが知ってよいのは、  
> 「今のStateが何か」と「ユーザーが何をしたか」までです。

### 🧭 Routeの責務

Routeは、ComposableとViewModel、Android UI機構を接続する場所です。

#### やること

- `hiltViewModel()` でViewModelを取得する
- `collectAsStateWithLifecycle()` でUiStateを購読する
- `LaunchedEffect(viewModel)` またはRoute引数を含むKeyで、UiEffect購読を開始してから初期Eventを `sendEvent` する
- `launch(start = CoroutineStart.UNDISPATCHED)` でEffect collectorを先に起動し、初期処理のEffect取りこぼしを避ける
- NavControllerによる遷移を実行する
- Dialog、Snackbar、DrawerなどのCompose一時状態を管理する
- Screenへ `uiState` と `onEvent = viewModel::sendEvent` を渡す

#### やらないこと

- 業務判断を行う
- UseCaseを直接呼ぶ
- DB / APIに触る
- State更新理由を判断する
- ViewModelへNavControllerを渡す

```kotlin
@Composable
fun XxxRoute(
    navController: NavController,
    viewModel: XxxViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var dialogState by remember { mutableStateOf<DialogUiState?>(null) }

    LaunchedEffect(viewModel) {
        launch(start = CoroutineStart.UNDISPATCHED) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    XxxUiEffect.NavigateBack -> navController.popBackStack()
                    is XxxUiEffect.ShowSnackbar ->
                        snackbarHostState.showSnackbar(effect.message)
                    is XxxUiEffect.ShowDialog ->
                        dialogState = DialogUiState(
                            title = effect.title,
                            message = effect.message,
                            positiveButtonText = "OK"
                        )
                }
            }
        }

        viewModel.sendEvent(XxxUiEvent.Initialize)
    }

    XxxScreen(
        uiState = uiState,
        onEvent = viewModel::sendEvent
    )
}
```

表示文言は実装では `stringResource` または `StringProvider` から取得します。

> [!IMPORTANT]
> **Routeは接続、Screenは表示です。**
>
> NavController、ViewModel、DrawerState、DialogState、SnackbarHostStateはRouteに閉じます。

### 🧠 ViewModelの責務

ViewModelは画面単位の制御役です。すべてのFeature ViewModelは原則として
`BaseViewModel<S, E, M, F>` を継承します。

#### やること

- `handleEvent` でUiEventを解釈する
- UseCaseまたはUseCaseFacadeを呼ぶ
- `AppResult` をUiMessage / UiEffectへ変換する
- `dispatch` でReducerへMessageを渡す
- `emitEffect` で単発処理を通知する
- 必要な場合だけ `stateValue` / `currentState()` を参照する

#### やらないこと

- publicな `onEvent` をFeatureごとに実装する
- MutableStateFlow / MutableSharedFlowを各ViewModelで重複定義する
- 業務ルール、日時取得、表示変換をViewModel内の拡張関数へ置く
- Repository / DAO / APIを直接呼ぶ
- ContextやNavControllerを保持する
- Reducerを介さずStateを変更する

```kotlin
@HiltViewModel
class SampleViewModel @Inject constructor(
    reducer: SampleReducer,
    private val useCaseFacade: SampleHomeUseCaseFacade,
    private val presentationFacade: SampleHomePresentationFacade
) : BaseViewModel<
    SampleUiState,
    SampleUiEvent,
    SampleUiMessage,
    SampleUiEffect
>(
    initialState = SampleUiState.initial(),
    reducer = reducer
) {
    override suspend fun handleEvent(event: SampleUiEvent) {
        when (event) {
            SampleUiEvent.Initialize -> initialize()
        }
    }

    private suspend fun initialize() {
        dispatch(SampleUiMessage.LoadStarted)
        // UseCase結果をMessage / Effectへ変換する
    }
}
```

依存が増える画面では、次の2系統にまとめます。

- `XxxUseCaseFacade`: 画面が利用するUseCase群
- `XxxPresentationFacade`: Mapper / Formatter / Factory / Provider群

ReducerはState遷移の契約として別に注入します。  
Facadeは依存を束ねるだけで、処理手順や業務ロジックを持ちません。
1つしか依存がない単純画面では、Facadeを無理に作らず直接注入して構いません。

現行ではSample Home / History / SettingsとLicense list / detailが
2系統Facadeを採用しています。LegalとVersionInfoは依存が比較的小さいため、
UseCaseやMapperを直接注入しています。

> [!IMPORTANT]
> **ViewModelは交通整理に留めます。**
>
> 時間帯判定はPolicy、端末時刻取得はProvider、表示整形はFormatter、
> DTO/Entity変換はMapperへ置きます。

### 🧱 UiStateの責務

UiStateは、画面が保持すべき状態を表します。

#### 持ってよいもの

- 入力値
- 選択値
- 一覧データ
- ローディング状態
- バリデーション結果
- ボタン活性状態
- 画面内に表示し続けるエラー情報
- タブ選択状態
- 検索条件
- ソート条件

#### 持ってはいけないもの

- 画面遷移要求
- Toast表示要求
- Snackbar表示要求
- Dialog表示要求
- 外部アプリ起動要求
- 一度だけ実行したい処理
- Room Entity
- API Response DTO

> [!CAUTION]
> **`shouldNavigate` / `showDialog` / `showToast` をUiStateに持たせない。**
>
> これらは再描画・再購読・画面復元時に再実行される可能性があるため、  
> `UiEffect` として扱います。

### ⚡ UiEffectの責務

UiEffectは、一度だけ実行したい副作用を表します。

#### 代表例

- 画面遷移
- 戻る操作
- Dialog表示
- Snackbar表示
- Toast表示
- 外部アプリ起動
- 権限要求
- ファイル共有
- キーボードを閉じる
- フォーカス移動

UiEffectは、ViewModelから発行し、ComposableまたはEffect Handlerで購読して実行します。

```text
ViewModel
  ↓ UiEffect
Composable / EffectHandler
  ↓ 実際の副作用
```

> [!TIP]
> **再実行されたら困るものはEffect。**
>
> 画面回転・再描画・再購読で何度も発火すると困るものは、  
> StateではなくEffectとして設計してください。

### 🧪 Reducerの責務

Reducerは、現在のUiStateとUiMessageから、新しいUiStateを生成します。

#### Reducerのルール

- 副作用を持たない
- suspend関数にしない
- Repository / UseCase を呼ばない
- 同じ入力に対して同じ出力を返す
- UiStateを直接変更せず、copyで新しいStateを返す
- ログ出力やNavigationを行わない

```kotlin
class SampleReducer @Inject constructor() :
    Reducer<SampleUiState, SampleUiMessage> {

    override fun reduce(
        currentState: SampleUiState,
        message: SampleUiMessage
    ): SampleUiState {
        return when (message) {
            SampleUiMessage.LoadStarted -> {
                currentState.copy(
                    isLoading = true
                )
            }

            is SampleUiMessage.LoadSucceeded -> {
                currentState.copy(
                    isLoading = false,
                    title = message.title
                )
            }

            is SampleUiMessage.LoadFailed -> {
                currentState.copy(
                    isLoading = false,
                    errorMessage = message.error.message
                )
            }
        }
    }
}
```

> [!IMPORTANT]
> **「Reducerは状態計算だけを行う」**
>
> Reducerに副作用が入った時点で、状態変更の再現性が崩れます。

### 🧩 UseCase / Repository / Gatewayの責務

#### UseCase

UseCaseは、アプリケーションとしての処理手順を表します。

- Domain Policyを呼ぶ
- Repository / Gateway / Providerを必要な順序で呼ぶ
- 入出力をApplication用の型に整える
- 成功・失敗を `AppResult` で返す
- UI文言、UiState、NavControllerを知らない

#### Repository

Repositoryは、業務データの取得・保存契約です。  
Room、DataStore、File、APIなど、取得元が何であるかを上位層へ漏らしません。

現行実装では次の例があります。

- `SampleSettingsRepository` / `SampleSettingsRepositoryImpl`: DataStore設定
- `OperationLogRepository` / `OperationLogRepositoryImpl`: Room操作ログ
- `LatestVersionRepository` / `RetrofitLatestVersionRepository`: API取得

#### Gateway

Gatewayは、外部サービス・SDK・デバイス・OS機能との操作境界が
Repositoryという「データ集合」の概念に合わない場合に使います。

```text
REST APIをデータ取得元として扱う
  → Repositoryでよい

カメラ、NFC、Bluetooth、外部SDK、決済、認証操作
  → Gatewayが自然
```

Gatewayを全Featureへ機械的に作る必要はありません。  
責務に合う境界だけを作ります。

#### Facade

Facadeはレイヤを追加するものではなく、ViewModelの依存を読みやすくまとめる
Presentation内部の依存グループです。

```kotlin
class SampleHomeUseCaseFacade @Inject constructor(
    val getSampleHomeHeader: GetSampleHomeHeaderUseCase,
    val saveOperationLog: SaveOperationLogUseCase
)

class SampleHomePresentationFacade @Inject constructor(
    val errorMapper: SampleUiErrorMapper,
    val homeHeaderFormatter: SampleHomeHeaderFormatter,
    val operationLogMessageFactory: SampleOperationLogMessageFactory
)
```

> [!CAUTION]
> **Facadeへ処理を吸い込みすぎません。**
>
> UseCaseFacadeはUseCaseを束ねるだけ、PresentationFacadeは表示補助依存を束ねるだけです。
> 巨大なService Locatorや万能Controllerにしません。

### 🌊 Coroutine / Flow / Channel の基本ルール

| 要素 | 用途 |
|---|---|
| `Channel<UiEvent>(Channel.BUFFERED)` | ViewModelへ順番に渡すイベント入力キュー |
| `StateFlow<UiState>` | 現在の画面状態 |
| `SharedFlow<UiEffect>` | Navigation / Dialog / Snackbarなどの単発通知 |
| `viewModelScope` | ViewModel寿命に紐づく非同期処理 |
| `collectAsStateWithLifecycle` | Screen表示用StateのLifecycle-aware購読 |
| `LaunchedEffect(viewModel[, route args])` | Effect購読を開始してから初期Eventを通知 |

`MutableStateFlow`、`MutableSharedFlow`、Event Channelは `BaseViewModel` 内に閉じます。
Feature ViewModelは `dispatch`、`emitEffect`、`stateValue` を利用します。

```kotlin
private val eventChannel = Channel<E>(Channel.BUFFERED)

init {
    viewModelScope.launch {
        for (event in eventChannel) {
            handleEvent(event)
        }
    }
}

fun sendEvent(event: E) {
    viewModelScope.launch(start = CoroutineStart.UNDISPATCHED) {
        eventChannel.send(event)
    }
}
```

Stateは次のように購読します。

```kotlin
val uiState by viewModel.uiState.collectAsStateWithLifecycle()
```

EffectはRouteで購読します。Effectは再生しない単発通知として扱うため、
購読者がいない期間のEffectは保持しません。初期処理がEffectを発行する可能性がある場合は、
Effect購読を開始してから初期Eventを送信します。

```kotlin
LaunchedEffect(viewModel) {
    launch(start = CoroutineStart.UNDISPATCHED) {
        viewModel.uiEffect.collect(::handleEffect)
    }

    viewModel.sendEvent(XxxUiEvent.Initialize)
}
```

> [!CAUTION]
> `LaunchedEffect(uiState)` で初期化やAPI通信を行うと、
> State更新のたびに再実行されます。初期化は `viewModel` を基本Keyとし、
> Route引数に応じて再初期化する画面では `LaunchedEffect(viewModel, routeArg)` を使います。

> [!IMPORTANT]
> **初期Eventより先にEffect購読を確立する。**
>
> `SharedFlow`は過去のEffectをReplayしません。初期Eventの処理が即時にEffectを発行する可能性があるため、
> Routeではcollectorを先に起動してから`sendEvent`を呼び出します。

> [!IMPORTANT]
> **CancellationExceptionを業務エラーへ変換しない。**
>
> API、Database、DataStoreなどで`Throwable`を捕捉して`AppResult.Failure`へ変換する場合も、
> `CancellationException`は再送出します。画面破棄やCoroutineキャンセルを、利用者向けエラーとして扱わないためです。

### 🧰 共通部品

現行の `core` は、次の横断責務を提供します。

| パッケージ | 主な部品 | 役割 |
|---|---|---|
| `core.architecture` | `BaseViewModel`, `marker interfaces`, `Reducer` | Presentation標準フロー |
| `core.result` | `AppResult` | 成功・失敗の共通表現 |
| `core.error` | `AppError`, `UiError`, `UiErrorAction` | エラー構造 |
| `core.validation` | `Field/Form validation result` | Validation結果 |
| `core.auth` | `AuthTokenProvider`, `AuthTokenStore` | 認証契約 |
| `core.resource` | `StringProvider`, `RawResourceReader` | Contextを隠したResource取得 |
| `core.coroutine` | `DispatcherProvider` | Dispatcher差し替え |
| `core.time` | `DateTimeProvider`, `AppClock` | 日時・epoch time取得 |
| `core.datastore` | `PreferenceDataStore` | Key-Value保存の薄い抽象 |
| `core.database` | `DatabaseExecutor`, transaction runner | Room例外・DB実行の共通化 |
| `core.network` | `ApiResponseHandler`, `ApiErrorMapper` | Retrofit結果のAppResult変換 |
| `core.theme` | `Theme settings/repository` | Theme選択状態 |
| `core.ui.theme` | `BaseAppTheme`, preset/registry/spacing | 全画面テーマ適用 |
| `core.appinfo` | `AppInfoProvider` | PackageManager依存の隠蔽 |
| `core.license` | `LicenseProvider` | OSSライセンス情報取得 |
| `core.external` | `ExternalUriOpener` | Android Intent起動の隠蔽 |
| `core.ui.dialog` | `DialogUiState`, `AppDialog` | Material3 Dialog |
| `core.ui.loading` | `LoadingContent` | 最小のLoading slot |
| `core.ui.markdown` | `Markdown表示部品` | 法務文書表示 |
| `core.navigation` | `asRouteArgument` | Route引数のencodeだけを補助 |

CoreにNavHost、BaseRoute、BaseScreen、自動ErrorHandler、自動LoadingHandlerは置きません。  
Navigation Compose本体は `app.navigation` と各Feature Graphが直接扱います。

> [!IMPORTANT]
> **Coreは共通化のための最小境界です。**
>
> 便利だからではなく、複数Featureで同じ責務・同じ失敗変換・同じ差し替え点が
> 必要なものだけを置きます。

### 🧾 文字列管理方針

画面表示用の文言は、原則として `strings.xml` または `StringProvider` を通じて取得します。

#### 基本ルール

```text
Composable
  → 表示用文字列を受け取って表示する

ViewModel
  → Contextを持たない
  → 必要な場合はStringProviderを使う

UseCase
  → 表示文言を返さない

Domain
  → 表示文言を持たない
```

#### StringProvider例

```kotlin
interface StringProvider {

    fun getString(
        resId: Int
    ): String

    fun getString(
        resId: Int,
        vararg args: Any
    ): String
}
```

```kotlin
class AndroidStringProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : StringProvider {

    override fun getString(
        resId: Int
    ): String {
        return context.getString(resId)
    }

    override fun getString(
        resId: Int,
        vararg args: Any
    ): String {
        return context.getString(resId, *args)
    }
}
```

> [!CAUTION]
> **ViewModelでContextを持たない。**
>
> ViewModelがContextを保持すると、Android UI実装への依存が強くなります。  
> 文言取得が必要な場合は、StringProviderを経由します。

> [!IMPORTANT]
> **UseCaseは文言を返さない。**
>
> UseCaseは失敗理由を `AppError` として返します。  
> 画面表示文言への変換はPresentation層で行います。

#### サンプル内の直書き文言について

本書のサンプルコードでは、説明を簡潔にするために `"保存しました"` などの直書き文言を使う場合があります。  
実装時は、必要に応じて `strings.xml` または `StringProvider` 経由へ置き換えてください。

> [!TIP]
> **サンプルの直書き文言は説明用。**
>
> 実コードでは、文言管理方針に従ってリソース化します。

### 🖼 Compose Preview方針

Compose Previewでは、ViewModelを使いません。  
Preview用のUiStateを用意し、Screen単体で表示確認します。

#### 基本ルール

```text
1. PreviewでViewModelを使わない
2. PreviewではScreenを直接呼ぶ
3. Preview用UiStateを用意する
4. Normal / Loading / Error のPreviewを用意する
5. Preview内でUseCase / Repository / Gatewayを呼ばない
```

#### Preview例

```kotlin
@Preview(showBackground = true)
@Composable
private fun SampleScreenPreview_Normal() {
    SampleScreen(
        uiState = SampleUiState(
            isLoading = false,
            title = "サンプル画面",
            name = "テスト太郎",
            nameErrorMessage = null,
            screenErrorMessage = null
        ),
        onEvent = {}
    )
}
```

```kotlin
@Preview(showBackground = true)
@Composable
private fun SampleScreenPreview_Loading() {
    SampleScreen(
        uiState = SampleUiState(
            isLoading = true,
            title = "サンプル画面",
            name = "",
            nameErrorMessage = null,
            screenErrorMessage = null
        ),
        onEvent = {}
    )
}
```

```kotlin
@Preview(showBackground = true)
@Composable
private fun SampleScreenPreview_Error() {
    SampleScreen(
        uiState = SampleUiState(
            isLoading = false,
            title = "サンプル画面",
            name = "",
            nameErrorMessage = "名前を入力してください",
            screenErrorMessage = "読み込みに失敗しました"
        ),
        onEvent = {}
    )
}
```

> [!IMPORTANT]
> **PreviewはScreenの表示確認に使う。**
>
> RouteやViewModelをPreviewに持ち込まず、  
> UiStateごとの表示差分を確認できるようにします。

### 🧬 Hilt / DIの責務

Hiltは、設計した依存方向を実行時に接続するために使います。

```text
@Inject constructor
  → Reducer / UseCase / Mapper / Formatter / Facade

@Binds
  → Repository / Provider / Storeなどのinterfaceと実装

@Provides
  → Room / DataStore / Retrofit / OkHttp / DAOなど生成手順が必要な依存
```

現行のModuleは次の単位です。

```text
app/auth
app/database
app/datastore
app/network
core/di
core/theme
feature/sample/di
feature/operationlog/data/di
feature/legal/di
feature/versioninfo/di
```

> [!IMPORTANT]
> **DI構成は依存方向の実体です。**
>
> Core ModuleへFeature固有依存を集めず、app共通生成、Core契約、
> Feature固有契約の単位で分けます。

### 🎯 2章まとめ

```text
AppRoot
  → ThemeとAppNavHostを接続する

Screen
  → UiStateを描画し、onEvent callbackを呼ぶ

Route
  → State / Effectを購読し、viewModel::sendEventとNavigationを接続する

BaseViewModel
  → Event Channel、StateFlow、SharedFlow、dispatch、emitEffectを提供する

Feature ViewModel
  → handleEventでUseCase実行、Message dispatch、Effect発行を制御する

UseCase / Policy
  → 処理手順と業務ルールを担当する

Repository / Gateway / Provider
  → 保存、通信、Android環境の詳細を隠蔽する

Reducer
  → Messageから次のStateだけを計算する
```

> [!IMPORTANT]
> **明示的な責務分離を守りつつ、Composeの局所状態と宣言的描画は活かします。**

## 🧠 3. 用語対応表 / 命名規約

### 🎯 この章の目的

この章では、共通設計思想ガイドで使用している用語を、  
Android実装上の名称へ対応付けます。

また、Android実装で使用するクラス名・interface名・ファイル名の命名規約を定義します。

名前を完全に揃えることが目的ではありません。  
重要なのは、用語が違っても同じ責務として扱えることです。

> [!IMPORTANT]
> **「名前ではなく責務で見る」**
>
> Androidでは `UiState` / `StateFlow` / `SharedFlow` などの用語を使います。  
> ただし、共通思想上の役割と対応しているかを常に確認してください。

### 🔁 共通概念とAndroid実装の対応

| 共通概念 | Android実装 | 役割 |
|---|---|---|
| View / UI Component | Composable / Screen | 表示とユーザー操作の検知 |
| Presentation Controller | ViewModel | UiEventを受け取り、UseCase呼び出し・State更新・Effect発行を制御する |
| State | UiState / StateFlow | 画面が保持すべき状態 |
| Event / User Action | UiEvent | ユーザーが何をしたかを表す入力事実 |
| Message | UiMessage | State変更の理由・処理結果 |
| Reducer | Reducer / reduce関数 | 現在StateとMessageから新しいStateを作る |
| Effect | UiEffect | 一度だけ実行される副作用 |
| Effect Handler | LaunchedEffect / Effect Collector | Effectを実際のNavigation / Dialog / Snackbarへ変換する |
| UseCase | XxxUseCase | アプリケーションとしての処理手順 |
| Domain | Domain Model / ValueObject / DomainService | 業務上の不変条件・値の正しさ |
| Repository | XxxRepository | 業務データの取得・保存を隠蔽する |
| Gateway | XxxGateway | 外部API / SDK / デバイス接続を隠蔽する |
| Dependency Wiring | Hilt Module | 依存関係の登録 |

> [!TIP]
> **Mutationという呼び方について**
>
> 他のMVI実装や文献では、`UiMessage` に近い概念を `Mutation` と呼ぶ場合があります。  
> 本ガイドでは、呼称を `UiMessage` に統一します。

### 🖼 View / UI Component

#### 共通思想上の役割

View / UI Component は、表示とユーザー操作の検知を行います。

#### Androidでの実装

Androidでは、主に `Composable` がこの役割を担います。

```kotlin
@Composable
fun SampleScreen(
    uiState: SampleUiState,
    onEvent: (SampleUiEvent) -> Unit
) {
    Button(
        onClick = {
            onEvent(SampleUiEvent.SaveClicked)
        }
    ) {
        Text("保存")
    }
}
```

#### やること

- UiStateを表示する
- ユーザー操作をUiEventへ変換する
- UI部品固有の表示制御を行う

#### やらないこと

- UseCaseを呼ぶ
- Repositoryを呼ぶ
- 業務判断を行う
- 成功 / 失敗を判断する

> [!CAUTION]
> **Composableは、結果を知らない。**
>
> Composableが知ってよいのは、  
> `uiState` と `onEvent` だけです。

### 🧭 Route

#### 共通思想上の役割

Routeは、View / UI Component と Presentation Controller を接続します。

#### Androidでの実装

Androidでは、`XxxRoute` として定義し、ViewModel接続・State購読・Effect購読を担当します。

```kotlin
@Composable
fun SampleRoute(
    viewModel: SampleViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SampleScreen(
        uiState = uiState,
        onEvent = viewModel::sendEvent
    )
}
```

> [!IMPORTANT]
> **Routeは接続、Screenは表示。**
>
> ScreenにViewModelやNavControllerを直接持ち込まないようにします。

### 🧠 Presentation Controller

#### 共通思想上の役割

Presentation Controllerは、Eventを受け、UseCase実行、Message dispatch、
Effect発行を制御します。

#### Androidでの実装

Androidでは `XxxViewModel : BaseViewModel<S, E, M, F>` が担当します。

```kotlin
@HiltViewModel
class XxxViewModel @Inject constructor(
    reducer: XxxReducer,
    private val useCaseFacade: XxxUseCaseFacade,
    private val presentationFacade: XxxPresentationFacade
) : BaseViewModel<XxxUiState, XxxUiEvent, XxxUiMessage, XxxUiEffect>(
    initialState = XxxUiState.initial(),
    reducer = reducer
) {
    override suspend fun handleEvent(event: XxxUiEvent) {
        when (event) {
            XxxUiEvent.Initialize -> initialize()
        }
    }
}
```

Routeは `viewModel::sendEvent` をScreenへ渡します。  
`sendEvent` とEvent ChannelはBaseViewModelが提供し、Feature ViewModelは
`handleEvent` だけを実装します。

#### やること

- Eventを意味解釈する
- UseCase / UseCaseFacadeを呼ぶ
- AppResultをMessage / Effectへ変換する
- Reducer経由でStateを更新する
- 単発処理をEffectで通知する

#### やらないこと

- Event入力口をFeatureごとに作る
- MutableStateFlow / MutableSharedFlowを再実装する
- 業務ルールや表示整形をprivate拡張関数として抱える
- Context / NavController / DAO / APIを保持する

### 🧱 State / UiState

#### 共通思想上の役割

Stateは、画面が保持すべき事実です。

#### Androidでの実装

Androidでは、`UiState` を `data class` として定義し、  
`StateFlow` で公開します。

```kotlin
data class SampleUiState(
    val isLoading: Boolean,
    val title: String,
    val inputText: String,
    val errorMessage: String?
) : UiState {
    companion object {
        fun initial(): SampleUiState {
            return SampleUiState(
                isLoading = false,
                title = "",
                inputText = "",
                errorMessage = null
            )
        }
    }
}
```

#### 持ってよいもの

- 画面に表示する値
- 入力中の値
- 選択状態
- ローディング状態
- 画面内に残すエラー情報
- ボタン活性条件

#### 持ってはいけないもの

- 遷移要求
- Snackbar表示要求
- Toast表示要求
- Dialog表示要求
- DTO / Entity / RawConfig

> [!TIP]
> **画面に残る事実はState。**
>
> 再描画されても同じ意味で扱える情報だけをUiStateに持たせます。

### ⚡ Event / UiEvent

#### 共通思想上の役割

Eventは、ユーザーや画面ライフサイクルから入った入力事実です。

#### Androidでの実装

```kotlin
sealed interface SampleUiEvent : UiEvent {
    data object Initialize : SampleUiEvent
    data class NameChanged(val value: String) : SampleUiEvent
    data object SaveClicked : SampleUiEvent
}
```

Screenは `onEvent(event)` を呼び、Routeが `viewModel::sendEvent` へ接続します。
BaseViewModelはEventを `Channel.BUFFERED` に積み、順番に `handleEvent` へ渡します。

#### やること

- ユーザーが行った操作を表す
- 初期表示・再試行などの入力を表す
- 操作に必要な最小値だけを持つ

#### やらないこと

- 処理結果を表す
- UiStateを丸ごと持つ
- Repository / Entity / DTOを持つ
- Navigationを直接実行する

### 📨 Message / UiMessage

#### 共通思想上の役割

Messageは、State変更の理由・処理結果を表します。

#### Androidでの実装

Androidでは、`UiMessage` として定義します。

```kotlin
sealed interface SampleUiMessage : UiMessage {

    data object LoadStarted : SampleUiMessage

    data class LoadSucceeded(
        val title: String
    ) : SampleUiMessage

    data class LoadFailed(
        val error: UiError
    ) : SampleUiMessage

    data class InputUpdated(
        val value: String
    ) : SampleUiMessage
}
```

#### やること

- UseCaseの結果を表す
- State変更の理由を表す
- Reducerに渡す情報を表す

#### やらないこと

- ユーザー操作そのものを表す
- 副作用を実行する
- Navigation / Dialog / Snackbar を表す

> [!IMPORTANT]
> **StateはMessageを通じて変更する。**
>
> ViewModelが気分で直接Stateを書き換え始めると、  
> 状態変更の理由が追えなくなります。

### 🧮 Reducer

#### 共通思想上の役割

Reducerは、現在のStateとMessageから新しいStateを生成します。

#### Androidでの実装

Androidでは、専用のReducerクラスとして実装します。  
単純な画面ではViewModel内のprivate関数でも実装可能ですが、  
本ガイドでは、レビュー性とテスト容易性を優先してReducerクラス化を基本とします。

```kotlin
class SampleReducer @Inject constructor() :
    Reducer<SampleUiState, SampleUiMessage> {

    override fun reduce(
        currentState: SampleUiState,
        message: SampleUiMessage
    ): SampleUiState {
        return when (message) {
            SampleUiMessage.LoadStarted -> {
                currentState.copy(
                    isLoading = true
                )
            }

            is SampleUiMessage.LoadSucceeded -> {
                currentState.copy(
                    isLoading = false,
                    title = message.title
                )
            }

            is SampleUiMessage.LoadFailed -> {
                currentState.copy(
                    isLoading = false,
                    errorMessage = message.error.message
                )
            }

            is SampleUiMessage.InputUpdated -> {
                currentState.copy(
                    inputText = message.value
                )
            }
        }
    }
}
```

> [!IMPORTANT]
> **Reducerは純粋関数にする。**
>
> Repository呼び出し、UseCase呼び出し、ログ出力、Navigation、Snackbar表示などは  
> Reducerに書いてはいけません。

### 💥 Effect / UiEffect

#### 共通思想上の役割

Effectは、一度だけ実行される副作用です。

#### Androidでの実装

Androidでは、`UiEffect` を `SharedFlow` で流します。

```kotlin
sealed interface SampleUiEffect : UiEffect {

    data object NavigateBack : SampleUiEffect

    data class NavigateToDetail(
        val id: String
    ) : SampleUiEffect

    data class ShowSnackbar(
        val message: String
    ) : SampleUiEffect

    data class ShowDialog(
        val title: String,
        val message: String
    ) : SampleUiEffect
}
```

#### 代表例

- 画面遷移
- 戻る
- Snackbar
- Toast
- Dialog
- 権限要求
- 外部アプリ起動

> [!CAUTION]
> **EffectをStateに混ぜない。**
>
> `showSnackbar = true` のようなStateは、再描画や再購読で多重発火する可能性があります。

### 🧭 Effect Handler

#### 共通思想上の役割

Effect Handlerは、UiEffectを実際のUI副作用へ変換する役割名です。

#### Androidでの実装

現行プロジェクトは専用のEffect Handler基底クラスを持ちません。  
各 `XxxRoute` の `LaunchedEffect(viewModel)` 内にあるEffect collectorがこの役割を担います。

```kotlin
@Composable
fun XxxRoute(
    navController: NavController,
    viewModel: XxxViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        launch(start = CoroutineStart.UNDISPATCHED) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    XxxUiEffect.NavigateBack ->
                        navController.popBackStack()
                    is XxxUiEffect.NavigateToDetail ->
                        navController.navigate(XxxRoutes.detail(effect.id))
                    is XxxUiEffect.ShowSnackbar ->
                        snackbarHostState.showSnackbar(effect.message)
                    is XxxUiEffect.ShowDialog ->
                        dialogState = DialogUiState(
                            title = effect.title,
                            message = effect.message,
                            positiveButtonText = okText
                        )
                }
            }
        }
    }

    XxxScreen(
        uiState = uiState,
        onEvent = viewModel::sendEvent
    )
}
```

共通化により各Routeが読みにくくなるまでは、専用HandlerやBaseRouteを作りません。

> [!IMPORTANT]
> ViewModelは「何をしてほしいか」をEffectで通知し、
> NavControllerやCompose stateを知るRouteが実行します。

### 🧩 UseCase

#### 共通思想上の役割

UseCaseは、アプリケーションとしての処理手順を表します。

#### Androidでの実装

Androidでは、`XxxUseCase` としてクラス化します。

```kotlin
class SaveSampleUseCase @Inject constructor(
    private val repository: SampleRepository
) {

    suspend operator fun invoke(
        input: SaveSampleInput
    ): AppResult<Unit> {
        return repository.save(input)
    }
}
```

#### やること

- 業務処理の手順を定義する
- Repository / Gatewayを呼ぶ
- AppResultで成功 / 失敗を返す
- 例外を構造化されたAppErrorへ変換する

#### やらないこと

- UiStateを受け取る
- UiEffectを発行する
- UI文言を返す
- Navigationを行う
- Contextを持つ

> [!IMPORTANT]
> **UseCaseはUIを知らない。**
>
> UseCaseが画面名・文言・Composable・NavControllerを知っていたら、  
> 責務が崩れています。

### 🗄 Repository / Gateway

#### Repository

Repositoryは、業務データの取得・保存を隠蔽します。

```kotlin
interface SampleRepository {

    suspend fun getSample(
        id: SampleId
    ): AppResult<Sample>

    suspend fun save(
        sample: Sample
    ): AppResult<Unit>
}
```

#### Gateway

Gatewayは、外部API・SDK・デバイスなどとの通信を隠蔽します。

```kotlin
interface SampleGateway {

    suspend fun requestSample(
        id: SampleId
    ): AppResult<Sample>
}
```

> [!CAUTION]
> **Repository / Gatewayに業務判断を持ち込まない。**
>
> 保存可否・遷移可否・処理順序などの判断は、  
> UseCaseまたはDomainに置きます。

### 🧬 Dependency Wiring / Hilt

#### 共通思想上の役割

Dependency Wiringは、依存関係を組み立てる場所です。

#### Androidでの実装

Androidでは、Hilt Moduleで登録します。

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class SampleRepositoryModule {

    @Binds
    abstract fun bindSampleRepository(
        impl: SampleRepositoryImpl
    ): SampleRepository
}
```

> [!IMPORTANT]
> **Hilt Moduleは依存の地図である。**
>
> Moduleを見れば、どのinterfaceにどの実装が紐づいているか、  
> どのレイヤの依存なのかが分かる状態にします。

### 🏷 命名規約一覧

Android実装では、以下の命名に統一します。

| 種別 | 命名 |
|---|---|
| Route | `XxxRoute` |
| Screen | `XxxScreen` |
| ViewModel | `XxxViewModel` |
| UiState | `XxxUiState` |
| UiEvent | `XxxUiEvent` |
| UiMessage | `XxxUiMessage` |
| UiEffect | `XxxUiEffect` |
| Reducer | `XxxReducer` |
| 初期表示UseCase | `InitializeXxxUseCase` |
| 読み込みUseCase | `LoadXxxUseCase` |
| 再読み込みUseCase | `ReloadXxxUseCase` |
| 入力検証UseCase | `ValidateXxxUseCase` |
| 保存UseCase | `SaveXxxUseCase` |
| 削除UseCase | `DeleteXxxUseCase` |
| 実行UseCase | `ExecuteXxxUseCase` |
| Repository interface | `XxxRepository` |
| Repository implementation | `XxxRepositoryImpl` |
| Gateway interface | `XxxGateway` |
| Gateway implementation | `XxxGatewayImpl` |
| Retrofit API | `XxxApi` |
| Request DTO | `XxxRequest` |
| Response DTO | `XxxResponse` |
| Entity | `XxxEntity` |
| DAO | `XxxDao` |
| Mapper | `XxxMapper` |
| Hilt Module | `XxxModule` |

> [!IMPORTANT]
> **interfaceに `I` prefixは付けない。**
>
> Kotlin / Androidでは、`ISampleRepository` ではなく  
> `SampleRepository` / `SampleRepositoryImpl` のように命名します。

#### OK例

```kotlin
interface SampleGateway

class SampleGatewayImpl @Inject constructor(
    private val sampleApi: SampleApi
) : SampleGateway
```

```kotlin
interface SampleRepository

class SampleRepositoryImpl @Inject constructor(
    private val sampleDao: SampleDao
) : SampleRepository
```

#### NG例

```kotlin
interface ISampleGateway

class SampleGateway : ISampleGateway
```

```kotlin
interface ISampleRepository

class SampleRepository : ISampleRepository
```

### 🎯 3章まとめ

Android実装では、以下の対応で考えます。

```text
View / UI Component
  → Composable / Screen

Presentation Controller
  → ViewModel

State
  → UiState + StateFlow

Event / User Action
  → UiEvent

Message
  → UiMessage

Reducer
  → Reducer / reduce関数

Effect
  → UiEffect + SharedFlow

Effect Handler
  → LaunchedEffect / collect

UseCase
  → XxxUseCase

Repository / Gateway
  → interface + Impl

Dependency Wiring
  → Hilt Module
```

> [!IMPORTANT]
> **「Androidらしく書く。ただし設計思想は変えない」**
>
> Compose、StateFlow、SharedFlow、Hilt、Navigation Composeなど、Androidの標準的な仕組みを使いながら、  
> 責務・状態・副作用・依存方向は共通設計思想ガイドに合わせて統一します。

---

## 🖼 4. 画面 / Feature を追加したいとき

### 🎯 この章の目的

この章では、Androidアプリで新しい画面やFeatureを追加する際の  
**ファイル構成・追加手順・責務分離・命名ルール** を定義します。

Androidアプリでは、画面を追加するときにComposableだけを追加すればよいわけではありません。  
画面表示、状態管理、ユーザー操作、UseCase、Repository、Gateway、DI登録などを、  
責務ごとに分けて追加します。

```text
Feature追加
  ↓
Presentation
  - Route
  - Screen
  - ViewModel
  - UiState
  - UiEvent
  - UiMessage
  - UiEffect
  - Reducer

Application
  - UseCase
  - Input / Result

Domain
  - Model
  - ValueObject
  - Policy

Data / Infrastructure
  - RepositoryImpl
  - GatewayImpl
  - Entity / DTO / Mapper

DI
  - Hilt Module
```

> [!IMPORTANT]
> **「画面追加」とは、Composableを1つ増やすことではない。**
>
> 画面に必要な状態・操作・処理・副作用・外部接続を、  
> それぞれ適切な責務へ分けて追加します。

### 📌 この章はこういうときに見る

- 新しい画面を追加したいとき
- 新しいFeatureを追加したいとき
- どのファイルを作ればよいか迷ったとき
- Route / Screen / ViewModel の分け方に迷ったとき
- Navigation登録を追加したいとき
- Feature単位のフォルダ構成を揃えたいとき
- 既存画面を参考にして横展開したいとき

### 🧭 基本方針

画面 / Feature追加では、以下の方針を守ります。

```text
1. Feature単位でフォルダを分ける
2. Presentation / Application / Domain / Data / DI を混ぜない
3. ComposableはRouteとScreenに分ける
4. RouteはViewModel接続・Effect購読・Navigation接続を担当する
5. ScreenはUiState表示とUiEvent通知だけを担当する
6. ViewModelはUseCase呼び出し・Message dispatch・Effect発行を担当する
7. 業務処理はUseCaseへ置く
8. 外部I/OはRepository / Gatewayへ閉じ込める
9. DI登録はFeatureまたはレイヤ単位で行う
```

> [!TIP]
> **まずFeatureの骨組みを作ってから、中身を実装する。**
>
> 先にComposableだけ作ると、後からViewModelやUseCaseへの分離が苦しくなります。  
> 最初に責務ごとの箱を用意しておくと、レビューもしやすくなります。

### 🗂 Featureフォルダ構成例

1画面だけのFeatureは `presentation` 直下でも構いません。  
複数画面を持つFeatureは、現行Sample / Licenseと同じく画面単位へ分けます。

```text
feature/sample/
  application/
    GetSampleHomeHeaderUseCase.kt
    ObserveSampleSettingsUseCase.kt
    UpdateSampleNotificationEnabledUseCase.kt

  domain/
    SampleGreetingPolicy.kt
    SampleHomeHeader.kt
    SampleSettings.kt
    SampleSettingsRepository.kt

  data/
    SampleSettingsRepositoryImpl.kt

  di/
    SampleModule.kt

  presentation/
    common/
      SampleRoutes.kt
      SampleGraph.kt
      SampleNavigationComponents.kt
      SampleUiErrorMapper.kt

    home/
      SampleRoute.kt
      SampleScreen.kt
      SampleViewModel.kt
      SampleUiState.kt
      SampleUiEvent.kt
      SampleUiMessage.kt
      SampleUiEffect.kt
      SampleReducer.kt
      SampleHomeUseCaseFacade.kt
      SampleHomePresentationFacade.kt

    history/
      SampleHistoryRoute.kt
      SampleHistoryScreen.kt
      SampleHistoryViewModel.kt
      SampleHistoryUiState.kt
      SampleHistoryUiEvent.kt
      SampleHistoryUiMessage.kt
      SampleHistoryUiEffect.kt
      SampleHistoryReducer.kt
      SampleHistoryUseCaseFacade.kt
      SampleHistoryPresentationFacade.kt

    settings/
      SampleSettingsRoute.kt
      SampleSettingsScreen.kt
      SampleSettingsViewModel.kt
      SampleSettingsUiState.kt
      SampleSettingsUiEvent.kt
      SampleSettingsUiMessage.kt
      SampleSettingsUiEffect.kt
      SampleSettingsReducer.kt
      SampleSettingsUseCaseFacade.kt
      SampleSettingsPresentationFacade.kt
```

`presentation/common` へ置くのは、そのFeature内の複数画面で共有するものだけです。  
画面固有のMapper / Formatter / Facadeは画面ディレクトリへ置きます。

### 🧩 最小構成

新しい画面を追加する際のPresentation最小構成は次のとおりです。

```text
presentation/xxx/
  XxxRoute.kt
  XxxScreen.kt
  XxxViewModel.kt
  XxxUiState.kt
  XxxUiEvent.kt
  XxxUiMessage.kt
  XxxUiEffect.kt
  XxxReducer.kt
```

UseCaseが複数に増えたら `XxxUseCaseFacade`、表示補助依存が複数に増えたら
`XxxPresentationFacade` を追加します。  
Facadeを先に作るのではなく、コンストラクタの見通しが悪くなった時点で導入します。

一覧・詳細など複数画面を持つ場合は `presentation/list`、
`presentation/detail` と `presentation/common` に分けます。

### 🧭 画面追加の基本手順

画面を追加するときは、以下の順番で作ります。

```text
1. 画面の目的を決める
2. Feature名を決める
3. UiStateを定義する
4. UiEventを定義する
5. UiMessageを定義する
6. UiEffectを定義する
7. Reducerを作る
8. ViewModelを作る
9. Screenを作る
10. Routeを作る
11. Navigation Graphへ登録する
12. 必要に応じてUseCase / Repository / Gateway / DIを追加する
13. 必要に応じてUseCase / ReducerのUnit Testを追加する
```

> [!IMPORTANT]
> **画面からではなく、状態から作る。**
>
> 先にUiStateを定義すると、  
> 画面が何を表示し、何を更新する必要があるかが整理しやすくなります。

### 🧱 UiStateを定義する

画面に表示し続ける情報をUiStateとして定義します。

```kotlin
data class SampleUiState(
    val isLoading: Boolean,
    val title: String,
    val name: String,
    val nameErrorMessage: String?,
    val screenErrorMessage: String?
) : UiState {
    val canSave: Boolean
        get() = !isLoading && name.isNotBlank()

    companion object {

        fun initial(): SampleUiState {
            return SampleUiState(
                isLoading = false,
                title = "",
                name = "",
                nameErrorMessage = null,
                screenErrorMessage = null
            )
        }
    }
}
```

> [!CAUTION]
> **UiStateに単発イベントを入れない。**
>
> `showDialog` / `showSnackbar` / `shouldNavigate` はUiStateではなくUiEffectで扱います。

### ⚡ UiEventを定義する

ユーザー操作や画面起点の処理をUiEventとして定義します。

```kotlin
sealed interface SampleUiEvent : UiEvent {

    data object Initialize : SampleUiEvent

    data class NameChanged(
        val value: String
    ) : SampleUiEvent

    data object SaveClicked : SampleUiEvent

    data object BackClicked : SampleUiEvent
}
```

> [!IMPORTANT]
> **UiEventは「ユーザーが何をしたか」を表す。**
>
> `SaveSucceeded` のような処理結果はUiEventではありません。  
> 処理結果はUiMessageで表現します。

### 📨 UiMessageを定義する

State変更の理由や処理結果をUiMessageとして定義します。

```kotlin
sealed interface SampleUiMessage : UiMessage {

    data object LoadStarted : SampleUiMessage

    data class LoadSucceeded(
        val title: String
    ) : SampleUiMessage

    data class LoadFailed(
        val error: UiError
    ) : SampleUiMessage

    data class NameUpdated(
        val value: String
    ) : SampleUiMessage

    data object SaveStarted : SampleUiMessage

    data object SaveSucceeded : SampleUiMessage

    data class SaveFailed(
        val error: UiError
    ) : SampleUiMessage
}
```

> [!TIP]
> **UiMessageは、Reducerに渡す材料。**
>
> UiStateをどう変えるかではなく、  
> 何が起きた結果State変更が必要になったかを表します。

### 💥 UiEffectを定義する

Navigation / Dialog / Snackbarなどの単発副作用をUiEffectとして定義します。

```kotlin
sealed interface SampleUiEffect : UiEffect {

    data object NavigateBack : SampleUiEffect

    data class ShowSnackbar(
        val message: String
    ) : SampleUiEffect

    data class ShowDialog(
        val title: String,
        val message: String
    ) : SampleUiEffect
}
```

> [!IMPORTANT]
> **一度だけ実行したいものはUiEffect。**
>
> 画面遷移、Dialog、Snackbar、Toast、外部アプリ起動はUiEffectとして扱います。

### 🧮 Reducerを作る

Reducerは、現在のUiStateとUiMessageから新しいUiStateを作ります。

```kotlin
class SampleReducer @Inject constructor() :
    Reducer<SampleUiState, SampleUiMessage> {

    override fun reduce(
        currentState: SampleUiState,
        message: SampleUiMessage
    ): SampleUiState {
        return when (message) {
            SampleUiMessage.LoadStarted -> {
                currentState.copy(
                    isLoading = true,
                    screenErrorMessage = null
                )
            }

            is SampleUiMessage.LoadSucceeded -> {
                currentState.copy(
                    isLoading = false,
                    title = message.title,
                    screenErrorMessage = null
                )
            }

            is SampleUiMessage.LoadFailed -> {
                currentState.copy(
                    isLoading = false,
                    screenErrorMessage = message.error.message
                )
            }

            is SampleUiMessage.NameUpdated -> {
                currentState.copy(
                    name = message.value,
                    nameErrorMessage = null
                )
            }

            SampleUiMessage.SaveStarted -> {
                currentState.copy(
                    isLoading = true,
                    screenErrorMessage = null
                )
            }

            SampleUiMessage.SaveSucceeded -> {
                currentState.copy(
                    isLoading = false,
                    screenErrorMessage = null
                )
            }

            is SampleUiMessage.SaveFailed -> {
                currentState.copy(
                    isLoading = false,
                    screenErrorMessage = message.error.message
                )
            }
        }
    }
}
```

> [!CAUTION]
> **ReducerにUseCase呼び出しやEffect発行を書かない。**
>
> Reducerは純粋にStateを計算するだけです。

### 🧠 ViewModelを作る

```kotlin
@HiltViewModel
class XxxViewModel @Inject constructor(
    reducer: XxxReducer,
    private val useCaseFacade: XxxUseCaseFacade,
    private val presentationFacade: XxxPresentationFacade
) : BaseViewModel<XxxUiState, XxxUiEvent, XxxUiMessage, XxxUiEffect>(
    initialState = XxxUiState.initial(),
    reducer = reducer
) {
    override suspend fun handleEvent(event: XxxUiEvent) {
        when (event) {
            XxxUiEvent.Initialize -> initialize()
            XxxUiEvent.RetryClicked -> initialize()
            XxxUiEvent.BackClicked ->
                emitEffect(XxxUiEffect.NavigateBack)
        }
    }

    private suspend fun initialize() {
        dispatch(XxxUiMessage.LoadStarted)

        when (val result = useCaseFacade.load()) {
            is AppResult.Success ->
                dispatch(XxxUiMessage.LoadSucceeded(result.value))

            is AppResult.Failure -> {
                val error = presentationFacade.errorMapper.toUiError(result.error)
                dispatch(XxxUiMessage.LoadFailed(error))
                emitEffect(
                    XxxUiEffect.ShowDialog(
                        title = error.title,
                        message = error.message
                    )
                )
            }
        }
    }
}
```

`handleEvent` はEventの分岐を見渡せる入口にし、処理本体は
`initialize`、`save`、`updateXxx` などへ分けます。  
各処理はEvent Queueからsuspendで順次呼ばれます。

> [!CAUTION]
> `handleEvent` から毎回 `viewModelScope.launch` すると、
> Channelで受けた順番と処理完了順が分離します。
> 並列化が必要な処理だけ、意図を明確にしてJobを管理します。

### 🖼 Screenを作る

Screenは、UiStateを表示し、ユーザー操作をUiEventとして通知します。

```kotlin
@Composable
fun SampleScreen(
    modifier: Modifier = Modifier,
    uiState: SampleUiState,
    onEvent: (SampleUiEvent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = uiState.title
        )

        TextField(
            value = uiState.name,
            onValueChange = { value ->
                onEvent(SampleUiEvent.NameChanged(value))
            },
            isError = uiState.nameErrorMessage != null
        )

        uiState.nameErrorMessage?.let { message ->
            Text(
                text = message
            )
        }

        Button(
            enabled = uiState.canSave,
            onClick = {
                onEvent(SampleUiEvent.SaveClicked)
            }
        ) {
            Text("保存")
        }

        uiState.screenErrorMessage?.let { message ->
            Text(
                text = message
            )
        }

        if (uiState.isLoading) {
            CircularProgressIndicator()
        }
    }
}
```

> [!CAUTION]
> **ScreenでUseCaseを呼ばない。**
>
> Screenは表示とイベント通知だけです。

### 🧭 Routeを作る

RouteはViewModel接続、State購読、Effect購読、Navigation接続を担当します。

```kotlin
@Composable
fun XxxRoute(
    navController: NavController,
    viewModel: XxxViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var dialogState by remember { mutableStateOf<DialogUiState?>(null) }

    LaunchedEffect(viewModel) {
        launch(start = CoroutineStart.UNDISPATCHED) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    XxxUiEffect.NavigateBack -> navController.popBackStack()
                    is XxxUiEffect.NavigateToDetail ->
                        navController.navigate(XxxRoutes.detail(effect.id))
                    is XxxUiEffect.ShowSnackbar ->
                        snackbarHostState.showSnackbar(effect.message)
                    is XxxUiEffect.ShowDialog ->
                        dialogState = DialogUiState(
                            title = effect.title,
                            message = effect.message,
                            positiveButtonText = "OK"
                        )
                }
            }
        }

        viewModel.sendEvent(XxxUiEvent.Initialize)
    }

    XxxScreen(
        uiState = uiState,
        onEvent = viewModel::sendEvent
    )
}
```

Drawer、Dialog、Snackbar、検索欄のフォーカスなど、画面表示中だけ必要な状態は
Route / ScreenのCompose stateで管理して構いません。

### 🧭 Navigation Graphへ登録する

アプリ全体の `AppNavHost` は各Feature Graphを束ねるだけにします。

```kotlin
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = SampleRoutes.List
    ) {
        sampleGraph(navController)
        licenseGraph(navController)
        legalDocumentGraph(navController)
        versionInfoGraph(navController)
    }
}
```

Feature側はRouteと画面を対応付けます。

```kotlin
fun NavGraphBuilder.sampleGraph(navController: NavController) {
    composable(SampleRoutes.List) {
        SampleRoute(navController)
    }
    composable(SampleRoutes.History) {
        SampleHistoryRoute(navController)
    }
    composable(SampleRoutes.Settings) {
        SampleSettingsRoute(navController)
    }
}
```

引数付き画面は、License詳細と同じ形でRoute定義とencodeをFeatureへ閉じます。

```kotlin
composable(
    route = LicenseRoutes.Detail,
    arguments = listOf(
        navArgument(LicenseRoutes.ArgLicenseId) {
            type = NavType.StringType
        }
    )
) { entry ->
    LicenseDetailRoute(
        licenseId = entry.arguments
            ?.getString(LicenseRoutes.ArgLicenseId)
            .orEmpty(),
        navController = navController
    )
}
```

### 🧩 Route定義例

```kotlin
object SampleRoutes {
    const val List = "sample"
    const val History = "sample/history"
    const val Settings = "sample/settings"
}
```

```kotlin
object LicenseRoutes {
    const val List = "license"
    const val ArgLicenseId = "licenseId"
    const val Detail = "license/{$ArgLicenseId}"

    fun detail(licenseId: String): String {
        return "license/${licenseId.asRouteArgument()}"
    }
}
```

CoreのNavigation補助は `String.asRouteArgument()` だけです。  
NavHost、NavGraph、route名はapp / featureが直接Navigation Composeで管理します。

### ✅ 画面追加チェックリスト

画面追加時は、以下を確認します。

```text
□ Feature名が明確である
□ UiStateを定義した
□ UiEventを定義した
□ UiMessageを定義した
□ UiEffectを定義した
□ Reducerを定義した
□ BaseViewModelを継承したViewModelを定義した
□ RouteからviewModel::sendEventを渡した
□ Screenを定義した
□ Routeを定義した
□ Navigation Graphへ登録した
□ 必要なUseCaseを追加した
□ 依存が多い場合はUseCaseFacade / PresentationFacadeへ整理した
□ 必要なRepository / Gatewayを追加した
□ 必要なHilt Moduleを追加した
□ 必要に応じてReducer Testを追加した
□ 必要に応じてUseCase Testを追加した
```

### 🚫 やってはいけないこと

#### ❌ Composableだけ追加して終わる

```text
SampleScreen.kt だけ作る
```

#### ✅ 状態・イベント・処理・副作用を分ける

```text
SampleScreen
SampleRoute
SampleViewModel
SampleUiState
SampleUiEvent
SampleUiMessage
SampleUiEffect
SampleReducer
```

#### ❌ ScreenがViewModelを直接取得する

```kotlin
@Composable
fun SampleScreen(
    viewModel: SampleViewModel = hiltViewModel()
)
```

#### ✅ RouteでViewModelを取得し、Screenへ渡す

```kotlin
SampleScreen(
    uiState = uiState,
    onEvent = viewModel::sendEvent
)
```

### 🎯 4章まとめ

画面 / Feature追加では、以下を守ります。

```text
Route
  → ViewModel接続 / Effect購読 / Navigation接続

Screen
  → UiState表示 / UiEvent通知

ViewModel
  → UseCase呼び出し / Message dispatch / Effect発行

Reducer
  → UiState更新

UseCase
  → 業務処理

Repository / Gateway
  → 外部I/O

Hilt
  → 依存関係登録
```

> [!IMPORTANT]
> **「画面を作る」とは、責務ごとの部品を作ること。**
>
> Composableだけを作るのではなく、  
> State / Event / Message / Effect / UseCase / DIまで含めてFeatureとして追加します。

---

## ✏️ 5. 入力内容によって画面表示を変えたいとき

### 🎯 この章の目的

この章では、Androidアプリで入力内容に応じて画面表示を変える場合の  
**入力イベント・Validation・State更新・表示反映の流れ** を定義します。

入力欄の値変更、エラーメッセージ表示、ボタン活性制御、表示切替などは、  
Composable内で完結させず、UiEvent / ViewModel / ValidationPolicy / Reducer / UiStateを通じて制御します。

```text
TextField
  ↓ onValueChange
UiEvent
  ↓
ViewModel
  ↓
ValidationPolicy / ValidateUseCase
  ↓
UiMessage
  ↓
Reducer
  ↓
UiState
  ↓
Composable
```

> [!IMPORTANT]
> **「入力内容による表示変更もStateで表現する」**
>
> Composable内の一時変数で制御するのではなく、  
> UiStateへ反映して画面を更新します。

### 📌 この章はこういうときに見る

- TextFieldの入力値を管理したいとき
- 入力エラーを表示したいとき
- 入力内容によってボタン活性を変えたいとき
- 入力内容によって表示項目を切り替えたいとき
- 入力中バリデーションと保存時バリデーションの扱いに迷ったとき
- `remember` に入力値を持ってよいか迷ったとき

### 🧭 基本方針

入力内容による表示変更では、以下の方針を守ります。

```text
1. 入力値はUiStateに持つ
2. TextFieldのonValueChangeではUiEventを送る
3. Composableで業務バリデーションをしない
4. ValidationはPolicy / UseCaseへ分離する
5. Validation結果はUiMessageでReducerへ渡す
6. エラー表示はUiStateに持つ
7. ボタン活性はUiStateから導出する
8. DialogやSnackbar通知はUiEffectで扱う
```

> [!CAUTION]
> **入力値をrememberだけで管理しない。**
>
> `remember` はUI都合の一時状態には使えますが、  
> 業務的な入力値はUiStateに集約します。

### 🧱 UiStateで入力値を持つ

```kotlin
data class SampleFormUiState(
    val name: String,
    val nameErrorMessage: String?,
    val age: String,
    val ageErrorMessage: String?,
    val selectedType: SampleType?,
    val isLoading: Boolean
) : UiState {
    val canSave: Boolean
        get() =
            !isLoading &&
            name.isNotBlank() &&
            age.isNotBlank() &&
            nameErrorMessage == null &&
            ageErrorMessage == null &&
            selectedType != null

    companion object {

        fun initial(): SampleFormUiState {
            return SampleFormUiState(
                name = "",
                nameErrorMessage = null,
                age = "",
                ageErrorMessage = null,
                selectedType = null,
                isLoading = false
            )
        }
    }
}
```

> [!TIP]
> **ボタン活性はUiStateから導出すると読みやすい。**
>
> `canSave` をUiStateのcomputed propertyにすると、  
> Screen側は `uiState.canSave` を見るだけになります。

### ⚡ UiEventで入力変更を通知する

```kotlin
sealed interface SampleFormUiEvent : UiEvent {

    data class NameChanged(
        val value: String
    ) : SampleFormUiEvent

    data class AgeChanged(
        val value: String
    ) : SampleFormUiEvent

    data class TypeSelected(
        val type: SampleType
    ) : SampleFormUiEvent

    data object SaveClicked : SampleFormUiEvent
}
```

> [!IMPORTANT]
> **UiEventは入力事実を表す。**
>
> `NameChanged` は「名前が変更された」という事実です。  
> それが有効かどうかはValidationで判断します。

### 🖼 Composableで入力イベントを送る

```kotlin
@Composable
fun SampleFormScreen(
    uiState: SampleFormUiState,
    onEvent: (SampleFormUiEvent) -> Unit
) {
    Column {
        TextField(
            value = uiState.name,
            onValueChange = { value ->
                onEvent(
                    SampleFormUiEvent.NameChanged(value)
                )
            },
            isError = uiState.nameErrorMessage != null,
            supportingText = {
                uiState.nameErrorMessage?.let { message ->
                    Text(message)
                }
            }
        )

        TextField(
            value = uiState.age,
            onValueChange = { value ->
                onEvent(
                    SampleFormUiEvent.AgeChanged(value)
                )
            },
            isError = uiState.ageErrorMessage != null,
            supportingText = {
                uiState.ageErrorMessage?.let { message ->
                    Text(message)
                }
            }
        )

        Button(
            enabled = uiState.canSave,
            onClick = {
                onEvent(SampleFormUiEvent.SaveClicked)
            }
        ) {
            Text("保存")
        }
    }
}
```

> [!CAUTION]
> **Composableで入力値の正しさを判定しない。**
>
> `isError` はUiStateを見るだけにします。  
> 判定処理はViewModel / Policy / UseCaseへ寄せます。

### 📨 UiMessageでValidation結果を表す

```kotlin
sealed interface SampleFormUiMessage : UiMessage {

    data class NameUpdated(
        val value: String,
        val errorMessage: String?
    ) : SampleFormUiMessage

    data class AgeUpdated(
        val value: String,
        val errorMessage: String?
    ) : SampleFormUiMessage

    data class TypeUpdated(
        val type: SampleType
    ) : SampleFormUiMessage

    data object SaveStarted : SampleFormUiMessage

    data object SaveSucceeded : SampleFormUiMessage

    data class SaveFailed(
        val error: UiError
    ) : SampleFormUiMessage
}
```

> [!TIP]
> **入力値とエラーを同じMessageで渡すとReducerが単純になる。**
>
> 入力値更新とValidation結果を別々のMessageにすると、  
> 状態遷移が増えて読みにくくなる場合があります。

### 🧩 ValidationPolicyを作る

単純な入力検証はPolicyとして分離します。

```kotlin
class SampleFormValidationPolicy @Inject constructor() {

    fun validateName(
        value: String
    ): FieldValidationResult {
        if (value.isBlank()) {
            return FieldValidationResult.Invalid(
                message = "名前を入力してください"
            )
        }

        if (value.length > 50) {
            return FieldValidationResult.Invalid(
                message = "名前は50文字以内で入力してください"
            )
        }

        return FieldValidationResult.Valid
    }

    fun validateAge(
        value: String
    ): FieldValidationResult {
        if (value.isBlank()) {
            return FieldValidationResult.Invalid(
                message = "年齢を入力してください"
            )
        }

        val age = value.toIntOrNull()
            ?: return FieldValidationResult.Invalid(
                message = "年齢は数値で入力してください"
            )

        if (age !in 0..150) {
            return FieldValidationResult.Invalid(
                message = "年齢は0〜150の範囲で入力してください"
            )
        }

        return FieldValidationResult.Valid
    }
}
```

```kotlin
sealed interface FieldValidationResult {

    data object Valid : FieldValidationResult

    data class Invalid(
        val message: String
    ) : FieldValidationResult
}
```

> [!CAUTION]
> **サンプル内の直書き文言は説明用です。**
>
> 実装時は、必要に応じて `strings.xml` または `StringProvider` 経由に置き換えてください。

> [!IMPORTANT]
> **ValidationはComposableに書かない。**
>
> 入力検証は、Policy / UseCaseとして分離します。  
> 画面は検証結果を表示するだけです。

### 🧠 ViewModelで入力変更を処理する

入力変更もUiEventとして受け、Policyの結果をUiMessageへ変換します。

```kotlin
override suspend fun handleEvent(event: SampleFormUiEvent) {
    when (event) {
        is SampleFormUiEvent.NameChanged -> {
            val result = validationPolicy.validateName(event.value)
            dispatch(
                SampleFormUiMessage.NameUpdated(
                    value = event.value,
                    errorMessage = presentationFacade.validationFormatter
                        .toMessageOrNull(result)
                )
            )
        }

        SampleFormUiEvent.SaveClicked -> save()
    }
}
```

入力値の正しさはPolicy、Validation結果から表示文言への変換は
PresentationのFormatter / Mapperへ置きます。  
ViewModel内の拡張関数へ業務ルールを埋め込みません。

連続入力が多い検索欄などは、必ずしも1文字ごとに重いUseCaseを同期実行しません。  
入力値自体はStateへ反映し、検索実行だけdebounceするか、明示Eventに分けます。

### 🧮 Reducerで入力状態を更新する

```kotlin
class SampleFormReducer @Inject constructor() :
    Reducer<SampleFormUiState, SampleFormUiMessage> {

    override fun reduce(
        currentState: SampleFormUiState,
        message: SampleFormUiMessage
    ): SampleFormUiState {
        return when (message) {
            is SampleFormUiMessage.NameUpdated -> {
                currentState.copy(
                    name = message.value,
                    nameErrorMessage = message.errorMessage
                )
            }

            is SampleFormUiMessage.AgeUpdated -> {
                currentState.copy(
                    age = message.value,
                    ageErrorMessage = message.errorMessage
                )
            }

            is SampleFormUiMessage.TypeUpdated -> {
                currentState.copy(
                    selectedType = message.type
                )
            }

            SampleFormUiMessage.SaveStarted -> {
                currentState.copy(
                    isLoading = true
                )
            }

            SampleFormUiMessage.SaveSucceeded -> {
                currentState.copy(
                    isLoading = false
                )
            }

            is SampleFormUiMessage.SaveFailed -> {
                currentState.copy(
                    isLoading = false
                )
            }
        }
    }
}
```

> [!IMPORTANT]
> **Reducerは入力値とエラー状態を一元的に更新する。**
>
> ScreenやViewModelでStateを直接書き換えず、  
> Reducerに集約します。

### 🔄 入力中Validationと保存時Validation

入力検証は、タイミングによって役割が異なります。

| 種類 | 目的 | 表示 |
|---|---|---|
| 入力中Validation | 入力補助 | TextField下のエラー |
| フォーカスアウトValidation | 入力完了時の確認 | TextField下のエラー |
| 保存時Validation | 保存前の最終確認 | Field Error / Dialog |
| 業務Validation | 業務ルール確認 | Dialog / 画面エラー |

> [!TIP]
> **入力中Validationは優しく、保存時Validationは厳密に。**
>
> 入力中からすべてを強くエラー表示すると、操作感が悪くなります。  
> ただし保存時には必ず厳密に検証します。

### 👁 入力内容による表示切替

入力値や選択値によって表示項目を変える場合も、UiStateで制御します。

```kotlin
data class SampleFormUiState(
    val selectedType: SampleType?,
    val extraInput: String,
    val extraInputErrorMessage: String?
) : UiState {
    val shouldShowExtraInput: Boolean
        get() = selectedType == SampleType.Special
}
```

Composable側：

```kotlin
if (uiState.shouldShowExtraInput) {
    TextField(
        value = uiState.extraInput,
        onValueChange = { value ->
            onEvent(
                SampleFormUiEvent.ExtraInputChanged(value)
            )
        },
        isError = uiState.extraInputErrorMessage != null
    )
}
```

> [!IMPORTANT]
> **表示切替条件はUiStateに寄せる。**
>
> Screen側に複雑な条件式を書き散らさず、  
> UiStateのcomputed propertyとして表現します。

### 🚫 やってはいけないこと

#### ❌ TextField入力値をrememberだけで持つ

```kotlin
var name by remember { mutableStateOf("") }
```

#### ✅ UiStateで持つ

```kotlin
TextField(
    value = uiState.name,
    onValueChange = {
        onEvent(SampleFormUiEvent.NameChanged(it))
    }
)
```

#### ❌ ComposableでValidationする

```kotlin
if (name.length > 50) {
    Text("50文字以内で入力してください")
}
```

#### ✅ ValidationPolicyの結果をUiStateへ反映する

```kotlin
uiState.nameErrorMessage?.let { message ->
    Text(message)
}
```

#### ❌ 保存ボタン内で直接判定する

```kotlin
Button(
    onClick = {
        if (name.isBlank()) return@Button
        save()
    }
)
```

#### ✅ canSaveをUiStateから見る

```kotlin
Button(
    enabled = uiState.canSave,
    onClick = {
        onEvent(SampleFormUiEvent.SaveClicked)
    }
)
```

### 🎯 5章まとめ

入力内容によって画面表示を変える場合は、以下を守ります。

```text
TextField
  → UiEventを送る

ViewModel
  → ValidationPolicy / UseCaseを呼ぶ

UiMessage
  → 入力値とValidation結果を表す

Reducer
  → UiStateへ反映する

UiState
  → 入力値・エラー・表示切替条件を持つ

Composable
  → UiStateを表示するだけ
```

> [!IMPORTANT]
> **「入力値も画面状態である」**
>
> 入力値・入力エラー・ボタン活性・表示切替は、  
> UiStateとして一元管理します。

---

## 🧩 6. UseCaseを呼び出したいとき

### 🎯 この章の目的

この章では、Androidアプリにおける  
**ViewModelからUseCaseを呼び出す標準パターン** を定義します。

UseCaseは、アプリケーションとしての処理手順を表す場所です。  
ViewModelは、ユーザー操作を受け取り、必要なUseCaseを呼び出し、結果をStateまたはEffectへ接続します。

```text
Composable
  ↓ UiEvent
ViewModel
  ↓ UseCase呼び出し
UseCase
  ↓ AppResult
ViewModel
  ↓ UiMessage / UiEffect
Reducer
  ↓ UiState
Composable
```

> [!IMPORTANT]
> **「ViewModelは処理を実行しない。UseCaseへ委譲する」**
>
> ViewModelは、どのUseCaseを呼ぶか、結果をどうState / Effectへつなぐかを制御します。  
> 業務処理そのものはUseCaseに閉じ込めます。

### 📌 この章はこういうときに見る

- ボタン押下で業務処理を実行したいとき
- 初期表示時にデータを取得したいとき
- 保存・更新・削除処理を実装したいとき
- 複数Repository / Gatewayを使う処理を追加したいとき
- ViewModelに処理を書きすぎていないか確認したいとき

### 🧭 UseCase呼び出しの基本フロー

Androidでは、UseCase呼び出しを以下の流れで統一します。

```text
1. Composable が UiEvent を送る
2. ViewModel が UiEvent を受け取る
3. ViewModel が Loading 用 UiMessage を dispatchする
4. ViewModel が UseCase を呼び出す
5. UseCase が AppResult を返す
6. ViewModel が AppResult を UiMessage / UiEffect に変換する
7. Reducer が UiState を更新する
8. 必要に応じて UiEffect を発行する
```

> [!TIP]
> **UseCase呼び出し前後で、必ずStateの変化をMessage化する。**
>
> `isLoading = true` のような状態変更も、  
> 直接Stateを書き換えるのではなく `LoadStarted` のようなUiMessageで表現します。

### 🧱 UseCaseの基本形

UseCaseは、`XxxUseCase` という名前で定義します。

```kotlin
class LoadSampleUseCase @Inject constructor(
    private val repository: SampleRepository
) {

    suspend operator fun invoke(
        input: LoadSampleInput
    ): AppResult<Sample> {
        return repository.findById(
            SampleId(input.id)
        )
    }
}
```

#### 命名ルール

| 種別 | 命名例 |
|---|---|
| 初期表示 | `InitializeXxxUseCase` |
| 読み込み | `LoadXxxUseCase` |
| 再読み込み | `ReloadXxxUseCase` |
| 入力検証 | `ValidateXxxUseCase` |
| 保存 | `SaveXxxUseCase` |
| 削除 | `DeleteXxxUseCase` |
| 実行 | `ExecuteXxxUseCase` |

> [!IMPORTANT]
> **UseCase名は「何の業務処理か」が分かる名前にする。**
>
> `DoUseCase` や `CommonUseCase` のような名前は、責務が曖昧になるため禁止します。

### 📦 UseCaseの入力

UseCaseの入力は、画面Stateをそのまま渡さず、  
UseCase専用のInput modelへ変換します。

```kotlin
data class SaveSampleInput(
    val id: String,
    val name: String,
    val enabled: Boolean
)
```

ViewModel側で、UiStateからUseCase入力を組み立てます。

```kotlin
private fun createSaveInput(
    state: SampleUiState
): SaveSampleInput {
    return SaveSampleInput(
        id = state.id,
        name = state.name,
        enabled = state.enabled
    )
}
```

> [!CAUTION]
> **UseCaseにUiStateを渡さない。**
>
> UiStateは画面表示用の状態です。  
> UseCaseがUiStateを知ると、Application層がPresentation層に依存してしまいます。

### 📤 UseCaseの出力

UseCaseの出力は、成功 / 失敗を表現できる `AppResult<T>` で返します。

```kotlin
sealed interface AppResult<out T> {

    data class Success<T>(
        val value: T
    ) : AppResult<T>

    data class Failure(
        val error: AppError
    ) : AppResult<Nothing>
}
```

成功値を返さない場合は、`Unit` を使います。

```kotlin
suspend operator fun invoke(
    input: SaveSampleInput
): AppResult<Unit>
```

> [!IMPORTANT]
> **Kotlin標準のResultではなく、AppResultに統一する。**
>
> 本アプリでは、失敗理由を `AppError` として構造化するため、  
> UseCaseの戻り値は `AppResult<T>` に統一します。

### 🧠 ViewModelでの呼び出し例

```kotlin
override suspend fun handleEvent(event: SampleUiEvent) {
    when (event) {
        SampleUiEvent.Initialize -> load()
        SampleUiEvent.SaveClicked -> save()
    }
}

private suspend fun load() {
    dispatch(SampleUiMessage.LoadStarted)

    when (val result = useCaseFacade.loadSample()) {
        is AppResult.Success ->
            dispatch(SampleUiMessage.LoadSucceeded(result.value))

        is AppResult.Failure -> {
            val error = presentationFacade.errorMapper.toUiError(result.error)
            dispatch(SampleUiMessage.LoadFailed(error))
            emitEffect(
                SampleUiEffect.ShowDialog(
                    title = error.title,
                    message = error.message
                )
            )
        }
    }
}
```

ViewModelは `AppResult` の分岐とMessage / Effectへの接続を担当します。  
Repository選択、時間帯判定、入力検証、DTO変換などの判断はUseCase / Policy / Mapperへ置きます。

### 📨 UiMessage定義例

```kotlin
sealed interface SampleUiMessage : UiMessage {

    data object LoadStarted : SampleUiMessage

    data class LoadSucceeded(
        val sample: Sample
    ) : SampleUiMessage

    data class LoadFailed(
        val error: UiError
    ) : SampleUiMessage

    data class NameUpdated(
        val value: String
    ) : SampleUiMessage

    data object SaveStarted : SampleUiMessage

    data object SaveSucceeded : SampleUiMessage

    data class SaveFailed(
        val error: UiError
    ) : SampleUiMessage
}
```

> [!IMPORTANT]
> **UiMessageはState更新に必要な最小情報だけを持つ。**
>
> UseCaseの戻り値を何でも詰め込むのではなく、  
> State更新に必要な情報だけに絞ります。

### 🧮 Reducer定義例

```kotlin
class SampleReducer @Inject constructor() :
    Reducer<SampleUiState, SampleUiMessage> {

    override fun reduce(
        currentState: SampleUiState,
        message: SampleUiMessage
    ): SampleUiState {
        return when (message) {
            SampleUiMessage.LoadStarted -> {
                currentState.copy(
                    isLoading = true,
                    screenErrorMessage = null
                )
            }

            is SampleUiMessage.LoadSucceeded -> {
                currentState.copy(
                    isLoading = false,
                    id = message.sample.id.value,
                    name = message.sample.name.value,
                    enabled = message.sample.enabled,
                    screenErrorMessage = null
                )
            }

            is SampleUiMessage.LoadFailed -> {
                currentState.copy(
                    isLoading = false,
                    screenErrorMessage = message.error.message
                )
            }

            is SampleUiMessage.NameUpdated -> {
                currentState.copy(
                    name = message.value
                )
            }

            SampleUiMessage.SaveStarted -> {
                currentState.copy(
                    isLoading = true,
                    screenErrorMessage = null
                )
            }

            SampleUiMessage.SaveSucceeded -> {
                currentState.copy(
                    isLoading = false,
                    screenErrorMessage = null
                )
            }

            is SampleUiMessage.SaveFailed -> {
                currentState.copy(
                    isLoading = false,
                    screenErrorMessage = message.error.message
                )
            }
        }
    }
}
```

> [!CAUTION]
> **ReducerからUseCaseを呼ばない。**
>
> Reducerは状態計算だけを行う純粋関数です。  
> Repository / UseCase / Gateway / Navigation / Snackbar などを呼んではいけません。

### 🚫 やってはいけないこと

#### ❌ ViewModelに業務処理を書く

```kotlin
private fun handleSave() {
    if (uiState.value.name.length > 20) {
        // 業務ルールをViewModelに書いている
        return
    }

    // 保存処理もViewModelに書いている
}
```

#### ✅ UseCaseへ委譲する

```kotlin
private fun handleSave() {
    viewModelScope.launch {
        val input = createSaveInput(uiState.value)
        val result = saveSampleUseCase(input)

        // AppResultをUiMessage / UiEffectへ変換する
    }
}
```

#### ❌ UiStateをUseCaseへ渡す

```kotlin
val result = saveSampleUseCase(uiState.value)
```

#### ✅ UseCase専用Inputへ変換する

```kotlin
val input = SaveSampleInput(
    id = uiState.value.id,
    name = uiState.value.name,
    enabled = uiState.value.enabled
)

val result = saveSampleUseCase(input)
```

#### ❌ 成功時に直接Stateを書き換える

```kotlin
_uiState.value = _uiState.value.copy(
    isLoading = false,
    name = result.value.name
)
```

#### ✅ UiMessageを経由する

```kotlin
dispatch(
    SampleUiMessage.LoadSucceeded(
        sample = result.value
    )
)
```

### 🎯 6章まとめ

UseCase呼び出しでは、以下を守ります。

```text
Composable
  → UiEventを送るだけ

ViewModel
  → UiEventを受け取る
  → UseCaseを呼ぶ
  → AppResultをUiMessage / UiEffectへ変換する

UseCase
  → 業務処理を実行する
  → Repository / Gatewayを使う
  → AppResultを返す

Reducer
  → UiMessageからUiStateを作る
```

> [!IMPORTANT]
> **「UseCase呼び出しは、State更新ではなく処理実行である」**
>
> 処理の実行はUseCase、状態の更新はReducer、  
> その接続はViewModel、という分担を崩さないでください。

---

## 🗄 7. ローカル永続化を扱いたいとき

### 🎯 この章の目的

この章では、Androidアプリにおける  
**Room / DataStore / 設定値 / Config / Entity変換 / 読み込み・保存・削除** の扱い方を定義します。

ローカル永続化は、画面やViewModelから直接扱いません。  
Room / DataStore / File / Encrypted storage などの具体技術は、Repository実装に閉じ込めます。

```text
Room / DataStore / File / Encrypted storage
  ↓
RepositoryImpl
  ↓
Repository interface
  ↓
UseCase
  ↓
ViewModel
  ↓
UiMessage / UiEffect
  ↓
Reducer
  ↓
UiState
  ↓
Composable
```

> [!IMPORTANT]
> **「ローカル永続化はRepositoryの内側に閉じ込める」**
>
> ViewModelやUseCaseが、RoomのDAO・DataStore・Preferences Keyを直接知ってはいけません。

### 📌 この章はこういうときに見る

- DBの値を画面に表示したいとき
- 入力内容をDBへ保存したいとき
- Room Entity / Domain Model / UiState の変換に迷ったとき
- DataStoreで設定値を保存したいとき
- 設定値 / Configを読み込みたいとき
- DB値を削除したいとき
- ローカル保存時のエラーを扱いたいとき

### 🧭 基本方針

ローカル永続化では、以下の方針を守ります。

```text
1. ComposableからRoom / DataStoreへ直接アクセスしない
2. ViewModelからDAO / DataStoreへ直接アクセスしない
3. UseCaseはRepository interfaceだけを知る
4. RepositoryImplがRoom / DataStore / Fileを扱う
5. Entity / Preferences / RawConfigをUiStateへ直接渡さない
6. 保存形式と使用形式を分ける
7. ローカル保存例外はAppErrorへ変換する
8. 業務判断はRepositoryではなくUseCase / Domainへ置く
9. 読み込み / 保存 / 削除の意味付けはUseCaseで行う
```

> [!CAUTION]
> **DB構造と画面構造を直結させない。**
>
> EntityをUiStateへそのまま持たせると、  
> DB変更が画面変更に直結します。

### 🧱 保存先の使い分け

| 保存先 | 向いているもの | 向いていないもの |
|---|---|---|
| Room | 一覧、履歴、検索、業務データ | 小さな設定値 |
| DataStore | 設定値、フラグ、ユーザー設定 | 一覧データ、大量データ |
| File | 画像、ログ、大きなJSON | 頻繁な検索・更新 |
| Encrypted storage | Token、鍵、秘匿情報 | 通常の一覧データ |
| In-memory | 画面内の一時状態 | 永続化が必要なデータ |

> [!TIP]
> **一覧・検索・履歴はRoom、小さな設定はDataStore。**
>
> DataStoreに業務データを詰め込みすぎないようにします。

### 🧩 Roomの基本構造

Roomを使う場合は、以下の構成を基本とします。

```text
Entity
  → DB保存用モデル

Dao
  → DB操作の窓口

RepositoryImpl
  → Daoを呼び、Entity / Domain変換を行う

Repository interface
  → UseCaseが参照する境界

UseCase
  → 読み込み / 保存 / 削除の意味付けを行う
```

### 🧱 Entity定義例

```kotlin
@Entity(
    tableName = "samples"
)
data class SampleEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val enabled: Boolean,
    val updatedAt: Long
)
```

> [!IMPORTANT]
> **EntityはDB保存用モデル。**
>
> Entityに画面表示用の文言や、UI都合のフラグを持たせてはいけません。

### 🧱 DAO定義例

```kotlin
@Dao
interface SampleDao {

    @Query("SELECT * FROM samples WHERE id = :id")
    suspend fun findById(id: String): SampleEntity?

    @Query("SELECT * FROM samples ORDER BY updatedAt DESC")
    suspend fun findAll(): List<SampleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: SampleEntity)

    @Delete
    suspend fun delete(entity: SampleEntity)
}
```

> [!CAUTION]
> **DAOに業務判断を入れない。**
>
> DAOはDB操作だけを行います。  
> 保存可否・削除可否・表示可否はUseCase / Domainで判断します。

### 🧩 Domain Model定義例

```kotlin
data class Sample(
    val id: SampleId,
    val name: SampleName,
    val enabled: Boolean,
    val updatedAt: Instant
)
```

```kotlin
@JvmInline
value class SampleId(
    val value: String
)

@JvmInline
value class SampleName(
    val value: String
)
```

> [!TIP]
> **Domain Modelはアプリ内の意味を表す。**
>
> DBの列構造ではなく、  
> アプリとして扱いたい意味に寄せます。

### 🔁 Entity / Domain変換

EntityとDomain Modelの変換は、RepositoryImplまたはMapperで行います。

```kotlin
fun SampleEntity.toDomain(): Sample {
    return Sample(
        id = SampleId(id),
        name = SampleName(name),
        enabled = enabled,
        updatedAt = Instant.ofEpochMilli(updatedAt)
    )
}

fun Sample.toEntity(): SampleEntity {
    return SampleEntity(
        id = id.value,
        name = name.value,
        enabled = enabled,
        updatedAt = updatedAt.toEpochMilli()
    )
}
```

> [!IMPORTANT]
> **EntityをUseCaseより上へ漏らさない。**
>
> EntityはRoomの保存形式です。  
> UseCaseやViewModelがEntityを知ると、DB変更の影響が上位層へ漏れます。

### 🗄 Repository interface

UseCaseはRepository interfaceだけを参照します。

```kotlin
interface SampleRepository {

    suspend fun findById(
        id: SampleId
    ): AppResult<Sample?>

    suspend fun findAll(): AppResult<List<Sample>>

    suspend fun save(
        sample: Sample
    ): AppResult<Unit>

    suspend fun delete(
        sample: Sample
    ): AppResult<Unit>
}
```

> [!IMPORTANT]
> **UseCaseはDAOを知らない。**
>
> UseCaseから見えるのは、  
> `SampleRepository` というアプリ内の境界だけです。

### 🗄 RepositoryImpl

RepositoryImplでDAOを呼び、Entity / Domain変換と例外変換を行います。

```kotlin
class SampleRepositoryImpl @Inject constructor(
    private val sampleDao: SampleDao
) : SampleRepository {

    override suspend fun findById(
        id: SampleId
    ): AppResult<Sample?> {
        return try {
            val entity = sampleDao.findById(id.value)

            AppResult.Success(
                entity?.toDomain()
            )
        } catch (exception: Exception) {
            AppResult.Failure(
                AppError.LocalStorage(
                    code = "SAMPLE-DB-001",
                    cause = exception
                )
            )
        }
    }

    override suspend fun findAll(): AppResult<List<Sample>> {
        return try {
            val samples = sampleDao
                .findAll()
                .map { entity -> entity.toDomain() }

            AppResult.Success(samples)
        } catch (exception: Exception) {
            AppResult.Failure(
                AppError.LocalStorage(
                    code = "SAMPLE-DB-002",
                    cause = exception
                )
            )
        }
    }

    override suspend fun save(
        sample: Sample
    ): AppResult<Unit> {
        return try {
            sampleDao.upsert(sample.toEntity())

            AppResult.Success(Unit)
        } catch (exception: Exception) {
            AppResult.Failure(
                AppError.LocalStorage(
                    code = "SAMPLE-DB-003",
                    cause = exception
                )
            )
        }
    }

    override suspend fun delete(
        sample: Sample
    ): AppResult<Unit> {
        return try {
            sampleDao.delete(sample.toEntity())

            AppResult.Success(Unit)
        } catch (exception: Exception) {
            AppResult.Failure(
                AppError.LocalStorage(
                    code = "SAMPLE-DB-004",
                    cause = exception
                )
            )
        }
    }
}
```

> [!TIP]
> **ローカル保存例外はRepositoryImplでAppErrorへ変換する。**
>
> SQLiteExceptionやDataStore例外を、ViewModelやComposableまで漏らさないようにします。

### 📥 読み込みUseCase

DBから取得した値の意味付けはUseCaseで行います。

```kotlin
class LoadSampleUseCase @Inject constructor(
    private val sampleRepository: SampleRepository
) {

    suspend operator fun invoke(
        input: LoadSampleInput
    ): AppResult<Sample> {
        return when (
            val result = sampleRepository.findById(
                SampleId(input.id)
            )
        ) {
            is AppResult.Success -> {
                val sample = result.value

                if (sample == null) {
                    AppResult.Failure(
                        AppError.Business(
                            code = "SAMPLE-NOT-FOUND",
                            reason = "Sample not found"
                        )
                    )
                } else {
                    AppResult.Success(sample)
                }
            }

            is AppResult.Failure -> {
                result
            }
        }
    }
}
```

> [!IMPORTANT]
> **データがない場合の扱いはUseCaseで決める。**
>
> 空表示でよいのか、業務エラーなのか、初期データを作るのかはUseCaseで判断します。

### 📤 保存UseCase

保存処理では、入力値からDomain Modelを作り、Repositoryへ保存します。

```kotlin
class SaveSampleUseCase @Inject constructor(
    private val sampleRepository: SampleRepository,
    private val validationPolicy: SampleValidationPolicy
) {

    suspend operator fun invoke(
        input: SaveSampleInput
    ): AppResult<Unit> {
        val validationResult =
            validationPolicy.validate(input)

        if (validationResult is AppResult.Failure) {
            return validationResult
        }

        val sample = Sample(
            id = SampleId(input.id),
            name = SampleName(input.name),
            enabled = input.enabled,
            updatedAt = dateTimeProvider.now().toInstant()
        )

        return sampleRepository.save(sample)
    }
}
```

> [!CAUTION]
> **Repositoryに保存可否の業務判断を書かない。**
>
> 保存してよいかどうかはUseCase / Domainで判断します。

### 🗑 削除UseCase

削除も、DB操作そのものではなく業務処理として扱います。

```kotlin
class DeleteSampleUseCase @Inject constructor(
    private val sampleRepository: SampleRepository
) {

    suspend operator fun invoke(
        input: DeleteSampleInput
    ): AppResult<Unit> {
        val loadResult = sampleRepository.findById(
            SampleId(input.id)
        )

        return when (loadResult) {
            is AppResult.Success -> {
                val sample = loadResult.value

                if (sample == null) {
                    AppResult.Failure(
                        AppError.Business(
                            code = "SAMPLE-DELETE-NOT-FOUND",
                            reason = "Delete target not found"
                        )
                    )
                } else {
                    sampleRepository.delete(sample)
                }
            }

            is AppResult.Failure -> {
                loadResult
            }
        }
    }
}
```

> [!TIP]
> **削除対象が存在しない場合の扱いもUseCaseで決める。**
>
> 成功扱いにするのか、業務エラーにするのかを明確にします。

### ⚙️ DataStoreで設定値を扱う

現行実装では、Android DataStoreを `PreferenceDataStore` という薄いCore契約で包みます。

```kotlin
interface PreferenceDataStore {
    fun observeBoolean(key: String, defaultValue: Boolean): Flow<Boolean>
    suspend fun putBoolean(key: String, value: Boolean)
    fun observeString(key: String, defaultValue: String): Flow<String>
    suspend fun putString(key: String, value: String)
}
```

Feature側のRepository実装がキーとDefault値を所有します。

```text
SampleSettingsViewModel
  ↓ SampleSettingsUseCaseFacade
Observe / Update SampleSettings UseCase
  ↓ SampleSettingsRepository
SampleSettingsRepositoryImpl
  ↓ PreferenceDataStore
AndroidPreferenceDataStore
  ↓ DataStore<Preferences>
```

`SampleSettingsRepositoryImpl` は通知、ログイン時確認、キャッシュ保持の設定を
`SampleSettings` Domain Modelへまとめます。  
Theme設定は横断機能なので `core.theme.ThemeRepository / ThemeDataStore` が担当します。

Preferences Key、保存ファイル名、DataStore例外はPresentationへ漏らしません。

### 🧾 RawConfig / ResolvedConfigを使う場合

外部設定ファイルや複数ソースを統合する機能では、保存形式の `RawConfig` と
利用可能な `ResolvedConfig` を分け、Resolverで変換します。  
ただし現行の単純Boolean設定には不要なので導入していません。

### 🧩 Roomと操作ログ

現行のRoom実装は `feature.operationlog` に置かれています。

```text
Sample History / Settings / Home
  ↓ OperationLog UseCase
OperationLogRepository
  ↓
OperationLogRepositoryImpl
  ↓
OperationLogLocalDataSource
  ↓
RoomOperationLogLocalDataSource
  ↓
OperationLogDao / OperationLogEntity
```

Entity / Domain変換は `OperationLogMapper`、DB例外と `AppResult` 変換は
`core.database.DatabaseExecutor` が担当します。  
RepositoryはDomain契約からLocalDataSourceへ委譲し、Room例外を直接処理しません。

履歴画面は `ObserveOperationLogsUseCase` のFlowを購読し、
`SampleHistoryItemMapper` で画面表示用Itemへ変換します。

### 🧬 Hilt登録

- DataStore生成: `app.datastore.PreferenceDataStoreModule`
- Room生成: `app.database.DatabaseProvideModule`
- DB共通契約: `app.database.DatabaseBindModule`
- 操作ログDAO / Repository: `OperationLogDataModule`
- Sample設定Repository: `SampleModule`
- ThemeRepository: `core.storage.theme.ThemeModule`
- BaseThemeRegistry: `core.ui.theme.ThemeUiModule`

生成処理は `@Provides`、interfaceと実装の紐付けは `@Binds` を基本とします。

### 🚫 やってはいけないこと

#### ❌ ViewModelからDAOを直接呼ぶ

```kotlin
class SampleViewModel @Inject constructor(
    private val sampleDao: SampleDao
) : ViewModel()
```

#### ✅ UseCase経由でRepositoryを呼ぶ

```kotlin
class LoadSampleUseCase @Inject constructor(
    private val sampleRepository: SampleRepository
)
```

#### ❌ EntityをUiStateに持たせる

```kotlin
data class SampleUiState(
    val sample: SampleEntity
) : UiState
```

#### ✅ 表示に必要な値だけをUiStateに持つ

```kotlin
data class SampleUiState(
    val id: String,
    val name: String,
    val enabled: Boolean
) : UiState
```

#### ❌ DataStoreをComposableから読む

```kotlin
@Composable
fun SampleScreen(
    dataStore: DataStore<Preferences>
)
```

#### ✅ ConfigRepository / UseCase経由で読む

```kotlin
val result = loadSampleConfigUseCase()
```

#### ❌ RawConfigを画面へ渡す

```kotlin
data class SampleUiState(
    val config: RawSampleConfig
) : UiState
```

#### ✅ 表示に必要な値だけUiStateへ持つ

```kotlin
data class SampleUiState(
    val featureEnabled: Boolean,
    val maxItemCount: String
) : UiState
```

### 🎯 7章まとめ

ローカル永続化では、以下を守ります。

```text
Room
  → 一覧・履歴・業務データ

DataStore
  → 小さな設定値・フラグ

Entity
  → DB保存用モデル

RawConfig
  → 設定保存用モデル

Domain / ResolvedConfig
  → アプリ内で使用するモデル

RepositoryImpl
  → Room / DataStore / Fileを扱う

UseCase
  → 読み込み / 保存 / 削除の意味付けを行う

UiState
  → 表示に必要な値だけを持つ
```

> [!IMPORTANT]
> **「保存形式」と「使用形式」と「表示形式」を分ける。**
>
> Entity / RawConfig をそのままUseCaseやUiStateに渡さず、  
> Repository / Resolver / Reducerを通じて責務ごとに変換します。

---

## 🌐 8. API通信をしたいとき

### 🎯 この章の目的

この章では、Retrofit、DTO、`ApiResponseHandler`、AppError変換、
Repository / Gateway境界、Retryの扱いを定義します。

API通信は、Composable・Route・ViewModelから直接扱いません。  
UseCaseはアプリ内のRepositoryまたはGateway契約を参照し、
Retrofit、HTTP status、Request / Response DTOはData / Infrastructureへ閉じます。

```text
Retrofit API
  ↓
RepositoryImpl または GatewayImpl
  ↓ AppResult<Domain Model>
Repository / Gateway interface
  ↓
UseCase
  ↓ AppResult
ViewModel
  ↓ UiMessage / UiEffect
Reducer
  ↓ UiState
```

> [!IMPORTANT]
> **APIを必ずGatewayと呼ぶのではなく、責務で境界名を選びます。**
>
> VersionInfoのようにAPIをデータ取得元として扱う場合はRepository、
> 外部SDKや操作APIとの命令的な接続はGatewayが自然です。

### 📌 この章はこういうときに見る

- Retrofit interfaceを追加したい
- Request / Response DTOを定義したい
- RepositoryとGatewayのどちらを使うか迷った
- HTTP / Network errorをAppErrorへ変換したい
- APIレスポンスをDomain Modelへ変換したい
- Retry方針を決めたい

### 🧭 基本方針

```text
1. Composable / Route / ViewModelからAPIを直接呼ばない
2. UseCaseからRetrofit APIを直接呼ばない
3. UseCaseはRepository / Gateway契約だけを知る
4. RepositoryImpl / GatewayImplがRetrofit APIを呼ぶ
5. DTOをUseCase・UiStateへ漏らさない
6. ApiResponseHandlerでHTTP / Network errorをAppErrorへ変換する
7. DTOはMapperでDomain Modelへ変換する
8. Retry条件はUseCaseまたはPolicyで判断する
```

### 🧱 Retrofit API定義

Retrofit interfaceは、APIのエンドポイント定義だけを行います。

```kotlin
interface SampleApi {

    @GET("samples/{id}")
    suspend fun getSample(
        @Path("id") id: String
    ): Response<SampleResponse>

    @POST("samples")
    suspend fun saveSample(
        @Body request: SaveSampleRequest
    ): Response<Unit>
}
```

> [!IMPORTANT]
> **Retrofit interfaceは通信定義のみ。**
>
> レスポンスの意味付けや業務判断は、Retrofit interfaceには書きません。

### 📦 Request DTO

Request DTOは、API送信用のモデルです。

```kotlin
data class SaveSampleRequest(
    val id: String,
    val name: String,
    val enabled: Boolean
)
```

> [!CAUTION]
> **Request DTOをUseCase Inputとして使わない。**
>
> API都合の項目名・形式をUseCaseへ持ち込むと、  
> API仕様変更の影響がApplication層へ漏れます。

### 📦 Response DTO

Response DTOは、API受信用のモデルです。

```kotlin
data class SampleResponse(
    val id: String?,
    val name: String?,
    val enabled: Boolean?,
    val updatedAt: String?
)
```

> [!TIP]
> **APIレスポンスは信用しすぎない。**
>
> nullable / 不正形式 / 欠損 / 想定外値を考慮して、  
> GatewayまたはMapperで安全に変換します。

### 🧩 Domain Model

アプリ内では、Response DTOではなくDomain Modelを扱います。

```kotlin
data class Sample(
    val id: SampleId,
    val name: SampleName,
    val enabled: Boolean,
    val updatedAt: Instant
)
```

```kotlin
@JvmInline
value class SampleId(
    val value: String
)

@JvmInline
value class SampleName(
    val value: String
)
```

> [!IMPORTANT]
> **APIの形とアプリ内の形を分ける。**
>
> APIレスポンスは外部仕様です。  
> アプリ内ではDomain Modelへ変換して扱います。

### 🔁 DTO / Domain変換

DTOからDomain Modelへの変換は、GatewayImplまたは専用Mapperで行います。

```kotlin
class SampleResponseMapper @Inject constructor() {

    fun map(
        response: SampleResponse
    ): AppResult<Sample> {
        val id = response.id
            ?: return AppResult.Failure(
                AppError.ApiInvalidResponse(
                    code = "SAMPLE-API-INVALID-001",
                    reason = "id is null"
                )
            )

        val name = response.name
            ?: return AppResult.Failure(
                AppError.ApiInvalidResponse(
                    code = "SAMPLE-API-INVALID-002",
                    reason = "name is null"
                )
            )

        val updatedAt = response.updatedAt
            ?.let { value -> Instant.parse(value) }
            ?: return AppResult.Failure(
                AppError.ApiInvalidResponse(
                    code = "SAMPLE-API-INVALID-003",
                    reason = "updatedAt is null"
                )
            )

        return AppResult.Success(
            Sample(
                id = SampleId(id),
                name = SampleName(name),
                enabled = response.enabled ?: false,
                updatedAt = updatedAt
            )
        )
    }
}
```

> [!CAUTION]
> **不正なAPIレスポンスを正常扱いしない。**
>
> 欠損や不正形式を握りつぶすと、  
> 後続処理で原因不明の不具合になります。

### 🌐 Gateway / Repository実装

API通信を「外部操作」として扱う場合はGateway、
「データ取得元」として扱う場合はRepositoryを使います。  
現行のVersionInfoでは、最新バージョン情報の取得元として
`LatestVersionRepository` を採用しています。

```kotlin
class RetrofitLatestVersionRepository @Inject constructor(
    private val api: LatestVersionApi,
    private val responseHandler: ApiResponseHandler,
    private val mapper: LatestVersionMapper
) : LatestVersionRepository {

    override suspend fun getLatestVersion(): AppResult<LatestVersionInfo> {
        return responseHandler.handle { api.getLatestVersion() }
            .map(mapper::map)
    }
}
```

`ApiResponseHandler` が次を共通処理します。

- HTTP成功 / 失敗判定
- null body判定
- 通信例外の捕捉
- `ApiError` から `AppError` への変換

RepositoryはAPI呼び出しとDTO→Domain変換へ集中します。

### 🚨 HTTP errorの変換

```text
Retrofit Response / Throwable
  ↓ ApiErrorMapper
ApiError
  ↓ ApiResponseHandler
AppError.Network / Api / ApiInvalidResponse / Unexpected
  ↓ AppResult.Failure
UseCase / ViewModel
```

HTTP statusやRetrofit例外をViewModelで分岐しません。

### 📡 Network errorの扱い

- 接続不可、timeout: `AppError.Network`
- HTTP status error: `AppError.Api`
- 空Body、parse不正: `AppError.ApiInvalidResponse`
- 想定外: `AppError.Unexpected`

### 🧩 UseCaseでAPI通信を使う

UseCaseはRepository / Gateway契約だけを参照します。  
VersionInfoでは端末アプリ情報、最新バージョン、外部リンクを組み合わせて
`VersionInfo` を返します。

### 🧠 ViewModelでAPI結果を画面へ反映する

ViewModelはAppResultをMessage / Effectへ変換します。  
DTO、Response、HTTP statusをUiStateへ入れません。

### 🔁 Retry方針

再試行は `RetryClicked` EventとしてEvent Queueへ送り、同じUseCaseを再実行します。  
Retry自体をUiEffectにしません。UiEffectは「再試行ボタンを持つDialogを表示する」
などのUI副作用に使います。

### 🧬 Retrofit / OkHttp のHilt登録

`app.network.NetworkModule` がMoshi、OkHttpClient、Retrofit、
`LatestVersionApi` を生成します。  
認証Headerと共通Headerは `core.network.interceptor` に分離されています。

### 🚫 やってはいけないこと

#### ❌ ViewModelからRetrofit APIを直接呼ぶ

```kotlin
class SampleViewModel @Inject constructor(
    private val sampleApi: SampleApi
) : ViewModel()
```

#### ✅ UseCaseからGateway interfaceを呼ぶ

```kotlin
class LoadSampleUseCase @Inject constructor(
    private val sampleGateway: SampleGateway
)
```

#### ❌ Response DTOをUiStateに持たせる

```kotlin
data class SampleUiState(
    val response: SampleResponse
) : UiState
```

#### ✅ 表示に必要な値だけをUiStateに持つ

```kotlin
data class SampleUiState(
    val id: String,
    val name: String,
    val enabled: Boolean
) : UiState
```

#### ❌ HTTP statusをViewModelで分岐する

```kotlin
if (response.code() == 404) {
    // ViewModelでHTTP判断
}
```

#### ✅ GatewayImplでAppErrorへ変換する

```kotlin
response.toApiError(
    codePrefix = "SAMPLE-GET"
)
```

### 🎯 8章まとめ

API通信では、以下を守ります。

```text
Retrofit API
  → GatewayImplに閉じ込める

Request / Response DTO
  → API通信専用モデル

Domain Model
  → アプリ内で扱うモデル

Gateway
  → API通信の境界

UseCase
  → API通信を使った業務手順

ViewModel
  → AppResultをUiMessage / UiEffectへ変換

UiState
  → 表示に必要な値だけを持つ
```

> [!IMPORTANT]
> **「API仕様」と「画面仕様」を直結させない。**
>
> APIレスポンスをそのまま画面へ渡すのではなく、  
> Gateway / UseCase / UiStateを通じて責務を分離します。

---

## 🚨 9. エラーハンドリング

### 🎯 この章の目的

この章では、Androidアプリにおける  
**エラー処理の構造化ルール** を定義します。

エラーは、文字列・null・booleanで扱ってはいけません。  
失敗理由を構造として扱い、UseCase / ViewModel / UiState / UiEffect の責務を分離します。

```text
Exception
  ↓
RepositoryImpl / GatewayImpl / UseCase
  ↓
AppError
  ↓
AppResult.Failure
  ↓
ViewModel
  ↓
UiError
  ↓
UiMessage / UiEffect
  ↓
UiState / Dialog / Snackbar
```

> [!IMPORTANT]
> **「例外は構造に変換してから扱う」**
>
> `Exception.message` をそのまま画面に出すのではなく、  
> アプリ内で扱えるエラー型へ変換してからPresentation層へ渡します。

### 📌 この章はこういうときに見る

- UseCaseで失敗を返したいとき
- Repository / Gateway で例外が発生する可能性があるとき
- 入力バリデーションエラーを画面に表示したいとき
- Dialog / Snackbar でエラーを通知したいとき
- エラー文言をどこで組み立てるか迷ったとき
- `Exception` / `String` / `Boolean` / `null` のどれで失敗を表すか迷ったとき

### 🧭 基本方針

エラーハンドリングでは、以下の方針を守ります。

```text
1. Exceptionを画面まで漏らさない
2. 失敗理由はAppErrorとして構造化する
3. UseCase / Repository / GatewayはAppResultで成功・失敗を返す
4. ViewModelはAppErrorをUiErrorへ変換する
5. ComposableはUiState / UiEffectに従って表示するだけにする
6. 表示文言はUiErrorMapper / StringProviderで組み立てる
7. Dialog / Snackbar / ToastはUiEffectで通知する
8. 入力欄のエラーはUiStateで保持する
```

> [!CAUTION]
> **エラーを文字列だけで扱わない。**
>
> 文字列だけでは、エラー種別・復旧導線・ログ用コード・再試行可否が判断できません。

### 🧩 エラーの分類

Android実装では、エラーを以下のように分類します。

| 種別 | 役割 | 主な利用場所 |
|---|---|---|
| `Exception` | 技術的な例外 | Data / Infrastructure |
| `AppError` | アプリ内で扱う失敗理由 | Repository / Gateway / UseCase |
| `AppResult` | 成功 / 失敗の戻り値 | UseCase / Repository / Gateway |
| `UiError` | 画面表示用のエラー | ViewModel / UiState / UiEffect |

> [!IMPORTANT]
> **AppErrorは意味、UiErrorは表示。**
>
> Domain / Application層に、画面表示用の文言を持ち込んではいけません。

### 🧱 AppError定義例

```kotlin
sealed interface AppError {
    val code: String
    val cause: Throwable?

    data class Validation(
        override val code: String,
        val field: String,
        val reason: String,
        override val cause: Throwable? = null
    ) : AppError

    data class Business(
        override val code: String,
        val reason: String,
        override val cause: Throwable? = null
    ) : AppError

    data class LocalStorage(
        override val code: String,
        override val cause: Throwable? = null
    ) : AppError

    data class Network(
        override val code: String,
        override val cause: Throwable? = null
    ) : AppError

    data class Api(
        override val code: String,
        val statusCode: Int,
        val reason: String? = null,
        override val cause: Throwable? = null
    ) : AppError

    data class ApiInvalidResponse(
        override val code: String,
        val reason: String,
        override val cause: Throwable? = null
    ) : AppError

    data class Unexpected(
        override val code: String = "UNEXPECTED",
        override val cause: Throwable? = null
    ) : AppError
}
```

`AppError` は表示文言を持ちません。  
FeatureのErrorMapperが `StringProvider` を使い、`UiError` または表示用messageへ変換します。

### 📤 AppResult定義例

現行の `AppResult` はKotlin標準 `Result<T>` を使わず、`AppError` を失敗値として持ちます。

```kotlin
sealed interface AppResult<out T> {
    data class Success<T>(val value: T) : AppResult<T>
    data class Failure(val error: AppError) : AppResult<Nothing>
}
```

提供する拡張関数は、次の最小範囲です。

| 関数 | 用途 |
|---|---|
| `map` | Success値だけを変換する |
| `fold` | Success / Failureを1つの戻り値へ畳み込む |
| `onSuccess` | Success時だけ補助処理を行う |
| `onFailure` | Failure時だけ補助処理を行う |
| `getOrNull` | Success値をnullableで取り出す |
| `errorOrNull` | FailureのAppErrorをnullableで取り出す |

これらのcallback内で発生した例外は自動変換しません。  
例外を `AppError` にする責務はRepository / Gateway / Handlerなどの境界に置きます。

成功値を返さない処理は `AppResult<Unit>` を使います。

> [!IMPORTANT]
> **通常想定される失敗は `AppResult.Failure(AppError)` で返します。**
>
> Kotlin標準Result、Throwable、Boolean、文字列だけの失敗表現を混在させません。

### 🧩 UseCaseでのエラー変換

UseCaseでは、業務的な失敗理由を `AppError` に変換します。

```kotlin
class SaveSampleUseCase @Inject constructor(
    private val repository: SampleRepository,
    private val validationPolicy: SampleValidationPolicy
) {

    suspend operator fun invoke(
        input: SaveSampleInput
    ): AppResult<Unit> {
        val validationResult = validationPolicy.validate(input)

        if (validationResult is AppResult.Failure) {
            return validationResult
        }

        val sample = Sample(
            id = SampleId(input.id),
            name = SampleName(input.name),
            enabled = input.enabled,
            updatedAt = dateTimeProvider.now().toInstant()
        )

        return repository.save(sample)
    }
}
```

> [!CAUTION]
> **UseCaseからExceptionをそのままthrowしない。**
>
> 復旧不能な致命的エラーを除き、  
> UseCaseは失敗を `AppResult.Failure` として返します。

### 🗄 Repository / Gatewayでのエラー変換

RepositoryImpl / GatewayImplでは、外部I/Oの例外を `AppError` へ変換します。

```kotlin
class SampleRepositoryImpl @Inject constructor(
    private val sampleDao: SampleDao
) : SampleRepository {

    override suspend fun save(
        sample: Sample
    ): AppResult<Unit> {
        return try {
            sampleDao.upsert(sample.toEntity())

            AppResult.Success(Unit)
        } catch (exception: Exception) {
            AppResult.Failure(
                AppError.LocalStorage(
                    code = "SAMPLE-DB-SAVE-001",
                    cause = exception
                )
            )
        }
    }
}
```

```kotlin
class SampleGatewayImpl @Inject constructor(
    private val sampleApi: SampleApi
) : SampleGateway {

    override suspend fun getSample(
        id: SampleId
    ): AppResult<Sample> {
        return try {
            val response = sampleApi.getSample(id.value)

            if (response.isSuccessful.not()) {
                return AppResult.Failure(
                    response.toApiError(
                        codePrefix = "SAMPLE-GET"
                    )
                )
            }

            val body = response.body()
                ?: return AppResult.Failure(
                    AppError.ApiInvalidResponse(
                        code = "SAMPLE-GET-EMPTY-BODY",
                        reason = "Response body is null"
                    )
                )

            AppResult.Success(body.toDomain())
        } catch (exception: IOException) {
            AppResult.Failure(
                AppError.Network(
                    code = "SAMPLE-GET-NETWORK",
                    cause = exception
                )
            )
        } catch (exception: Exception) {
            AppResult.Failure(
                AppError.Unexpected(
                    cause = exception
                )
            )
        }
    }
}
```

> [!IMPORTANT]
> **低レイヤ例外をPresentation層へ漏らさない。**
>
> SQLiteException / IOException / Retrofit例外などは、  
> RepositoryImpl / GatewayImpl でAppErrorへ変換します。

### 🧠 ViewModelでのエラー変換

ViewModelは、UseCaseから受け取った `AppError` をFeature固有のErrorMapperで
表示可能な情報へ変換します。

```kotlin
when (val result = useCaseFacade.load()) {
    is AppResult.Success ->
        dispatch(XxxUiMessage.LoadSucceeded(result.value))

    is AppResult.Failure -> {
        val uiError = presentationFacade.errorMapper.toUiError(result.error)
        dispatch(XxxUiMessage.LoadFailed(uiError))
        emitEffect(
            XxxUiEffect.ShowDialog(
                title = uiError.title,
                message = uiError.message
            )
        )
    }
}
```

ErrorMapperはFeatureのPresentationに置きます。  
Coreは汎用の自動ErrorHandlerを持たず、画面ごとの表示方針を強制しません。
文字列はMapper / Formatter内で `StringProvider` から取得します。

### 🧾 文字列管理

エラー表示文言は、`strings.xml` または `StringProvider` 経由で取得します。

```text
AppError
  → 失敗理由

UiError
  → 表示用情報

StringProvider
  → Androidリソース文字列取得
```

> [!CAUTION]
> **UseCaseで表示文言を返さない。**
>
> UseCaseは失敗理由を `AppError` として返します。  
> 表示文言への変換はPresentation層で行います。

### 🧭 Stateに反映するエラーとEffectにするエラー

エラー表示は、StateとEffectを使い分けます。

| 表示内容 | 扱い |
|---|---|
| 入力欄のエラーメッセージ | UiState |
| 画面内に表示し続けるエラー | UiState |
| Dialog表示 | UiEffect |
| Snackbar表示 | UiEffect |
| Toast表示 | UiEffect |
| 画面遷移を伴うエラー | UiEffect |

> [!TIP]
> **画面に残るエラーはState。一度だけ通知するエラーはEffect。**
>
> 入力欄下のエラーはUiState。  
> DialogやSnackbarはUiEffectです。

### 🧱 UiStateへのエラー反映例

```kotlin
data class SampleUiState(
    val isLoading: Boolean,
    val name: String,
    val nameErrorMessage: String?,
    val screenErrorMessage: String?
) : UiState {
    companion object {
        fun initial(): SampleUiState {
            return SampleUiState(
                isLoading = false,
                name = "",
                nameErrorMessage = null,
                screenErrorMessage = null
            )
        }
    }
}
```

UiMessage定義：

```kotlin
sealed interface SampleUiMessage : UiMessage {

    data object SaveStarted : SampleUiMessage

    data object SaveSucceeded : SampleUiMessage

    data class ValidationFailed(
        val field: String,
        val error: UiError
    ) : SampleUiMessage

    data class SaveFailed(
        val error: UiError
    ) : SampleUiMessage
}
```

Reducer：

```kotlin
class SampleReducer @Inject constructor() :
    Reducer<SampleUiState, SampleUiMessage> {

    override fun reduce(
        currentState: SampleUiState,
        message: SampleUiMessage
    ): SampleUiState {
        return when (message) {
            SampleUiMessage.SaveStarted -> {
                currentState.copy(
                    isLoading = true,
                    screenErrorMessage = null
                )
            }

            SampleUiMessage.SaveSucceeded -> {
                currentState.copy(
                    isLoading = false,
                    screenErrorMessage = null
                )
            }

            is SampleUiMessage.ValidationFailed -> {
                currentState.copy(
                    isLoading = false,
                    nameErrorMessage = message.error.message
                )
            }

            is SampleUiMessage.SaveFailed -> {
                currentState.copy(
                    isLoading = false,
                    screenErrorMessage = message.error.message
                )
            }
        }
    }
}
```

### 💥 Effectとして通知する例

```kotlin
private fun handleSave() {
    viewModelScope.launch {
        dispatch(SampleUiMessage.SaveStarted)

        val result = saveSampleUseCase(
            createSaveInput(uiState.value)
        )

        when (result) {
            is AppResult.Success -> {
                dispatch(SampleUiMessage.SaveSucceeded)

                emitEffect(
                    SampleUiEffect.ShowSnackbar(
                        message = "保存しました"
                    )
                )
            }

            is AppResult.Failure -> {
                val uiError = errorMapper.map(result.error)

                dispatch(
                    SampleUiMessage.SaveFailed(
                        error = uiError
                    )
                )

                emitEffect(
                    SampleUiEffect.ShowDialog(
                        title = uiError.title,
                        message = uiError.message
                    )
                )
            }
        }
    }
}
```

> [!CAUTION]
> **サンプル内の直書き文言は説明用です。**
>
> 実装時は、必要に応じて `strings.xml` または `StringProvider` 経由に置き換えてください。

> [!IMPORTANT]
> **Dialog表示用の `showDialog = true` をStateに持たない。**
>
> Dialogは一度だけ実行する副作用です。  
> `UiEffect.ShowDialog` として流します。

### 🚫 やってはいけないこと

#### ❌ UseCaseが文字列エラーを返す

```kotlin
return "保存に失敗しました"
```

#### ✅ 構造化されたエラーを返す

```kotlin
return AppResult.Failure(
    AppError.Business(
        code = "SAMPLE-SAVE-001",
        reason = "SaveRejected"
    )
)
```

#### ❌ ViewModelでExceptionを直接分岐する

```kotlin
catch (exception: IOException) {
    _uiState.value = _uiState.value.copy(
        errorMessage = "通信に失敗しました"
    )
}
```

#### ✅ UseCase / GatewayでAppErrorへ変換し、ViewModelでUiErrorへ変換する

```kotlin
val result = saveSampleUseCase(input)

if (result is AppResult.Failure) {
    val uiError = errorMapper.map(result.error)

    dispatch(
        SampleUiMessage.SaveFailed(uiError)
    )
}
```

#### ❌ Composableでエラー文言を組み立てる

```kotlin
Text("エラー：" + error.code)
```

#### ✅ Stateに反映された表示用メッセージを表示する

```kotlin
uiState.screenErrorMessage?.let { message ->
    Text(message)
}
```

### 🎯 9章まとめ

エラーハンドリングでは、以下を守ります。

```text
Exception
  → RepositoryImpl / GatewayImpl / UseCaseで捕捉

AppError
  → 失敗理由を構造化

AppResult.Failure
  → UseCase / Repository / Gatewayの戻り値

UiError
  → 画面表示用エラー

UiMessage
  → 画面内状態に反映

UiEffect
  → Dialog / Snackbar / Toastなど一度だけ通知
```

> [!IMPORTANT]
> **「エラーの意味」と「エラーの表示」を分ける。**
>
> UseCaseは失敗の意味を返す。  
> ViewModelは表示用に変換する。  
> ComposableはState / Effectに従って表示するだけです。

---

## 🧭 10. Navigation / Dialog / Snackbar

### 🎯 この章の目的

この章では、Androidアプリにおける  
**画面遷移・Dialog表示・Snackbar表示の扱い方** を定義します。

Navigation / Dialog / Snackbar は、いずれも一度だけ実行したい副作用です。  
そのため、UiStateではなくUiEffectとして扱います。

```text
ViewModel
  ↓ UiEffect
Route / Effect Handler
  ↓
Navigation / Dialog / Snackbar
```

> [!IMPORTANT]
> **「一度だけ実行する処理はEffectにする」**
>
> 画面遷移・Dialog・Snackbar・Toastは、  
> StateではなくUiEffectとして扱います。

### 📌 この章はこういうときに見る

- ボタン押下で別画面へ遷移したいとき
- 戻る操作を実装したいとき
- 保存完了後にSnackbarを出したいとき
- エラー発生時にDialogを表示したいとき
- NavigationをViewModelに書いてよいか迷ったとき
- Dialogの表示中状態をどこに持つか迷ったとき

### ⚡ UiEffect定義

画面遷移・Dialog・Snackbarは、UiEffectとして定義します。

```kotlin
sealed interface SampleUiEffect : UiEffect {

    data object NavigateBack : SampleUiEffect

    data class NavigateToDetail(
        val id: String
    ) : SampleUiEffect

    data class ShowDialog(
        val title: String,
        val message: String
    ) : SampleUiEffect

    data class ShowSnackbar(
        val message: String
    ) : SampleUiEffect
}
```

> [!TIP]
> **Effect名は「何をしてほしいか」で書く。**
>
> `Saved` ではなく `ShowSnackbar`。  
> `DetailSelected` ではなく `NavigateToDetail` のように、  
> Effect Handlerが実行すべき内容が分かる名前にします。

### 🧠 ViewModelからEffectを発行する

Feature ViewModelはBaseViewModelの `emitEffect` を利用します。

```kotlin
override suspend fun handleEvent(event: XxxUiEvent) {
    when (event) {
        XxxUiEvent.BackClicked ->
            emitEffect(XxxUiEffect.NavigateBack)

        is XxxUiEvent.DetailClicked ->
            emitEffect(XxxUiEffect.NavigateToDetail(event.id))
    }
}
```

`MutableSharedFlow` や独自の `emitEffect` を各ViewModelへ定義しません。  
Dialog / Snackbar用文言は `StringProvider` やFeatureのErrorMapper / Factoryで解決します。

### 🧭 RouteでEffectを購読する

```kotlin
LaunchedEffect(viewModel) {
    viewModel.uiEffect.collect { effect ->
        when (effect) {
            XxxUiEffect.NavigateBack ->
                navController.popBackStack()

            is XxxUiEffect.NavigateToDetail ->
                navController.navigate(XxxRoutes.detail(effect.id))

            is XxxUiEffect.ShowSnackbar ->
                snackbarHostState.showSnackbar(effect.message)

            is XxxUiEffect.ShowDialog ->
                dialogState = DialogUiState(
                    title = effect.title,
                    message = effect.message,
                    positiveButtonText = okText
                )
        }
    }
}
```

NavControllerを知るのはGraph / Routeだけです。  
Dialog表示中StateとSnackbarHostStateもRouteのCompose stateとして保持します。

### 🧭 Navigationの扱い

NavigationはUiEffectとして要求します。

#### Effect定義

```kotlin
sealed interface SampleUiEffect : UiEffect {

    data class NavigateToDetail(
        val id: String
    ) : SampleUiEffect

    data object NavigateBack : SampleUiEffect
}
```

#### ViewModel

```kotlin
private fun handleDetailClicked(id: String) {
    emitEffect(
        SampleUiEffect.NavigateToDetail(id)
    )
}
```

#### Route / Effect Handler

```kotlin
LaunchedEffect(viewModel) {
    viewModel.uiEffect.collect { effect ->
        when (effect) {
            is SampleUiEffect.NavigateToDetail -> {
                navController.navigate(
                    XxxRoutes.detail(effect.id)
                )
            }

            SampleUiEffect.NavigateBack -> {
                navController.popBackStack()
            }

            else -> Unit
        }
    }
}
```

> [!CAUTION]
> **`uiState.shouldNavigate = true` で遷移を表現しない。**
>
> Navigationは画面の状態ではなく、一度だけ実行する副作用です。

### 🧩 Route定義

Route文字列は専用objectへまとめます。

```kotlin
object SampleRoutes {

    const val List = "sample"

    const val Detail = "sample/{sampleId}"

    fun detail(
        sampleId: String
    ): String {
        return "sample/$sampleId"
    }
}
```

> [!IMPORTANT]
> **Route文字列を各所に直書きしない。**
>
> 画面数が増えるほど、文字列不一致によるNavigation不具合が増えます。

### 💬 Snackbarの扱い

SnackbarもUiEffectとして扱います。

#### Effect定義

```kotlin
data class ShowSnackbar(
    val message: String
) : SampleUiEffect
```

#### ViewModel

```kotlin
private fun handleSaveSucceeded() {
    emitEffect(
        SampleUiEffect.ShowSnackbar(
            message = "保存しました"
        )
    )
}
```

#### Route

```kotlin
val snackbarHostState = remember {
    SnackbarHostState()
}

LaunchedEffect(viewModel) {
    viewModel.uiEffect.collect { effect ->
        when (effect) {
            is SampleUiEffect.ShowSnackbar -> {
                snackbarHostState.showSnackbar(
                    message = effect.message
                )
            }

            else -> Unit
        }
    }
}

Scaffold(
    snackbarHost = {
        SnackbarHost(
            hostState = snackbarHostState
        )
    }
) { paddingValues ->
    SampleScreen(
        modifier = Modifier.padding(paddingValues),
        uiState = uiState,
        onEvent = viewModel::sendEvent
    )
}
```

> [!TIP]
> **Snackbarは「通知」であり「状態」ではない。**
>
> 画面上に一時的に表示されるだけなので、  
> 原則としてUiEffectで扱います。

### 🪟 Dialogの扱い

Dialogは一度だけ表示したい通知であるため、  
基本的にはUiEffectとして発行します。

ただし、ComposeのDialogは表示中の状態管理が必要になるため、  
Effectを受け取ったRoute側で **UI表示用の一時State** に変換します。

#### Effect定義

```kotlin
data class ShowDialog(
    val title: String,
    val message: String
) : SampleUiEffect
```

#### DialogUiState

```kotlin
data class DialogUiState(
    val title: String,
    val message: String
)
```

#### Route実装例

```kotlin
var dialogState by remember {
    mutableStateOf<DialogUiState?>(null)
}

LaunchedEffect(viewModel) {
    viewModel.uiEffect.collect { effect ->
        when (effect) {
            is SampleUiEffect.ShowDialog -> {
                dialogState = DialogUiState(
                    title = effect.title,
                    message = effect.message
                )
            }

            else -> Unit
        }
    }
}

dialogState?.let { dialog ->
    AlertDialog(
        onDismissRequest = {
            dialogState = null
        },
        title = {
            Text(dialog.title)
        },
        text = {
            Text(dialog.message)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    dialogState = null
                }
            ) {
                Text("OK")
            }
        }
    )
}
```

> [!IMPORTANT]
> **Dialog表示要求はEffect、Dialog表示中のUI状態はRoute側の一時State。**
>
> ViewModelのUiStateに `isDialogVisible` を持たせるのではなく、  
> Effectを受け取ったUI側で表示中だけ管理します。

### 🔁 Effectを共通ハンドラ化する場合

画面ごとにEffect購読を書くと重複する場合、  
共通のEffect Handlerを用意してもよいです。

```kotlin
@Composable
fun HandleSampleEffect(
    effectFlow: Flow<SampleUiEffect>,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    onShowDialog: (DialogUiState) -> Unit
) {
    LaunchedEffect(effectFlow) {
        effectFlow.collect { effect ->
            when (effect) {
                SampleUiEffect.NavigateBack -> {
                    navController.popBackStack()
                }

                is SampleUiEffect.NavigateToDetail -> {
                    navController.navigate(
                        XxxRoutes.detail(effect.id)
                    )
                }

                is SampleUiEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }

                is SampleUiEffect.ShowDialog -> {
                    onShowDialog(
                        DialogUiState(
                            title = effect.title,
                            message = effect.message
                        )
                    )
                }
            }
        }
    }
}
```

> [!TIP]
> **Effect Handlerは共通化してよいが、業務判断は入れない。**
>
> Effect Handlerは、Effectを実際のUI副作用へ変換するだけです。  
> 遷移可否や保存可否の判断はViewModel / UseCase側で行います。

### 🚫 やってはいけないこと

#### ❌ ViewModelにNavControllerを注入する

```kotlin
class SampleViewModel(
    private val navController: NavController
) : ViewModel()
```

#### ✅ UiEffectで遷移要求を出す

```kotlin
emitEffect(
    SampleUiEffect.NavigateToDetail(id)
)
```

#### ❌ StateでSnackbarを表す

```kotlin
data class SampleUiState(
    val showSnackbar: Boolean
) : UiState
```

#### ✅ EffectでSnackbarを流す

```kotlin
emitEffect(
    SampleUiEffect.ShowSnackbar("保存しました")
)
```

#### ❌ Dialog表示フラグをViewModel Stateに持つ

```kotlin
data class SampleUiState(
    val isDialogVisible: Boolean
) : UiState
```

#### ✅ ShowDialog EffectをUI側で一時Stateに変換する

```kotlin
is SampleUiEffect.ShowDialog -> {
    dialogState = DialogUiState(
        title = effect.title,
        message = effect.message
    )
}
```

### 🎯 10章まとめ

Navigation / Dialog / Snackbarでは、以下を守ります。

```text
ViewModel
  → UiEffectを発行する

Route / Effect Handler
  → UiEffectを購読する
  → Navigation / Dialog / Snackbarを実行する

UiState
  → 一度だけ実行する処理を持たない
```

> [!IMPORTANT]
> **「Navigation / Dialog / Snackbar はStateではなくEffect」**
>
> 画面に残る事実はUiState。  
> 一度だけ実行する通知・遷移・ダイアログはUiEffectとして扱います。

---

## 🧬 11. Hilt

### 🎯 この章の目的

この章では、Androidアプリにおける  
**Hiltを使った依存関係の登録ルール** を定義します。

DI構成は、アーキテクチャの依存方向を表す地図です。  
どのModuleに何を登録するかを見ることで、レイヤ構造が分かる状態にします。

```text
Route
  ↓ hiltViewModel()
ViewModel
  ↓ @Inject
UseCase
  ↓ @Inject
Repository interface / Gateway interface
  ↓ @Binds
RepositoryImpl / GatewayImpl
```

> [!IMPORTANT]
> **「DI構成はアーキテクチャそのもの」**
>
> Hilt Moduleに何を登録するかは、単なる実装都合ではありません。  
> 依存方向・責務分離・差し替え可能性を表します。

### 📌 この章はこういうときに見る

- 新しいViewModelを追加したいとき
- UseCaseを追加したいとき
- Repository / Gatewayのinterfaceと実装を紐づけたいとき
- Hilt Moduleをどこに作るか迷ったとき
- `@Provides` と `@Binds` の使い分けに迷ったとき
- DI登録漏れのビルドエラーが出たとき

### 🧭 基本方針

Hilt登録では、以下を基本方針とします。

```text
1. ViewModelは @HiltViewModel を付ける
2. 依存は constructor injection で受け取る
3. UseCaseは @Inject constructor で生成できるようにする
4. interfaceと実装の紐づけは @Binds を使う
5. 生成に手順が必要なものは @Provides を使う
6. Moduleはレイヤまたは機能単位で分離する
7. Moduleに業務判断を書かない
```

> [!TIP]
> **まずはconstructor injectionで解決できる形を優先する。**
>
> `@Provides` は便利ですが、乱用すると生成責務がModuleに集まりすぎます。  
> 単純なクラスは `@Inject constructor` を基本にします。

### 🧠 ViewModelの定義

```kotlin
@HiltViewModel
class XxxViewModel @Inject constructor(
    reducer: XxxReducer,
    private val useCaseFacade: XxxUseCaseFacade,
    private val presentationFacade: XxxPresentationFacade
) : BaseViewModel<XxxUiState, XxxUiEvent, XxxUiMessage, XxxUiEffect>(
    initialState = XxxUiState.initial(),
    reducer = reducer
) {
    override suspend fun handleEvent(event: XxxUiEvent) {
        // Event処理
    }
}
```

Reducer、UseCase、Mapper、Formatter、Facadeはconstructor injection可能な
具象クラスであれば、原則として個別Module登録は不要です。

interfaceと実装の対応、Android framework型を使う生成処理、外部ライブラリBuilderだけを
Moduleへ登録します。

### 🧩 UseCaseの登録

UseCaseは、基本的に `@Inject constructor` で定義します。

```kotlin
class SaveSampleUseCase @Inject constructor(
    private val repository: SampleRepository
) {

    suspend operator fun invoke(
        input: SaveSampleInput
    ): AppResult<Unit> {
        return repository.save(
            Sample(
                id = SampleId(input.id),
                name = SampleName(input.name),
                enabled = input.enabled,
                updatedAt = dateTimeProvider.now().toInstant()
            )
        )
    }
}
```

この形であれば、Hilt Moduleに明示登録しなくても解決できます。

> [!IMPORTANT]
> **UseCaseはinterface化しすぎない。**
>
> 差し替え理由が明確でないUseCaseまでinterface化すると、  
> 構造が重くなります。  
> まずはclass + constructor injectionで十分です。

### 🗄 Repositoryの登録

Repositoryは、Application側からinterfaceとして参照し、  
Data / Infrastructure側で実装します。

#### interface

```kotlin
interface SampleRepository {

    suspend fun findById(
        id: SampleId
    ): AppResult<Sample?>

    suspend fun save(
        sample: Sample
    ): AppResult<Unit>
}
```

#### implementation

```kotlin
class SampleRepositoryImpl @Inject constructor(
    private val sampleDao: SampleDao
) : SampleRepository {

    override suspend fun findById(
        id: SampleId
    ): AppResult<Sample?> {
        return try {
            AppResult.Success(
                sampleDao.findById(id.value)?.toDomain()
            )
        } catch (exception: Exception) {
            AppResult.Failure(
                AppError.LocalStorage(
                    code = "SAMPLE-DB-FIND-001",
                    cause = exception
                )
            )
        }
    }

    override suspend fun save(
        sample: Sample
    ): AppResult<Unit> {
        return try {
            sampleDao.upsert(sample.toEntity())
            AppResult.Success(Unit)
        } catch (exception: Exception) {
            AppResult.Failure(
                AppError.LocalStorage(
                    code = "SAMPLE-DB-SAVE-001",
                    cause = exception
                )
            )
        }
    }
}
```

#### Module

interfaceと実装の紐づけは `@Binds` を使います。

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class SampleRepositoryModule {

    @Binds
    abstract fun bindSampleRepository(
        impl: SampleRepositoryImpl
    ): SampleRepository
}
```

> [!IMPORTANT]
> **interfaceと実装の対応はModuleで明示する。**
>
> UseCaseは `SampleRepository` を知ります。  
> `SampleRepositoryImpl` を直接知ってはいけません。

### 🌐 Gatewayの登録

外部API / SDK / デバイス接続などはGatewayとして隠蔽します。

#### interface

```kotlin
interface SampleGateway {

    suspend fun requestSample(
        id: SampleId
    ): AppResult<Sample>
}
```

#### implementation

```kotlin
class SampleGatewayImpl @Inject constructor(
    private val api: SampleApi,
    private val mapper: SampleResponseMapper
) : SampleGateway {

    override suspend fun requestSample(
        id: SampleId
    ): AppResult<Sample> {
        return try {
            val response = api.getSample(id.value)

            if (response.isSuccessful.not()) {
                return AppResult.Failure(
                    response.toApiError(
                        codePrefix = "SAMPLE-GET"
                    )
                )
            }

            val body = response.body()
                ?: return AppResult.Failure(
                    AppError.ApiInvalidResponse(
                        code = "SAMPLE-GET-EMPTY",
                        reason = "Response body is null"
                    )
                )

            mapper.map(body)
        } catch (exception: IOException) {
            AppResult.Failure(
                AppError.Network(
                    code = "SAMPLE-GET-NETWORK",
                    cause = exception
                )
            )
        }
    }
}
```

#### Module

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class SampleGatewayModule {

    @Binds
    abstract fun bindSampleGateway(
        impl: SampleGatewayImpl
    ): SampleGateway
}
```

> [!CAUTION]
> **UseCaseにRetrofit APIやSDKクラスを直接注入しない。**
>
> 外部仕様はGatewayに閉じ込め、  
> UseCaseからはアプリ内のinterfaceだけを見るようにします。

### 🧪 @Binds と @Provides の使い分け

| 使い方 | 使用するもの |
|---|---|
| interfaceに実装クラスを紐づける | `@Binds` |
| BuilderやFactoryが必要 | `@Provides` |
| Retrofit / Room / DataStoreなど生成手順がある | `@Provides` |
| 単純なclass生成 | `@Inject constructor` |
| 抽象型への紐づけ | `@Binds` |

### 🔗 @Binds の例

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class SampleRepositoryModule {

    @Binds
    abstract fun bindSampleRepository(
        impl: SampleRepositoryImpl
    ): SampleRepository
}
```

> [!TIP]
> **単純なinterface実装の紐づけは@Binds。**
>
> `@Provides` で `return SampleRepositoryImpl(...)` と書くより、  
> `@Binds` のほうが責務が明確です。

### 🏭 @Provides の例

RetrofitやRoomなど、生成手順が必要なものは `@Provides` を使います。

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideSampleApi(
        retrofit: Retrofit
    ): SampleApi {
        return retrofit.create(SampleApi::class.java)
    }
}
```

Roomの例：

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app.db"
        ).build()
    }

    @Provides
    fun provideSampleDao(
        database: AppDatabase
    ): SampleDao {
        return database.sampleDao()
    }
}
```

> [!CAUTION]
> **@Providesに業務判断を書かない。**
>
> Moduleは依存を作る場所です。  
> 業務条件や画面条件を入れてはいけません。

### 🗂 Module配置方針

現行プロジェクトのModule配置は次のとおりです。

```text
app/
  auth/AuthModule.kt
  database/DatabaseProvideModule.kt
  database/DatabaseBindModule.kt
  datastore/PreferenceDataStoreModule.kt
  network/NetworkModule.kt

core/
  di/CoreModule.kt
  storage/theme/ThemeModule.kt
  ui/theme/ThemeUiModule.kt

feature/
  sample/di/SampleModule.kt
  operationlog/data/di/OperationLogDataModule.kt
  legal/di/LegalDocumentModule.kt
  versioninfo/di/VersionInfoModule.kt
```

- Android framework型やBuilderを使う生成処理は `app` の `@Provides`
- Coreのinterfaceと標準Android実装は `core/platform` の `CoreModule`
- Theme設定の保存は `core/storage/theme/ThemeModule`、Composeテーマの解決は
  `core/ui/theme/ThemeUiModule`へ分ける
- Feature固有Repository / DataSourceはFeature Module

LicenseのUseCase / Facade / Mapperはconstructor injectionで解決できるため、
専用Moduleを持ちません。

> [!CAUTION]
> `CoreModule` や1つの `AppModule` にFeature依存を集めません。
> Moduleは「どの層・どの機能の依存を接続しているか」が名前と配置で分かる単位にします。

### 🔒 Scopeの考え方

HiltのScopeは、インスタンスの生存期間を決めます。

| Scope | 用途 |
|---|---|
| `@Singleton` | アプリ全体で1つ |
| `@ActivityRetainedScoped` | Activity再生成をまたいで保持 |
| `@ViewModelScoped` | ViewModel単位 |
| Scopeなし | 必要時に生成 |

基本方針：

```text
Repository / Gateway
  → 原則 Singleton または Scopeなし

UseCase
  → Scopeなし

Reducer
  → Scopeなし

ViewModel
  → @HiltViewModel

Retrofit / Room / DataStore
  → Singleton
```

> [!TIP]
> **迷ったらScopeなしから始める。**
>
> 共有する理由が明確なものだけ `@Singleton` を付けます。  
> 何でもSingletonにすると、状態共有バグの原因になります。

### 🧪 テスト時の差し替え

RepositoryやGatewayをinterfaceにしておくことで、テスト時にFakeへ差し替えやすくなります。

```kotlin
class FakeSampleRepository : SampleRepository {

    var sample: Sample? = null

    override suspend fun findById(
        id: SampleId
    ): AppResult<Sample?> {
        return AppResult.Success(sample)
    }

    override suspend fun save(
        sample: Sample
    ): AppResult<Unit> {
        this.sample = sample
        return AppResult.Success(Unit)
    }
}
```

> [!IMPORTANT]
> **外部依存はinterface越しにする。**
>
> Repository / Gatewayを差し替え可能にしておくことで、  
> UseCaseのUnit Testが書きやすくなります。

### 🚫 やってはいけないこと

#### ❌ ViewModelを手動生成する

```kotlin
val viewModel = SampleViewModel(
    saveUseCase = SaveSampleUseCase(...)
)
```

#### ✅ hiltViewModelで取得する

```kotlin
val viewModel: SampleViewModel = hiltViewModel()
```

#### ❌ UseCaseにRepository実装を直接注入する

```kotlin
class SaveSampleUseCase @Inject constructor(
    private val repository: SampleRepositoryImpl
)
```

#### ✅ interfaceを注入する

```kotlin
class SaveSampleUseCase @Inject constructor(
    private val repository: SampleRepository
)
```

#### ❌ AppModuleに全部詰め込む

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // NetworkもDBもRepositoryもGatewayも全部ここ
}
```

#### ✅ レイヤ / 機能単位でModuleを分ける

```text
NetworkModule
DatabaseModule
SampleRepositoryModule
SampleGatewayModule
```

#### ❌ Moduleで業務条件を分岐する

```kotlin
@Provides
fun provideRepository(): SampleRepository {
    return if (isSpecialUser()) {
        SpecialSampleRepository()
    } else {
        SampleRepositoryImpl()
    }
}
```

#### ✅ 業務判断はUseCaseへ置く

```kotlin
class ResolveSampleUseCase @Inject constructor(
    private val repository: SampleRepository
) {
    // 業務条件はUseCaseまたはDomainで判断する
}
```

### 🎯 11章まとめ

Hiltでは、以下を守ります。

```text
ViewModel
  → @HiltViewModel

UseCase
  → @Inject constructor

Repository / Gateway
  → interface + implementation

interfaceと実装の紐づけ
  → @Binds

生成手順が必要なもの
  → @Provides

Module
  → レイヤ / 機能単位で分離
```

> [!IMPORTANT]
> **「Hilt Moduleは依存の地図である」**
>
> Moduleを見れば、どのinterfaceにどの実装が対応し、  
> どのレイヤの責務なのかが分かる状態にします。

---

## 🧠 12. Event / Message / Effect 分割の判断基準

### 🎯 この章の目的

この章では、Androidアプリで実装時に迷いやすい  
**UiEvent / UiMessage / UiEffect / UiState の分割基準** を定義します。

MVIベースの設計では、同じ「何かが起きた」という情報でも、  
それが **ユーザー操作なのか、処理結果なのか、状態なのか、副作用なのか** を分けて扱います。

```text
UiEvent
  → ユーザー操作・画面起点

UiMessage
  → State変更の理由・処理結果

UiState
  → 画面に残る事実

UiEffect
  → 一度だけ実行する副作用
```

> [!IMPORTANT]
> **「何が起きたか」ではなく「どこで扱うべきか」で分ける。**
>
> 名前が似ていても、責務が違えば置き場所も変わります。

### 📌 この章はこういうときに見る

- これはUiEventかUiMessageか迷ったとき
- Dialog表示をStateに持つかEffectにするか迷ったとき
- 入力値更新をMessageにするかState直接更新するか迷ったとき
- 処理成功をEventにしてよいか迷ったとき
- 一度だけ実行する処理の扱いに迷ったとき
- ViewModel内の処理分岐が増えすぎて整理したいとき

### 🧭 判断の基本

まずは、以下の順で考えます。

```text
1. ユーザー操作か？
   → UiEvent

2. State変更の理由か？
   → UiMessage

3. 画面に残る事実か？
   → UiState

4. 一度だけ実行する副作用か？
   → UiEffect
```

> [!TIP]
> **迷ったら「再描画されても意味が変わらないか？」を考える。**
>
> 再描画後も保持すべきならUiState。  
> 再実行されたら困るならUiEffectです。

### ⚡ UiEventにするもの

UiEventは、ユーザー操作や画面起点の処理を表します。

#### UiEventにするもの

- ボタン押下
- TextField入力変更
- プルダウン選択
- 画面初期表示
- 再読み込み操作
- 戻るボタン押下
- DialogのOK / Cancel / Retry
- 一覧行クリック
- チェックボックス変更

```kotlin
sealed interface SampleUiEvent : UiEvent {

    data object Initialize : SampleUiEvent

    data class NameChanged(
        val value: String
    ) : SampleUiEvent

    data object SaveClicked : SampleUiEvent

    data object RetryClicked : SampleUiEvent

    data class ItemClicked(
        val id: String
    ) : SampleUiEvent
}
```

> [!IMPORTANT]
> **UiEventは「入力」である。**
>
> ユーザーや画面からViewModelへ入ってくるものをUiEventとして扱います。

### 📨 UiMessageにするもの

UiMessageは、State変更の理由や処理結果を表します。

#### UiMessageにするもの

- 読み込み開始
- 読み込み成功
- 読み込み失敗
- 保存開始
- 保存成功
- 保存失敗
- 入力値更新結果
- Validation結果
- 選択状態更新
- 一覧取得結果
- エラー状態反映

```kotlin
sealed interface SampleUiMessage : UiMessage {

    data object LoadStarted : SampleUiMessage

    data class LoadSucceeded(
        val sample: Sample
    ) : SampleUiMessage

    data class LoadFailed(
        val error: UiError
    ) : SampleUiMessage

    data class NameUpdated(
        val value: String,
        val errorMessage: String?
    ) : SampleUiMessage

    data object SaveStarted : SampleUiMessage

    data object SaveSucceeded : SampleUiMessage

    data class SaveFailed(
        val error: UiError
    ) : SampleUiMessage
}
```

> [!IMPORTANT]
> **UiMessageは「State変更の材料」である。**
>
> UiStateをどう変えるかはReducerが決めます。  
> UiMessageは、その判断に必要な材料を持ちます。

### 🧱 UiStateにするもの

UiStateは、画面に残る事実を表します。

#### UiStateにするもの

- 入力値
- 選択値
- 一覧データ
- ローディング状態
- エラーメッセージ
- ボタン活性
- 表示切替条件
- タブ選択状態
- 検索条件
- ソート条件

```kotlin
data class SampleUiState(
    val isLoading: Boolean,
    val name: String,
    val nameErrorMessage: String?,
    val items: List<SampleItemUiState>,
    val selectedItemId: String?
) : UiState {
    val canSave: Boolean
        get() = !isLoading &&
            name.isNotBlank() &&
            nameErrorMessage == null
}
```

> [!TIP]
> **画面に残るものはUiState。**
>
> 再描画・画面復元・State再購読後も意味がある情報はUiStateに置きます。

### 💥 UiEffectにするもの

UiEffectは、一度だけ実行する副作用を表します。

#### UiEffectにするもの

- 画面遷移
- 戻る
- Dialog表示
- Snackbar表示
- Toast表示
- 外部アプリ起動
- 権限要求
- ファイル共有
- キーボードを閉じる
- スクロール位置移動
- フォーカス移動

```kotlin
sealed interface SampleUiEffect : UiEffect {

    data object NavigateBack : SampleUiEffect

    data class NavigateToDetail(
        val id: String
    ) : SampleUiEffect

    data class ShowDialog(
        val title: String,
        val message: String
    ) : SampleUiEffect

    data class ShowSnackbar(
        val message: String
    ) : SampleUiEffect
}
```

> [!CAUTION]
> **再実行されたら困るものはUiEffect。**
>
> `showDialog = true` や `snackbarMessage` をUiStateに持たせると、  
> 再描画や再購読で再実行される可能性があります。

### 🧭 判断表

| やりたいこと | 置き場所 | 理由 |
|---|---|---|
| 保存ボタンが押された | UiEvent | ユーザー操作 |
| 保存処理を開始した | UiMessage | State変更理由 |
| 保存中である | UiState | 画面に残る状態 |
| 保存に成功した | UiMessage | State変更理由 |
| 保存しましたSnackbarを出す | UiEffect | 単発通知 |
| 保存失敗Dialogを出す | UiEffect | 単発通知 |
| 入力値が変わった | UiEvent | ユーザー操作 |
| 入力値をStateに反映する | UiMessage | State変更理由 |
| 入力エラーがある | UiState | 画面に残る状態 |
| 詳細画面へ遷移する | UiEffect | 単発副作用 |
| 一覧行が押された | UiEvent | ユーザー操作 |
| 選択中IDを保持する | UiState | 画面に残る状態 |
| Retryボタンが押された | UiEvent | ユーザー操作 |
| Retry用Dialogを出す | UiEffect | 単発通知 |

### 🧪 例：保存ボタン押下

```text
SaveClicked
  → UiEvent

SaveStarted
  → UiMessage

isLoading = true
  → UiState

SaveSucceeded
  → UiMessage

ShowSnackbar("保存しました")
  → UiEffect
```

```kotlin
override suspend fun handleEvent(event: SampleUiEvent) {
    when (event) {
        SampleUiEvent.SaveClicked -> {
            handleSave()
        }
    }
}
```

```kotlin
private fun handleSave() {
    viewModelScope.launch {
        dispatch(SampleUiMessage.SaveStarted)

        when (val result = saveUseCase(createInput())) {
            is AppResult.Success -> {
                dispatch(SampleUiMessage.SaveSucceeded)

                emitEffect(
                    SampleUiEffect.ShowSnackbar("保存しました")
                )
            }

            is AppResult.Failure -> {
                val uiError = errorMapper.map(result.error)

                dispatch(
                    SampleUiMessage.SaveFailed(uiError)
                )

                emitEffect(
                    SampleUiEffect.ShowDialog(
                        title = uiError.title,
                        message = uiError.message
                    )
                )
            }
        }
    }
}
```

> [!CAUTION]
> **サンプル内の直書き文言は説明用です。**
>
> 実装時は、必要に応じて `strings.xml` または `StringProvider` 経由に置き換えてください。

### 🧪 例：一覧行クリック

```text
ItemClicked(id)
  → UiEvent

NavigateToDetail(id)
  → UiEffect
```

```kotlin
sealed interface SampleUiEvent : UiEvent {

    data class ItemClicked(
        val id: String
    ) : SampleUiEvent
}
```

```kotlin
private fun handleItemClicked(id: String) {
    emitEffect(
        SampleUiEffect.NavigateToDetail(id)
    )
}
```

> [!TIP]
> **一覧行クリック自体はUiEvent、遷移要求はUiEffect。**

### 🧪 例：入力値変更

```text
NameChanged(value)
  → UiEvent

NameUpdated(value, error)
  → UiMessage

name / nameErrorMessage
  → UiState
```

```kotlin
private fun handleNameChanged(value: String) {
    val errorMessage =
        validationPolicy.validateName(value).toMessageOrNull()

    dispatch(
        SampleUiMessage.NameUpdated(
            value = value,
            errorMessage = errorMessage
        )
    )
}
```

### 🚫 よくある間違い

#### ❌ 処理結果をUiEventにする

```kotlin
sealed interface SampleUiEvent : UiEvent {

    data object SaveSucceeded : SampleUiEvent
}
```

#### ✅ 処理結果はUiMessageにする

```kotlin
sealed interface SampleUiMessage : UiMessage {

    data object SaveSucceeded : SampleUiMessage
}
```

#### ❌ Dialog表示フラグをUiStateに持つ

```kotlin
data class SampleUiState(
    val showDialog: Boolean
) : UiState
```

#### ✅ Dialog表示はUiEffectにする

```kotlin
data class ShowDialog(
    val title: String,
    val message: String
) : SampleUiEffect
```

#### ❌ UiMessageにユーザー操作を書く

```kotlin
sealed interface SampleUiMessage : UiMessage {

    data object SaveButtonClicked : SampleUiMessage
}
```

#### ✅ ユーザー操作はUiEventにする

```kotlin
sealed interface SampleUiEvent : UiEvent {

    data object SaveClicked : SampleUiEvent
}
```

### 🎯 12章まとめ

Event / Message / Effect は、以下の基準で分けます。

```text
UiEvent
  → ユーザー操作・画面起点

UiMessage
  → State変更の理由・処理結果

UiState
  → 画面に残る事実

UiEffect
  → 一度だけ実行する副作用
```

> [!IMPORTANT]
> **「入力」「状態変更」「画面状態」「副作用」を混ぜない。**
>
> 分割が曖昧になると、ViewModelが肥大化し、  
> State更新やEffect発行の流れが追えなくなります。

---

## 🧪 13. テスト方針

### 🎯 この章の目的

この章では、Androidアプリにおける  
**Unit Test / UI Test の対象・優先順位・考え方** を定義します。

この設計では、ComposableやViewModelに業務ロジックを集めず、  
UseCase / Domain / Reducer / Mapper / Policy へ処理を分離します。

そのため、テストでは **画面そのもの** よりも、  
**画面の裏側にある判断・変換・状態遷移** を優先して守ります。

```text
UseCase
  → 業務処理の入出力を固定する

Domain / Policy
  → 業務ルールを固定する

Reducer
  → UiMessageからUiStateへの状態遷移を固定する

Mapper / Resolver
  → エラーや表示モデルへの変換を固定する

ViewModel
  → 原則レビュー中心。必要に応じて結合的に確認する

Composable
  → 原則レビュー中心。必要に応じてCompose UI Testを行う
```

> [!IMPORTANT]
> **「Unit Testは画面ではなくロジックを守る」**
>
> ViewModelやComposableを無理に厚くテストするのではなく、  
> ロジックをUseCase / Reducer / Mapperへ逃がして、そこをテストします。

### 📌 この章はこういうときに見る

- Unit Testを書く対象に迷ったとき
- ViewModel Testを書くべきか迷ったとき
- Compose UI Testを書くべきか迷ったとき
- UseCase / Reducer / Mapper のテスト観点を確認したいとき
- Fake / Mock の使い分けに迷ったとき
- レビュー時にテスト不足を確認したいとき

### 🧭 基本方針

このアーキテクチャでは、判断と変換をViewModel / Composableから分離し、
次の順でUnit Testを優先します。

1. Domain Policy / Value Object
2. UseCase
3. Reducer
4. Mapper / Formatter / Resolver
5. Repositoryの変換・エラー境界
6. 必要なViewModel Event flow
7. 重要導線だけCompose UI Test

現行プロジェクトには、次のテストがあります。

- `AppResultTest`
- `BaseViewModelTest`（Event Queueの順序処理）
- `ApiResponseHandlerTest`
- `DatabaseExecutorTest`（CancellationExceptionの再送出を含む）
- `ThemeDataStoreTest`
- `ThemeModeResolverTest`
- Sample greeting / home header / settings reducer / theme mapper
- OperationLog UseCase / Mapper
- License UseCase / ItemMapper
- Legal UseCase / raw resource mapper / reducer
- VersionInfo UseCase / Mapper / Reducer / external links

ViewModelやComposableを厚くテストすることでロジックを守るのではなく、
Policy / UseCase / Reducer / Mapperへ分離して小さく固定します。

KDocから設計リファレンスを生成する場合は、プロジェクト直下で次を実行します。

```powershell
.\gradlew.bat :app:dokkaHtml
```

出力先は `app/build/dokka/html/index.html` です。

### 🧪 テスト優先順位

| 優先度 | 対象 | 理由 |
|---|---|---|
| 高 | UseCase | 業務処理の入口であり、失敗時の影響が大きい |
| 高 | Domain / Policy | 業務ルール・Validationを守る |
| 高 | Reducer | 状態遷移の正しさを固定できる |
| 中 | Mapper / Resolver | DTO / Entity / Error / UiState変換を固定できる |
| 中 | RepositoryImpl | Entity変換・例外変換を確認する |
| 低 | ViewModel | ロジックを薄く保てばレビュー中心でよい |
| 低 | Composable | 表示責務に留め、必要な場合のみUI Test |

> [!IMPORTANT]
> **テスト対象は「壊れると困る判断」から選ぶ。**
>
> 画面の見た目よりも、保存可否・検証結果・状態遷移・エラー変換を優先します。

### 🧩 UseCase Test

UseCase Testでは、入力に対して期待する `AppResult` が返ることを確認します。

```kotlin
class SaveSampleUseCaseTest {

    private lateinit var repository: FakeSampleRepository
    private lateinit var validationPolicy: SampleValidationPolicy
    private lateinit var useCase: SaveSampleUseCase

    @Before
    fun setUp() {
        repository = FakeSampleRepository()
        validationPolicy = SampleValidationPolicy()
        useCase = SaveSampleUseCase(
            sampleRepository = repository,
            validationPolicy = validationPolicy
        )
    }

    @Test
    fun `valid input returns success`() = runTest {
        val input = SaveSampleInput(
            id = "sample-001",
            name = "sample",
            enabled = true
        )

        val result = useCase(input)

        assertTrue(result is AppResult.Success)
        assertEquals(
            "sample-001",
            repository.savedSample?.id?.value
        )
    }

    @Test
    fun `blank name returns validation error`() = runTest {
        val input = SaveSampleInput(
            id = "sample-001",
            name = "",
            enabled = true
        )

        val result = useCase(input)

        assertTrue(result is AppResult.Failure)

        val error = (result as AppResult.Failure).error
        assertTrue(error is AppError.Validation)
    }
}
```

> [!TIP]
> **UseCase Testでは、成功系より異常系を厚めに見る。**
>
> 保存できることだけでなく、保存してはいけない条件で失敗することを確認します。

### 🧪 Reducer Test

Reducer Testでは、現在のUiStateとUiMessageから、  
期待するUiStateが返ることを確認します。

```kotlin
class SampleReducerTest {

    private lateinit var reducer: SampleReducer

    @Before
    fun setUp() {
        reducer = SampleReducer()
    }

    @Test
    fun `LoadStarted sets loading true`() {
        val currentState = SampleUiState.initial()

        val actual = reducer.reduce(
            currentState = currentState,
            message = SampleUiMessage.LoadStarted
        )

        assertTrue(actual.isLoading)
        assertNull(actual.screenErrorMessage)
    }

    @Test
    fun `LoadSucceeded updates sample fields`() {
        val currentState = SampleUiState.initial()

        val sample = Sample(
            id = SampleId("sample-001"),
            name = SampleName("sample"),
            enabled = true,
            updatedAt = Instant.parse("2026-01-01T00:00:00Z")
        )

        val actual = reducer.reduce(
            currentState = currentState,
            message = SampleUiMessage.LoadSucceeded(sample)
        )

        assertFalse(actual.isLoading)
        assertEquals("sample-001", actual.id)
        assertEquals("sample", actual.name)
        assertTrue(actual.enabled)
    }

    @Test
    fun `LoadFailed sets screen error`() {
        val currentState = SampleUiState.initial()

        val actual = reducer.reduce(
            currentState = currentState,
            message = SampleUiMessage.LoadFailed(
                error = UiError(
                    code = "ERROR-001",
                    title = "エラー",
                    message = "読み込みに失敗しました"
                )
            )
        )

        assertFalse(actual.isLoading)
        assertEquals(
            "読み込みに失敗しました",
            actual.screenErrorMessage
        )
    }
}
```

> [!IMPORTANT]
> **Reducer Testはコスパが高い。**
>
> 入力Messageに対してStateがどう変わるかを固定できるため、  
> 画面の状態管理ミスを早い段階で検出できます。

### 🧪 Mapper / Resolver Test

Mapper / Resolverでは、変換結果をテストします。

#### DTO → Domain

```kotlin
class SampleResponseMapperTest {

    private lateinit var mapper: SampleResponseMapper

    @Before
    fun setUp() {
        mapper = SampleResponseMapper()
    }

    @Test
    fun `valid response maps to domain`() {
        val response = SampleResponse(
            id = "sample-001",
            name = "sample",
            enabled = true,
            updatedAt = "2026-01-01T00:00:00Z"
        )

        val result = mapper.map(response)

        assertTrue(result is AppResult.Success)

        val sample = (result as AppResult.Success).value
        assertEquals("sample-001", sample.id.value)
        assertEquals("sample", sample.name.value)
    }

    @Test
    fun `null id returns invalid response error`() {
        val response = SampleResponse(
            id = null,
            name = "sample",
            enabled = true,
            updatedAt = "2026-01-01T00:00:00Z"
        )

        val result = mapper.map(response)

        assertTrue(result is AppResult.Failure)
        assertTrue(
            (result as AppResult.Failure).error is AppError.ApiInvalidResponse
        )
    }
}
```

#### RawConfig → ResolvedConfig

```kotlin
class SampleConfigResolverTest {

    private lateinit var resolver: SampleConfigResolver

    @Before
    fun setUp() {
        resolver = SampleConfigResolver()
    }

    @Test
    fun `null values are resolved with default values`() {
        val rawConfig = RawSampleConfig(
            featureEnabled = null,
            maxItemCount = null,
            apiTimeoutSeconds = null
        )

        val actual = resolver.resolve(rawConfig)

        assertFalse(actual.featureEnabled)
        assertEquals(100, actual.maxItemCount)
        assertEquals(30_000L, actual.apiTimeoutMillis)
    }

    @Test
    fun `out of range values are clamped`() {
        val rawConfig = RawSampleConfig(
            featureEnabled = true,
            maxItemCount = 9999,
            apiTimeoutSeconds = 999
        )

        val actual = resolver.resolve(rawConfig)

        assertEquals(500, actual.maxItemCount)
        assertEquals(120_000L, actual.apiTimeoutMillis)
    }
}
```

> [!TIP]
> **Mapper / Resolverは境界値をテストする。**
>
> null、範囲外、不正形式、欠損値を優先して確認します。

### 🧪 ViewModel Testの扱い

ViewModelは、原則として **ロジックを薄く保ち、レビュー中心** とします。

ただし、以下の場合はViewModel Testを検討します。

```text
ViewModel内で複数UseCaseの呼び出し順を制御している
UiEffect発行条件が複雑
初期表示処理が複雑
State更新とEffect発行の組み合わせが重要
```

> [!CAUTION]
> **ViewModel Testを書きたくなるほど複雑なら、まず責務分離を疑う。**
>
> ViewModelがUseCase / Reducer / Mapperの責務を持っていないか確認してください。

### 🖼 Compose UI Testの扱い

Composableは、原則として表示責務に留めます。  
そのため、すべてのComposableにUI Testを書く必要はありません。

UI Testを検討するのは、以下のような場合です。

```text
入力内容によって表示項目が大きく切り替わる
重要なボタン活性条件がある
エラーメッセージ表示が重要
一覧表示の有無が重要
Navigation起点の操作が重要
```

> [!IMPORTANT]
> **Compose UI Testは、見た目の細部ではなく重要な表示条件を守る。**
>
> 色・余白・フォントサイズなどを細かくテストしすぎると、  
> UI変更のたびにテスト修正が必要になります。

### 🧪 Fake / Mock の使い分け

| 使い方 | 推奨 |
|---|---|
| 戻り値を制御したい | Fake |
| 呼ばれたことを検証したい | Mock |
| 呼ばれていないことを検証したい | Mock |
| 複雑な外部I/Oの代替 | Fake |
| UseCase Test | Fake中心 |
| ViewModel Test | Fake / Mock併用 |

#### Fake Repository例

```kotlin
class FakeSampleRepository : SampleRepository {

    var savedSample: Sample? = null
    var findByIdResult: AppResult<Sample?> =
        AppResult.Success(null)

    override suspend fun findById(
        id: SampleId
    ): AppResult<Sample?> {
        return findByIdResult
    }

    override suspend fun findAll(): AppResult<List<Sample>> {
        return AppResult.Success(emptyList())
    }

    override suspend fun save(
        sample: Sample
    ): AppResult<Unit> {
        savedSample = sample
        return AppResult.Success(Unit)
    }

    override suspend fun delete(
        sample: Sample
    ): AppResult<Unit> {
        return AppResult.Success(Unit)
    }
}
```

> [!TIP]
> **UseCase TestではFakeを基本にする。**
>
> Mockだらけにすると、テストが実装詳細に寄りすぎる場合があります。

### 🚫 やってはいけないこと

#### ❌ ViewModelにロジックを集めてViewModel Testで頑張る

```text
ViewModelにValidationも変換も保存可否も書く
→ ViewModel Testが巨大化する
```

#### ✅ ロジックをUseCase / Reducer / Mapperへ分離してテストする

```text
Validation
  → Policy / UseCase Test

State更新
  → Reducer Test

変換
  → Mapper Test
```

#### ❌ Composableの細かい見た目を過剰にテストする

```text
色
余白
フォントサイズ
文言の一字一句
```

#### ✅ 重要な表示条件をテストする

```text
Loading時にProgressが出る
Error時にエラーが出る
入力不正時に保存ボタンが無効
```

### 🎯 13章まとめ

テスト方針では、以下を守ります。

```text
UseCase
  → 業務処理を守る

Domain / Policy
  → 業務ルールを守る

Reducer
  → 状態遷移を守る

Mapper / Resolver
  → 変換処理を守る

ViewModel
  → 原則レビュー中心。複雑なら責務分離を見直す

Composable
  → 原則レビュー中心。重要条件のみUI Test
```

> [!IMPORTANT]
> **「テストしやすい場所にロジックを置く」**
>
> ViewModelやComposableを無理にテストするのではなく、  
> UseCase / Reducer / Mapperへ分離し、そこを重点的にテストします。

---

## 🔍 14. レビュー観点

### 🎯 この章の目的

この章では、Androidアプリ実装に対する  
**レビュー観点** を定義します。

レビューでは、個人の好みや書き方のクセではなく、  
責務分離・状態管理・副作用制御・依存方向・テスト容易性を確認します。

```text
Composable
  → 表示とUiEvent通知だけか

ViewModel
  → UseCase呼び出し・Message dispatch・Effect発行に留まっているか

UseCase
  → 業務処理の手順に集中しているか

Repository / Gateway
  → 外部I/Oを隠蔽しているか

Reducer
  → 純粋なState計算になっているか

UiState / UiEffect
  → 画面状態と単発副作用が分離されているか
```

> [!IMPORTANT]
> **レビューは「好み」ではなく「観点」で行う。**
>
> 何となく読みにくい、ではなく、  
> どの責務・依存・状態管理が崩れているかを指摘します。

### 📌 この章はこういうときに見る

- PR / MRレビューを行うとき
- 自分の実装をセルフチェックしたいとき
- ViewModelが太くなっていないか確認したいとき
- Composableに処理を書きすぎていないか確認したいとき
- Repository / Gateway / UseCaseの責務が混ざっていないか確認したいとき
- レビューコメントの書き方に迷ったとき

### 🧭 全体レビュー観点

| 観点 | 確認内容 |
|---|---|
| 責務分離 | Composable / ViewModel / UseCase / Repository / Gateway の責務が混ざっていないか |
| 状態管理 | UiStateが画面に残る状態だけを持っているか |
| 副作用制御 | Navigation / Dialog / Snackbar がUiEffectになっているか |
| 依存方向 | Presentation → Application → Domain / Data interface の方向になっているか |
| エラー処理 | Exception / AppError / UiError が分離されているか |
| テスト容易性 | UseCase / Reducer / Mapperがテスト可能な形になっているか |
| 命名 | XxxUiState / XxxUiEvent / XxxUseCase などの命名が揃っているか |
| DI | Hilt Moduleが適切に分離されているか |

### 🖼 Composableレビュー観点

#### 確認すること

```text
□ UiStateを表示しているだけか
□ ユーザー操作をUiEventとして通知しているか
□ UseCase / Repository / Gatewayを呼んでいないか
□ 業務判断を書いていないか
□ Navigationを直接実行していないか
□ エラー文言を組み立てていないか
□ 業務的な入力値をrememberだけで持っていないか
```

#### 指摘例

```text
このComposable内で入力値の業務バリデーションを行っています。
画面はUiStateの表示とUiEvent通知に留め、
ValidationPolicyまたはUseCaseへ分離した方がよさそうです。
```

> [!CAUTION]
> **Composableに「判断」が増えてきたら要注意。**
>
> 表示条件はUiStateから導出し、業務判断はUseCase / Domainへ寄せます。

### 🧭 Routeレビュー観点

```text
□ hiltViewModelでViewModelを取得しているか
□ collectAsStateWithLifecycleでUiStateを購読しているか
□ 初期EventをviewModel.sendEventへ送っているか
□ Effect購読を開始してから初期Eventを送信しているか
□ ScreenへviewModel::sendEventを渡しているか
□ NavControllerをScreen / ViewModelへ渡していないか
□ Dialog / Snackbar / Drawerの一時状態をRouteで扱っているか
□ Route内にUseCase呼び出しや業務判断がないか
```

Drawer開閉、SnackbarHostState、Dialog表示中、Focus、Scrollなどの
短命なUI状態はCompose local stateで構いません。  
業務上意味のある入力・選択・読込結果をlocal stateへ逃がしていないかを見ます。

### 🧠 ViewModelレビュー観点

#### 確認すること

```text
□ BaseViewModelを継承しているか
□ RouteからsendEventで受けたUiEventをhandleEventで処理しているか
□ UseCaseを呼び出しているか
□ AppResultをUiMessage / UiEffectへ変換しているか
□ State更新はReducer経由になっているか
□ Repository / Gateway / DAO / Retrofit APIを直接呼んでいないか
□ NavControllerを保持していないか
□ Contextを保持していないか
□ エラー文言を直接組み立てていないか
□ handleEventが分岐の入口に留まり、処理本体へ分割されているか
□ 再利用可能な業務ルールや表示変換をprivate拡張関数にしていないか
□ 依存が多い場合はUseCaseFacade / PresentationFacadeで整理しているか
```

#### 指摘例

```text
ViewModelからRepositoryを直接呼んでいるため、
画面制御とデータ取得処理の責務が近くなっています。

UseCaseを追加して、ViewModelはUseCase呼び出しと
UiMessage / UiEffectへの変換に留めるのがよさそうです。
```

> [!IMPORTANT]
> **ViewModelは交通整理役に留まっているかを見る。**
>
> ViewModelに業務判断や外部I/Oが入り始めたら、責務分離の見直し対象です。

### 🧩 UseCaseレビュー観点

#### 確認すること

```text
□ 1つのUseCaseが1つの処理目的に集中しているか
□ UiStateを受け取っていないか
□ UiEffectを発行していないか
□ UI文言を返していないか
□ Repository / Gateway interfaceを使っているか
□ RepositoryImpl / GatewayImplを直接参照していないか
□ AppResultで成功 / 失敗を返しているか
□ 業務判断がRepository / Gatewayに漏れていないか
```

#### 指摘例

```text
UseCaseの入力にUiStateをそのまま渡しています。
UiStateはPresentation層の表示状態なので、
SaveSampleInputのようなUseCase専用Inputへ変換して渡した方がよさそうです。
```

> [!CAUTION]
> **UseCaseがUIを知っていたら責務違反。**
>
> UseCaseは画面名・文言・NavController・UiStateを知ってはいけません。

### 🗄 Repository / Gatewayレビュー観点

#### Repository

```text
□ Room / DataStore / Fileの詳細を隠蔽しているか
□ Entity / RawConfigを上位層へ漏らしていないか
□ Entity / Domain変換を行っているか
□ 例外をAppErrorへ変換しているか
□ 業務判断を書いていないか
```

#### Gateway

```text
□ Retrofit API / SDK / 外部デバイスの詳細を隠蔽しているか
□ Request / Response DTOを上位層へ漏らしていないか
□ DTO / Domain変換を行っているか
□ HTTP error / Network errorをAppErrorへ変換しているか
□ Retry判断を勝手に行っていないか
```

#### 指摘例

```text
Response DTOをUseCaseへ返しているため、
API仕様がApplication層へ漏れています。

GatewayImpl内でDomain Modelへ変換し、
UseCaseにはSampleを返す形にした方がよさそうです。
```

> [!IMPORTANT]
> **保存形式・通信形式を上位層へ漏らさない。**
>
> Entity / DTO / Preferences Key はData / Infrastructure層に閉じ込めます。

### 🧮 Reducerレビュー観点

#### 確認すること

```text
□ 現在State + UiMessage → 新State の形になっているか
□ 副作用を持っていないか
□ suspend関数になっていないか
□ UseCase / Repository / Gatewayを呼んでいないか
□ Stateをcopyで更新しているか
□ 同じ入力に対して同じ出力を返すか
```

#### 指摘例

```text
Reducer内でRepositoryを呼び出しています。
ReducerはState計算だけを行う純粋関数にし、
Repository呼び出しはUseCase側へ移動した方がよさそうです。
```

> [!CAUTION]
> **Reducerに副作用が入ると状態遷移が壊れる。**
>
> Reducerはテスト可能な純粋関数として保ちます。

### 🧱 UiState / UiEffectレビュー観点

#### UiState

```text
□ 画面に残る事実だけを持っているか
□ 入力値・選択値・一覧・Loading・エラー状態を表しているか
□ Navigation / Dialog / Snackbar要求を持っていないか
□ Entity / DTOを持っていないか
□ computed propertyで表示条件を整理しているか
```

#### UiEffect

```text
□ 一度だけ実行する副作用を表しているか
□ Navigation / Dialog / Snackbar / Toastを扱っているか
□ Stateに持つべき情報をEffectにしていないか
□ Effect Handler側で実行されているか
```

#### 指摘例

```text
UiStateにshowDialogを持たせていますが、
Dialog表示は一度だけ実行する副作用なのでUiEffectにした方がよさそうです。
```

### 🧬 Hiltレビュー観点

```text
□ ViewModelに@HiltViewModelが付いているか
□ UseCaseは@Inject constructorで生成できるか
□ Repository / Gateway interfaceに@Binds登録があるか
□ Retrofit / Room / DataStoreは@Providesで生成しているか
□ Moduleが巨大化していないか
□ AppModuleに全部詰め込んでいないか
□ UseCaseにRepositoryImpl / GatewayImplを直接注入していないか
```

#### 指摘例

```text
UseCaseにSampleRepositoryImplを直接注入しています。
Application層は実装クラスではなくSampleRepository interfaceに依存させ、
Hilt ModuleでSampleRepositoryImplをbindする形にした方がよさそうです。
```

### 🧾 レビューコメントの書き方

レビューコメントは、以下の形にすると伝わりやすいです。

```text
1. どこが問題か
2. なぜ問題か
3. どう直すとよいか
```

#### NG例

```text
なんかViewModelが微妙です。
```

#### OK例

```text
ViewModelからRepository実装を直接呼んでいるため、
Presentation層がData層の具体実装に依存しています。

UseCaseを追加し、ViewModelはUseCase呼び出しに留める形にすると、
責務分離とテスト容易性が保ちやすくなります。
```

> [!TIP]
> **人格ではなく構造を指摘する。**
>
> 「書き方が悪い」ではなく、  
> 「責務が混ざっている」「依存方向が崩れている」と構造で伝えます。

### ✅ セルフチェックリスト

実装後は、以下を確認します。

```text
□ ComposableはUiState表示とUiEvent通知だけになっている
□ RouteがViewModel接続・State購読・Effect購読を担当している
□ ViewModelはUseCase呼び出し・Message dispatch・Effect発行に留まっている
□ UseCaseが業務処理の手順を担当している
□ Repository / Gatewayが外部I/Oを隠蔽している
□ Entity / DTO / RawConfigがUiStateへ漏れていない
□ UiStateに単発イベントが入っていない
□ UiEffectでNavigation / Dialog / Snackbarを扱っている
□ Reducerが純粋関数になっている
□ AppResult / AppError / UiErrorが分離されている
□ Hilt Moduleがレイヤ / 機能単位で分かれている
□ UseCase / Reducer / Mapperのテストが書ける構造になっている
```

### 🎯 14章まとめ

レビューでは、以下を見る。

```text
Composable
  → 表示と入力だけか

Route
  → 接続だけか

ViewModel
  → 司令塔に留まっているか

UseCase
  → 業務処理に集中しているか

Repository / Gateway
  → 外部I/Oを隠蔽しているか

Reducer
  → 純粋関数か

UiState
  → 画面に残る事実だけか

UiEffect
  → 単発副作用を扱っているか

Hilt
  → 依存の地図になっているか

Test
  → UseCase / Reducer / Mapperを守れているか
```

> [!IMPORTANT]
> **レビューの目的は、構造を守ること。**
>
> 個人の好みではなく、  
> 責務分離・依存方向・状態管理・副作用制御の観点で確認します。

---

## ☠️ 15. アンチパターン

### 🎯 この章の目的

この章では、Androidアプリ実装で避けるべき  
**設計上のアンチパターン** を定義します。

ここで扱うのは、単なる「動かない実装」ではありません。  
動くかもしれないが、責務分離・変更容易性・テスト容易性を壊す実装です。

```text
アンチパターン
  → 設計として何が壊れているか

ハマるポイント
  → 実装時にどういう症状が出るか
```

> [!IMPORTANT]
> **「動くからOK」ではない。**
>
> 責務が混ざった実装は、後から修正・レビュー・テストが難しくなります。

### 1. Composableに業務ロジックを書く

#### ❌ NG

```kotlin
Button(
    onClick = {
        if (name.length > 50) {
            errorMessage = "50文字以内で入力してください"
            return@Button
        }

        repository.save(name)
    }
)
```

#### 何が壊れているか

```text
Composableが業務判断をしている
Composableが保存処理を知っている
Validationが再利用できない
UI変更と業務変更が混ざる
```

#### ✅ OK

```kotlin
Button(
    onClick = {
        onEvent(SampleUiEvent.SaveClicked)
    }
)
```

> [!IMPORTANT]
> **Composableは表示とUiEvent通知だけ。**

### 2. ViewModelがRepository / DAO / APIを直接呼ぶ

#### ❌ NG

```kotlin
class SampleViewModel @Inject constructor(
    private val sampleDao: SampleDao,
    private val sampleApi: SampleApi
) : ViewModel()
```

#### 何が壊れているか

```text
ViewModelが外部I/Oを知っている
UseCaseの責務が消える
Presentation層がData層に近づく
ViewModelが肥大化する
BaseViewModelの標準フローから外れる
```

#### ✅ OK

```kotlin
@HiltViewModel
class SampleViewModel @Inject constructor(
    reducer: SampleReducer,
    private val useCaseFacade: SampleHomeUseCaseFacade,
    private val presentationFacade: SampleHomePresentationFacade
) : BaseViewModel<
    SampleUiState,
    SampleUiEvent,
    SampleUiMessage,
    SampleUiEffect
>(
    initialState = SampleUiState.initial(),
    reducer = reducer
) {
    override suspend fun handleEvent(event: SampleUiEvent) {
        // UseCaseFacadeを通じてApplication処理を呼ぶ
    }
}
```

> [!CAUTION]
> **ViewModelは外部I/Oを知りません。**
>
> 外部I/OはUseCaseからRepository / Gatewayへ委譲します。

### 3. UiStateに単発イベントを持たせる

#### ❌ NG

```kotlin
data class SampleUiState(
    val showDialog: Boolean,
    val snackbarMessage: String?,
    val shouldNavigate: Boolean
) : UiState
```

#### 何が壊れているか

```text
再描画でDialogが再表示される
画面回転でSnackbarが再表示される
状態と副作用が混ざる
表示済み管理が必要になる
```

#### ✅ OK

```kotlin
sealed interface SampleUiEffect : UiEffect {

    data class ShowDialog(
        val title: String,
        val message: String
    ) : SampleUiEffect

    data class ShowSnackbar(
        val message: String
    ) : SampleUiEffect

    data object NavigateBack : SampleUiEffect
}
```

> [!IMPORTANT]
> **再実行されたら困るものはUiEffect。**

### 4. UseCaseにUiStateを渡す

#### ❌ NG

```kotlin
val result = saveSampleUseCase(uiState.value)
```

#### 何が壊れているか

```text
Application層がPresentation層に依存する
UseCaseが画面構造を知る
画面変更がUseCaseへ波及する
UseCaseの再利用性が落ちる
```

#### ✅ OK

```kotlin
val input = SaveSampleInput(
    id = uiState.value.id,
    name = uiState.value.name,
    enabled = uiState.value.enabled
)

val result = saveSampleUseCase(input)
```

> [!CAUTION]
> **UseCaseには専用Inputを渡す。**

### 5. Entity / DTOをUiStateへそのまま入れる

#### ❌ NG

```kotlin
data class SampleUiState(
    val entity: SampleEntity,
    val response: SampleResponse
) : UiState
```

#### 何が壊れているか

```text
DB / API仕様が画面へ漏れる
保存形式と表示形式が直結する
仕様変更の影響範囲が広がる
Composableで変換処理が増える
```

#### ✅ OK

```kotlin
data class SampleUiState(
    val id: String,
    val name: String,
    val enabled: Boolean
) : UiState
```

> [!IMPORTANT]
> **保存形式・通信形式・表示形式を分ける。**

### 6. Reducerに副作用を書く

#### ❌ NG

```kotlin
class SampleReducer @Inject constructor(
    private val repository: SampleRepository
) {

    suspend fun reduce(
        state: SampleUiState,
        message: SampleUiMessage
    ): SampleUiState {
        repository.save(...)
        return state.copy(isLoading = false)
    }
}
```

#### 何が壊れているか

```text
Reducerが純粋関数でなくなる
State遷移が再現できない
Unit Testが難しくなる
データフローが読めなくなる
```

#### ✅ OK

```kotlin
fun reduce(
    state: SampleUiState,
    message: SampleUiMessage
): SampleUiState {
    return when (message) {
        SampleUiMessage.SaveStarted -> {
            state.copy(isLoading = true)
        }

        SampleUiMessage.SaveSucceeded -> {
            state.copy(isLoading = false)
        }
    }
}
```

> [!IMPORTANT]
> **ReducerはState計算だけ。**

### 7. DataStoreをどこからでも読む

#### ❌ NG

```kotlin
@Composable
fun SampleScreen(
    dataStore: DataStore<Preferences>
) {
    // DataStoreから直接読む
}
```

#### 何が壊れているか

```text
設定値の取得箇所が散らばる
デフォルト値や破損時処理が統一されない
画面が保存形式に依存する
テストしづらくなる
```

#### ✅ OK

```text
DataStore
  ↓
SampleConfigRepositoryImpl
  ↓
SampleConfigRepository
  ↓
SampleConfigResolver
  ↓
UseCase
  ↓
ViewModel
  ↓
UiState
```

> [!CAUTION]
> **設定値もRepository / UseCase経由で扱う。**

### 8. Hilt Moduleが巨大化する

#### ❌ NG

```kotlin
object AppModule {
    // DBもAPIもRepositoryもProviderも全部ここ
}
```

#### 何が壊れているか

```text
依存関係の所在が分からない
app / core / featureの境界が消える
Gitコンフリクトが増える
Moduleが便利置き場になる
```

#### ✅ OK

```text
app/database/DatabaseProvideModule
app/datastore/PreferenceDataStoreModule
app/network/NetworkModule
core/di/CoreModule
core/storage/theme/ThemeModule
core/ui/theme/ThemeUiModule
feature/operationlog/data/di/OperationLogDataModule
feature/sample/di/SampleModule
feature/versioninfo/di/VersionInfoModule
```

> [!TIP]
> **Moduleは依存の地図です。**
>
> constructor injectionで解決できる具象クラスまで、機械的にModuleへ登録しません。

### 9. 入力値をrememberだけで持つ

#### ❌ NG

```kotlin
var name by remember { mutableStateOf("") }
```

#### 何が壊れているか

```text
UiStateが唯一の状態でなくなる
ViewModel / Reducerを経由しない更新になる
保存時にStateと画面表示がズレる
レビューしづらくなる
```

#### ✅ OK

```kotlin
TextField(
    value = uiState.name,
    onValueChange = {
        onEvent(SampleUiEvent.NameChanged(it))
    }
)
```

> [!IMPORTANT]
> **業務的な入力値はUiStateへ集約する。**

### 10. Exception.messageをそのまま画面に出す

#### ❌ NG

```kotlin
Text(exception.message ?: "エラー")
```

#### 何が壊れているか

```text
技術者向け文言が利用者に出る
エラー表示が統一されない
復旧導線を出せない
不要な内部情報が出る可能性がある
```

#### ✅ OK

```text
Exception
  ↓
AppError
  ↓
UiError
  ↓
UiState / UiEffect
```

> [!CAUTION]
> **Exceptionは表示用情報ではない。**

### 🎯 15章まとめ

避けるべきアンチパターンは以下です。

```text
Composableに業務ロジックを書く
ViewModelがRepository / DAO / APIを直接呼ぶ
UiStateに単発イベントを持たせる
UseCaseにUiStateを渡す
Entity / DTOをUiStateへ入れる
Reducerに副作用を書く
DataStoreをどこからでも読む
Hilt Moduleを巨大化させる
入力値をrememberだけで持つ
Exception.messageを画面に出す
```

> [!IMPORTANT]
> **アンチパターンは「責務の混線」として見る。**
>
> どの層が、どの責務を持ちすぎているかを確認してください。

---

## 🧨 16. ハマるポイントランキング

### 🎯 この章の目的

この章では、Androidアプリ実装で特にハマりやすいポイントを  
**症状ベース** で整理します。

15章のアンチパターンは「設計として何が壊れているか」を扱います。  
この章では「実装時にどういう症状として現れるか」を中心に整理します。

> [!IMPORTANT]
> **ハマったときは、症状から責務の混線を疑う。**
>
> 多くの不具合は、UiEvent / UiMessage / UiState / UiEffect / UseCase / Repository の  
> 境界が曖昧になったときに発生します。

### 🥇 1位：DialogやSnackbarが何度も出る

#### 症状

```text
画面回転後にDialogがまた出る
戻ってきたらSnackbarが再表示される
State更新のたびに同じ通知が出る
```

#### よくある原因

```kotlin
data class SampleUiState(
    val showDialog: Boolean,
    val snackbarMessage: String?
) : UiState
```

#### 見る場所

```text
UiStateに単発イベントが入っていないか
UiEffectで通知しているか
LaunchedEffectのkeyが適切か
```

#### 対応

```kotlin
emitEffect(
    SampleUiEffect.ShowDialog(
        title = uiError.title,
        message = uiError.message
    )
)
```

> [!TIP]
> 設計理由は「15. アンチパターン」の  
> 「UiStateに単発イベントを持たせる」を確認してください。

### 🥈 2位：入力してもTextFieldが変わらない

#### 症状

```text
文字を入力しても表示が戻る
一文字も入力できない
入力値が保存時に反映されない
```

#### よくある原因

```kotlin
TextField(
    value = "",
    onValueChange = {
        onEvent(SampleUiEvent.NameChanged(it))
    }
)
```

または、

```kotlin
var name by remember {
    mutableStateOf(uiState.name)
}
```

#### 見る場所

```text
TextFieldのvalueがuiStateを参照しているか
onValueChangeでUiEventを送っているか
ReducerでUiStateを更新しているか
rememberに業務値をコピーしていないか
```

#### 対応

```kotlin
TextField(
    value = uiState.name,
    onValueChange = {
        onEvent(SampleUiEvent.NameChanged(it))
    }
)
```

### 🥉 3位：ボタンを押しても何も起きない

#### 症状

```text
保存ボタンを押しても反応しない
ログが出ない
UseCaseが呼ばれない
Stateが変わらない
```

#### よくある原因

```kotlin
Button(
    onClick = {
        // onEventを呼んでいない
    }
)
```

または、

```kotlin
override suspend fun handleEvent(event: SampleUiEvent) {
    when (event) {
        // SaveClickedの分岐がない
    }
}
```

#### 見る場所

```text
ButtonのonClick
Screenから渡しているonEvent
BaseViewModelのeventChannelとFeature ViewModel.handleEventのwhen分岐
handleXxxの呼び出し
```

#### 対応

```kotlin
Button(
    onClick = {
        onEvent(SampleUiEvent.SaveClicked)
    }
)
```

```kotlin
override suspend fun handleEvent(event: SampleUiEvent) {
    when (event) {
        SampleUiEvent.SaveClicked -> {
            handleSave()
        }
    }
}
```

### 4位：Stateは変えているはずなのに画面が変わらない

#### 症状

```text
handleEventは呼ばれている
Reducerも実装した
しかしScreen表示が変わらない
```

#### よくある原因

- Feature ViewModelが `dispatch(message)` を呼んでいない
- Reducerが現在Stateと同じ値を返している
- ReducerのMessage分岐が不足している
- Routeが `collectAsStateWithLifecycle` で購読していない
- ScreenがUiStateではなく古い `remember` 値を表示している

#### 見る場所

```text
FeatureViewModel.handleEvent / handleXxx
UiMessage定義
XxxReducer.reduce
BaseViewModel.dispatch
XxxRouteのState購読
XxxScreenの表示値
```

#### 対応

Feature ViewModelはStateを直接変更せず、Messageをdispatchします。

```kotlin
dispatch(
    SampleUiMessage.LoadSucceeded(
        sample = result.value
    )
)
```

`BaseViewModel.dispatch` が `MutableStateFlow.update` とReducer呼び出しを担当します。  
Feature側に独自のdispatchやMutableStateFlowを作りません。

### 5位：初期表示処理が何度も走る

#### 症状

```text
画面を開くとAPIが何度も呼ばれる
State更新のたびにInitializeが走る
DialogやSnackbarも連動して何度も出る
```

#### よくある原因

```kotlin
LaunchedEffect(uiState) {
    viewModel.sendEvent(SampleUiEvent.Initialize)
}
```

#### 見る場所

```text
LaunchedEffectのkey
Initialize Eventの発行箇所
State更新をkeyにしていないか
```

#### 対応

```kotlin
LaunchedEffect(viewModel) {
    viewModel.sendEvent(SampleUiEvent.Initialize)
}
```

> [!CAUTION]
> **`LaunchedEffect(uiState)` は原則避ける。**
>
> UiState更新ごとに再実行されます。

### 6位：API結果が取れているのに画面に出ない

#### 症状

```text
GatewayではSuccessになっている
UseCaseでも値が返っている
でも画面に表示されない
```

#### よくある原因

```kotlin
when (result) {
    is AppResult.Success -> {
        // dispatchしていない
    }
}
```

#### 見る場所

```text
ViewModelでSuccess分岐に入っているか
UiMessageをdispatchしているか
ReducerでUiStateへ反映しているか
Screenが該当プロパティを表示しているか
```

#### 対応

```kotlin
dispatch(
    SampleUiMessage.LoadSucceeded(
        sample = result.value
    )
)
```

### 7位：Hiltでビルドエラーになる

#### 症状

```text
SampleRepository cannot be provided
SampleGateway cannot be provided
MissingBinding
ViewModelが生成できない
```

#### よくある原因

```text
interfaceの@Binds登録がない
実装クラスに@Inject constructorがない
Moduleの@InstallInがない
Retrofit / Room / DataStoreの@Providesがない
```

#### 見る場所

```text
RepositoryModule
GatewayModule
NetworkModule
DatabaseModule
@Inject constructor
@HiltViewModel
```

#### 対応

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class SampleRepositoryModule {

    @Binds
    abstract fun bindSampleRepository(
        impl: SampleRepositoryImpl
    ): SampleRepository
}
```

### 8位：保存ボタンが常に押せる / 押せない

#### 症状

```text
未入力でも保存できる
入力済みなのに保存できない
エラーが消えてもボタンが有効にならない
```

#### よくある原因

```text
canSaveの条件がUiStateとズレている
Validation結果がUiStateへ反映されていない
入力値をrememberに持っていてUiStateとズレている
```

#### 見る場所

```text
UiState.canSave
入力Event
ValidationPolicy
Reducerのエラー更新
```

#### 対応

```kotlin
val canSave: Boolean
    get() =
        !isLoading &&
        name.isNotBlank() &&
        nameErrorMessage == null
```

### 9位：保存は成功しているのにSnackbarが出ない

#### 症状

```text
保存はできている
Stateも更新されている
でもSnackbarが出ない
```

#### よくある原因

```text
UiEffectをemitしていない
RouteでuiEffectをcollectしていない
SnackbarHostStateをrememberしていない
ScaffoldにSnackbarHostを渡していない
```

#### 対応

```kotlin
val snackbarHostState = remember {
    SnackbarHostState()
}

Scaffold(
    snackbarHost = {
        SnackbarHost(snackbarHostState)
    }
) { paddingValues ->
    SampleScreen(
        modifier = Modifier.padding(paddingValues),
        uiState = uiState,
        onEvent = viewModel::sendEvent
    )
}
```

### 10位：エラー表示が画面ごとにバラバラになる

#### 症状

```text
同じ通信エラーなのに画面ごとに文言が違う
Exception.messageが表示される
復旧ボタンがあったりなかったりする
```

#### よくある原因

```text
ViewModelで文言を直接組み立てている
ComposableでExceptionを見ている
FeatureのUiErrorMapperを経由していない
```

#### 対応

```text
Exception
  ↓
AppError
  ↓
UiErrorMapper
  ↓
UiError
  ↓
UiState / UiEffect
```

### 🎯 16章まとめ

ハマりやすい症状は以下です。

```text
1. DialogやSnackbarが何度も出る
2. 入力してもTextFieldが変わらない
3. ボタンを押しても何も起きない
4. Stateは変えているはずなのに画面が変わらない
5. 初期表示処理が何度も走る
6. API結果が取れているのに画面に出ない
7. Hiltでビルドエラーになる
8. 保存ボタンが常に押せる / 押せない
9. 保存は成功しているのにSnackbarが出ない
10. エラー表示が画面ごとにバラバラになる
```

> [!IMPORTANT]
> **症状から、どの境界で止まっているかを見る。**
>
> UiEvent / UiMessage / UiState / UiEffect / UseCase / Repository / Gateway の  
> どこで流れが止まったかを確認します。

---

## 🧯 17. 動かないときのチェックフロー

### 🎯 この章の目的

この章では、Androidアプリ実装時に  
**画面が動かない・状態が変わらない・Effectが出ない・DIで落ちる** 場合の確認手順を定義します。

問題が起きたときは、闇雲に修正せず、  
データフローのどこで止まっているかを順番に確認します。

```text
Composable
  ↓ UiEvent
ViewModel
  ↓ UseCase
AppResult
  ↓ UiMessage
Reducer
  ↓ UiState
Composable

ViewModel
  ↓ UiEffect
Effect Handler
  ↓ Navigation / Dialog / Snackbar
```

> [!IMPORTANT]
> **「どこで止まっているか」を確認する。**
>
> 原因を推測で直すのではなく、  
> UiEvent / UseCase / AppResult / UiMessage / Reducer / UiState / UiEffect のどこで止まっているかを順番に追います。

### 📌 この章はこういうときに見る

- ボタンを押しても反応しない
- 入力しても画面が変わらない
- Stateは変わっているのに画面に反映されない
- Snackbarが出ない
- Dialogが出ない
- 画面遷移しない
- API結果が画面に出ない
- DB値が画面に出ない
- Hiltのビルドエラーが出る
- Flowがcollectされない

### 🧭 まず確認する全体フロー

最初に、以下のどこで止まっているかを確認します。

```text
1. ComposableからUiEventが送られているか
2. ViewModelのonEventに届いているか
3. handleXxxが呼ばれているか
4. UseCaseが呼ばれているか
5. UseCaseが期待するAppResultを返しているか
6. ViewModelでUiMessageをdispatchしているか
7. ReducerでUiStateが更新されているか
8. ComposableがUiStateを購読しているか
9. ScreenがUiStateを表示に使っているか
10. UiEffectが発行されているか
11. Effect Handlerでcollectされているか
```

> [!TIP]
> **ログを入れるなら境界に入れる。**
>
> Composable / ViewModel / UseCase / Reducer / Effect Handler の境界にログを入れると、  
> どこで止まっているかが分かりやすくなります。

### 🧩 症状別チェック表

| 症状 | 確認するところ |
|---|---|
| ボタンを押しても反応しない | `onClick` / `onEvent` / `sendEvent` / `handleEvent` |
| 入力しても値が変わらない | `onValueChange` / `UiEvent` / `Reducer` |
| Stateが変わらない | `dispatch` / `Reducer` / `collectAsStateWithLifecycle` |
| Stateは変わるが画面に出ない | `collectAsStateWithLifecycle` / Screen引数 / 表示参照 |
| Snackbarが出ない | `UiEffect` emit / `LaunchedEffect` collect / `SnackbarHostState` |
| Dialogが出ない | `ShowDialog` Effect / dialogState / AlertDialog表示条件 |
| 画面遷移しない | `Navigate` Effect / NavGraph / route文字列 |
| API結果が出ない | Repository / Gateway / AppResult / ViewModel分岐 |
| DB値が出ない | DAO / Repository / UseCase / Entity変換 |
| Hiltで落ちる | `@Inject` / `@Binds` / Module / Scope |
| 初期表示が呼ばれない | `LaunchedEffect` / `Initialize` Event |
| 何度も処理が走る | `LaunchedEffect` key / StateにEffectを持っていないか |

### 🖱 ボタンを押しても反応しない

#### 確認順

```text
1. ButtonのonClickが呼ばれているか
2. onEventが呼ばれているか
3. sendEventからeventChannelを経由してhandleEventに届いているか
4. 対応するwhen分岐があるか
5. handleXxxが呼ばれているか
```

#### よくある原因

```kotlin
Button(
    onClick = {
        // onEventを呼んでいない
    }
)
```

#### 正しい例

```kotlin
Button(
    onClick = {
        onEvent(SampleUiEvent.SaveClicked)
    }
)
```

```kotlin
override suspend fun handleEvent(event: SampleUiEvent) {
    when (event) {
        SampleUiEvent.SaveClicked -> {
            handleSave()
        }
    }
}
```

### ✏️ 入力しても値が変わらない

#### 確認順

```text
1. TextFieldのvalueがuiStateの値を参照しているか
2. onValueChangeでUiEventを送っているか
3. ViewModelで入力Eventを受けているか
4. UiMessageをdispatchしているか
5. Reducerで対象の値をcopyしているか
```

#### よくある原因

```kotlin
TextField(
    value = "",
    onValueChange = {
        onEvent(SampleUiEvent.NameChanged(it))
    }
)
```

#### 正しい例

```kotlin
TextField(
    value = uiState.name,
    onValueChange = {
        onEvent(SampleUiEvent.NameChanged(it))
    }
)
```

> [!IMPORTANT]
> **TextFieldのvalueは必ずUiStateを見る。**

### 🧱 Stateが変わらない

#### 確認順

```text
1. handleEvent / handleXxxでdispatchしているか
2. 対応するUiMessageが定義されているか
3. Reducerのwhen分岐が存在するか
4. Reducerがcopyで対象プロパティを更新しているか
5. RouteがcollectAsStateWithLifecycleで購読しているか
6. Screenが最新UiStateを参照しているか
```

#### よくある原因

```kotlin
// Reducerを呼ぶだけ、またはStateを直接書き換えようとしている
reducer.reduce(
    currentState = stateValue,
    message = message
)
```

#### 正しい例

```kotlin
dispatch(message)
```

`dispatch` の内部実装はBaseViewModelが所有します。  
Reducerの戻り値を `MutableStateFlow.update` で反映する処理を
Feature ViewModelへ重複実装しません。

### 🖼 Stateは変わるが画面に反映されない

#### 確認順

```text
1. RouteでcollectAsStateWithLifecycleしているか
2. Screenへ最新uiStateを渡しているか
3. ScreenがuiStateの対象プロパティを参照しているか
4. rememberで古い値を保持していないか
```

#### よくある原因

```kotlin
var name by remember {
    mutableStateOf(uiState.name)
}
```

初回値だけrememberしてしまい、UiState更新が反映されません。

#### 正しい例

```kotlin
Text(
    text = uiState.name
)
```

> [!TIP]
> **UiStateをrememberにコピーしない。**

### 💬 Snackbarが出ない

#### 確認順

```text
1. ViewModelでUiEffect.ShowSnackbarをemitしているか
2. RouteでuiEffectをcollectしているか
3. LaunchedEffectが正しく起動しているか
4. SnackbarHostStateをScaffoldへ渡しているか
5. showSnackbarを呼んでいるか
```

#### よくある原因

```kotlin
val snackbarHostState = SnackbarHostState()
```

Composable再描画のたびに新しいSnackbarHostStateが作られている可能性があります。

#### 正しい例

```kotlin
val snackbarHostState = remember {
    SnackbarHostState()
}
```

```kotlin
Scaffold(
    snackbarHost = {
        SnackbarHost(snackbarHostState)
    }
)
```

> [!IMPORTANT]
> **SnackbarHostStateはrememberする。**

### 🪟 Dialogが出ない

#### 確認順

```text
1. ShowDialog Effectがemitされているか
2. Effect collectでdialogStateへ変換しているか
3. dialogStateがnullでないときAlertDialogを表示しているか
4. onDismissRequestで即nullにしていないか
```

#### 正しい例

```kotlin
var dialogState by remember {
    mutableStateOf<DialogUiState?>(null)
}

LaunchedEffect(viewModel) {
    viewModel.uiEffect.collect { effect ->
        when (effect) {
            is SampleUiEffect.ShowDialog -> {
                dialogState = DialogUiState(
                    title = effect.title,
                    message = effect.message
                )
            }

            else -> Unit
        }
    }
}
```

### 🧭 画面遷移しない

#### 確認順

```text
1. Navigate系UiEffectがemitされているか
2. Effect Handlerでcollectされているか
3. navController.navigateが呼ばれているか
4. route文字列がNavGraph登録と一致しているか
5. 引数ありrouteの場合、arguments定義があるか
```

#### 対策

Route定義を共通化します。

```kotlin
object SampleRoutes {

    const val Detail = "sample/{sampleId}"

    fun detail(
        sampleId: String
    ): String {
        return "sample/$sampleId"
    }
}
```

> [!CAUTION]
> **route文字列を各所で直書きしない。**

### 🌐 API結果が画面に出ない

#### 確認順

```text
1. ViewModelからUseCaseが呼ばれているか
2. UseCaseからRepository / Gatewayが呼ばれているか
3. RepositoryImpl / GatewayImplでRetrofit APIが呼ばれているか
4. AppResult.Successが返っているか
5. ViewModelでSuccess分岐に入っているか
6. UiMessageをdispatchしているか
7. ReducerでUiStateに反映しているか
```

#### 正しい例

```kotlin
dispatch(
    SampleUiMessage.LoadSucceeded(
        sample = result.value
    )
)
```

> [!IMPORTANT]
> **APIが成功しても、UiMessageをdispatchしなければ画面は変わらない。**

### 🗄 DB値が画面に出ない

#### 確認順

```text
1. DAOのQueryが正しいか
2. RepositoryImplでEntityをDomainへ変換しているか
3. UseCaseでnull時の扱いを決めているか
4. ViewModelでSuccess分岐を処理しているか
5. ReducerでUiStateへ反映しているか
6. ScreenがUiStateを表示しているか
```

#### 正しい例

```kotlin
AppResult.Success(
    entity?.toDomain()
)
```

> [!CAUTION]
> **Entityを上位層へ漏らさない。**

### 🧬 Hiltで落ちる

#### 確認順

```text
1. ViewModelに@HiltViewModelが付いているか
2. constructorに@Injectが付いているか
3. UseCaseに@Inject constructorがあるか
4. Repository interfaceの@Binds登録があるか
5. RepositoryImplに@Inject constructorがあるか
6. Moduleの@InstallInが正しいか
7. @Providesが必要なものを登録しているか
8. Scopeが矛盾していないか
```

#### 正しい例

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class SampleRepositoryModule {

    @Binds
    abstract fun bindSampleRepository(
        impl: SampleRepositoryImpl
    ): SampleRepository
}
```

> [!TIP]
> **interfaceを注入するなら@Bindsが必要。**

### 🔁 初期表示が何度も呼ばれる

#### よくある原因

```kotlin
LaunchedEffect(uiState) {
    viewModel.sendEvent(SampleUiEvent.Initialize)
}
```

UiStateが変わるたびにInitializeが再実行されます。

#### 正しい例

```kotlin
LaunchedEffect(viewModel) {
    viewModel.sendEvent(SampleUiEvent.Initialize)
}
```

> [!CAUTION]
> **LaunchedEffectのkeyに注意する。**
>
> `uiState` をkeyにすると、State更新のたびに再実行されます。

### ✅ 最終チェックリスト

動かないときは、以下を確認します。

```text
□ ComposableからUiEventが送られている
□ sendEventからeventChannelを経由してhandleEventに届いている
□ handleEventのwhen分岐がある
□ handleXxxが呼ばれている
□ UseCaseが呼ばれている
□ UseCaseが期待するAppResultを返している
□ Failure時にUiErrorへ変換している
□ Success / FailureでUiMessageをdispatchしている
□ Reducerが対象Messageを処理している
□ Reducerがcopyで対象Stateを更新している
□ BaseViewModelのdispatchを通じてStateを反映している
□ RouteでcollectAsStateWithLifecycleしている
□ ScreenがUiStateを参照している
□ UiEffectをemitしている
□ RouteでuiEffectをcollectしている
□ SnackbarHostStateをrememberしている
□ Dialog表示用の一時Stateがある
□ Navigation routeがNavGraphと一致している
□ Hilt Moduleにinterface実装を登録している
```

### 🎯 17章まとめ

動かないときは、以下の流れで確認します。

```text
UiEventが届いているか
  ↓
UseCaseが呼ばれているか
  ↓
AppResultが返っているか
  ↓
UiMessageをdispatchしているか
  ↓
ReducerでUiStateを更新しているか
  ↓
ComposableがUiStateを表示しているか
  ↓
UiEffectをemitしているか
  ↓
Effect Handlerで実行しているか
```

> [!IMPORTANT]
> **「なんとなく直す」ではなく「流れを追う」**
>
> Androidの画面不具合は、  
> ほとんどが UiEvent / UiMessage / UiState / UiEffect のどこかで流れが止まっています。

---

## 📋 18. コピー用テンプレート

### 🎯 この章の目的

現行の `BaseViewModel + Event Queue + Reducer + Effect` に合わせた
最小テンプレートを示します。  
画面単位のディレクトリへ配置し、必要になった責務だけ追加します。

### 🗂 Feature構成テンプレート

```text
feature/xxx/
  application/
    LoadXxxUseCase.kt

  domain/
    Xxx.kt
    XxxRepository.kt

  data/
    XxxRepositoryImpl.kt

  di/
    XxxModule.kt

  presentation/
    common/
      XxxRoutes.kt
      XxxGraph.kt

    list/
      XxxListRoute.kt
      XxxListScreen.kt
      XxxListViewModel.kt
      XxxListUiState.kt
      XxxListUiEvent.kt
      XxxListUiMessage.kt
      XxxListUiEffect.kt
      XxxListReducer.kt
      XxxListUseCaseFacade.kt
      XxxListPresentationFacade.kt
```

### 🧱 UiStateテンプレート

```kotlin
data class XxxUiState(
    val isLoading: Boolean = false,
    val items: List<XxxItemUiState> = emptyList(),
    val screenErrorMessage: String? = null
) : UiState
```

### ⚡ UiEventテンプレート

```kotlin
sealed interface XxxUiEvent : UiEvent {
    data object Initialize : XxxUiEvent
    data object RetryClicked : XxxUiEvent
    data object BackClicked : XxxUiEvent
    data class ItemClicked(val id: String) : XxxUiEvent
}
```

### 📨 UiMessageテンプレート

```kotlin
sealed interface XxxUiMessage : UiMessage {
    data object LoadStarted : XxxUiMessage
    data class LoadSucceeded(
        val items: List<XxxItemUiState>
    ) : XxxUiMessage
    data class LoadFailed(
        val message: String
    ) : XxxUiMessage
}
```

### 💥 UiEffectテンプレート

```kotlin
sealed interface XxxUiEffect : UiEffect {
    data object NavigateBack : XxxUiEffect
    data class NavigateToDetail(val id: String) : XxxUiEffect
    data class ShowDialog(
        val title: String,
        val message: String
    ) : XxxUiEffect
    data class ShowSnackbar(val message: String) : XxxUiEffect
}
```

### 🧮 Reducerテンプレート

```kotlin
class XxxReducer @Inject constructor() :
    Reducer<XxxUiState, XxxUiMessage> {

    override fun reduce(
        currentState: XxxUiState,
        message: XxxUiMessage
    ): XxxUiState {
        return when (message) {
            XxxUiMessage.LoadStarted ->
                currentState.copy(
                    isLoading = true,
                    screenErrorMessage = null
                )

            is XxxUiMessage.LoadSucceeded ->
                currentState.copy(
                    isLoading = false,
                    items = message.items,
                    screenErrorMessage = null
                )

            is XxxUiMessage.LoadFailed ->
                currentState.copy(
                    isLoading = false,
                    screenErrorMessage = message.message
                )
        }
    }
}
```

### 🧠 ViewModelテンプレート

```kotlin
@HiltViewModel
class XxxViewModel @Inject constructor(
    reducer: XxxReducer,
    private val useCaseFacade: XxxUseCaseFacade,
    private val presentationFacade: XxxPresentationFacade
) : BaseViewModel<XxxUiState, XxxUiEvent, XxxUiMessage, XxxUiEffect>(
    initialState = XxxUiState(),
    reducer = reducer
) {
    override suspend fun handleEvent(event: XxxUiEvent) {
        when (event) {
            XxxUiEvent.Initialize,
            XxxUiEvent.RetryClicked -> load()

            XxxUiEvent.BackClicked ->
                emitEffect(XxxUiEffect.NavigateBack)

            is XxxUiEvent.ItemClicked ->
                emitEffect(XxxUiEffect.NavigateToDetail(event.id))
        }
    }

    private suspend fun load() {
        dispatch(XxxUiMessage.LoadStarted)

        when (val result = useCaseFacade.load()) {
            is AppResult.Success -> {
                val items = result.value.map(presentationFacade.itemMapper::map)
                dispatch(XxxUiMessage.LoadSucceeded(items))
            }

            is AppResult.Failure -> {
                val error = presentationFacade.errorMapper.toUiError(result.error)
                dispatch(XxxUiMessage.LoadFailed(error.message))
                emitEffect(
                    XxxUiEffect.ShowDialog(
                        title = error.title,
                        message = error.message
                    )
                )
            }
        }
    }
}
```

### 🖼 Routeテンプレート

```kotlin
@Composable
fun XxxRoute(
    navController: NavController,
    viewModel: XxxViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var dialogState by remember { mutableStateOf<DialogUiState?>(null) }

    LaunchedEffect(viewModel) {
        launch(start = CoroutineStart.UNDISPATCHED) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    XxxUiEffect.NavigateBack ->
                        navController.popBackStack()
                    is XxxUiEffect.NavigateToDetail ->
                        navController.navigate(XxxRoutes.detail(effect.id))
                    is XxxUiEffect.ShowDialog ->
                        dialogState = DialogUiState(
                            title = effect.title,
                            message = effect.message,
                            positiveButtonText = "OK"
                        )
                    is XxxUiEffect.ShowSnackbar -> Unit
                }
            }
        }

        viewModel.sendEvent(XxxUiEvent.Initialize)
    }

    XxxScreen(
        uiState = uiState,
        onEvent = viewModel::sendEvent
    )
}
```

### 🖼 Screenテンプレート

```kotlin
@Composable
fun XxxScreen(
    uiState: XxxUiState,
    onEvent: (XxxUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LoadingContent(
        isLoading = uiState.isLoading,
        modifier = modifier
    ) {
        XxxContent(
            items = uiState.items,
            onItemClick = { id ->
                onEvent(XxxUiEvent.ItemClicked(id))
            }
        )
    }
}
```

ScreenはViewModelを知らず、PreviewではUiStateを直接渡します。

### 🧩 Facadeテンプレート

```kotlin
class XxxUseCaseFacade @Inject constructor(
    val load: LoadXxxUseCase,
    val saveOperationLog: SaveOperationLogUseCase
)

class XxxPresentationFacade @Inject constructor(
    val itemMapper: XxxItemMapper,
    val errorMapper: XxxUiErrorMapper,
    val stringProvider: StringProvider
)
```

Facadeは依存を束ねるだけです。処理手順はUseCase、状態制御はViewModel、
表示変換はMapper / Formatterへ置きます。

### 🗄 Repositoryテンプレート

```kotlin
interface XxxRepository {
    suspend fun findAll(): AppResult<List<Xxx>>
}

class XxxRepositoryImpl @Inject constructor(
    private val localDataSource: XxxLocalDataSource
) : XxxRepository {
    override suspend fun findAll(): AppResult<List<Xxx>> {
        return localDataSource.findAll()
    }
}
```

API・Room・DataStoreの例外変換は、各境界またはCoreの共通Handlerへ委譲します。

### 🧬 Hilt Moduleテンプレート

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class XxxModule {
    @Binds
    abstract fun bindXxxRepository(
        impl: XxxRepositoryImpl
    ): XxxRepository
}
```

### ✅ コピー後チェック

```text
□ ScreenはuiStateとonEventだけを受け取る
□ RouteはviewModel::sendEventを渡す
□ ViewModelはBaseViewModelを継承する
□ handleEventはEvent分岐の入口に留める
□ UseCase結果はUiMessage / UiEffectへ変換する
□ State更新はdispatch / Reducer経由にする
□ NavController / ContextをViewModelへ入れない
□ Entity / DTO / RawConfigをUiStateへ入れない
□ Dialog / Snackbar / NavigationはUiEffectで扱う
□ 業務ルールはPolicy、現在時刻はProvider、表示変換はMapper / Formatterへ置く
□ 依存が多い画面だけ2系統Facadeで整理する
□ PreviewはViewModelなしでUiStateを直接渡す
```

---

本書の設計思想は、状態・入力・処理結果・副作用を明示的に分け、
Feature実装者、レビュー担当者、テスト、AI支援の探索範囲を揃えることにあります。  
Android固有のAPIは適切な境界へ閉じつつ、Composeの局所状態・Preview・宣言的UIは
そのまま活かします。
