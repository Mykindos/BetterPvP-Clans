package net.betterpvp.clans.economy.shops.nms;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import net.minecraft.server.v1_16_R1.*;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;


public class UtilShop {

    public static void registerEntity(String name, int id, Class<? extends Entity> nmsClass, Class<? extends Entity> customClass) {
        try {
            for (Field f : EntityTypes.class.getDeclaredFields()) {
                if (f.getType().getSimpleName().equals(Map.class.getSimpleName())) {

                    if (f.getName().equalsIgnoreCase("f")) {
                        f.setAccessible(true);
                        @SuppressWarnings("unchecked")
                        Map<Class<? extends Entity>, Integer> map = (Map<Class<? extends Entity>, Integer>) f.get(null);
                        map.put(customClass, id);
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
