package jp.co.nsco.basearchitecture.feature.starter.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import jp.co.nsco.basearchitecture.R

/**
 * Starter画面のRoute。
 *
 * StarterRouteは、画面表示に必要なResource解決とScreenへの引き渡しを担当する。
 * 初期画面では状態管理が不要なため、ViewModelを追加していない。
 * 実案件で状態や副作用が必要になった時点で、FeatureのRouteとして拡張する。
 */
@Composable
fun StarterRoute() {
    StarterScreen(
        title = stringResource(R.string.template_starter_title),
        message = stringResource(R.string.template_starter_message)
    )
}
