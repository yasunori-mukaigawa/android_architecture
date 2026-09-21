package jp.co.nsco.basearchitecture.core.resource

import androidx.annotation.RawRes
import jp.co.nsco.basearchitecture.core.result.AppResult

/**
 * raw resource を読み取るための契約。
 *
 * 本インターフェースは、Android の raw resource に配置されたファイルを
 * アプリ内から読み取るための入口を定義する。
 *
 * ■ 提供する責務
 *   raw resource テキスト読込の抽象化
 *   Android Resources 依存の隠蔽
 *   読込結果の AppResult 化
 *
 * ■ 設計上の意図
 *   呼び出し側は Context / Resources / InputStream を直接扱わず、
 *   RawResourceReader の契約だけを利用する。
 *
 *   これにより、ライセンスファイル、固定JSON、初期データなどの読込処理を
 *   Android 実装詳細から分離し、テスト時には Fake 実装へ差し替えられる。
 */
interface RawResourceReader {

    /**
     * 指定された raw resource をテキストとして読み取る。
     *
     * @param resId 読み取り対象の raw resource ID。
     * @return 読み取り結果。
     *         成功時は resource のテキスト、失敗時は AppError を含む AppResult.Failure を返す。
     */
    suspend fun readText(@RawRes resId: Int): AppResult<String>
}