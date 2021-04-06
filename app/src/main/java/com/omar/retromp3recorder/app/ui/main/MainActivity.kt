package com.omar.retromp3recorder.app.ui.main

import android.Manifest
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.alkurop.jpermissionmanager.PermissionOptionalDetails
import com.github.alkurop.jpermissionmanager.PermissionRequiredDetails
import com.github.alkurop.jpermissionmanager.PermissionsManager
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.uiutils.observe
import java.util.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val permissionsManager: PermissionsManager by lazy { PermissionsManager(this) }
    private val permissionsMap: Map<String, PermissionOptionalDetails> by lazy { createPermissionsMap() }

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.state.observe(this, ::renderView)
    }

    private fun renderView(state: MainView.State) {
        renderPermissions(state.requestForPermissions.ghost)
    }

    private fun createPermissionsMap(): Map<String, PermissionOptionalDetails> =
        listOf(
            Pair(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                PermissionRequiredDetails(
                    getString(R.string.write_permission_title),
                    getString(R.string.write_permission_message),
                    getString(R.string.write_required_message)
                )
            ),
            Pair(
                Manifest.permission.RECORD_AUDIO,
                PermissionRequiredDetails(
                    getString(R.string.record_permission_title),
                    getString(R.string.record_permission_message),
                    getString(R.string.record_required_message)
                )
            )
        ).toMap()

    private fun renderPermissions(requestForPermissions: Set<String>?) {
        if (requestForPermissions == null) {
            return
        }
        val permissionRequests = HashMap<String, PermissionOptionalDetails?>()
        for (permissionName in requestForPermissions) {
            permissionRequests[permissionName] = permissionsMap[permissionName]
        }
        permissionsManager.addPermissions(permissionRequests)
        permissionsManager.makePermissionRequest(true)
    }
}
