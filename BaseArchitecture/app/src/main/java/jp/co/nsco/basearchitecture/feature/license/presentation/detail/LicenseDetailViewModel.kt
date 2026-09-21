package jp.co.nsco.basearchitecture.feature.license.presentation.detail

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.architecture.BaseViewModel
import jp.co.nsco.basearchitecture.core.result.AppResult

/**
 * ライセンス詳細画面の ViewModel。
 *
 * LicenseDetailViewModel は、画面から通知された Event を解釈し、
 * UseCase 呼び出し、Message dispatch、Effect 発行を行う。
 *
 * ■ 提供する責務
 *   画面イベントの受け取り
 *   ライセンス詳細取得 UseCase の実行
 *   読み込み状態の Message dispatch
 *   エラー表示用 Message dispatch
 *   戻る Navigation Effect の発行
 *   Dialog 表示 Effect の発行
 *
 * ■ 設計上の意図
 *   ViewModel は UI の状態更新を直接行わず、
 *   LicenseDetailUiMessage を Reducer へ dispatch する。
 *
 *   また、Navigation や Dialog などの一回性処理は
 *   LicenseDetailUiEffect として Route へ通知する。
 *
 *   これにより、State 更新と副作用を分離し、
 *   MVI の流れを保つ。
 *
 * ■ 注意
 *   licenseId の空文字や不正値の検証は GetLicenseDetailUseCase 側で行う。
 *   ViewModel は UseCase の AppResult を受け取り、
 *   成功・失敗に応じて Message / Effect へ変換する。
 *
 * @param reducer ライセンス詳細画面の State 更新を行う Reducer。
 * @param useCaseFacade ライセンス詳細画面で利用する UseCase 群。
 * @param presentationFacade ライセンス詳細画面で利用する Presentation 補助部品群。
 */
@HiltViewModel
class LicenseDetailViewModel @Inject constructor(
    reducer: LicenseDetailReducer,
    private val useCaseFacade: LicenseDetailUseCaseFacade,
    private val presentationFacade: LicenseDetailPresentationFacade
) : BaseViewModel<
        LicenseDetailUiState,
        LicenseDetailUiEvent,
        LicenseDetailUiMessage,
        LicenseDetailUiEffect
        >(
    initialState = LicenseDetailUiState(),
    reducer = reducer
) {

    /**
     * 画面イベントを処理する。
     *
     * @param event View から通知された Event。
     */
    override suspend fun handleEvent(event: LicenseDetailUiEvent) {
        when (event) {
            is LicenseDetailUiEvent.Initialize -> initialize(event.id)

            LicenseDetailUiEvent.BackClicked -> {
                emitEffect(LicenseDetailUiEffect.NavigateBack)
            }
        }
    }

    /**
     * 指定されたライセンスIDの詳細情報を読み込む。
     *
     * 読み込み開始時に Loading 状態へ更新し、
     * 成功時は詳細表示状態へ、失敗時は Error 状態へ更新する。
     *
     * 失敗時は画面内エラーメッセージを State に反映し、
     * 追加で Dialog 表示 Effect を発行する。
     *
     * @param id 読み込み対象のライセンスID。
     */
    private suspend fun initialize(id: String) {
        dispatch(LicenseDetailUiMessage.LoadStarted)

        when (val result = useCaseFacade.getLicenseDetail(id)) {
            is AppResult.Success -> {
                val detail = result.value
                dispatch(
                    LicenseDetailUiMessage.LoadSucceeded(
                        name = detail.name,
                        licenseName = detail.licenseName,
                        copyright = detail.copyright,
                        licenseText = detail.licenseText
                    )
                )
            }

            is AppResult.Failure -> {
                val message = presentationFacade.errorMapper.message(result.error)
                dispatch(LicenseDetailUiMessage.LoadFailed(message))
                emitEffect(
                    LicenseDetailUiEffect.ShowDialog(
                        title = presentationFacade.errorMapper.title(),
                        message = message
                    )
                )
            }
        }
    }
}