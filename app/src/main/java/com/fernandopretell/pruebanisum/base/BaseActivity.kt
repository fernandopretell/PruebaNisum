package com.fernandopretell.pruebanisum.base

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.fernandopretell.pruebanisum.R
import com.fernandopretell.pruebanisum.util.ConnectivityReceiver
import com.fernandopretell.pruebanisum.util.LoadingView
import com.fernandopretell.pruebanisum.util.removeFromParent
import dagger.android.support.DaggerAppCompatActivity


abstract class BaseActivity : DaggerAppCompatActivity(),
    ConnectivityReceiver.ConnectivityReceiverListener {

    private var isReceiverRegistered = false
    private val loadingView by lazy { LoadingView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransitionEnter()
    }

    fun showProgressDialog(message: String) {
        loadingView.setMessage(message)
        if (loadingView.parent == null) {
            addContentView(
                loadingView, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    fun showLoading(show: Boolean) {
        if (show) showProgressDialog() else dismissProgressDialog()
    }

    fun showProgressDialog() = showProgressDialog(R.string.copy_loading)

    fun dismissProgressDialog() = loadingView.removeFromParent()

    fun showProgressDialog(@StringRes idMessage: Int) = showProgressDialog(getString(idMessage))

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
        isReceiverRegistered = true
    }

    override fun finish() {
        super.finish()
        overridePendingTransitionExit()
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected fun overridePendingTransitionEnter() {
        overridePendingTransition(R.animator.slide_from_right, R.animator.slide_to_left)
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected fun overridePendingTransitionExit() {
        overridePendingTransition(R.animator.slide_from_left, R.animator.slide_to_right)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    protected fun unregisterNetworkChanges() {
        try {
            unregisterReceiver(ConnectivityReceiver())
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        unregisterNetworkChanges()
    }

    abstract fun showNetworkMessage(isConnected: Boolean)

    fun isNetworkVConnected(context: Context):Boolean{
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
}