package io.github.starwishsama.comet.commands.chats

import io.github.starwishsama.comet.BotVariables
import io.github.starwishsama.comet.api.annotations.CometCommand
import io.github.starwishsama.comet.api.command.CommandProps
import io.github.starwishsama.comet.api.command.interfaces.ChatCommand
import io.github.starwishsama.comet.enums.UserLevel
import io.github.starwishsama.comet.objects.BotUser
import io.github.starwishsama.comet.utils.StringUtil.convertToChain
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.EmptyMessageChain
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.at
import java.time.format.DateTimeFormatter

@CometCommand
class InfoCommand : ChatCommand {
    override suspend fun execute(event: MessageEvent, args: List<String>, user: BotUser): MessageChain {
        try {
            if (args.isEmpty()) {
                return run {
                    var reply =
                        "\n积分: " + String.format("%.1f", user.checkInPoint) +
                                "\n累计连续签到了 " + user.checkInTime.toString() + " 天" + "\n上次签到于: " +
                                user.lastCheckInTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString() +
                                "\n权限组: " + user.level.toString() +
                                "\n命令条数: " + user.commandTime

                    if (user.bindServerAccount != null) {
                        reply = reply + "绑定的游戏账号是: " + user.bindServerAccount
                    }

                    if (event is GroupMessageEvent) {
                        event.sender.at() + reply.convertToChain()
                    } else {
                        reply.convertToChain()
                    }
                }
            } else if (args.size == 1 && args[0].contentEquals("排行") || args[0].contentEquals("ph")) {
                val users = BotVariables.users
                users.sortedByDescending { it.checkInPoint }
                val sb = StringBuilder()
                sb.append("积分排行榜").append("\n")
                return if (users.size > 9) {
                    for (i in 0..9) {
                        sb.append(i + 1).append(" ")
                            .append(users[i].id)
                            .append(" ").append(String.format("%.1f", users[i].checkInPoint)).append("\n")
                    }
                    (sb.toString().trim { it <= ' ' }).convertToChain()
                } else {
                    "数据不足".convertToChain()
                }
            } else {
                return getHelp().convertToChain()
            }
        } catch (e: Exception) {
            BotVariables.logger.error(e)
        }
        return EmptyMessageChain
    }

    override fun getProps(): CommandProps =
        CommandProps("info", arrayListOf("cx", "查询"), "查询积分等", "nbot.commands.info", UserLevel.USER)

    override fun getHelp(): String = """
        ======= 命令帮助 =======
        /cx 查询自己的积分信息
        /cx ph 查询积分排行榜
    """.trimIndent()

}
