package com.github.rloic.phd.core.utils

import java.time.Duration
import java.time.Instant

fun Instant.elapsed(): Duration = Duration.between(this, Instant.now())