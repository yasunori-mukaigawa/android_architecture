# 手順02 Providerを追う

次のProviderについて、契約、Android実装、DI登録、利用側を順番に確認する。

- StringProvider
- RawResourceReader
- DateTimeProvider
- AppClock
- DispatcherProvider
- CoroutineScopeProvider

各Providerについて、次を記録する。

```text
契約:
Android実装:
DI登録:
利用側:
テスト時に何を差し替えられるか:
```

## 注意

Providerを新しく作るのではなく、既存Providerがどの問題を解決しているかを確認する。
