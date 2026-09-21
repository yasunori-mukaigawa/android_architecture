package jp.co.nsco.basearchitecture.feature.sample.domain

/**
 * Sample Feature の設定値。
 *
 * SampleSettings は、Sample 画面で扱う設定項目をまとめた Domain Model。
 *
 * ■ 提供する責務
 *   通知設定の保持
 *   ログイン時確認設定の保持
 *   キャッシュ保持設定の保持
 *   初期設定値の提供
 *
 * ■ 設計上の意図
 *   設定値を個別の Boolean として画面や UseCase 間で受け渡すのではなく、
 *   SampleSettings としてまとめて扱う。
 *
 *   これにより、設定項目が増えた場合も、
 *   Repository / UseCase / Presentation の境界で一貫したモデルとして扱える。
 *
 * @property notificationEnabled 通知設定が有効かどうか。
 * @property confirmOnLoginEnabled ログイン時確認が有効かどうか。
 * @property keepCacheEnabled キャッシュ保持が有効かどうか。
 */
data class SampleSettings(
    val notificationEnabled: Boolean,
    val confirmOnLoginEnabled: Boolean,
    val keepCacheEnabled: Boolean
) {

    companion object {

        /**
         * Sample設定の初期値を生成する。
         *
         * DataStore にまだ値が保存されていない場合や、
         * 初期表示時の既定値として利用する。
         *
         * @return 初期設定値。
         */
        fun initial(): SampleSettings {
            return SampleSettings(
                notificationEnabled = true,
                confirmOnLoginEnabled = true,
                keepCacheEnabled = true
            )
        }
    }
}