package io.github.starwishsama.comet.tasks

import java.util.concurrent.ScheduledFuture

object YTBStreamChecker : CometPusher {
    override val delayTime: Long = 10
    override val cycle: Long = 10
    override lateinit var future: ScheduledFuture<*>

    override fun retrieve() {
        TODO("Not yet implemented")
    }

    override fun push() {
        TODO("Not yet implemented")
    }
}