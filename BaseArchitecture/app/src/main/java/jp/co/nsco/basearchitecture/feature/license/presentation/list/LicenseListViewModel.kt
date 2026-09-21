package jp.co.nsco.basearchitecture.feature.license.presentation.list

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.architecture.BaseViewModel
import jp.co.nsco.basearchitecture.core.result.AppResult

/**
 * License list ViewModel.
 *
 * UI events are converted into license list loading, state updates, and one-shot effects.
 * UseCase / mapper dependencies are grouped by facade.
 */
@HiltViewModel
class LicenseListViewModel @Inject constructor(
    reducer: LicenseListReducer,
    private val useCaseFacade: LicenseListUseCaseFacade,
    private val presentationFacade: LicenseListPresentationFacade
) : BaseViewModel<
    LicenseListUiState,
    LicenseListUiEvent,
    LicenseListUiMessage,
    LicenseListUiEffect
    >(
    initialState = LicenseListUiState(),
    reducer = reducer
) {

    override suspend fun handleEvent(event: LicenseListUiEvent) {
        when (event) {
            LicenseListUiEvent.Initialize -> initialize()
            is LicenseListUiEvent.SearchKeywordChanged -> {
                dispatch(LicenseListUiMessage.SearchKeywordUpdated(event.value))
            }

            is LicenseListUiEvent.LicenseClicked -> {
                emitEffect(LicenseListUiEffect.NavigateToDetail(event.id))
            }

            LicenseListUiEvent.BackClicked -> {
                emitEffect(LicenseListUiEffect.NavigateBack)
            }
        }
    }

    private suspend fun initialize() {
        dispatch(LicenseListUiMessage.LoadStarted)

        when (val result = useCaseFacade.getLicenses()) {
            is AppResult.Success -> {
                dispatch(
                    LicenseListUiMessage.LoadSucceeded(
                        result.value.map(presentationFacade.itemMapper::map)
                    )
                )
            }

            is AppResult.Failure -> {
                val message = presentationFacade.errorMapper.message(result.error)
                dispatch(LicenseListUiMessage.LoadFailed(message))
                emitEffect(
                    LicenseListUiEffect.ShowDialog(
                        title = presentationFacade.errorMapper.title(),
                        message = message
                    )
                )
            }
        }
    }
}




