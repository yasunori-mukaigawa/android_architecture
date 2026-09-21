package jp.co.nsco.basearchitecture.core.time

import java.time.ZonedDateTime

/**
 * 現在日時を取得するための契約。
 *
 * DateTimeProvider は、`ZonedDateTime.now()` の直接参照を隠蔽し、
 * 呼び出し側が現在日時の取得方法へ直接依存しないようにする。
 *
 * ■ 提供する責務
 *   現在日時の取得
 *   java.time API 依存の隠蔽
 *   テスト時の固定日時差し替え
 *
 * ■ 設計上の意図
 *   表示用日付、業務日付、履歴日時、ログ日時などで
 *   現在日時を直接取得すると、Unit テストで日時を固定しづらくなる。
 *
 *   DateTimeProvider を経由することで、
 *   本番実装とテスト実装を差し替えられるようにする。
 *
 * ■ AppClock との使い分け
 *   epoch milliseconds による比較や期限判定には AppClock を使用する。
 *   日付表示やタイムゾーンを含む日時表現には DateTimeProvider を使用する。
 */
interface DateTimeProvider {

    /**
     * 現在日時を取得する。
     *
     * @return 現在日時。
     */
    fun now(): ZonedDateTime
}