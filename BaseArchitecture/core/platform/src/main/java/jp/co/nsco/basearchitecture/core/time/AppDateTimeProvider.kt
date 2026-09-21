package jp.co.nsco.basearchitecture.core.time

import java.time.ZonedDateTime
import javax.inject.Inject

/**
 * システム現在日時を提供する DateTimeProvider 実装。
 *
 * 本クラスは、現在日時を ZonedDateTime として取得する責務を持つ。
 *
 * ■ 提供する責務
 *   現在日時の取得
 *   java.time API 依存の隠蔽
 *   DateTimeProvider 契約の本番実装
 *
 * ■ 設計上の意図
 *   表示用日時、業務日付、ログ用日時などで `ZonedDateTime.now()` を直接呼び出すと、
 *   テスト時に現在日時を固定しづらくなる。
 *
 *   DateTimeProvider を経由することで、
 *   本番では AppDateTimeProvider、
 *   テストでは FixedDateTimeProvider などへ差し替えられるようにする。
 *
 * ■ 注意
 *   本実装は `ZonedDateTime.now()` をそのまま使用するため、
 *   実行環境のデフォルトタイムゾーンに依存する。
 *
 *   アプリ全体で特定タイムゾーンに固定したい場合は、
 *   ZoneId を注入する別実装を検討する。
 */
class AppDateTimeProvider @Inject constructor() : DateTimeProvider {

    /**
     * 現在日時を取得する。
     *
     * @return 実行環境のデフォルトタイムゾーンに基づく現在日時。
     */
    override fun now(): ZonedDateTime {
        return ZonedDateTime.now()
    }
}