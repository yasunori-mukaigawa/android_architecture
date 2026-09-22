# Step 05 SampleAppとの差分確認と自己レビュー

## 目的

Coreの理解結果と実装状態をSampleAppと比較し、後続Featureで使える状態にする。

## 実施内容

1. Templateの`core/foundation`とSampleAppの対応Moduleを比較する
2. Templateの`core/presentation`とSampleAppの対応Moduleを比較する
3. KDocや学習用コメントを除いた実装差分を確認する
4. 差分がある場合は、不要な独自実装か、対応表に記録された差分かを分類する
5. `レビュー確認項目.md`を使って自己レビューする
6. ビルドとUnit Testを実行する

## 実行コマンド

```powershell
.\gradlew.bat :core:foundation:test
.\gradlew.bat :core:presentation:test
.\gradlew.bat :app:compileDebugKotlin
```

## 完了条件

- Core実装に不要な変更がない
- SampleAppとの実装差分が未記録のまま残っていない
- 後続StepでCoreを使うための責務説明ができる
- 実行したコマンドと結果を回答側のレビュー結果へ記録できる
