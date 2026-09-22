package jp.co.nsco.basearchitecture.feature.sample.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import jp.co.nsco.basearchitecture.R

/**
 * Sample Home画面のRoute。
 *
 * 初期段階では状態管理やNavigation接続を持たず、Resourceから取得した表示値を
 * StatelessなScreenへ渡す責務だけを持つ。
 */
@Composable
fun SampleRoute() {
    SampleScreen(
        title = stringResource(R.string.sample_home_title),
        message = stringResource(R.string.sample_home_placeholder)
    )
}
