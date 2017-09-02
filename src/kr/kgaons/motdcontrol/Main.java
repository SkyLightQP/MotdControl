package kr.kgaons.motdcontrol;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Main extends JavaPlugin implements Listener{
    private FileConfiguration config;
    private File file = new File("plugins/MotdControl/config.yml");

    public void onEnable(){
        Bukkit.getLogger().info("[MotdControl] Enable!");
        getCommand("motd").setExecutor(this);
        getServer().getPluginManager().registerEvents(this, this);
        loadConfig();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if(args.length > 0) {
                String text = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
                // 그냥 하면 띄어쓰기를 쓸 수 때문에 위 코드로 띄어쓰기까지 합쳐서 만들어줍시다.
                config.set("motd.text", text);
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                p.sendMessage("§a[MotdConrol] 완료!");
            }
            else p.sendMessage("§c[MotdConrol] 사용법: /motd <내용>");
        }
        else Bukkit.getConsoleSender().sendMessage("§c[MotdControl] 인게임에서 사용이 가능합니다!");
        return false;
    }
    @EventHandler
    public void onServerPing(ServerListPingEvent e){
        if(config.get("motd.text") != null){  // 콘피그의 내용이 null이 아니라면
            String motd = config.getString("motd.text"); // 해당 내용을 불러와 변수에 담고
            e.setMotd(motd); // motd를 바꿔줍시다!
        }
    }
    private void loadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
        try {
            if (!file.exists()) {
                config.set("motd.text", "Hello!");
                config.save(file);
            }
            config.load(file);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }
    public void onDisable(){
        Bukkit.getLogger().info("[MotdControl] Disable!");
    }
}
