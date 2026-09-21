package jp.co.nsco.basearchitecture.core.license

import jp.co.nsco.basearchitecture.core.result.AppResult

/**
 * OSS ライセンス情報を取得するための契約。
 *
 * 本インターフェースは、アプリで利用している OSS / ライブラリの
 * ライセンス一覧および詳細情報を取得する入口を定義する。
 *
 * ■ 提供する責務
 *   ライセンス一覧取得の抽象化
 *   ライセンス詳細取得の抽象化
 *   ライセンス情報の保存元の隠蔽
 *
 * ■ 設計上の意図
 *   呼び出し側は、ライセンス情報が raw resource、asset、生成ファイル、
 *   または fallback データのどこから取得されるかを意識しない。
 *
 *   表示側は LicenseProvider の契約を通じて必要な情報を取得し、
 *   取得失敗時は AppResult.Failure として扱う。
 */
interface LicenseProvider {

    /**
     * OSS ライセンス一覧を取得する。
     *
     * 一覧表示に必要な軽量情報のみを返す。
     * ライセンス本文が必要な場合は、LicenseInfo.id を使用して
     * getLicenseDetail を呼び出す。
     *
     * @return ライセンス一覧の取得結果。
     *         成功時は LicenseInfo の一覧、失敗時は AppError を返す。
     */
    fun getLicenses(): AppResult<List<LicenseInfo>>

    /**
     * 指定された ID の OSS ライセンス詳細を取得する。
     *
     * @param id 取得対象のライセンス ID。
     * @return ライセンス詳細の取得結果。
     *         成功時は LicenseDetail、失敗時は AppError を返す。
     */
    fun getLicenseDetail(id: String): AppResult<LicenseDetail>
}