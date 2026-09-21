package jp.co.nsco.basearchitecture.feature.sample.presentation.home

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.core.resource.StringProvider
import jp.co.nsco.basearchitecture.feature.sample.domain.SampleGreetingType

/**
 * Sample Home 画面のヘッダー表示値を整形する Formatter。
 *
 * SampleHomeHeaderFormatter は、Domain 層で扱う SampleGreetingType / LocalDate を、
 * Sample Home 画面で表示する文字列へ変換する。
 *
 * ■ 提供する責務
 *   挨拶種別から表示文言への変換
 *   日付から表示文字列への変換
 *
 * ■ 設計上の意図
 *   Domain 層は「朝・昼・夜」という意味や日付を持つだけで、
 *   実際にどの文言・フォーマットで表示するかは知らない。
 *
 *   表示用の文言解決や日付フォーマットを Presentation 層の Formatter に分離することで、
 *   ViewModel / Composable が string resource や DateTimeFormatter を直接扱わないようにする。
 *
 * @param stringProvider 文字列リソース取得を行う Provider。
 */
class SampleHomeHeaderFormatter @Inject constructor(
    private val stringProvider: StringProvider
) {

    /**
     * 挨拶種別を表示文言へ変換する。
     *
     * @param greetingType 挨拶種別。
     * @return Sample Home 画面に表示する挨拶文言。
     */
    fun formatGreeting(greetingType: SampleGreetingType): String {
        return when (greetingType) {
            SampleGreetingType.Morning -> {
                stringProvider.getString(R.string.sample_home_greeting_morning)
            }

            SampleGreetingType.Afternoon -> {
                stringProvider.getString(R.string.sample_home_greeting_afternoon)
            }

            SampleGreetingType.Evening -> {
                stringProvider.getString(R.string.sample_home_greeting_evening)
            }
        }
    }

    /**
     * 日付を Sample Home 画面用の表示文字列へ変換する。
     *
     * @param date 表示対象の日付。
     * @return 日本語表記の日付文字列。
     */
    fun formatDate(date: LocalDate): String {
        return date.format(DateTimeFormatter.ofPattern(DateFormatPattern, Locale.JAPANESE))
    }

    private companion object {

        /**
         * Sample Home 画面の日付表示フォーマット。
         */
        private const val DateFormatPattern = "yyyy年M月d日（E）"
    }
}