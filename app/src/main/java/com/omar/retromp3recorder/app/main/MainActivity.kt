package com.omar.retromp3recorder.app.main

import android.Manifest
import android.annotation.SuppressLint
import android.media.audiofx.Visualizer
import android.media.audiofx.Visualizer.OnDataCaptureListener
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.alkurop.jpermissionmanager.PermissionOptionalDetails
import com.github.alkurop.jpermissionmanager.PermissionRequiredDetails
import com.github.alkurop.jpermissionmanager.PermissionsManager
import com.jakewharton.rxbinding3.view.clicks
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.customviews.VisualizerView
import com.omar.retromp3recorder.app.recording.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.app.recording.recorder.Mp3VoiceRecorder.SampleRate
import com.squareup.picasso.Picasso
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val actionPublishSubject = PublishSubject.create<MainView.Action>()
    private val compositeDisposable = CompositeDisposable()
    private val scrollDownHandler = Handler(Looper.getMainLooper())
    private val stateSubject: Subject<MainView.State> = BehaviorSubject.create()

    private val playButton: ImageView by lazy { findViewById(R.id.iv_play) }
    private val recordButton: ImageView by lazy { findViewById(R.id.iv_record) }
    private val shareButton: ImageView by lazy { findViewById(R.id.iv_share) }

    private val logContainerView: LinearLayout by lazy { findViewById(R.id.ll_logHolder) }
    private val sampleRateContainer: LinearLayout by lazy { findViewById(R.id.left_radio_container) }
    private val bitRateContainer: LinearLayout by lazy { findViewById(R.id.right_radio_container) }

    private val scrollView: ScrollView by lazy { findViewById(R.id.scrollView) }
    private val visualizerView: VisualizerView by lazy { findViewById(R.id.visualizer) }
    private val background: ImageView by lazy { findViewById(R.id.background) }

    private val sampleRateGroup: List<RadioButton> by lazy { createSampleRateGroup() }
    private val bitRateGroup: List<RadioButton> by lazy { createBitRateGroup() }

    private val checkboxHeight by lazy { resources.getDimensionPixelSize(R.dimen.cb_height) }

    private val permissionsManager: PermissionsManager by lazy { PermissionsManager(this) }
    private val permissionsMap: Map<String, PermissionOptionalDetails> by lazy { createPermissionsMap() }

    private var visualizer: Visualizer? = null

    @Inject
    lateinit var interactor: MainViewInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        addTitleView(bitRateContainer, getString(R.string.bit_rate))
        addTitleView(sampleRateContainer, getString(R.string.sample_rate))
        setUpButtonListeners()
        Picasso.with(this).load(R.drawable.bg).fit().into(background)
        val disposable = actionPublishSubject
            .compose(interactor.process())
            .compose(MainViewResultMapper.map())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { mainViewModel: MainView.MainViewModel ->
                renderView(
                    mainViewModel
                )
            }
        compositeDisposable.add(disposable)
    }

    private fun renderView(mainViewModel: MainView.MainViewModel) {
        renderPermissions(mainViewModel.requestForPermissions.ghost)
        renderBitrate(mainViewModel.bitRate)
        renderSampleRate(mainViewModel.sampleRate)
        renderError(mainViewModel.error.ghost?.bell(this))
        renderMessage(mainViewModel.message.ghost?.bell(this))
        renderState(mainViewModel.state)
        renderPlayerId(mainViewModel.playerId.ghost)
    }

    private fun addTitleView(container: ViewGroup, title: String) {
        layoutInflater.inflate(R.layout.container_title, container)
        val titleView = container.findViewById<TextView>(R.id.title)
        titleView.text = title
    }

    private fun createSampleRateGroup(): List<RadioButton> =
        SampleRate.values().map { sampleRate ->
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
        compositeDisposable.addAll(
            playButton.clicks().map { stateSubject.blockingFirst() }
                .subscribe { state ->
                    when (state) {
                        MainView.State.Idle,
                        MainView.State.Recording -> actionPublishSubject.onNext(MainView.Action.Play)
                        MainView.State.Playing -> actionPublishSubject.onNext(MainView.Action.Stop)
                    }
                },
            recordButton.clicks().map { stateSubject.blockingFirst() }
                .subscribe { state ->
                    when (state) {
                        MainView.State.Idle,
                        MainView.State.Playing -> actionPublishSubject.onNext(MainView.Action.Record)
                        MainView.State.Recording -> actionPublishSubject.onNext(MainView.Action.Stop)
                    }
                },
            shareButton.clicks().subscribe { actionPublishSubject.onNext(MainView.Action.Share) }
        )
        bitRateGroup.mapIndexed { index, radioButton ->
            radioButton.clicks().subscribe {
                val bitRate = Mp3VoiceRecorder.BitRate.values()[index]
                actionPublishSubject.onNext(MainView.Action.BitRateChange(bitRate))
            }
        }.forEach { compositeDisposable.add(it) }

        sampleRateGroup.mapIndexed { index, radioButton ->
            radioButton.clicks()
                .subscribe {
                    val sampleRate = SampleRate.values()[index]
                    actionPublishSubject.onNext(MainView.Action.SampleRateChange(sampleRate))
                }
        }.forEach { compositeDisposable.add(it) }
    }

    private fun renderPlayerId(playerId: Int?) {
        if (playerId == null) {
            return
        }
        visualizer = Visualizer(playerId).apply {
            captureSize = Visualizer.getCaptureSizeRange()[1]
            setDataCaptureListener(object : OnDataCaptureListener {
                override fun onWaveFormDataCapture(
                    visualizer: Visualizer,
                    bytes: ByteArray,
                    samplingRate: Int
                ) = visualizerView.updateVisualizer(bytes)

                override fun onFftDataCapture(
                    visualizer: Visualizer,
                    bytes: ByteArray,
                    samplingRate: Int
                ) = Unit
            }, Visualizer.getMaxCaptureRate() / 2, true, false)
            enabled = true
        }
    }

    private fun stopVisualizer() {
        compositeDisposable.add(Completable
            .fromAction {
                if (visualizer != null) {
                    visualizer?.release()
                    visualizer = null
                }
            }
            .subscribeOn(Schedulers.computation())
            .subscribe())
    }

    private fun renderState(state: MainView.State) {
        stateSubject.onNext(state)
        playButton.setImageResource(
            if (state == MainView.State.Playing) R.drawable.ic_action_stop
            else R.drawable.ic_action_play
        )
        recordButton.setImageResource(
            if (state == MainView.State.Recording) R.drawable.ic_action_stop
            else R.drawable.ic_action_rec
        )
        if (state == MainView.State.Idle) stopVisualizer()
    }

    private fun renderMessage(message: String?) {
        if (message == null) {
            return
        }
        @SuppressLint("InflateParams") val inflate = layoutInflater.inflate(
            R.layout.log_view,
            null
        ) as TextView
        logContainerView.addView(inflate)
        inflate.text = message
        scrollDownHandler.postDelayed(
            { scrollView.fullScroll(View.FOCUS_DOWN) },
            DELAY_MILLIS.toLong()
        )
    }

    private fun renderError(error: String?) {
        if (error == null) {
            return
        }
        @SuppressLint("InflateParams") val inflate =
            layoutInflater.inflate(R.layout.log_view, null) as TextView
        logContainerView.addView(inflate)
        inflate.text = getString(
            R.string.error_string,
            error
        )
        inflate.setTextColor(
            ContextCompat.getColor(
                this,
                android.R.color.holo_orange_light
            )
        )
        scrollDownHandler.postDelayed(
            { scrollView.fullScroll(View.FOCUS_DOWN) }, DELAY_MILLIS
                .toLong()
        )
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

    private fun renderSampleRate(sampleRate: SampleRate) {
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
        scrollDownHandler.removeCallbacksAndMessages(null)
        stopVisualizer()
    }
}

private const val DELAY_MILLIS = 150
