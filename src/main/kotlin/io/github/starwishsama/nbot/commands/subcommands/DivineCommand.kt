package io.github.starwishsama.nbot.commands.subcommands

import cn.hutool.core.util.RandomUtil
import cn.hutool.extra.emoji.EmojiUtil
import io.github.starwishsama.nbot.BotConstants
import io.github.starwishsama.nbot.commands.CommandProps
import io.github.starwishsama.nbot.commands.interfaces.UniversalCommand
import io.github.starwishsama.nbot.enums.UserLevel
import io.github.starwishsama.nbot.objects.BotUser
import io.github.starwishsama.nbot.objects.RandomResult
import io.github.starwishsama.nbot.util.BotUtil
import io.github.starwishsama.nbot.util.BotUtil.getLocalMessage
import io.github.starwishsama.nbot.util.BotUtil.isNoCoolDown
import net.mamoe.mirai.message.ContactMessage
import net.mamoe.mirai.message.data.EmptyMessageChain
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.asMessageChain
import net.mamoe.mirai.message.data.toMessage

class DivineCommand : UniversalCommand {
    override suspend fun execute(message: ContactMessage, args: List<String>, user: BotUser): MessageChain {
        if (isNoCoolDown(message.sender.id) && args.isNotEmpty()) {
            val underCover = getResultFromList(BotConstants.underCovers, message.sender.id)
            return if (args.isNotEmpty()) {
                if (underCover == null) {
                    if (user.commandTime > 0 || user.level != UserLevel.USER) {
                        val randomEventName = BotUtil.getRestStringInArgs(args, 0)
                        if (randomEventName.length < 30 && EmojiUtil.extractEmojis(randomEventName).isEmpty()) {
                            val result = RandomResult(-1000, RandomUtil.randomDouble(0.0, 1.0), randomEventName)
                            user.decreaseTime()
                            RandomResult.getChance(result).toMessage().asMessageChain()
                        } else {
                            (getLocalMessage("msg.bot-prefix") + "需要占卜的东西太长了或者含有非法字符!").toMessage().asMessageChain()
                        }
                    } else {
                        (getLocalMessage("msg.bot-prefix") + "今日占卜次数已达上限, 如需增加次数请咨询机器人管理.").toMessage().asMessageChain()
                    }
                } else {
                    BotConstants.underCovers.minusElement(underCover)
                    RandomResult.getChance(underCover).toMessage().asMessageChain()
                }
            } else {
                return getHelp().toMessage().asMessageChain()
            }
        }
        return EmptyMessageChain
    }

    override fun getProps(): CommandProps = CommandProps("divine", arrayListOf("zb", "占卜"), "nbot.commands.divine", UserLevel.USER)
    override fun getHelp(): String = """
         ======= 命令帮助 =======
         /zb [占卜内容] 占卜
    """.trimIndent()

    private fun getResultFromList(results: List<RandomResult>, id: Long): RandomResult? {
        if (results.isNotEmpty()) {
            for (result in results) {
                if (result.id == id) {
                    return result
                }
            }
        }
        return null
    }
}