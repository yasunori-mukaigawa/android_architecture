package jp.co.nsco.basearchitecture.core.time

import javax.inject.Inject

/**
 * システム時刻を使用する AppClock 実装。
 *
 * 本クラスは、`System.currentTimeMillis()` を利用して、
 * 現在時刻を epoch milliseconds として返す責務を持つ。
 *
 * ■ 提供する責務
 *   システム現在時刻の取得
 *   AppClock 契約の本番実装
 *   System.currentTimeMillis 依存の隠蔽
 *
 * ■ 設計上の意図
 *   呼び出し側が `System.currentTimeMillis()` を直接参照しないようにする。
 *
 *   AppClock 経由にすることで、
 *   キャッシュ期限判定や経過時間計算を行う処理でも、
 *   Unit テスト時に固定時刻を注入できるようにする。
 */
class SystemAppClock @Inject constructor() : AppClock {

    /**
     * 現在時刻を epoch milliseconds として取得する。
     *
     * @return 1970-01-01T00:00:00Z からの経過ミリ秒。
     */
    override fun nowMillis(): Long {
        return System.currentTimeMillis()
    }
}