package io.github.starwishsama.namelessbot;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.PicqConfig;
import cc.moecraft.icq.accounts.BotAccount;
import cc.moecraft.icq.command.interfaces.IcqCommand;
import cc.moecraft.icq.sender.IcqHttpApi;
import cc.moecraft.logger.HyLogger;
import cc.moecraft.logger.environments.ColorSupportLevel;

import io.github.starwishsama.namelessbot.commands.*;
import io.github.starwishsama.namelessbot.config.Config;
import io.github.starwishsama.namelessbot.config.Message;
import io.github.starwishsama.namelessbot.entities.RssItem;
import io.github.starwishsama.namelessbot.utils.BotUtils;
import net.kronos.rkon.core.Rcon;
import net.kronos.rkon.core.ex.AuthenticationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BotMain {
    private static HyLogger logger;
    public static Rcon rcon;

    private static IcqCommand[] commands = new IcqCommand[]{
            new VersionCommand(),
            new DebugCommand(),
            new RefreshCacheCommand(),
            new ServerInfoCommand(),
            new CheckInCommand(),
            new InfoCommand(),
            new RConGroupCommand(),
            new MuteCommand(),
            new MusicCommand()
    };

    //private static IcqListener[] listeners = new IcqListener[]{
    //};

    public static void main(String[] args){
        try {
            Config.jarPath = getPath();
            System.out.println("[Path] Jar path is at" + Config.jarPath);
            System.out.println("[Path] Config path is at "+ Config.jarPath + "qiandao.json");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Config.loadCfg();
        Message.loadLang();

        PicqConfig cfg = new PicqConfig(Config.botPort).setUseAsyncCommands(true).setColorSupportLevel(ColorSupportLevel.OS_DEPENDENT);
        PicqBotX bot = new PicqBotX(cfg);
        cfg.setDebug(true);
        logger = bot.getLogger();
        bot.setUniversalHyExpSupport(true);
        bot.addAccount(Config.botName, Config.postUrl, Config.postPort);
        BotAccount account = new BotAccount(Config.botName, bot, Config.postUrl, Config.postPort);
        bot.enableCommandManager(Config.cmdPrefix);
        bot.getCommandManager().registerCommands(commands);
        // bot.getEventManager().registerListeners(listeners);
        bot.startBot();
        IcqHttpApi api = new IcqHttpApi(bot, account, Config.postUrl, Config.postPort);


        if (Config.rconPwd != null) {
            try {
                rcon = new Rcon(Config.rconUrl, Config.rconPort, Config.rconPwd);
                logger.log("[RCON] 已连接至服务器");
            } catch (IOException e) {
                logger.warning("[RCON] 连接至服务器时发生了错误, 错误信息: " + e.getMessage());
            } catch (AuthenticationException ae) {
                logger.warning("[RCON] RCON 密码有误, 请检查是否输入了正确的密码!");
            }
        }

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 7);
        c.set(Calendar.MINUTE, 5);

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (Calendar.getInstance().get(Calendar.MINUTE) == Config.autoSaveTime) {
                    Config.saveCfg();
                    logger.log("[Bot] 自动保存数据完成");
                }
            }
        }, c.getTime(), 1000 * 60 * 15);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Config.saveCfg();
            Message.saveLang();
        }));
    }

    // https://blog.csdn.net/df0128/article/details/90484684
    private static String getPath() {
        String path = BotMain.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (System.getProperty("os.name").toLowerCase().contains("dows")) {
            path = path.substring(1);
        }
        if (path.contains("jar")) {
            path = path.substring(0, path.lastIndexOf("."));
            return path;
        }
        return path.replace("target/classes/", "");
    }

    public static HyLogger getLogger(){
        return logger;
    }
}