package com.koshertech.su.core.base

import android.app.job.JobService
import android.content.Context
import com.koshertech.su.core.patch

abstract class BaseJobService : JobService() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base.patch())
    }
}
