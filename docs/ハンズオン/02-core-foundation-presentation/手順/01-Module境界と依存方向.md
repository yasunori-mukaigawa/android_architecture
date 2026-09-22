# Step 01 Module境界と依存方向

## 目的

FoundationとPresentationをModule単位で分ける理由を確認する。

## 確認内容

1. `core/foundation/build.gradle.kts`を読む
2. `core/presentation/build.gradle.kts`を読む
3. `settings.gradle.kts`のModule登録を確認する
4. SampleAppの対応Moduleと比較する
5. FoundationがUIやAndroid固有処理へ依存していないことを確認する

## 考えること

- Result、Error、ValidationをなぜFoundationへ置くのか
- State、Event、Reducer、BaseViewModelをなぜPresentationへ置くのか
- Feature固有のDomain型をCoreへ置いてはいけない理由は何か

## 完了条件

- Moduleごとの責務を説明できる
- 依存方向を図または文章で説明できる
- 不要な依存追加がない
