package jp.co.nsco.basearchitecture.feature.sample.presentation.history

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import jp.co.nsco.basearchitecture.R

/** Sample History画面のRoute。 */
@Composable
fun SampleHistoryRoute() {
    SampleHistoryScreen(
        title = stringResource(R.string.sample_history_title),
        message = stringResource(R.string.sample_history_placeholder)
    )
}
