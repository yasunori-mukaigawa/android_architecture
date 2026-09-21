package jp.co.nsco.basearchitecture.core.database

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.error.AppError

/**
 * Database 処理中に発生した例外を AppError へ変換する Mapper。
 *
 * 本クラスは、Room / SQLite などのローカルDB処理で発生した例外を、
 * アプリ共通エラーである AppError.LocalStorage へ変換する責務を持つ。
 *
 * ■ 提供する責務
 *   DB例外の AppError 変換
 *   DB操作種別に応じたエラーコード付与
 *   Throwable の上位層への漏れ出し抑止
 *
 * ■ 設計上の意図
 *   LocalDataSource / Repository が Throwable をそのまま上位層へ返さないようにする。
 *
 *   DB処理の失敗は AppError.LocalStorage として統一し、
 *   表示文言や復旧導線は Presentation 側で解決する。
 */
class DatabaseErrorMapper @Inject constructor() {

    /**
     * Throwable を AppError.LocalStorage へ変換する。
     *
     * 標準の DatabaseOperation に応じた共通エラーコードを使用する。
     *
     * @param throwable DB処理中に発生した例外。
     * @param operation DB操作種別。
     * @return DB失敗を表す AppError.LocalStorage。
     */
    fun fromThrowable(
        throwable: Throwable,
        operation: DatabaseOperation
    ): AppError {
        return AppError.LocalStorage(
            code = DatabaseErrorCode.fromOperation(operation),
            cause = throwable
        )
    }

    /**
     * Throwable を任意のエラーコードで AppError.LocalStorage へ変換する。
     *
     * Feature 固有の DB エラーコードを使用したい場合に利用する。
     *
     * @param throwable DB処理中に発生した例外。
     * @param code 変換後に使用するエラーコード。
     * @return DB失敗を表す AppError.LocalStorage。
     */
    fun fromThrowable(
        throwable: Throwable,
        code: String
    ): AppError {
        return AppError.LocalStorage(
            code = code,
            cause = throwable
        )
    }
}