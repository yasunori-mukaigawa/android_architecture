package jp.co.nsco.basearchitecture.feature.sample.domain

import java.time.ZonedDateTime
import javax.inject.Inject

/**
 * Sample Home の挨拶種別を判定する Policy。
 *
 * SampleGreetingPolicy は、現在日時から
 * 朝・昼・夜の挨拶種別を決定する Domain Policy。
 *
 * ■ 提供する責務
 *   時刻に応じた挨拶種別の判定
 *
 * ■ 設計上の意図
 *   UseCase や ViewModel が、時刻ごとの挨拶判定ルールを
 *   直接持たないようにする。
 *
 *   判定ルールを Policy に分離することで、
 *   挨拶時間帯の変更やテストを行いやすくする。
 *
 * ■ 判定ルール
 *   05:00〜11:59 は Morning
 *   12:00〜17:59 は Afternoon
 *   18:00〜04:59 は Evening
 */
class SampleGreetingPolicy @Inject constructor() {

    /**
     * 指定日時から挨拶種別を判定する。
     *
     * @param dateTime 判定対象の日時。
     * @return 判定された挨拶種別。
     */
    fun resolveGreetingType(dateTime: ZonedDateTime): SampleGreetingType {
        return when (dateTime.hour) {
            in 5 until 12 -> SampleGreetingType.Morning
            in 12 until 18 -> SampleGreetingType.Afternoon
            else -> SampleGreetingType.Evening
        }
    }
}