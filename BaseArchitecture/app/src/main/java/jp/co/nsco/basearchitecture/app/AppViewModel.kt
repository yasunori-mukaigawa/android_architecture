package jp.co.nsco.basearchitecture.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.theme.ThemeRepository
import jp.co.nsco.basearchitecture.core.theme.ThemeSettings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * アプリ共通状態を管理する ViewModel。
 *
 * 本 ViewModel は、Feature に依存しないアプリ全体の状態を保持し、
 * AppRoot へ公開する責務を持つ。
 *
 * ■ 提供する責務
 *   テーマ設定の購読
 *   Compose から扱いやすい StateFlow への変換
 *   アプリ起動直後に使用する初期テーマ設定の提供
 *
 * ■ 設計上の意図
 *   テーマ設定は複数画面にまたがるアプリ共通状態であるため、
 *   Feature 個別の ViewModel ではなく AppViewModel で扱う。
 *
 *   Repository が公開する Flow を stateIn で StateFlow 化し、
 *   AppRoot 側では collectAsStateWithLifecycle により Lifecycle を考慮して購読する。
 *
 * ■ 注意
 *   本 ViewModel では、個別画面の業務状態や画面イベントは扱わない。
 *   AppRoot で必要となるアプリ共通状態のみを公開する。
 */
@HiltViewModel
class AppViewModel @Inject constructor(
    themeRepository: ThemeRepository
) : ViewModel() {

    /**
     * 現在のテーマ設定。
     *
     * ThemeRepository の Flow を AppRoot から購読しやすい StateFlow として公開する。
     *
     * SharingStarted.WhileSubscribed を使用することで、
     * 購読者がいない間の不要な購読継続を抑制する。
     * initialValue には、テーマ設定の読込前でも描画可能な既定値を設定する。
     */
    val themeSettings: StateFlow<ThemeSettings> = themeRepository.themeSettings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ThemeSettings()
        )
}