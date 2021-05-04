package com.omar.retromp3recorder.bl

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.omar.retromp3recorder.di.DaggerUseCaseComponent
import com.omar.retromp3recorder.storage.repo.RequestPermissionsRepo
import com.omar.retromp3recorder.utils.PermissionChecker
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class CheckPermissionsUCTest {
    @Inject
    lateinit var permissionChecker: PermissionChecker

    @Inject
    lateinit var requestPermissionsRepo: RequestPermissionsRepo

    @Inject
    lateinit var checkPermissionsUC: CheckPermissionsUC

    @Before
    fun setUp() {
        DaggerUseCaseComponent.create().inject(this)
    }

    @Test
    fun `if granted permissions RequestPermissionsRepo updated`() {
        whenever(permissionChecker.showUnchecked(any())) doReturn emptySet()

        checkPermissionsUC.execute(setOf("test")).test().assertComplete()

        requestPermissionsRepo.observe().test()
            .assertValue(RequestPermissionsRepo.ShouldRequestPermissions.Granted)
    }

    @Test
    fun `if denied permissions RequestPermissionsRepo updated`() {
        whenever(permissionChecker.showUnchecked(any())) doReturn setOf("test")

        checkPermissionsUC.execute(setOf("test")).test().assertComplete()

        requestPermissionsRepo.observe().test()
            .assertValue { value ->
                val castValue = (value as RequestPermissionsRepo.ShouldRequestPermissions.Denied)
                castValue.permissions.ghost == setOf("test")
            }
    }
}