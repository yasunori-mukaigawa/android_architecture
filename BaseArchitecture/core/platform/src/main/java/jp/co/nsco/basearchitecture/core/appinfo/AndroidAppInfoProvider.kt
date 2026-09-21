package jp.co.nsco.basearchitecture.core.appinfo

import android.content.Context
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.error.AppError
import jp.co.nsco.basearchitecture.core.result.AppResult

/**
 * Android 標準 API を利用してアプリ情報を取得する AppInfoProvider 実装。
 *
 * 本クラスは、Android の Context / PackageManager から取得した情報を、
 * アプリ内で扱う AppVersionInfo に変換して返す責務を持つ。
 *
 * ■ 提供する責務
 *   アプリ名の取得
 *   パッケージ名の取得
 *   バージョン名の取得
 *   バージョンコードの取得
 *   取得失敗時の AppError 変換
 *
 * ■ 設計上の意図
 *   呼び出し側へ Android の PackageManager や SDK バージョン差分を漏らさない。
 *   バージョンコード取得方法は Android API レベルによって異なるため、
 *   本クラス内で差分を吸収する。
 *
 *   取得処理で例外が発生した場合も例外をそのまま外へ投げず、
 *   AppResult.Failure として構造化して返す。
 *
 * @param context アプリケーション Context。
 *                Activity Context を保持しないよう、ApplicationContext を注入する。
 */
class AndroidAppInfoProvider @Inject constructor(
    @param:ApplicationContext private val context: Context
) : AppInfoProvider {

    /**
     * アプリのバージョン情報を取得する。
     *
     * Android の PackageManager からアプリ情報を取得し、
     * AppVersionInfo として返す。
     *
     * 取得に失敗した場合は例外を送出せず、
     * AppError.LocalStorage を含む AppResult.Failure を返す。
     *
     * @return アプリバージョン情報の取得結果。
     *         成功時は AppVersionInfo、失敗時は AppError.LocalStorage を返す。
     */
    override fun getAppVersionInfo(): AppResult<AppVersionInfo> {
        return runCatching {
            val packageName = context.packageName
            val packageInfo = context.packageManager.getPackageInfo(packageName, 0)
            val appInfo = context.applicationInfo

            // versionCode は Android 9 以降で longVersionCode に変更されたため、
            // SDK バージョン差分を Provider 内で吸収する。
            val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode.toLong()
            }

            AppVersionInfo(
                appName = context.packageManager.getApplicationLabel(appInfo).toString(),
                packageName = packageName,
                versionName = packageInfo.versionName.orEmpty(),
                versionCode = versionCode,
                channel = "Production"
            )
        }.fold(
            onSuccess = { AppResult.Success(it) },
            onFailure = {
                AppResult.Failure(
                    AppError.LocalStorage("APP_INFO_READ_FAILED", it)
                )
            }
        )
    }
}