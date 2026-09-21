package jp.co.nsco.basearchitecture.core.external

import jp.co.nsco.basearchitecture.core.result.AppResult

/**
 * 外部 URI を開くための契約。
 *
 * 本インターフェースは、ブラウザ起動や外部アプリ起動など、
 * Android の Intent 起動処理を呼び出し側から隠蔽する。
 *
 * ■ 提供する責務
 *   外部 URI 起動の抽象化
 *   fallback URI 起動の抽象化
 *   Android Intent 依存の隠蔽
 *   起動結果の AppResult 化
 *
 * ■ 設計上の意図
 *   ViewModel / UseCase / Presentation 層が Context や Intent を直接扱わないようにする。
 *
 *   外部 URI の起動可否や fallback 処理は実装側に閉じ込め、
 *   利用側は ExternalUriOpener の契約だけを参照する。
 */
interface ExternalUriOpener {

    /**
     * 指定された外部 URI を開く。
     *
     * @param uri 起動対象の外部 URI 情報。
     * @return 起動結果。成功時は Unit、失敗時は AppError を含む AppResult.Failure を返す。
     */
    fun open(uri: ExternalUri): AppResult<Unit>
}