# ヒント01 Coreを作り直さない

この課題では、StringProvider、Logger、Dispatcher、Themeなどを別名で作り直さない。

見る順番は次のとおり。

```text
interface
  -> Android実装
  -> DI Module
  -> AppRoot / Theme / Feature
```

既存の型が見つからない場合は、新規作成する前にModule、package、SampleApp対応表を確認する。
