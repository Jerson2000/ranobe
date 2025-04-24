package org.ranobe.ranobe.di

import okhttp3.OkHttpClient
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.ranobe.ranobe.ViewModel
import org.ranobe.ranobe.data.repository.SourceRepository
import org.ranobe.ranobe.data.source.AllNovel
import org.ranobe.ranobe.util.CloudflareBypassInterceptor
import org.ranobe.ranobe.util.bypassSSLErrors

val appModule = module {
    single{
        OkHttpClient.Builder()
            .bypassSSLErrors()
            .build()
    }
    singleOf(::AllNovel){ bind<SourceRepository>()}
    viewModelOf(::ViewModel)
}