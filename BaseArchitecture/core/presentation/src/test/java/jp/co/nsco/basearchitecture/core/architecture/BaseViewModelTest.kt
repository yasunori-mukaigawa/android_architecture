package jp.co.nsco.basearchitecture.core.architecture

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * BaseViewModelのState / Event / Effect契約を確認する。
 */
@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest {

    @Test
    fun sendEvent_processesEventsSequentiallyAndPublishesStateAndEffect() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        try {
            val viewModel = TestViewModel()
            val effects = mutableListOf<TestEffect>()
            val effectCollector = viewModel.uiEffect
                .onEach { effects += it }
                .launchIn(this)
            runCurrent()

            try {
                viewModel.sendEvent(TestEvent.Increment)
                viewModel.sendEvent(TestEvent.Increment)
                viewModel.sendEvent(TestEvent.Notify)

                advanceUntilIdle()

                assertEquals(TestState(count = 2), viewModel.uiState.value)
                assertEquals(listOf(TestEffect.Notified), effects)
                assertEquals(viewModel.uiState.value, viewModel.exposedCurrentState())
            } finally {
                effectCollector.cancel()
            }
        } finally {
            Dispatchers.resetMain()
        }
    }

    private enum class TestEvent : UiEvent {
        Increment,
        Notify
    }

    private data class TestState(
        val count: Int = 0
    ) : UiState

    private enum class TestMessage : UiMessage {
        Incremented
    }

    private enum class TestEffect : UiEffect {
        Notified
    }

    private class TestViewModel : BaseViewModel<
        TestState,
        TestEvent,
        TestMessage,
        TestEffect
        >(
        initialState = TestState(),
        reducer = Reducer { state, message ->
            when (message) {
                TestMessage.Incremented -> state.copy(count = state.count + 1)
            }
        }
    ) {
        override suspend fun handleEvent(event: TestEvent) {
            when (event) {
                TestEvent.Increment -> dispatch(TestMessage.Incremented)
                TestEvent.Notify -> emitEffect(TestEffect.Notified)
            }
        }

        fun exposedCurrentState(): TestState = stateValue
    }
}
