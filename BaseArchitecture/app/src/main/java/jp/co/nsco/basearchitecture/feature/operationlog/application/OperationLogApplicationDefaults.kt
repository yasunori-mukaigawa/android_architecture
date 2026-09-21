package jp.co.nsco.basearchitecture.feature.operationlog.application

/**
 * OperationLog Application 層で使用する既定値。
 *
 * 本ファイルは、操作ログ UseCase 群で共通利用する
 * アプリケーション層の既定値を定義する。
 *
 * ■ 提供する責務
 *   操作ログ取得件数の既定値定義
 *
 * ■ 設計上の意図
 *   GetOperationLogsUseCase と ObserveOperationLogsUseCase で
 *   同じ既定取得件数を利用する。
 *
 *   各 UseCase に同じ数値を個別定義すると、
 *   将来的に片方だけ変更されて挙動がずれる可能性がある。
 *
 *   共通定数として集約することで、
 *   操作ログ取得件数の既定値を一箇所で管理する。
 */

/**
 * 操作ログ一覧取得時の既定取得件数。
 */
internal const val DefaultOperationLogLimit = 50