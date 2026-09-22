# 手順02 Providerを追う: 回答

## Providerの利用経路

| 契約 | Android実装 | DI登録 | 現時点の利用状況 |
|---|---|---|---|
| `StringProvider` | `AndroidStringProvider` | `CoreModule` | TemplateのShellではFeature利用は限定的。後続Featureの文言解決で利用する |
| `RawResourceReader` | `AndroidRawResourceReader` | `CoreModule` | Legal / LicenseなどのRawResource読込で後続利用する |
| `DateTimeProvider` | `AppDateTimeProvider` | `CoreModule` | Homeの日付・挨拶、Operation Logの日時で後続利用する |
| `AppClock` | `SystemAppClock` | `CoreModule` | キャッシュ期限、保存時刻、経過時間の判定で利用する |
| `DispatcherProvider` | `AppDispatcherProvider` | `CoreModule` | Application ScopeへIO Dispatcherを渡し、後続のStorage / Networkでも利用する |
| `CoroutineScopeProvider` | `AppCoroutineScopeProvider` | `CoreModule` | アプリケーション単位の長寿命処理へ利用する |

## StringProvider

`StringProvider`はstring resourceの取得契約だけを持ち、`AndroidStringProvider`が`@ApplicationContext Context`の`getString`へ委譲する。

ViewModelやUseCaseがContextを直接保持せず、文字列取得を契約経由にできる。テストではFakeStringProviderを渡して、resource IDと期待文言の対応を制御できる。

文言の選択ロジックはStringProviderへ置かない。例えばエラーコードから表示文言を選ぶ処理は、ErrorResolverやFacadeなどの利用側が担当し、StringProviderは解決済みresourceを取得するだけにする。

## RawResourceReader

`RawResourceReader`はraw resource読込の契約であり、`AndroidRawResourceReader`がUTF-8テキストとして読み取る。失敗時は例外をそのまま上位へ投げず、`AppResult.Failure(AppError.LocalStorage(...))`へ変換する。

呼び出し側はContext、Resources、InputStreamを知らない。License本文や法務文書など、Androidのraw resourceを使うFeatureから契約を通して利用する。

## Time Provider

`DateTimeProvider`は`ZonedDateTime`として現在日時を提供し、表示用日付やタイムゾーンを含む業務日時に使う。`AppClock`はepoch millisecondsを提供し、キャッシュ期限や経過時間などの比較に使う。

本番実装はそれぞれ`ZonedDateTime.now()`と`System.currentTimeMillis()`を内部で使うが、利用側は直接呼ばない。テストでは固定日時実装へ差し替えられる。

## Coroutine Provider

`DispatcherProvider`はMain、IO、Defaultを提供する。どの処理をどのDispatcherで実行するかはUseCase、Repository、Gatewayなど処理を持つ側が判断する。Provider自身は`withContext`を実行しない。

`AppCoroutineScopeProvider`は`SupervisorJob() + dispatcherProvider.io`でアプリケーション単位のScopeを一つ提供する。DataStoreやアプリ全体同期のような長寿命処理を対象とし、画面処理には使用しない。

- ViewModelの処理: `viewModelScope`
- ComposeのUI処理: `rememberCoroutineScope`など既存Lifecycleに紐づくScope
- アプリ全体の長寿命処理: `CoroutineScopeProvider`

## 結論

ProviderはAndroid依存を消すための単なるラッパーではなく、テスト時に時刻、Dispatcher、Resource、Scopeを差し替えるための契約でもある。Featureが直接ContextやシステムAPIへ依存しないことが重要である。
