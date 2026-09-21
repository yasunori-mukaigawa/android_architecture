package jp.co.nsco.basearchitecture.core.architecture

/**
 * View から ViewModel へ通知する UI 入力を表すマーカーインターフェース。
 *
 * UiEvent は、ユーザーが画面上で行った操作や、
 * 画面ライフサイクル上の入力事実を表す。
 *
 * ■ 設計上の意図
 *   View は処理結果や状態更新の方法を判断せず、
 *   「何が起きたか」だけを UiEvent として ViewModel へ通知する。
 *
 * ■ 例
 *   OnLoad
 *   OnClickSave
 *   OnChangeInput
 *   OnClickBack
 */
interface UiEvent