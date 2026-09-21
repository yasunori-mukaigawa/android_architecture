package jp.co.nsco.basearchitecture.core.external

import android.content.Context
import android.content.Intent
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.error.AppError
import jp.co.nsco.basearchitecture.core.result.AppResult

/**
 * Android の Intent を利用して外部 URI を開く ExternalUriOpener 実装。
 *
 * 本クラスは、ブラウザ起動や外部アプリ起動など、
 * Android の ACTION_VIEW による URI 起動処理を実行する責務を持つ。
 *
 * ■ 提供する責務
 *   外部 URI 起動
 *   fallback URI 起動
 *   Intent 生成処理の隠蔽
 *   起動失敗時の AppError 変換
 *
 * ■ 設計上の意図
 *   呼び出し側へ Context / Intent / Uri などの Android 実装詳細を漏らさない。
 *
 *   利用側は ExternalUriOpener の契約に対して ExternalUri を渡すだけでよく、
 *   外部アプリ起動の具体的な処理は本クラスに閉じ込める。
 *
 *   primaryUri の起動に失敗した場合、fallbackUri が指定されていれば fallbackUri の起動を試みる。
 *   どちらも失敗した場合は例外をそのまま投げず、AppResult.Failure として返す。
 *
 * @param context アプリケーション Context。
 *                Activity 外から起動できるよう、ApplicationContext を使用する。
 */
class AndroidExternalUriOpener @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ExternalUriOpener {

    /**
     * 指定された外部 URI を開く。
     *
     * primaryUri の起動を試み、失敗した場合は fallbackUri の起動を試みる。
     * fallbackUri が存在しない場合、または fallbackUri も起動できない場合は、
     * AppError.Unexpected を含む AppResult.Failure を返す。
     *
     * @param uri 起動対象の外部 URI 情報。
     * @return 起動結果。成功時は Unit、失敗時は AppError.Unexpected を返す。
     */
    override fun open(uri: ExternalUri): AppResult<Unit> {
        return openUri(uri.primaryUri).recoverCatching {
            val fallback = uri.fallbackUri ?: throw it
            openUri(fallback).getOrThrow()
        }.fold(
            onSuccess = { AppResult.Success(Unit) },
            onFailure = {
                AppResult.Failure(
                    AppError.Unexpected("EXTERNAL_URI_OPEN_FAILED", it)
                )
            }
        )
    }

    /**
     * 指定された URI 文字列を Android Intent で開く。
     *
     * ApplicationContext から Activity を起動するため、
     * FLAG_ACTIVITY_NEW_TASK を付与する。
     *
     * @param value 起動対象の URI 文字列。
     * @return 起動結果。startActivity が失敗した場合は Result.failure を返す。
     */
    private fun openUri(value: String): Result<Unit> {
        return runCatching {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(value))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(intent)
        }
    }
}