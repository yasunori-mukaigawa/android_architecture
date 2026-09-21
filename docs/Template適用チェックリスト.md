# Template適用チェックリスト

**対象:** 新規Android案件を本Templateから開始する担当者向け

このチェックリストは、Templateを案件用プロジェクトへ適用するときに確認する初期設定をまとめたものです。

---

## 🧭 適用前に確認すること

- [ ] [共通設計思想ガイド](設計思想/共通設計思想ガイド.md)を確認した
- [ ] [Androidカスタマイズ設計書](設計思想/Androidカスタマイズ設計書.md)を確認した
- [ ] 案件のアプリ名、識別子、対象Android versionが決まっている
- [ ] 必要なCore moduleとOptional moduleの範囲が決まっている

## 🏷 アプリ識別情報

- [ ] `settings.gradle.kts` の `rootProject.name` を変更した
- [ ] `app/build.gradle.kts` の `namespace` を変更した
- [ ] `app/build.gradle.kts` の `applicationId` を変更した
- [ ] `BaseArchitectureApplication` のpackageとManifest登録を確認した
- [ ] アプリ名を `strings.xml` へ設定した
- [ ] アプリアイコンを案件用へ置き換えた

## 🧱 初期構成

- [ ] Starter画面を案件の初期画面へ置き換えた
- [ ] `StarterRoutes` / `StarterGraph` を案件のFeature構成へ置き換えた
- [ ] `AppNavHost` の `startDestination` を確認した
- [ ] 必要なFeatureごとに `presentation` / `application` / `domain` / `data` / `di` を作成した
- [ ] ScreenへViewModelやRepositoryを直接追加していない
- [ ] ViewModelへContextやNavControllerを追加していない

## 🧩 Optional module判断

| 要件 | 追加するmodule / 設定 |
|---|---|
| DataStoreやRoomを使う | `:core:storage` とアプリ側の保存実装 |
| RetrofitやAPI通信を使う | `:core:network` とFeature側のAPI実装 |
| OSS License画面を持つ | `:core:license` とOSS License設定 |
| Markdown文書を表示する | `core:ui`へMarkdown部品と依存を追加 |

- [ ] 不要なOptional moduleを追加していない
- [ ] Optional module追加時に `settings.gradle.kts` と依存関係を更新した
- [ ] Optional module追加時に詳細設計書のDI構成を更新した

## 📝 設計資料

- [ ] 要件定義書を案件用に作成した
- [ ] 基本設計書へ画面一覧と画面遷移を記載した
- [ ] 詳細設計書へState / Event / Message / Effect遷移を記載した
- [ ] UI文字列を文字列定義として整理した
- [ ] 保存値を永続化仕様として整理した
- [ ] SDK、Plugin、Libraryのバージョンを記録した
- [ ] 画面レイアウトとキャプチャを更新した

## ✅ 初回確認

```powershell
cd BaseArchitecture
.\gradlew.bat :app:assembleDebug
.\gradlew.bat test
.\gradlew.bat :app:dokkaHtml
```

- [ ] Gradle syncが成功する
- [ ] Debug buildが成功する
- [ ] Unit Testが成功する
- [ ] Starterまたは初期画面がエミュレーターで起動する
- [ ] Navigationの初期Routeが正しい
- [ ] `app/build/dokka/html/index.html` が生成される
