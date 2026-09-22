# Step 7: Back Stack確認と自己レビュー

## 目的

App Shellの動作と責務分離を確認し、次のHome Feature課題へ渡せる状態にする。

## 手動確認

- 起動時にHomeが表示される
- HomeからHistoryへ移動し、BackでHomeへ戻る
- HomeからSettingsへ移動し、BackでHomeへ戻る
- Drawerを開いてBackで閉じる
- DrawerからLicenseへ移動し、Backで元の画面へ戻る
- 同じBottom Navigation itemを連続して押す
- Home / History / Settingsを何度か切り替える
- 小さい画面サイズでも項目が欠けないことを確認する

## 設計確認

- `feature/sample/presentation/common`に共通部品がある
- `SampleGraph`がRoute登録を担当している
- `SampleRoutes`がRoute文字列を集約している
- `SampleNavigationComponents`がNavigation実行をしていない
- ScreenがNavControllerを参照していない
- ViewModel / UseCase / Repositoryを先行して追加していない

## 次の課題への引き渡し

次のHome課題では、`feature/sample/presentation/home`へ業務状態を追加する。
このStepの共通Navigation部品は変更せず、HomeのRoute / Screen / ViewModelを拡張する前提とする。
