package jp.co.nsco.basearchitecture.core.result

import jp.co.nsco.basearchitecture.core.error.AppError

/**
 * アプリ内の処理結果を表す共通結果型。
 *
 * AppResult は、処理が成功した場合の値と、
 * 失敗した場合の AppError を明示的に表現する。
 *
 * ■ 提供する責務
 *   成功結果の表現
 *   失敗結果の表現
 *   例外送出に頼らない結果返却
 *   UseCase / Repository / Gateway 間の失敗伝播
 *
 * ■ 設計上の意図
 *   アプリケーション内の通常想定される失敗を Throwable として投げず、
 *   AppResult.Failure として構造化して返す。
 *
 *   これにより、呼び出し側は成功/失敗を when や fold で明示的に分岐でき、
 *   失敗理由に応じた復旧処理や UI 表示へつなげやすくなる。
 *
 *   失敗時の詳細は AppError に集約し、
 *   AppResult 自体は成功/失敗のコンテナとして扱う。
 *
 * ■ 注意
 *   AppResult は Kotlin 標準 Result の単純なラッパーではない。
 *   Kotlin 標準 Result は Throwable を中心に扱うが、
 *   AppResult はアプリとして意味付けされた AppError を扱う。
 *
 * @param T 成功時に返す値の型。
 */
sealed interface AppResult<out T> {

    /**
     * 処理成功を表す結果。
     *
     * @property value 処理成功時に返す値。
     */
    data class Success<T>(
        val value: T
    ) : AppResult<T>

    /**
     * 処理失敗を表す結果。
     *
     * 失敗理由は Throwable ではなく、
     * アプリ内で意味付けされた AppError として保持する。
     *
     * @property error 処理失敗の理由。
     */
    data class Failure(
        val error: AppError
    ) : AppResult<Nothing>
}

/**
 * 成功時の値を別の値へ変換する。
 *
 * AppResult.Success の場合のみ transform を実行し、
 * 変換後の値を AppResult.Success として返す。
 *
 * AppResult.Failure の場合は transform を実行せず、
 * 元の Failure をそのまま返す。
 *
 * ■ 設計上の意図
 *   成功時の値変換を行うための補助関数。
 *   失敗時の AppError を維持したまま、成功値だけを別モデルへ変換できる。
 *
 * ■ 注意
 *   transform 内で発生した例外は捕捉しない。
 *   例外を AppError へ変換したい場合は、呼び出し側で明示的に扱う。
 *
 * @param transform 成功時の値を変換する処理。
 * @return 成功時は変換後の AppResult.Success、失敗時は元の AppResult.Failure。
 */
inline fun <T, R> AppResult<T>.map(
    transform: (T) -> R
): AppResult<R> {
    return when (this) {
        is AppResult.Success -> AppResult.Success(transform(value))
        is AppResult.Failure -> this
    }
}

/**
 * 成功時・失敗時の処理を1つの値へ畳み込む。
 *
 * AppResult.Success の場合は onSuccess を実行し、
 * AppResult.Failure の場合は onFailure を実行する。
 *
 * ■ 設計上の意図
 *   AppResult を呼び出し側の戻り値へ変換したい場合に使用する。
 *
 *   成功/失敗の分岐を呼び出し側へ明示しつつ、
 *   when 式の重複を抑えるための補助関数である。
 *
 * ■ 注意
 *   onSuccess / onFailure 内で発生した例外は捕捉しない。
 *   例外を AppError として扱いたい場合は、呼び出し側で明示的に変換する。
 *
 * @param onSuccess 成功時の値を変換する処理。
 * @param onFailure 失敗時の AppError を変換する処理。
 * @return 成功または失敗から変換された値。
 */
inline fun <T, R> AppResult<T>.fold(
    onSuccess: (T) -> R,
    onFailure: (AppError) -> R
): R {
    return when (this) {
        is AppResult.Success -> onSuccess(value)
        is AppResult.Failure -> onFailure(error)
    }
}

/**
 * 成功時に副作用処理を実行する。
 *
 * AppResult.Success の場合のみ action を実行し、
 * 戻り値として元の AppResult をそのまま返す。
 *
 * ログ出力、後続処理の呼び出し、状態更新前の補助処理など、
 * 成功時だけ追加処理を行いたい場合に使用する。
 *
 * ■ 注意
 *   action 内で発生した例外は捕捉しない。
 *   本関数は値変換ではなく、副作用実行用の補助関数である。
 *
 * @param action 成功時に実行する処理。
 * @return 元の AppResult。
 */
inline fun <T> AppResult<T>.onSuccess(
    action: (T) -> Unit
): AppResult<T> {
    if (this is AppResult.Success) {
        action(value)
    }
    return this
}

/**
 * 失敗時に副作用処理を実行する。
 *
 * AppResult.Failure の場合のみ action を実行し、
 * 戻り値として元の AppResult をそのまま返す。
 *
 * ログ出力、エラー監視、復旧処理前の補助処理など、
 * 失敗時だけ追加処理を行いたい場合に使用する。
 *
 * ■ 注意
 *   action 内で発生した例外は捕捉しない。
 *   本関数はエラーを変換せず、元の AppResult をそのまま返す。
 *
 * @param action 失敗時に実行する処理。
 * @return 元の AppResult。
 */
inline fun <T> AppResult<T>.onFailure(
    action: (AppError) -> Unit
): AppResult<T> {
    if (this is AppResult.Failure) {
        action(error)
    }
    return this
}

/**
 * 成功時の値を取得する。
 *
 * AppResult.Success の場合は value を返し、
 * AppResult.Failure の場合は null を返す。
 *
 * ■ 設計上の意図
 *   成功値だけを nullable として扱いたい場面のための補助関数。
 *   ただし、失敗理由が必要な処理では when や fold による明示分岐を優先する。
 *
 * @return 成功時の値。失敗時は null。
 */
fun <T> AppResult<T>.getOrNull(): T? {
    return when (this) {
        is AppResult.Success -> value
        is AppResult.Failure -> null
    }
}

/**
 * 失敗時の AppError を取得する。
 *
 * AppResult.Failure の場合は error を返し、
 * AppResult.Success の場合は null を返す。
 *
 * ■ 設計上の意図
 *   失敗理由だけを nullable として扱いたい場面のための補助関数。
 *   成功/失敗の両方を扱う処理では when や fold による明示分岐を優先する。
 *
 * @return 失敗時の AppError。成功時は null。
 */
fun AppResult<*>.errorOrNull(): AppError? {
    return when (this) {
        is AppResult.Success -> null
        is AppResult.Failure -> error
    }
}