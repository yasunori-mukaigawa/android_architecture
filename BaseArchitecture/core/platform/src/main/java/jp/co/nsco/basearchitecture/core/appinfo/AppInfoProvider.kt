package jp.co.nsco.basearchitecture.core.appinfo

import jp.co.nsco.basearchitecture.core.result.AppResult

/**
 * アプリ情報を取得するための契約。
 *
 * 本インターフェースは、アプリ名・パッケージ名・バージョン情報など、
 * アプリ自身に関する情報を取得する入口を定義する。
 *
 * ■ 提供する責務
 *   アプリバージョン情報取得の抽象化
 *   Android 実装詳細の隠蔽
 *   呼び出し側へ AppResult として取得結果を返す契約の提供
 *
 * ■ 設計上の意図
 *   呼び出し側は Context / PackageManager / SDK バージョン差分を意識しない。
 *   Android 依存の取得処理は実装クラスに閉じ込め、
 *   利用側は AppInfoProvider の契約のみを参照する。
 */
interface AppInfoProvider {

    /**
     * アプリのバージョン情報を取得する。
     *
     * @return アプリバージョン情報の取得結果。
     *         成功時は AppVersionInfo、失敗時は AppError を含む AppResult.Failure を返す。
     */
    fun getAppVersionInfo(): AppResult<AppVersionInfo>
}