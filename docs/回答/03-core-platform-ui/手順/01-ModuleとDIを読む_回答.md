# 手順01 ModuleとDIを読む: 回答

## 確認対象

- `BaseArchitecture/settings.gradle.kts`
- `BaseArchitecture/core/platform/build.gradle.kts`
- `BaseArchitecture/core/ui/build.gradle.kts`
- `BaseArchitecture/core/platform/src/main/java/jp/co/nsco/basearchitecture/core/di/CoreModule.kt`
- `BaseArchitecture/core/ui/src/main/java/jp/co/nsco/basearchitecture/core/ui/theme/ThemeUiModule.kt`
- `BaseArchitecture/app/src/main/java/jp/co/nsco/basearchitecture/MainActivity.kt`
- `BaseArchitecture/app/src/main/java/jp/co/nsco/basearchitecture/app/AppRoot.kt`

## Moduleの依存方向

`settings.gradle.kts`では、`app`とCoreの各Moduleが登録されている。

```text
core:foundation
       ↑
core:platform     core:presentation
       ↑                ↑
       └────── app ─────┘
              ↑
            core:ui
```

実際には`core:platform`と`core:ui`はFoundationの共通契約を利用し、`app`が必要なCore Moduleを利用する。PlatformはAndroid API、UIはCompose UIを扱うが、Featureの業務処理は持たない。

## DI登録

`CoreModule`は、次の契約と標準実装を`@Binds`・`@Singleton`で紐付ける。

- `StringProvider` / `AndroidStringProvider`
- `RawResourceReader` / `AndroidRawResourceReader`
- `DispatcherProvider` / `AppDispatcherProvider`
- `CoroutineScopeProvider` / `AppCoroutineScopeProvider`
- `AppLogger` / `AndroidAppLogger`
- `AppLoggingConfiguration` / `DefaultAppLoggingConfiguration`
- `AppLoggingInitializer` / `TimberAppLoggingInitializer`
- `DateTimeProvider` / `AppDateTimeProvider`
- `AppClock` / `SystemAppClock`
- `AppInfoProvider` / `AndroidAppInfoProvider`
- `ExternalUriOpener` / `AndroidExternalUriOpener`

`ThemeUiModule`は`BaseThemeRegistry`に`DefaultBaseThemeRegistry`を登録する。

利用側は`AndroidStringProvider`や`AndroidAppLogger`を直接生成せず、契約をConstructor Injectionで受け取る。これにより、Android実装からFake実装やテスト実装へ差し替えられる。

## Appの入口

`MainActivity`は`@AndroidEntryPoint`を持つComposeの入口であり、`setContent`から`AppRoot()`を表示するだけである。`AppRoot`がアプリThemeと`AppNavHost`を接続する。

この構造により、ActivityへNavigation、Theme、Featureの業務処理を集約しない。AndroidのEntry Point、アプリ共通のCompose構成、Featureの処理を分離できる。

## 結論

Hilt Moduleは、共通契約と実行環境に依存する標準実装を接続する場所である。Feature固有のRepository、DAO、Gateway、業務ルールを`CoreModule`へ追加してはいけない。CoreがFeatureを知ると、共通基盤ではなくアプリ固有の依存置き場になるためである。
