package com.gizmin.bitstore

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.Disposable

abstract class BaseActivity : AppCompatActivity(), DisposableObservable {

    private val listDisposable = mutableListOf<Disposable>()

    override fun addDisposable(disposable: Disposable) {
        listDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        listDisposable.forEach { if(!it.isDisposed) it.dispose() }
    }
}

interface DisposableObservable {
    fun addDisposable(disposable : Disposable)
}