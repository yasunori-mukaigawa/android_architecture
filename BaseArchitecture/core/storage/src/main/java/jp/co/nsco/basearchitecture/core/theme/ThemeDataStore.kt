package jp.co.nsco.basearchitecture.core.theme

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.datastore.PreferenceDataStore
import jp.co.nsco.basearchitecture.core.error.AppError
import jp.co.nsco.basearchitecture.core.result.AppResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine

/**
 * PreferenceDataStore を利用してテーマ設定を保存・取得する ThemeRepository 実装。
 *
 * 本クラスは、アプリのテーマモードとテーマ ID を永続化し、
 * ThemeSettings として公開する責務を持つ。
 *
 * ■ 提供する責務
 *   テーマ設定の購読
 *   テーマモードの保存
 *   テーマ ID の保存
 *   保存値の ThemeSettings 変換
 *   不正値・読込失敗時の既定値補正
 *
 * ■ 設計上の意図
 *   テーマ設定の保存方式を呼び出し側へ漏らさない。
 *
 *   AppRoot や Theme 適用側は ThemeRepository の契約だけを参照し、
 *   PreferenceDataStore のキー名や保存形式を知らない状態にする。
 *
 *   保存値は String として保持し、
 *   読み出し時に AppThemeMode / AppThemeId へ変換する。
 *
 * @param preferenceDataStore 軽量設定値を保存・購読する DataStore 契約。
 */
class ThemeDataStore @Inject constructor(
    private val preferenceDataStore: PreferenceDataStore
) : ThemeRepository {

    /**
     * 現在のテーマ設定。
     *
     * テーマモードとテーマ ID の保存値を購読し、
     * 2つの値を組み合わせて ThemeSettings として公開する。
     *
     * 保存値が不正な場合は既定値へ補正する。
     * 読込中に例外が発生した場合も、画面描画を継続できるよう ThemeSettings() を流す。
     */
    override val themeSettings: Flow<ThemeSettings> = combine(
        preferenceDataStore.observeString(
            key = ThemePreferenceKeys.ThemeMode,
            defaultValue = AppThemeMode.System.name
        ),
        preferenceDataStore.observeString(
            key = ThemePreferenceKeys.ThemeId,
            defaultValue = AppThemeId.Default.value
        )
    ) { modeValue, idValue ->
        ThemeSettings(
            themeMode = AppThemeMode.entries.firstOrNull { it.name == modeValue }
                ?: AppThemeMode.System,
            themeId = idValue
                .takeIf { it.isNotBlank() }
                ?.let(::AppThemeId)
                ?: AppThemeId.Default
        )
    }.catch { throwable ->
        if (throwable is CancellationException) {
            throw throwable
        }

        emit(ThemeSettings())
    }

    /**
     * テーマモードを更新する。
     *
     * AppThemeMode は enum 名を保存値として使用する。
     *
     * @param themeMode 保存するテーマモード。
     * @return 保存結果。成功時は Unit、失敗時は AppError.LocalStorage を返す。
     */
    override suspend fun updateThemeMode(themeMode: AppThemeMode): AppResult<Unit> {
        return putString(
            key = ThemePreferenceKeys.ThemeMode,
            value = themeMode.name,
            errorCode = "theme_mode_save_failed"
        )
    }

    /**
     * テーマ ID を更新する。
     *
     * 空文字の ThemeId が指定された場合は、既定テーマ ID として保存する。
     *
     * @param themeId 保存するテーマ ID。
     * @return 保存結果。成功時は Unit、失敗時は AppError.LocalStorage を返す。
     */
    override suspend fun updateThemeId(themeId: AppThemeId): AppResult<Unit> {
        val value = themeId.value
            .takeIf { it.isNotBlank() }
            ?: AppThemeId.Default.value

        return putString(
            key = ThemePreferenceKeys.ThemeId,
            value = value,
            errorCode = "theme_id_save_failed"
        )
    }

    /**
     * PreferenceDataStore に String 値を保存する。
     *
     * 保存時に発生した例外は AppError.LocalStorage へ変換し、
     * AppResult.Failure として返す。
     *
     * @param key 保存対象の設定キー。
     * @param value 保存する値。
     * @param errorCode 保存失敗時に使用するエラーコード。
     * @return 保存結果。
     */
    private suspend fun putString(
        key: String,
        value: String,
        errorCode: String
    ): AppResult<Unit> {
        return try {
            preferenceDataStore.putString(key, value)
            AppResult.Success(Unit)
        } catch (throwable: Throwable) {
            if (throwable is CancellationException) {
                throw throwable
            }

            AppResult.Failure(
                AppError.LocalStorage(
                    code = errorCode,
                    cause = throwable
                )
            )
        }
    }
}
