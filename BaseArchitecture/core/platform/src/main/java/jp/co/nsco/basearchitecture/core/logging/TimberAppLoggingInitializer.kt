package jp.co.nsco.basearchitecture.core.logging

import javax.inject.Inject
import timber.log.Timber

/**
 * Timber を利用したログ出力基盤の初期化実装。
 *
 * 本クラスは、Debug ビルド時に Timber.DebugTree を登録し、
 * Application 起動後のログを Logcat へ出力可能にする責務を持つ。
 *
 * ■ 提供する責務
 *   DebugTree の登録
 *   初期化処理の多重実行抑制
 *   Timber 初期化処理の隠蔽
 *
 * ■ 設計上の意図
 *   Application や Feature が Timber.plant を直接呼び出さないようにし、
 *   ログ基盤の初期化をCoreの契約経由に統一する。
 *
 *   Release ビルドでは本クラスからTreeを登録しない。
 *   Crashlyticsやファイル出力などのProduction用Treeが必要になった場合は、
 *   アプリのログ方針に合わせて実装を拡張する。
 *
 * ■ 注意
 *   Timber はプロセス全体で共有されるため、初期化を複数回実行すると
 *   同じTreeの重複登録につながる。本クラスでは初期化済み状態を保持する。
 */
class TimberAppLoggingInitializer @Inject constructor(
    private val loggingConfiguration: AppLoggingConfiguration
) : AppLoggingInitializer {

    private var debugTreeRegistered = false

    /**
     * Timber のログ出力基盤を初期化する。
     *
     * Debug ビルドの場合のみ DebugTree を登録し、Release ビルドでは
     * アプリ側で別のProduction用Treeを登録できる余地を残す。
     *
     * @param isDebug Debug ビルドの場合は true。
     */
    @Synchronized
    override fun initialize(isDebug: Boolean, tag: String?) {
        loggingConfiguration.setDefaultTag(tag)

        if (isDebug && !debugTreeRegistered) {
            Timber.plant(Timber.DebugTree())
            debugTreeRegistered = true
        }
    }
}
