package jp.co.nsco.basearchitecture.feature.sample.domain

import java.time.LocalDate

/**
 * Sample Home 画面のヘッダー情報。
 *
 * SampleHomeHeader は、Sample Home 画面の上部に表示する
 * 挨拶種別と日付を表す Domain Model。
 *
 * ■ 提供する責務
 *   挨拶種別の保持
 *   表示対象日付の保持
 *
 * ■ 設計上の意図
 *   画面ヘッダーに必要な情報を、個別の値ではなく
 *   まとまった Domain Model として扱う。
 *
 *   挨拶文言や日付フォーマットは Presentation 層で解決し、
 *   Domain 層では意味のある値だけを保持する。
 *
 * @property greetingType 現在時刻に応じた挨拶種別。
 * @property date 表示対象の日付。
 */
data class SampleHomeHeader(
    val greetingType: SampleGreetingType,
    val date: LocalDate
)