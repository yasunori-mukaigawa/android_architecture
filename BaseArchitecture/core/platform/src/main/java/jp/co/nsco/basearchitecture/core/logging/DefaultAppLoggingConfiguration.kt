package jp.co.nsco.basearchitecture.core.logging

import javax.inject.Inject

/**
 * アプリ内ログの既定設定を保持する標準実装。
 *
 * 本クラスは、Application 起動時に設定された既定 TAG を保持し、
 * AndroidAppLogger へ提供する責務を持つ。
 *
 * ■ 提供する責務
 *   既定 TAG の保持
 *   未設定時の標準 TAG 提供
 *   空の TAG 設定の無視
 *
 * ■ 設計上の意図
 *   ログ出力側がTAG未指定の場合でも、アプリ共通のTAGを使用できるようにする。
 *   Feature単位のTAGが必要な場合は、各ログメソッドへ個別に指定する。
 */
class DefaultAppLoggingConfiguration @Inject constructor() : AppLoggingConfiguration {

    @Volatile
    private var configuredTag: String = DEFAULT_TAG

    /**
     * 現在設定されている既定 TAG。
     */
    override val defaultTag: String
        get() = configuredTag

    /**
     * 既定 TAG を設定する。
     *
     * null、空文字、空白だけの値は設定せず、既存のTAGを維持する。
     *
     * @param tag Application 起動時に設定する既定 TAG。
     */
    override fun setDefaultTag(tag: String?) {
        tag?.takeIf { it.isNotBlank() }?.let { configuredTag = it }
    }

    private companion object {

        /**
         * Application 起動時にTAGが指定されなかった場合の標準値。
         */
        private const val DEFAULT_TAG = "BaseArchitecture"
    }
}
