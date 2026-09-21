package jp.co.nsco.basearchitecture.feature.legal.presentation

/**
 * 法務文書画面の表示状態。
 *
 * LegalDocumentScreenState は、画面全体が現在どの表示状態にあるかを表す。
 *
 * ■ 提供する責務
 *   読み込み中状態の表現
 *   読み込み成功状態の表現
 *   読み込み失敗状態の表現
 *
 * ■ 設計上の意図
 *   title / markdown / errorMessage の値だけで表示状態を推測せず、
 *   ScreenState として明示的に管理する。
 *
 *   これにより、UI 側は Loading / Loaded / Error の分岐を
 *   安定して表現できる。
 */
enum class LegalDocumentScreenState {

    /**
     * 法務文書を読み込み中の状態。
     */
    Loading,

    /**
     * 法務文書の読み込みに成功し、本文を表示できる状態。
     */
    Loaded,

    /**
     * 法務文書の読み込みに失敗した状態。
     */
    Error
}