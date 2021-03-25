package com.omar.retromp3recorder.app.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.alkurop.jpermissionmanager.PermissionOptionalDetails
import com.github.alkurop.jpermissionmanager.PermissionRequiredDetails
import com.github.alkurop.jpermissionmanager.PermissionsManager
import com.jakewharton.rxbinding3.view.clicks
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val compositeDisposable = CompositeDisposable()

    private val sampleRateContainer: LinearLayout by lazy { findViewById(R.id.left_radio_container) }
    private val bitRateContainer: LinearLayout by lazy { findViewById(R.id.right_radio_container) }
    private val background: ImageView by lazy { findViewById(R.id.background) }

    private val sampleRateGroup: List<RadioButton> by lazy { createSampleRateGroup() }
    private val bitRateGroup: List<RadioButton> by lazy { createBitRateGroup() }

    private val checkboxHeight by lazy { resources.getDimensionPixelSize(R.dimen.cb_height) }
    private val permissionsManager: PermissionsManager by lazy { PermissionsManager(this) }
    private val permissionsMap: Map<String, PermissionOptionalDetails> by lazy { createPermissionsMap() }

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addTitleView(bitRateContainer, getString(R.string.bit_rate))
        addTitleView(sampleRateContainer, getString(R.string.sample_rate))
        setUpButtonListeners()
        Picasso.with(this).load(R.drawable.bg).fit().into(background)

        viewModel.state.observe(this, ::renderView)
    }

    private fun renderView(state: MainView.State) {
        renderPermissions(state.requestForPermissions.ghost)
        renderBitrate(state.bitRate)
        renderSampleRate(state.sampleRate)
    }

    private fun addTitleView(container: ViewGroup, title: String) {
        layoutInflater.inflate(R.layout.container_title, container)
        val titleView = container.findViewById<TextView>(R.id.title)
        titleView.text = title
    }

    private fun createSampleRateGroup(): List<RadioButton> =
        Mp3VoiceRecorder.SampleRate.values().map { sampleRate ->
            sampleRateContainer.addCheckBox(
                title = getString(
                    R.string.sample_rate_format,
                    sampleRate.value
                )
            )
        }

    private fun createBitRateGroup(): List<RadioButton> =
        Mp3VoiceRecorder.BitRate.values().map { bitRate ->
            bitRateContainer.addCheckBox(
                title = getString(
                    R.string.bit_rate_format,
                    bitRate.value
                )
            )
        }

    @SuppressLint("InflateParams")
    private fun ViewGroup.addCheckBox(title: String): RadioButton {
        val container = this
        return (layoutInflater.inflate(R.layout.checkbox, null) as RadioButton).apply {
            this.text = title
            this.height = checkboxHeight
            container.addView(this)
        }
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

    private fun setUpButtonListeners() {
        bitRateGroup.mapIndexed { index, radioButton ->
            radioButton.clicks().subscribe {
                val bitRate = Mp3VoiceRecorder.BitRate.values()[index]
                viewModel.onInput(MainView.Input.BitRateChange(bitRate))
            }
        }.forEach { compositeDisposable.add(it) }

        sampleRateGroup.mapIndexed { index, radioButton ->
            radioButton.clicks()
                .subscribe {
                    val sampleRate = Mp3VoiceRecorder.SampleRate.values()[index]
                    viewModel.onInput(MainView.Input.SampleRateChange(sampleRate))
                }
        }.forEach { compositeDisposable.add(it) }
    }

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

    private fun renderSampleRate(sampleRate: Mp3VoiceRecorder.SampleRate) {
        sampleRateGroup.forEachIndexed { index, radioButton ->
            radioButton.isChecked = sampleRate.ordinal == index
        }
    }

    private fun renderBitrate(bitRate: Mp3VoiceRecorder.BitRate) {
        bitRateGroup.forEachIndexed { index, radioButton ->
            radioButton.isChecked = bitRate.ordinal == index
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}

