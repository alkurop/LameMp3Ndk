package com.omar.retromp3recorder.app.files

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.utils.disposedBy
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class FilesFragment : Fragment() {

    @Inject
    lateinit var interactor: FilesInteractor

    private val compositeDisposable = CompositeDisposable()
    private val actionsBus = PublishSubject.create<FilesAction>()
    private lateinit var filesAdapter: FilesAdapter
    private val recyclerView: RecyclerView
        get() = view?.findViewById(R.id.recyclerView)!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_files, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        filesAdapter = FilesAdapter(
            onDeleteListener = { fileName ->
                actionsBus.onNext(FilesAction.DeleteFileAction(fileName))
            },
            onItemSelectListener = { fileName ->
                actionsBus.onNext(FilesAction.SelectFileAction(fileName))
            }
        )
        recyclerView.adapter = filesAdapter
        actionsBus
            .startWith(FilesAction.InitAction)
            .compose(interactor.process())
            .compose(map())
            .distinctUntilChanged()
            .observeOn(mainThread())
            .subscribe { renderView(it) }
            .disposedBy(compositeDisposable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    private fun renderView(viewModel: FilesViewModel) {
        filesAdapter.setData(viewModel.files, viewModel.selectedFile)
    }
}