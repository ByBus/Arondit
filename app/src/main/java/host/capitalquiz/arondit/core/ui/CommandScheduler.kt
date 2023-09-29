package host.capitalquiz.arondit.core.ui

import android.os.Handler
import android.os.Looper
import androidx.annotation.IntRange
import java.util.LinkedList
import java.util.Queue

class CommandScheduler {
    private val queue: Queue<() -> Unit> = LinkedList()
    private val handler = Handler(Looper.getMainLooper())
    private var isInfinite = false
    private var totalIterations = 0
    private var repeatCount = 0

    private val nextCommand = Runnable {
        queue.poll()?.let { command ->
            if (isInfinite || repeatCount != totalIterations) queue.offer(command)
            command.invoke()
        }
    }

    fun schedule(context: CommandScheduler.() -> Unit): CommandScheduler {
        context.invoke(this)
        return this
    }

    fun execute() = nextCommand.run()

    fun cancel() {
        queue.clear()
        handler.removeCallbacks(nextCommand)
        repeatCount = 0
        totalIterations = 0
    }

    fun CommandScheduler.infinite() {
        isInfinite = true
        queue.offer{
            nextCommand.run()
        }
    }

    fun CommandScheduler.repeat(@IntRange(from = 1) times: Int) {
        totalIterations = times
        repeatCount = 0
        queue.offer {
            repeatCount++
            nextCommand.run()
        }
    }

    fun CommandScheduler.pause(millis: Long) {
        queue.offer { handler.postDelayed(nextCommand, millis) }
    }

    fun CommandScheduler.command(delay: Long = 0L, block: (Int) -> Unit) {
        if (delay > 0) pause(delay)
        queue.offer {
            block.invoke((repeatCount - 1).coerceAtLeast(0))
            nextCommand.run()
        }
    }
}