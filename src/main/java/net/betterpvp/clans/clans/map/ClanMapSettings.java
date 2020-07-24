package net.betterpvp.clans.clans.map;

import io.github.bananapuncher714.cartographer.core.api.BooleanOption;
import io.github.bananapuncher714.cartographer.core.map.MapSettings;
import org.bukkit.configuration.file.FileConfiguration;

public class ClanMapSettings extends MapSettings {

    public ClanMapSettings(FileConfiguration fileConfiguration) {
        super(fileConfiguration);

        allowedZooms.clear();
        allowedZooms.add(2D);
        allowedZooms.add(3D);
        setRotation(BooleanOption.FALSE);

    }
}
