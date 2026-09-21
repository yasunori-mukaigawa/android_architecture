package jp.co.nsco.basearchitecture.feature.versioninfo.presentation

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.core.architecture.BaseViewModel
import jp.co.nsco.basearchitecture.core.resource.StringProvider
import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.feature.versioninfo.application.GetVersionInfoUseCase
import jp.co.nsco.basearchitecture.feature.versioninfo.domain.VersionInfo

/**
 * VersionInfo 画面の ViewModel。
 *
 * VersionInfoViewModel は、画面から通知された Event を解釈し、
 * バージョン情報取得、表示用 State 変換、Message dispatch、
 * Effect 発行を行う。
 *
 * ■ 提供する責務
 *   画面イベントの受け取り
 *   バージョン情報の取得
 *   VersionInfo から UiState への変換
 *   読み込み状態の Message dispatch
 *   エラー発生時の Message dispatch
 *   戻る遷移 Effect の発行
 *   外部URI起動 Effect の発行
 *   Dialog 表示 Effect の発行
 *
 * ■ 設計上の意図
 *   ViewModel は UI の状態更新を直接行わず、
 *   VersionInfoUiMessage を Reducer へ dispatch する。
 *
 *   バージョン情報取得は UseCase に委譲し、
 *   表示用 State への変換は Presentation 側の Mapper に委譲する。
 *
 *   外部URI起動や画面遷移などの一回性処理は
 *   VersionInfoUiEffect として Route へ通知する。
 *
 *   これにより、State 更新、情報取得、表示変換、副作用を分離し、
 *   MVI の流れを保つ。
 *
 * ■ 注意
 *   外部URI起動に必要な AppExternalLinks は VersionInfo に含まれる。
 *   そのため、最後に取得した VersionInfo を latestVersionInfo として保持し、
 *   ボタン押下時の Effect 発行に利用する。
 *
 * @param reducer VersionInfo 画面の State 更新を行う Reducer。
 * @param getVersionInfoUseCase バージョン情報を取得する UseCase。
 * @param uiMapper VersionInfo を画面表示用 State へ変換する Mapper。
 * @param stringProvider 文字列リソース取得を行う Provider。
 */
@HiltViewModel
class VersionInfoViewModel @Inject constructor(
    reducer: VersionInfoReducer,
    private val getVersionInfoUseCase: GetVersionInfoUseCase,
    private val uiMapper: VersionInfoUiMapper,
    private val stringProvider: StringProvider
) : BaseViewModel<VersionInfoUiState, VersionInfoEvent, VersionInfoUiMessage, VersionInfoUiEffect>(
    initialState = VersionInfoUiState(),
    reducer = reducer
) {

    /**
     * 最後に取得した VersionInfo。
     *
     * ストアページやアプリ情報ページを開くための ExternalUri は
     * VersionInfo 内に含まれるため、外部リンクボタン押下時に参照する。
     */
    private var latestVersionInfo: VersionInfo? = null

    /**
     * 画面イベントを処理する。
     *
     * @param event View から通知された Event。
     */
    override suspend fun handleEvent(event: VersionInfoEvent) {
        when (event) {
            VersionInfoEvent.OnAppear,
            VersionInfoEvent.RetryClicked -> {
                load()
            }

            VersionInfoEvent.BackClicked -> {
                emitEffect(VersionInfoUiEffect.NavigateBack)
            }

            VersionInfoEvent.OpenStoreClicked -> {
                latestVersionInfo?.let { info ->
                    emitEffect(VersionInfoUiEffect.OpenExternalUri(info.externalLinks.storeUri))
                }
            }

            VersionInfoEvent.OpenAppInfoPageClicked -> {
                latestVersionInfo?.let { info ->
                    emitEffect(VersionInfoUiEffect.OpenExternalUri(info.externalLinks.appInfoPageUri))
                }
            }
        }
    }

    /**
     * バージョン情報を読み込む。
     *
     * 成功時は VersionInfo を保持し、画面表示用 State に変換して Reducer へ渡す。
     * 失敗時は画面内エラー表示と Dialog 表示を行う。
     */
    private suspend fun load() {
        dispatch(VersionInfoUiMessage.LoadStarted)

        when (val result = getVersionInfoUseCase()) {
            is AppResult.Success -> {
                latestVersionInfo = result.value
                dispatch(VersionInfoUiMessage.LoadSucceeded(uiMapper.map(result.value)))
            }

            is AppResult.Failure -> {
                val message = stringProvider.getString(R.string.version_info_error_load_failed)
                dispatch(VersionInfoUiMessage.LoadFailed(message))
                emitEffect(
                    VersionInfoUiEffect.ShowDialog(
                        title = stringProvider.getString(R.string.version_info_error_title),
                        message = message
                    )
                )
            }
        }
    }
}