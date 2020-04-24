package io.github.starwishsama.namelessbot.managers;

import io.github.starwishsama.namelessbot.BotMain;
import io.github.starwishsama.namelessbot.objects.ShopItem;
import io.github.starwishsama.namelessbot.objects.user.BotUser;

import java.io.IOException;

public class ShopManager {
    public static String executeBuy(ShopItem item, BotUser user) throws IOException {
        String command = item.getItemCommand();
        if (command.startsWith("bot")) {
            String[] cmds = command.split(" ");
            if (cmds[1].equalsIgnoreCase("time")) {
                user.addTime(1);
                user.cost(item.getPoint());
                return "购买成功";
            }
        } else {
            if (BotMain.getRcon() != null) {
                String result = BotMain.getRcon().command(command);
                if (result != null){
                    user.cost(item.getPoint());
                }
                return "购买成功";
            }
        }
        return "发生了意外问题, 请联系管理员.";
    }
}