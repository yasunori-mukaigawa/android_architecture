package jp.co.nsco.basearchitecture.core.architecture

/**
 * 画面が保持する UI 状態を表すマーカーインターフェース。
 *
 * UiState は、Compose が描画に使用する画面状態を表す。
 * 入力値、一覧データ、ローディング状態、エラー表示状態など、
 * 再描画されても同じ意味で扱える情報を保持する。
 *
 * ■ 設計上の意図
 *   View は UiState を唯一の表示根拠として扱う。
 *   State を一箇所に集約することで、画面表示の整合性を保つ。
 *
 * ■ 注意
 *   Navigation、Dialog、Toast など一度だけ実行したい処理は保持しない。
 *   それらは UiEffect として分離する。
 */
interface UiState