# 手順03 LoggerとScopeを確認: 回答

## Loggerの責務

`AppLogger`はdebug、info、warn、errorのログ出力契約を持つ。`AndroidAppLogger`がTimberへ委譲するため、UseCase、Repository、Gateway、ViewModelはAndroid Log APIやTimber APIを直接参照しない。

ログ実装を分けることで、次を差し替えられる。

- Unit Test用のFake Logger
- Debug用のTimber DebugTree
- Production用のCrashlyticsやDatadog Tree
- ログ出力抑制やマスキング方針

## Application起動時の初期化

`BaseArchitectureApplication`は`@HiltAndroidApp`を持ち、`AppLoggingInitializer`を注入する。`onCreate`で次の初期化を一度行う。

```text
BaseArchitectureApplication.onCreate
    -> AppLoggingInitializer.initialize
    -> TimberAppLoggingInitializer
    -> Debugビルドなら Timber.DebugTree を登録
```

ApplicationはTimberの具体APIを直接呼ばず、初期化契約だけを利用する。`TimberAppLoggingInitializer`は初期化済みフラグを持ち、DebugTreeの重複登録を抑制する。

## TAG解決

Initializerで既定TAGを設定できる。`AndroidAppLogger`の各メソッドに個別TAGが渡された場合はそれを優先し、nullまたは空白の場合は`AppLoggingConfiguration.defaultTag`へフォールバックする。

```text
個別TAGあり       -> 個別TAG
個別TAGなし       -> Initializer設定TAG
Initializer未設定 -> BaseArchitecture
```

## ScopeとDispatcher

`AppCoroutineScopeProvider`はアプリ全体の長寿命処理用であり、Featureごとの汎用Scopeではない。画面が破棄されても処理を続ける必要がない処理にこれを使うと、ライフサイクルを越えて処理が残るため不適切である。

Dispatcherは`DispatcherProvider`から取得し、テストではTestDispatcherへ差し替える。Scopeは`CoroutineScopeProvider`から取得するが、ViewModelやComposeの既存Scopeを置き換えるものではない。

## 現時点のFeature利用

03の開始時点では、Shell画面にFeature固有のログ処理や長寿命バックグラウンド処理を追加していない。ProviderとDIの契約を先に確定し、実際のFeature利用はSettings、Operation Log、Networkなどの後続Stepで行う。

## 結論

Loggerは個別ログ出力、Initializerはプロセス全体のログ基盤初期化、Scope Providerは長寿命処理の所有を担当する。それぞれを混ぜず、ApplicationとFeatureからの利用方法を分離する。
