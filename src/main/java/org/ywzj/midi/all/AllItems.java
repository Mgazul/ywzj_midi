package org.ywzj.midi.all;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.ywzj.midi.YwzjMidi;

import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class AllItems {

    public static final ConcurrentHashMap<String, DeferredRegister<Item>> ITEMS = new ConcurrentHashMap<>();

    public static final LinkedHashMap<String, RegistryObject<Item>> ITEMS_LOOKUP = new LinkedHashMap<>();

    public static <T extends Item> RegistryObject<Item> registerItem(String name, Supplier<T> item) {
        return registerItem(YwzjMidi.MODID, name, item);
    }

    public static <T extends Item> RegistryObject<Item> registerItem(String namespace, String name, Supplier<T> item) {
        DeferredRegister<Item> itemDeferredRegister = ITEMS.computeIfAbsent(namespace, k -> DeferredRegister.create(ForgeRegistries.ITEMS, namespace));
        RegistryObject<Item> registryObject = itemDeferredRegister.register(name, item);
        AllItems.ITEMS_LOOKUP.put(name, registryObject);
        return registryObject;
    }

    public static void register(IEventBus eventBus, String namespace) {
        if (ITEMS.get(namespace) != null) {
            ITEMS.get(namespace).register(eventBus);
        }
    }

}
