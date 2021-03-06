package io.github.starwishsama.comet.objects

import io.github.starwishsama.comet.enums.MusicApiType
import io.github.starwishsama.comet.enums.PicSearchApiType
import kotlinx.serialization.Serializable
import java.net.Proxy

@Serializable
data class Config(
        //@Comment("机器人的账号")
        var botId: Long = 0,

        //@Comment("机器人的密码")
        var botPassword: String = "",

        //@Comment("自动保存数据周期, 单位分钟")
        val autoSaveTime: Long = 60,

        //@Comment("RCON 服务器地址, 不使用 RCON 功能可不填")
        var rConUrl: String? = null,

        //@Comment("RCON 服务器端口, 不使用 RCON 功能可不填")
        var rConPort: Int = 0,

        //@Comment("RCON 服务器连接密码, 不使用 RCON 功能可不填")
        var rConPassword: String? = null,

        //@Comment("执行所有机器人命令的全局冷却时间, 单位秒")
        val coolDownTime: Int = 5,

        //@Comment("机器人发送消息需要屏蔽的字词")
        val filterWords: MutableList<String> = mutableListOf(),

        //@Comment("点歌 API 类型")
        var musicApi: MusicApiType = MusicApiType.QQ,

        //@Comment("命令前缀")
        val commandPrefix: MutableList<String> = mutableListOf(".", "。", "#", "!", "/"),

        //@Comment("BiliBili 账号, 使用开播提醒时必填")
        var biliUserName: String? = null,

        //@Comment("BiliBili 账号密码, 使用开播提醒时必填")
        var biliPassword: String? = null,

        //@Comment("机器人主人QQ号, 对应的用户将自动获得 OWNER 权限组")
        var ownerId: Long = 0L,

        //@Comment("SauceNao 以图搜图 APIKey, 不填亦可, 但可搜索次数会减少")
        val sauceNaoApiKey: String? = null,

        //@Comment("彩虹六号玩家数据 API, 需要自行向 r6tab 作者申请")
        val r6tabKey: String? = null,

        //@Comment("用于获取 Twitter Token 的 Access Token")
        var twitterAccessToken: String? = null,

        //@Comment("用于获取 Twitter Token 的 Access Secret")
        var twitterAccessSecret: String? = null,

        //@Comment("用于使用 Twitter Developer API 的 Token")
        var twitterToken: String? = null,

        //@Comment("代理服务器地址, 目前仅支持 HTTP 代理")
        var proxyUrl: String = "",

        //@Comment("代理服务器端口, 目前仅支持 HTTP 代理")
        var proxyPort: Int = 0,

        //@Comment("代理类型, 支持 HTTP 和 SOCKS")
        var proxyType: Proxy.Type = Proxy.Type.SOCKS,

        //@Comment("Mirai 心跳周期, 单位分钟, 过长会导致被服务器断开连接")
        var heartBeatPeriod: Int = 2,

        //@Comment("Youtube APIKey, 申请请见 https://developers.google.com/youtube/v3/getting-started")
        var youtubeApiKey: String = "",

        //@Comment("推文推送时候是否发送小图而不是原图")
        var smallImageMode: Boolean = true,

        //@Comment("以图搜图 API 类型, 请使用 /ps source 命令修改")
        var pictureSearchApi: PicSearchApiType = PicSearchApiType.SAUCENAO,

        //@Comment("查询 BiliBili 主播开播动态间隔时间")
        val biliInterval: Long = 2,

        //@Comment("查询 Twitter 用户动态间隔时间")
        val twitterInterval: Long = 4,

        //@Comment("明日方舟抽卡模拟器是否使用图片")
        var arkDrawUseImage: Boolean = true,

        //@Comment("调试模式")
        var debugMode: Boolean = false,

        //@Comment("WebDriver 使用的浏览器")
        val webDriverName: String = "chrome",

        //@Comment("WebDriver Remote 地址")
        val remoteWebDriver: String = ""
)