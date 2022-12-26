package net.starly.store.regionbgm.data;

import net.starly.core.data.Config;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import static net.starly.store.regionbgm.RegionBGM.plugin;

public class RegionBGMObj {

    private Player player;


    public RegionBGMObj(Player player) {
        this.player = player;
    }


    /**
     * 플레이어의 토글 정보를 생성합니다.
     */
    public void createPlayerData() {
        Config data = new Config("data/" + player.getUniqueId(), plugin);

        if (!data.isFileExist()) {
            data.setBoolean("toggle", true);
        }
    }


    /**
     * 구역브금을 생성합니다.
     *
     * @param region 구역 이름
     * @param bgmName    브금 이름
     * @param length 브금 길이
     * @param volume 브금 불륨
     * @param pitch  브금 높낮이
     * @param loop   브금 반복 여부
     */
    public void createRegionBGM(String region, String bgmName, Integer length, Integer volume, Integer pitch, Boolean loop) {
        Config config = new Config("bgm", plugin);
        config.loadDefaultConfig();

        ConfigurationSection section = config.getConfig().getConfigurationSection("bgm." + region);

        if (section == null) { //섹션이 없다면
            config.getConfig().createSection("bgm." + region); //섹션 만듬
            section = config.getConfig().getConfigurationSection("bgm." + region); //섹션 다시 불러옴

            section.set("bgm", bgmName);
            section.set("length", length);
            section.set("volume", volume);
            section.set("pitch", pitch);
            section.set("loop", loop);
        }

        config.saveConfig();
    }


    /**
     * 구역브금을 삭제합니다.
     *
     * @param region 구역 이름
     */
    public void removeRegionBGM(String region) {
        Config config = new Config("bgm/" + region, plugin);
        ConfigurationSection section = config.getConfig().getConfigurationSection(region);

        if (section != null) {
            config.getConfig().set(region, null);
        }
    }


    public void createBGM(String music) {

    }
}