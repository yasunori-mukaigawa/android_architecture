package jp.co.nsco.basearchitecture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import jp.co.nsco.basearchitecture.app.AppRoot

/**
 * アプリのメイン Activity。
 *
 * MainActivity は、Compose UI の起点として AppRoot を表示する。
 *
 * ■ 提供する責務
 *   Activity ライフサイクルの起点
 *   Edge-to-edge 表示の有効化
 *   Compose content の設定
 *   AppRoot の表示
 *
 * ■ 設計上の意図
 *   MainActivity には画面固有のロジックを持たせず、
 *   アプリ全体の UI 起点である AppRoot へ処理を委譲する。
 *
 *   Navigation、Theme、各 Feature Graph の接続は AppRoot 側で扱うことで、
 *   Activity は Android アプリのエントリーポイントとしての責務に集中する。
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Activity 生成時の初期化処理。
     *
     * @param savedInstanceState Activity 再生成時の保存状態。
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppRoot()
        }
    }
}