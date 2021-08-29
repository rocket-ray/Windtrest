package com.example.app_team35.repositories

import okhttp3.Credentials
import okhttp3.Interceptor

/**
 * Since met.no requires authentication, we have to make an interceptor to
 * add the "Authorization: Basic" HTTP header.
 *
 * Note that met.no also requires a unique user agent.
 */
class BasicAuthInterceptor(username: String, password: String): Interceptor {
    private var credentials: String = Credentials.basic(username, password)

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        request = request.newBuilder()
            .header("Authorization", credentials)
            .header("User-Agent", "appdev-uio")
            .build()

        return chain.proceed(request)
    }
}