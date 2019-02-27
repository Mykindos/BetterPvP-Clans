package net.betterpvp.clans.economy.shops.nms;

import java.lang.reflect.Field;
import java.util.Map;

import net.battleau.clans.utility.UtilMessage;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityTypes;


public class UtilShop {

	public static void registerEntity(String name, int id, Class<? extends EntityInsentient> nmsClass, Class<? extends EntityInsentient> customClass){
		try {
			for (Field f : EntityTypes.class.getDeclaredFields()){
				if (f.getType().getSimpleName().equals(Map.class.getSimpleName())){
				
					if(f.getName().equalsIgnoreCase("f")){
						f.setAccessible(true);
						@SuppressWarnings("unchecked")
						Map<Class<? extends Entity>, Integer> map = (Map<Class<? extends Entity>, Integer>) f.get(null);
						map.put(customClass, id);
						UtilMessage.broadcast("A","b");
					}
				}
			}



		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
