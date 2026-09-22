# 手順04 Navigationと共通UIを確認: 回答

## RouteArgumentEncoder

`RouteArgumentEncoder.kt`の`String.asRouteArgument()`は、Routeに埋め込む文字列を`Uri.encode`するだけの薄い補助である。

Route引数には`/`、`?`、`&`、`=`などが構文として使われるため、外部データや入力値をそのまま埋め込むとRouteが意図せず分割される。Coreはこのencodeだけを補助し、Route定義、NavHost、NavController、遷移条件はApp / Feature側に残す。

## ExternalUriOpener

`ExternalUriOpener`は外部URI起動の契約で、`AndroidExternalUriOpener`がApplicationContextとIntentを使って実装する。

```text
Feature / ViewModel / UseCase
    -> ExternalUriOpener.open(ExternalUri)
    -> AndroidExternalUriOpener
    -> Intent.ACTION_VIEW
    -> AppResult<Unit>
```

`ExternalUri`はprimary URIと任意のfallback URIを保持する。primaryの起動に失敗した場合はfallbackを試し、どちらも失敗した場合は`AppError.Unexpected`を含む`AppResult.Failure`へ変換する。呼び出し側へContextやIntentを漏らさない。

## AppDialog

`DialogUiState`はタイトル、メッセージ、ボタン文言だけを保持する。`AppDialog`はMaterial3の`AlertDialog`を表示し、クリックやdismissのcallbackを外部から受け取る。

表示判断や業務処理はAppDialogの責務ではない。

```text
ViewModel / Route
    -> Dialogを表示する状態またはEffectを決定
    -> 表示文言を解決済みStringとしてDialogUiStateへ変換
    -> AppDialogへ渡す
    -> callbackでViewModelへEventを通知
```

文字列をAppDialog内部でresource IDから解決しないのは、共通UIがFeature固有の文言やContextへ依存しないためである。

## LoadingContent

`LoadingContent`はcontent slotを表示し、`isLoading`がtrueの場合だけCircularProgressIndicatorと透明なクリック抑止Overlayを重ねる。Loading開始・終了の判断は呼び出し側のUiStateが担当する。

Core UIがLoadingを自動制御すると、UseCaseやViewModelの処理状態と表示状態が暗黙に結びつき、画面ごとの例外や複数処理の扱いが難しくなる。このため、部品は表示だけを担当する。

## 方針上の要確認事項

公開される`ExternalUriOpener.open`は`AppResult<Unit>`を返すが、実装内部のprivate `openUri`はKotlin標準`Result<Unit>`を使用している。SampleAppとTemplateで一致しているため実装差分ではないが、プロジェクト方針を厳密に適用するなら、標準Resultを使わずAppResultへ統一する修正候補である。

## 結論

CoreはNavigation全体やDialog判断を抽象化しない。Route引数encode、外部URI起動、Dialog表示、Loading表示という再利用可能な境界だけを提供し、遷移判断、表示判断、業務処理はApp / Feature側に残す。
