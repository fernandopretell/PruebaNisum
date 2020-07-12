package com.fernandopretell.pruebanisum.base

import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.fernandopretell.pruebanisum.R
import com.fernandopretell.pruebanisum.util.LoadingView
import com.fernandopretell.pruebanisum.util.removeFromParent

open class BaseFragment:Fragment() {

    private val loadingView by lazy { context?.let { LoadingView(it) } }

    fun showProgressDialog(message: String) {
        loadingView?.setMessage(message)
        if (loadingView?.parent == null) {
            activity?.addContentView(loadingView, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT))
        }
    }

    fun showLoading(show: Boolean) {
        if (show) showProgressDialog() else dismissProgressDialog()
    }

    fun showProgressDialog() = showProgressDialog(R.string.copy_loading)

    fun dismissProgressDialog() = loadingView?.removeFromParent()

    fun showProgressDialog(@StringRes idMessage: Int) = showProgressDialog(getString(idMessage))

}