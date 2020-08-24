package com.prongbang.awvpr

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.DialogFragment

class ConfirmationDialogFragment : DialogFragment() {

	var onConfirmation: ((allowed: Boolean, resources: Array<String>) -> Unit)? = null

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val resources: Array<String> = arguments?.getStringArray(ARG_RESOURCES) ?: arrayOf()
		return AlertDialog.Builder(activity)
				.setMessage(getString(R.string.confirmation, TextUtils.join("\n", resources)))
				.setNegativeButton(R.string.deny) { _, _ ->
					onConfirmation?.invoke(false, resources)
				}
				.setPositiveButton(R.string.allow) { _, _ ->
					onConfirmation?.invoke(true, resources)
				}
				.create()
	}

	fun setConfirmation(
			onConfirmation: (allowed: Boolean, resources: Array<String>) -> Unit): ConfirmationDialogFragment {
		this.onConfirmation = onConfirmation
		return this
	}

	companion object {
		private const val ARG_RESOURCES = "resources"
		fun newInstance(resources: Array<String>): ConfirmationDialogFragment {
			val fragment = ConfirmationDialogFragment()
			val args = Bundle()
			args.putStringArray(ARG_RESOURCES, resources)
			fragment.arguments = args
			return fragment
		}
	}
}
