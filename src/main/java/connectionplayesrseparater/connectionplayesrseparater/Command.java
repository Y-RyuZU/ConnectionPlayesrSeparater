package connectionplayesrseparater.connectionplayesrseparater;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class Command extends net.md_5.bungee.api.plugin.Command {
    public Command(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("cps.op")) {
            sender.sendMessage(ChatColor.RED + "ぽまえけんげんないやろ");
            return;
        }
        // 引数が指定されていない場合 (引数の個数が0以下の場合)
        if (args.length <= 0) {
            sender.sendMessage(ChatColor.GOLD + "------------------------使い方------------------------");
            sender.sendMessage(ChatColor.BLUE + "/" + getName() + " reload :リロード");
            return;
        }
        if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("r")) {
            ConnectionPlayesrSeparater.CPS.reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "リロード完了");
            return;
        }
        return;
    }
}
