package net.starly.regionbgm.data;

import net.starly.core.data.Config;
import net.starly.core.data.util.Tuple;
import net.starly.region.api.RegionAPI;
import net.starly.regionbgm.RegionBGMMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("all")
public class RegionBGMGuiEditorObj {

    private Player player;
    private String name;


    public RegionBGMGuiEditorObj(Player player) {
        this.player = player;
    }


    /**
     * 구역브금 편집기를 엽니다.
     */
    public void openBGMEditor(String region) {


        Config bgm = new Config("bgm", RegionBGMMain.getPlugin());
        ConfigurationSection regions = bgm.getConfig().getConfigurationSection("bgm.");

        if (!regions.getKeys(false).contains(region)) {
            player.sendMessage(ChatColor.RED + "해당 구역은 존재하지 않습니다.");
            return;
        }

        Config config = new Config("config", RegionBGMMain.getPlugin());
        Inventory inv = Bukkit.createInventory(null,
                config.getConfig().getInt("edit_gui_settings.size") * 9,
                config.getConfig().getString("edit_gui_settings.title"));
        RegionMapData.regionMap.put(player, region);

        statusEditor(inv);
        bgmNameEditor(inv, region);
        lengthEditor(inv);
        volumeEditor(inv);
        pitchEditor(inv);

        ConfigurationSection section = bgm.getConfig().getConfigurationSection("bgm." + region);

        if (section.getBoolean("loop")) {
            loopTrueEditor(inv);
        } else {
            loopFalseEditor(inv);
        }

        player.openInventory(inv);
    }


    /**
     * 구역브금 편집기 - 브금 이름
     *
     * @param inv 편집기 인벤토리
     */
    private void bgmNameEditor(@NotNull Inventory inv, String bgmName) {
        Config config = new Config("config", RegionBGMMain.getPlugin());
        String name = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("edit_gui_settings.bgmName.name"));
        String material = config.getConfig().getString("edit_gui_settings.bgmName.material");
        List<String> lore = Translate.color(config.getConfig().getStringList("edit_gui_settings.bgmName.lore"));
        Config bgm = new Config("bgm", RegionBGMMain.getPlugin());
        lore.replaceAll(s -> s.replace("{bgm}", bgm.getString("bgm." + bgmName + ".bgm")));
        int slot = config.getConfig().getInt("edit_gui_settings.bgmName.slot");

        ItemStack item = new ItemStack(Material.valueOf(material));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);

        inv.setItem(slot, item);
    }


    /**
     * 구역브금 편집기 - 상태
     *
     * @param inv 편집기 인벤토리
     */
    private void statusEditor(Inventory inv) {
        Config config = new Config("config", RegionBGMMain.getPlugin());

        String name = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("edit_gui_settings.status.name").replace("{region}", RegionMapData.regionMap.get(player)));
        String material = config.getConfig().getString("edit_gui_settings.status.material");
        List<String> lore = Translate.color(config.getConfig().getStringList("edit_gui_settings.status.lore"));
        Config bgm = new Config("bgm", RegionBGMMain.getPlugin());
        lore.replaceAll(s -> s.replace("{bgm}", bgm.getString("bgm." + RegionMapData.regionMap.get(player) + ".bgm"))
                .replace("{length}", String.valueOf(bgm.getConfig().getDouble("bgm." + RegionMapData.regionMap.get(player) + ".length")))
                .replace("{volume}", String.valueOf(bgm.getConfig().getDouble("bgm." + RegionMapData.regionMap.get(player) + ".volume")))
                .replace("{pitch}", String.valueOf(bgm.getConfig().getDouble("bgm." + RegionMapData.regionMap.get(player) + ".pitch")))
                .replace("{loop}", String.valueOf(bgm.getConfig().getBoolean("bgm." + RegionMapData.regionMap.get(player) + ".loop"))));

        int slot = config.getConfig().getInt("edit_gui_settings.status.slot");

        ItemStack item = new ItemStack(Material.valueOf(material));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);

        setItem(inv, name, material, lore, slot);
    }


    /**
     * 구역브금 편집기 - 브금 길이
     *
     * @param inv 편집기 인벤토리
     */
    private void lengthEditor(Inventory inv) {
        Config config = new Config("config", RegionBGMMain.getPlugin());

        String name = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("edit_gui_settings.length.name"));
        String material = config.getConfig().getString("edit_gui_settings.length.material");
        List<String> lore = Translate.color(config.getConfig().getStringList("edit_gui_settings.length.lore"));
        Config bgm = new Config("bgm", RegionBGMMain.getPlugin());
        lore.replaceAll(s -> s.replace("{length}", String.valueOf(bgm.getConfig().getDouble("bgm." + RegionMapData.regionMap.get(player) + ".length"))));
        int slot = config.getConfig().getInt("edit_gui_settings.length.slot");

        ItemStack item = new ItemStack(Material.valueOf(material));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);

        setItem(inv, name, material, lore, slot);
    }


    /**
     * 구역브금 편집기 - 브금 불륨
     *
     * @param inv 편집기 인벤토리
     */
    private void volumeEditor(Inventory inv) {
        Config config = new Config("config", RegionBGMMain.getPlugin());

        String name = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("edit_gui_settings.volume.name"));
        String material = config.getConfig().getString("edit_gui_settings.volume.material");
        List<String> lore = Translate.color(config.getConfig().getStringList("edit_gui_settings.volume.lore"));
        Config bgm = new Config("bgm", RegionBGMMain.getPlugin());
        lore.replaceAll(s -> s.replace("{volume}", String.valueOf(bgm.getConfig().getDouble("bgm." + RegionMapData.regionMap.get(player) + ".volume"))));
        int slot = config.getConfig().getInt("edit_gui_settings.volume.slot");

        ItemStack item = new ItemStack(Material.valueOf(material));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);

        setItem(inv, name, material, lore, slot);
    }


    /**
     * 구역브금 편집기 - 브금 높낮이
     *
     * @param inv 편집기 인벤토리
     */
    private void pitchEditor(Inventory inv) {
        Config config = new Config("config", RegionBGMMain.getPlugin());

        String name = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("edit_gui_settings.pitch.name"));
        String material = config.getConfig().getString("edit_gui_settings.pitch.material");
        List<String> lore = Translate.color(config.getConfig().getStringList("edit_gui_settings.pitch.lore"));
        Config bgm = new Config("bgm", RegionBGMMain.getPlugin());
        lore.replaceAll(s -> s.replace("{pitch}", String.valueOf(bgm.getConfig().getDouble("bgm." + RegionMapData.regionMap.get(player) + ".pitch"))));
        int slot = config.getConfig().getInt("edit_gui_settings.pitch.slot");

        ItemStack item = new ItemStack(Material.valueOf(material));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);

        setItem(inv, name, material, lore, slot);
    }


    /**
     * 구역브금 편집기 - 브금 반복 여부 | 켜져있을 때
     *
     * @param inv 편집기 인벤토리
     */
    private void loopTrueEditor(Inventory inv) {
        Config config = new Config("config", RegionBGMMain.getPlugin());

        String name = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("edit_gui_settings.loop.true.name"));
        String material = config.getConfig().getString("edit_gui_settings.loop.true.material");
        List<String> lore = Translate.color(config.getConfig().getStringList("edit_gui_settings.loop.true.lore"));
        int slot = config.getConfig().getInt("edit_gui_settings.loop.true.slot");

        ItemStack item = new ItemStack(Material.valueOf(material));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);

        setItem(inv, name, material, lore, slot);
    }


    /**
     * 구역브금 편집기 - 브금 반복 여부 | 꺼져있을 때
     *
     * @param inv 편집기 인벤토리
     */
    private void loopFalseEditor(Inventory inv) {
        Config config = new Config("config", RegionBGMMain.getPlugin());

        String name = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("edit_gui_settings.loop.false.name"));
        String material = config.getConfig().getString("edit_gui_settings.loop.false.material");
        List<String> lore = Translate.color(config.getConfig().getStringList("edit_gui_settings.loop.false.lore"));
        int slot = config.getConfig().getInt("edit_gui_settings.loop.false.slot");

        ItemStack item = new ItemStack(Material.valueOf(material));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);

        setItem(inv, name, material, lore, slot);
    }


    /**
     * ItemStack을 저장합니다.
     *
     * @param inv      인벤토리
     * @param name     아이템 이름
     * @param material 아이템 종류
     * @param lore     아이템 로어
     * @param slot     아이템 슬롯
     */
    private void setItem(@NotNull Inventory inv, String name, String material, List<String> lore, int slot) {

        ItemStack itemStack = new ItemStack(Material.valueOf(material));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        inv.setItem(slot, itemStack);
    }


    /**
     * 브금을 변경합니다.
     *
     * @param event AsyncPlayerChatEvent
     */
    public void changeBGM(@NotNull AsyncPlayerChatEvent event) {

        String message = event.getMessage();
        StringData stringData = new StringData();

        if (RegionMapData.changeBgmMap.containsKey(player) && RegionMapData.guiType.get(player) == GuiEditor.BGM) {
            event.setCancelled(true);

            if (message.equalsIgnoreCase("cancel") || message.equalsIgnoreCase("취소")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', stringData.msgCancelChangeBGM()));
                RegionMapData.changeBgmMap.remove(player);
                return;
            } else {


                Config config = new Config("bgm", RegionBGMMain.getPlugin());
                ConfigurationSection section = config.getConfig().getConfigurationSection("bgm." + RegionMapData.changeBgmMap.get(player));

                RegionAPI regionAPI = new RegionAPI(RegionBGMMain.getPlugin());
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (regionAPI.getRegion(RegionMapData.changeBgmMap.get(player)).contains(onlinePlayer.getLocation())) {
                        onlinePlayer.stopSound(section.getString("bgm"));

                        if (section.getBoolean("loop")) {
                            Bukkit.getScheduler().cancelTask(RegionMapData.taskIdMap.get(name + " " + player.getName()));
                        }
                    }
                }

                section.set("bgm", message);
                config.saveConfig();


                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (regionAPI.getRegion(RegionMapData.changeBgmMap.get(player)).contains(onlinePlayer.getLocation())) {
                        if (section.getBoolean("loop")) {
                            int taskId = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    onlinePlayer.stopSound(section.getString("bgm"));
                                    onlinePlayer.playSound(onlinePlayer.getLocation(), message,
                                            Float.parseFloat(section.getDouble("volume") + ""),
                                            Float.parseFloat(section.getDouble("pitch") + ""));
                                }
                            }.runTaskTimerAsynchronously(RegionBGMMain.getPlugin(), 0, section.getInt("length") * 20L).getTaskId();
                            RegionMapData.taskIdMap.put(name + " " + onlinePlayer.getUniqueId(), taskId);
                            System.out.println(Tuple.of(name, onlinePlayer.getName()) + " " + taskId);
                        }
                    }
                }


                RegionMapData.changeBgmMap.remove(player);
                RegionMapData.guiType.remove(player);

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', stringData.msgCompleteChangeBGMName())
                        .replace("{bgm}", message)
                        .replace("{region}", RegionMapData.regionMap.get(player)));


                if (RegionMapData.regionMap.containsKey(player))
                    RegionMapData.regionMap.remove(player);
            }
        }
    }


    /**
     * 구역브금 길이를 편집합니다
     *
     * @param event 플레이어가 채팅을 입력했을 때 발생하는 이벤트
     */
    public void changeBGMLength(@NotNull AsyncPlayerChatEvent event) {

        String message = event.getMessage();
        StringData stringData = new StringData();


        if (RegionMapData.changeBgmMap.containsKey(player) && RegionMapData.guiType.get(player) == GuiEditor.LENGTH) {
            event.setCancelled(true);

            if (message.equalsIgnoreCase("cancel") || message.equalsIgnoreCase("취소")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', stringData.msgCancelChangeLength()));
                RegionMapData.changeBgmMap.remove(player);
                return;
            } else {


                try {
                    Config config = new Config("bgm", RegionBGMMain.getPlugin());
                    ConfigurationSection section = config.getConfig().getConfigurationSection("bgm." + RegionMapData.changeBgmMap.get(player));

                    section.set("length", Integer.parseInt(message));
                    config.saveConfig();
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', stringData.errMsgNumberInvalid()));
                    return;
                }

                RegionMapData.changeBgmMap.remove(player);
                RegionMapData.guiType.remove(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', stringData.msgCompleteChangeBGMLength())
                        .replace("{length}", message)
                        .replace("{region}", RegionMapData.regionMap.get(player)));

                if (RegionMapData.regionMap.containsKey(player))
                    RegionMapData.regionMap.remove(player);

            }
        }
    }


    /**
     * 구역브금 볼륨을 편집합니다
     *
     * @param event InventoryClickEvent
     */
    public void changeBGMVolume(@NotNull InventoryClickEvent event) {

        Config bgm = new Config("bgm", RegionBGMMain.getPlugin());
        ConfigurationSection section = bgm.getConfig().getConfigurationSection("bgm." + RegionMapData.regionMap.get(player));

        if (event.getClick() == ClickType.LEFT) {
            section.set("volume", Float.parseFloat(section.getDouble("volume") + 0.1F + ""));
            bgm.saveConfig();
            openBGMEditor(RegionMapData.regionMap.get(player));
        } else if (event.getClick() == ClickType.RIGHT) {

            if (section.getDouble("volume") - 0.1F < 0) {
                section.set("volume", 0);
            } else {
                section.set("volume", Float.parseFloat(section.getDouble("volume") - 0.1F + ""));
            }
            bgm.saveConfig();
            openBGMEditor(RegionMapData.regionMap.get(player));
        } else if (event.getClick() == ClickType.SHIFT_LEFT) {

            section.set("volume", Float.parseFloat(section.getDouble("volume") + 1F + ""));
            bgm.saveConfig();
            openBGMEditor(RegionMapData.regionMap.get(player));
        } else if (event.getClick() == ClickType.SHIFT_RIGHT) {

            if (section.getDouble("volume") - 1F < 0) {
                section.set("volume", 0);
            } else {
                section.set("volume", Float.parseFloat(section.getDouble("volume") - 1F + ""));
            }
            bgm.saveConfig();
            openBGMEditor(RegionMapData.regionMap.get(player));
        }
    }


    /**
     * 구역브금 높낮이를 편집합니다
     *
     * @param event InventoryClickEvent
     */
    public void changeBGMPitch(@NotNull InventoryClickEvent event) {

        Config bgm = new Config("bgm", RegionBGMMain.getPlugin());
        ConfigurationSection section = bgm.getConfig().getConfigurationSection("bgm." + RegionMapData.regionMap.get(player));

        if (event.getClick() == ClickType.LEFT) {
            section.set("pitch", Float.parseFloat(section.getDouble("pitch") + 0.1F + ""));
            bgm.saveConfig();
            openBGMEditor(RegionMapData.regionMap.get(player));
        } else if (event.getClick() == ClickType.RIGHT) {

            if (section.getDouble("pitch") - 0.1F < 0) {
                section.set("pitch", 0);
            } else {
                section.set("pitch", Float.parseFloat(section.getDouble("pitch") - 0.1F + ""));
            }
            bgm.saveConfig();
            openBGMEditor(RegionMapData.regionMap.get(player));
        } else if (event.getClick() == ClickType.SHIFT_LEFT) {

            section.set("pitch", Float.parseFloat(section.getDouble("pitch") + 1F + ""));
            bgm.saveConfig();
            openBGMEditor(RegionMapData.regionMap.get(player));
        } else if (event.getClick() == ClickType.SHIFT_RIGHT) {

            if (section.getDouble("pitch") - 1F < 0) {
                section.set("pitch", 0);
            } else {
                section.set("pitch", Float.parseFloat(section.getDouble("pitch") - 1F + ""));
            }
            bgm.saveConfig();
            openBGMEditor(RegionMapData.regionMap.get(player));
        }
    }


    /**
     * 구역브금 반복 여부를 편집합니다
     */
    public void changeBGMLoop() {

        Config bgm = new Config("bgm", RegionBGMMain.getPlugin());
        ConfigurationSection section = bgm.getConfig().getConfigurationSection("bgm." + RegionMapData.regionMap.get(player));

        if (section.getBoolean("loop")) {
            section.set("loop", false);
            bgm.saveConfig();
            openBGMEditor(RegionMapData.regionMap.get(player));
        } else {
            section.set("loop", true);
            bgm.saveConfig();
            openBGMEditor(RegionMapData.regionMap.get(player));
        }
    }
}
