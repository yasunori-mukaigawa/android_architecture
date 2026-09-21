package jp.co.nsco.basearchitecture

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.logging.AppLoggingInitializer

/**
 * Base Architecture Sample の Application クラス。
 *
 * BaseArchitectureApplication は、アプリ全体の Application として起動され、
 * Hilt の依存性注入コンテナを初期化する。
 *
 * ■ 提供する責務
 *   Application ライフサイクルの起点
 *   Hilt コンポーネントの初期化
 *   ログ出力基盤の初期化
 *
 * ■ 設計上の意図
 *   @HiltAndroidApp を付与することで、Hilt がアプリ全体の依存関係グラフを生成できるようにする。
 *
 *   Activity、ViewModel、Repository、UseCase などの依存解決は、
 *   この Application を起点に Hilt へ委譲する。
 *
 *   Timber Tree の登録は AppLoggingInitializer へ委譲し、
 *   Application 自身はログライブラリの具体APIを参照しない。
 */
@HiltAndroidApp
class BaseArchitectureApplication : Application() {

    /**
     * アプリ起動時のログ出力基盤初期化契約。
     */
    @Inject
    lateinit var loggingInitializer: AppLoggingInitializer

    /**
     * Application 起動時に共通ログ基盤を初期化する。
     */
    override fun onCreate() {
        super.onCreate()

        loggingInitializer.initialize(
            isDebug = BuildConfig.DEBUG,
            tag = "BaseArchitecture"
        )
    }
}
