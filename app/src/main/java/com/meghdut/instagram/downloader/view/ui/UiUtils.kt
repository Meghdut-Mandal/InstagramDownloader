package com.meghdut.instagram.downloader.view.ui

import com.apps.inslibrary.interfaces.HttpListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


fun <T> igRequest(handler: (HttpListener<T>) -> Unit) : Flow<T> =
    callbackFlow {
        val listener = object : HttpListener<T> {
            override fun onError(error: String?) {
                throw Exception(error)
            }

            override fun onLogin(isLoggedIn: Boolean) {
                Exception("$isLoggedIn issue").printStackTrace()
            }

            override fun onSuccess(result: T) {
               trySendBlocking(result).onFailure {
                   it?.printStackTrace()
               }

            }
        }
        handler(listener)

        awaitClose {
            println("Closed !!!")
        }
    }

