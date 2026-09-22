# ヒント 02 EventとMessage

Eventは画面からViewModelへ入る入力で、処理の開始を表す。

MessageはViewModelが処理した結果をReducerへ渡すためのState更新理由である。

例えば「保存ボタンが押された」はEventであり、「保存に成功した」はMessageになる。保存成功時にSnackbarを表示する通知はEffectとして分離する。
