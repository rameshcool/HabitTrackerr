package ramesh.developer.habittrackerr.domain.model

import java.time.ZonedDateTime

data class ActivityDay(
    val date: ZonedDateTime,
    val completionRatio: Float  // 0.0–1.0
)
