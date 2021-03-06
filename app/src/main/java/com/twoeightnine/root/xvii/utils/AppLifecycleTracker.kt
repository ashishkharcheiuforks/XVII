package com.twoeightnine.root.xvii.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.twoeightnine.root.xvii.App
import com.twoeightnine.root.xvii.activities.LoginActivity
import com.twoeightnine.root.xvii.activities.PinActivity
import com.twoeightnine.root.xvii.lg.Lg
import com.twoeightnine.root.xvii.managers.Prefs
import com.twoeightnine.root.xvii.managers.Session
import com.twoeightnine.root.xvii.network.ApiService
import com.twoeightnine.root.xvii.network.response.BaseResponse
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AppLifecycleTracker : Application.ActivityLifecycleCallbacks {

    @Inject
    lateinit var api: ApiService

    private var disposable: Disposable? = null

    private var numStarted = 0

    init {
        App.appComponent?.inject(this)
    }

    private fun onForeground(context: Context) {
        if (Session.needToPromptPin()) {
            PinActivity.launch(context, PinActivity.ACTION_ENTER)
        }

        if (Prefs.beOnline) {
            startOnline()
        }
    }

    private fun onBackground() {
        stopOnline()
    }

    private fun ignore(activity: Activity?) =
            activity is LoginActivity ||
                    activity is PinActivity

    override fun onActivityStarted(activity: Activity?) {
        if (activity == null || ignore(activity)) return

        if (numStarted == 0) onForeground(activity)
        numStarted++
    }

    override fun onActivityStopped(activity: Activity?) {
        if (ignore(activity)) return

        numStarted--
        if (numStarted == 0) onBackground()
    }

    override fun onActivityPaused(activity: Activity?) {}

    override fun onActivityResumed(activity: Activity?) {}

    override fun onActivityDestroyed(activity: Activity?) {}

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {}

    private fun startOnline() {
        Lg.i("[online] start")
        disposable = Flowable.interval(0L, ONLINE_INTERVAL, TimeUnit.SECONDS)
                .flatMap {
                    api.setOnline()
                }
                .onErrorReturn {
                    Lg.wtf("[online] error: ${it.message}")
                    BaseResponse(response = 0)
                }
                .subscribe { response ->
                    Lg.i("[online] set status: ${response.response}")
                }
    }

    private fun stopOnline() {
        if (disposable?.isDisposed == false) {
            disposable?.dispose()
            Lg.i("[online] stop")
        }
    }

    companion object {

        private const val ONLINE_INTERVAL = 60L
    }
}