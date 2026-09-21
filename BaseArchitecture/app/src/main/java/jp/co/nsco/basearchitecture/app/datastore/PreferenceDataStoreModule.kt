package jp.co.nsco.basearchitecture.app.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import jp.co.nsco.basearchitecture.core.coroutine.CoroutineScopeProvider
import jp.co.nsco.basearchitecture.core.datastore.AndroidPreferenceDataStore
import jp.co.nsco.basearchitecture.core.datastore.PreferenceDataStore

/**
 * Preference DataStore 関連の依存を提供する Hilt Module。
 *
 * 本 Module は、DataStore Preferences の生成と、
 * PreferenceDataStore 実装の DI 登録を行う。
 *
 * ■ 提供する責務
 *   DataStore Preferences の生成
 *   DataStore ファイル名の決定
 *   PreferenceDataStore 実装の登録
 *
 * ■ 設計上の意図
 *   DataStore のファイル名はアプリ構成に依存するため、
 *   core.datastore ではなく app 側の Module で決定する。
 *
 *   core.datastore は DataStore の使い方を提供し、
 *   app.datastore はどのファイルに保存するかを決める。
 *
 *   DataStore の CoroutineScope は Core の CoroutineScopeProvider から取得し、
 *   app 側で Scope を直接生成しない。
 *
 * ■ 注意
 *   DataStore はアプリ全体で共有する永続化リソースであるため、
 *   Singleton として生成する。
 */
@Module
@InstallIn(SingletonComponent::class)
object PreferenceDataStoreModule {

    /**
     * DataStore Preferences のファイル名。
     *
     * アプリ側の構成責務としてここで定義する。
     * 用途別に DataStore を分ける場合は、別 Module / 別 Qualifier の追加を検討する。
     */
    private const val DATA_STORE_FILE_NAME = "app_preferences"

    /**
     * DataStore Preferences を生成する。
     *
     * @param context アプリケーション Context。
     *                DataStore は Activity のライフサイクルに依存しないため、
     *                ApplicationContext を使用する。
     * @param scopeProvider アプリケーション単位の CoroutineScope 提供契約。
     * @return アプリ全体で共有する DataStore Preferences。
     */
    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context,
        scopeProvider: CoroutineScopeProvider
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            scope = scopeProvider.scope,
            produceFile = {
                context.preferencesDataStoreFile(DATA_STORE_FILE_NAME)
            }
        )
    }
}

/**
 * PreferenceDataStore の実装を DI コンテナへ登録する Hilt Module。
 *
 * 本 Module は、PreferenceDataStore の契約に対して、
 * AndroidPreferenceDataStore 実装を紐付ける。
 *
 * ■ 設計上の意図
 *   利用側は AndroidPreferenceDataStore を直接参照せず、
 *   PreferenceDataStore の契約に依存する。
 *
 *   これにより、テスト時や保存方式変更時に実装を差し替えやすくする。
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class PreferenceDataStoreBindModule {

    /**
     * PreferenceDataStore の実装として AndroidPreferenceDataStore を登録する。
     *
     * @param impl AndroidX DataStore Preferences を利用する実装。
     * @return PreferenceDataStore 契約として公開する実装。
     */
    @Binds
    @Singleton
    abstract fun bindPreferenceDataStore(
        impl: AndroidPreferenceDataStore
    ): PreferenceDataStore
}
