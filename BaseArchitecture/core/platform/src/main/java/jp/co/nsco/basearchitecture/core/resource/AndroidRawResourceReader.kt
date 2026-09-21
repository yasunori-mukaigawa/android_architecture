package jp.co.nsco.basearchitecture.core.resource

import android.content.Context
import androidx.annotation.RawRes
import dagger.hilt.android.qualifiers.ApplicationContext
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.error.AppError
import jp.co.nsco.basearchitecture.core.result.AppResult

/**
 * Android の raw resource を読み取る RawResourceReader 実装。
 *
 * 本クラスは、Android Resources に配置された raw resource を読み取り、
 * UTF-8 文字列として返す責務を持つ。
 *
 * ■ 提供する責務
 *   raw resource のテキスト読込
 *   Android Resources 依存の隠蔽
 *   読込失敗時の AppError 変換
 *
 * ■ 設計上の意図
 *   呼び出し側が Context / Resources / InputStream を直接扱わないようにする。
 *
 *   ライセンスファイル、固定JSON、初期データ、テンプレートなど、
 *   raw resource に配置したテキストファイルを共通の契約で読み取れるようにする。
 *
 *   読込失敗時は例外をそのまま投げず、
 *   AppResult.Failure として構造化して返す。
 *
 * @param context アプリケーション Context。
 *                raw resource 読込に使用するため、ApplicationContext を注入する。
 */
class AndroidRawResourceReader @Inject constructor(
    @param:ApplicationContext private val context: Context
) : RawResourceReader {

    /**
     * 指定された raw resource を UTF-8 テキストとして読み取る。
     *
     * raw resource の open、reader 生成、読み取り、close を本メソッド内で完結させる。
     * 読込に失敗した場合は AppError.LocalStorage を含む AppResult.Failure を返す。
     *
     * @param resId 読み取り対象の raw resource ID。
     * @return 読み取り結果。
     *         成功時は resource のテキスト、失敗時は AppError.LocalStorage を返す。
     */
    override suspend fun readText(@RawRes resId: Int): AppResult<String> {
        return runCatching {
            context.resources.openRawResource(resId).use { inputStream ->
                inputStream.reader(StandardCharsets.UTF_8).use { reader ->
                    reader.readText()
                }
            }
        }.fold(
            onSuccess = { text ->
                AppResult.Success(text)
            },
            onFailure = { throwable ->
                AppResult.Failure(
                    AppError.LocalStorage(
                        code = "RAW_RESOURCE_READ_FAILED",
                        cause = throwable
                    )
                )
            }
        )
    }
}