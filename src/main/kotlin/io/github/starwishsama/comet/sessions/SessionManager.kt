package io.github.starwishsama.comet.sessions

import io.github.starwishsama.comet.utils.TaskUtil
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.MessageEvent
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

/**
 * 管理运行中产生的会话
 *
 * @author Nameless
 */
object SessionManager {
    /**
     * 会话列表
     */
    private val sessions: MutableMap<Session, LocalDateTime> = HashMap()

    init {
        TaskUtil.runScheduleTaskAsync(3, 3, TimeUnit.MINUTES) {
            val timeNow = LocalDateTime.now()
            sessions.forEach { (session, time) ->
                if (time.plusMinutes(3).isAfter(timeNow)) {
                    this.expireSession(session)
                }
            }
        }
    }

    fun addSession(session: Session) {
        sessions[session] = LocalDateTime.now()
    }

    fun addSession(session: Session, time: LocalDateTime) {
        sessions[session] = time
    }

    fun expireSession(session: Session) {
        sessions.remove(session)
    }

    fun expireSession(id: Long): Boolean {
        if (isValidSessionById(id)) {
            sessions.remove(getSession(id))
            return true
        }
        return false
    }

    fun isValidSessionById(id: Long): Boolean {
        return getSession(id) != null || isValidSessionByGroup(id)
    }

    fun isValidSessionByGroup(groupId: Long): Boolean {
        return getSessionByGroup(groupId) != null
    }

    private fun getSession(id: Long): Session? {
        if (sessions.isNotEmpty()) {
            for (session in sessions) {
                if (session.key.getUserById(id) != null) {
                    return session.key
                }
            }
        }
        return null
    }

    fun getSessionByGroup(id: Long): Session? {
        for (session in sessions) {
            if (session.key.groupId == id) {
                return session.key
            }
        }
        return null
    }

    fun getSessionByEvent(event: MessageEvent): Session? {
        return when (event) {
            is GroupMessageEvent -> {
                if (isValidSessionByGroup(event.group.id)) {
                    getSessionByGroup(event.group.id)
                } else {
                    getSession(event.sender.id)
                }
            }
            else -> getSession(event.sender.id)
        }
    }

    fun getSessions(): Map<Session, LocalDateTime> {
        return sessions
    }
}