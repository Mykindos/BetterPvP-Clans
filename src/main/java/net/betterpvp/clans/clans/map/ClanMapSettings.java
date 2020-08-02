package net.betterpvp.clans.clans.map;

import io.github.bananapuncher714.cartographer.core.api.BooleanOption;
import io.github.bananapuncher714.cartographer.core.map.MapSettings;
import org.bukkit.configuration.file.FileConfiguration;

public class ClanMapSettings extends MapSettings {

    public ClanMapSettings(FileConfiguration fileConfiguration) {
        super(fileConfiguration);

        allowedZooms.clear();
        allowedZooms.add(1.0D);
        allowedZooms.add(2.0D);
        allowedZooms.add(4.0D);
        setDefaultZoom(1.0D);
        setRotation(BooleanOption.FALSE);

    }
}
