package jp.co.nsco.basearchitecture.core.time

/**
 * 現在時刻を epoch milliseconds として取得するための契約。
 *
 * AppClock は、`System.currentTimeMillis()` の直接参照を隠蔽し、
 * 呼び出し側がシステム時刻取得方法へ直接依存しないようにする。
 *
 * ■ 提供する責務
 *   現在時刻の取得
 *   システム時刻 API 依存の隠蔽
 *   テスト時の固定時刻差し替え
 *
 * ■ 設計上の意図
 *   キャッシュ期限判定、経過時間計算、保存時刻記録などで
 *   `System.currentTimeMillis()` を直接呼び出すと、
 *   Unit テストで時刻を制御しづらくなる。
 *
 *   AppClock を経由することで、
 *   本番では SystemAppClock、
 *   テストでは FixedAppClock などへ差し替えられるようにする。
 */
interface AppClock {

    /**
     * 現在時刻を epoch milliseconds として取得する。
     *
     * 主にキャッシュ期限判定、保存時刻記録、経過時間計算など、
     * タイムゾーンに依存しない時刻比較に使用する。
     *
     * @return 1970-01-01T00:00:00Z からの経過ミリ秒。
     */
    fun nowMillis(): Long
}