package jp.co.nsco.basearchitecture.core.architecture

/**
 * 一度だけ実行する UI 副作用を表すマーカーインターフェース。
 *
 * UiEffect は、画面遷移、Dialog 表示、Toast 表示など、
 * State として保持すべきではない単発処理を表す。
 *
 * ■ 設計上の意図
 *   一回性の処理を State から分離することで、
 *   再描画・再購読・画面復元による副作用の多重実行を防ぐ。
 *
 * ■ 例
 *   NavigateToDetail
 *   ShowErrorDialog
 *   ShowToast
 */
interface UiEffect