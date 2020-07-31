package io.github.starwishsama.comet.commands.subcommands.chats

import io.github.starwishsama.comet.BotVariables
import io.github.starwishsama.comet.commands.CommandProps
import io.github.starwishsama.comet.commands.interfaces.SuspendCommand
import io.github.starwishsama.comet.commands.interfaces.UniversalCommand
import io.github.starwishsama.comet.enums.PicSearchApi
import io.github.starwishsama.comet.enums.UserLevel
import io.github.starwishsama.comet.objects.BotUser
import io.github.starwishsama.comet.sessions.Session
import io.github.starwishsama.comet.sessions.SessionManager
import io.github.starwishsama.comet.utils.BotUtil
import io.github.starwishsama.comet.utils.PictureSearchUtil
import io.github.starwishsama.comet.utils.toMsgChain
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.EmptyMessageChain
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.queryUrl

class PictureSearch : UniversalCommand, SuspendCommand {
    override suspend fun execute(event: MessageEvent, args: List<String>, user: BotUser): MessageChain {
        if (BotUtil.isNoCoolDown(event.sender.id)) {
            if (args.isEmpty() || SessionManager.isValidSessionById(event.sender.id)) {
                if (!SessionManager.isValidSessionById(event.sender.id)) {
                    SessionManager.addSession(Session(this, user.userQQ))
                }
                return BotUtil.sendMsgPrefix("请发送需要搜索的图片").toMsgChain()
            } else if (args[0].contentEquals("source") && args.size > 1) {
                try {
                    val api = PicSearchApi.valueOf(args[1])
                    BotVariables.cfg.pictureSearchApi = api
                } catch (e: Throwable) {
                    return BotUtil.sendMessage("该识图 API 不存在, 可用的 API 名称: ${PicSearchApi.values()}", true)
                }
            }
        }
        return EmptyMessageChain
    }

    override fun getProps(): CommandProps = CommandProps(
        "ps",
        arrayListOf("ytst", "st", "搜图", "以图搜图"),
        "以图搜图",
        "nbot.commands.picturesearch",
        UserLevel.USER
    )

    override fun getHelp(): String = """
        ======= 命令帮助 =======
        /ytst 以图搜图
    """.trimIndent()

    override suspend fun handleInput(event: MessageEvent, user: BotUser, session: Session) {
        SessionManager.expireSession(session)
        val image = event.message[Image]
        if (image != null) {
            event.reply("请稍等...")
            when (BotVariables.cfg.pictureSearchApi) {
                PicSearchApi.SAUCENAO -> {
                    val result = PictureSearchUtil.sauceNaoSearch(image.queryUrl())
                    when {
                        result.similarity >= 52.5 -> {
                            event.reply("相似度:${result.similarity}%\n原图链接:${result.originalUrl}\n")
                        }
                        result.similarity == -1.0 -> {
                            event.reply("在识图时发生了问题, 请联系管理员")
                        }
                        else -> {
                            event.reply("相似度过低 (${result.similarity}%), 请尝试更换图片重试")
                        }
                    }
                }
                PicSearchApi.ASCII2D -> {
                    val result = PictureSearchUtil.ascii2dSearch(image.queryUrl())
                    if (result.isNotEmpty()) {
                        event.reply("已找到可能相似的图片\n图片来源${result.originalUrl}\n打开 ascii2d 页面查看更多\n${result.openUrl}")
                    } else {
                        event.reply("找不到相似的图片")
                    }
                }
            }
        } else {
            event.reply("请发送图片!")
        }
    }
}