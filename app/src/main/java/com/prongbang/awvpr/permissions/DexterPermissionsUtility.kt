package com.prongbang.awvpr.permissions

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class DexterPermissionsUtility(private val activity: Activity) {

	fun checkCameraGranted(onNotGranted: () -> Unit, onGranted: () -> Unit) {
		val isCameraGranted = ContextCompat.checkSelfPermission(activity,
				Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
		val isReadExternalGranted = ContextCompat.checkSelfPermission(activity,
				Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
		val isWriteExternalGranted = ContextCompat.checkSelfPermission(activity,
				Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
		val isExternalGranted = isReadExternalGranted || isWriteExternalGranted
		if (isCameraGranted && isExternalGranted) {
			onGranted.invoke()
		} else {
			onNotGranted.invoke()
		}
	}

	fun requestCameraPermissions(onNotGranted: () -> Unit, onGranted: () -> Unit) {
		Dexter.withContext(activity)
				.withPermissions(Manifest.permission.CAMERA,
						Manifest.permission.READ_EXTERNAL_STORAGE,
						Manifest.permission.WRITE_EXTERNAL_STORAGE)
				.withListener(object : MultiplePermissionsListener {

					override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
						p0?.let {
							if (p0.areAllPermissionsGranted()) {
								onGranted.invoke()
							} else {
								onNotGranted.invoke()
							}
						}
					}

					override fun onPermissionRationaleShouldBeShown(
							p0: MutableList<PermissionRequest>?, p1: PermissionToken?) {
						p1?.continuePermissionRequest()
					}
				})
				.check()
	}
}