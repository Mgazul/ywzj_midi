package org.ywzj.midi.all;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.ywzj.midi.YwzjMidi;
import org.ywzj.midi.block.piano.CFXBlock;
import org.ywzj.midi.block.piano.U1HBlock;
import org.ywzj.midi.instrument.*;
import org.ywzj.midi.util.MidiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class AllInstruments {

    private final static ConcurrentHashMap<Integer, Instrument> ALL_INSTRUMENTS = new ConcurrentHashMap<>();
    public final static ConcurrentHashMap<ItemLike, Instrument> INSTRUMENTS_LOOKUP = new ConcurrentHashMap<>();

    public static final Instrument U1H = registerInstrument(() -> new UprightPiano("u1h", false, false, "a1", "c8"), () -> new U1HBlock(BlockBehaviour.Properties.of().strength(1f)), Type.BLOCK);
    public static final Instrument CFX = registerInstrument(() -> new GrandPiano("cfx", false, false, "a1", "c8"), () -> new CFXBlock(BlockBehaviour.Properties.of().strength(1f)), Type.BLOCK);

    public static void preRegister() {}

    public static Instrument registerInstrument(Supplier<Instrument> instrumentSupplier, Supplier<ItemLike> registrySupplier, Type type) {
        return registerInstrument(YwzjMidi.MODID, instrumentSupplier, registrySupplier, type);
    }

    public static Instrument registerInstrument(String namespace, Supplier<Instrument> instrumentSupplier, Supplier<ItemLike> registrySupplier, Type type) {
        Instrument instrument = instrumentSupplier.get();
        if (type.equals(Type.BLOCK)) {
            AllBlocks.registerBlock(namespace, instrument.getName(), () -> {
                Block block = (Block) registrySupplier.get();
                INSTRUMENTS_LOOKUP.put(block, instrument);
                return block;
            });
        } else if (type.equals(Type.ITEM)) {
            AllItems.registerItem(namespace, instrument.getName(), () -> {
                Item item = (Item) registrySupplier.get();
                INSTRUMENTS_LOOKUP.put(item, instrument);
                return item;
            });
        }
        AllSounds.registerKeys(namespace, instrument.getName(), MidiUtils.notationToNote(instrument.getKeyStart()), MidiUtils.notationToNote(instrument.getKeyEnd()), "");
        for (Instrument.Variant variant : instrument.getAllVariants()) {
            if (variant.getIndex().equals(0) && variant.getName().equals("raw")) {
                continue;
            }
            AllSounds.registerKeys(namespace, instrument.getName(), MidiUtils.notationToNote(variant.getKeyStart()), MidiUtils.notationToNote(variant.getKeyEnd()), variant.getName());
        }
        ALL_INSTRUMENTS.put(instrument.getIndex(), instrument);
        YwzjMidi.LOGGER.info("Registering instrument {} with id {} by {}", instrument.getName(), instrument.getIndex(), namespace);
        return instrument;
    }

    public static Instrument fromIndex(Integer index) {
        return ALL_INSTRUMENTS.get(index) == null ? AllInstruments.U1H : ALL_INSTRUMENTS.get(index);
    }

    public static List<Instrument> getInstruments() {
        return new ArrayList<>(ALL_INSTRUMENTS.values());
    }

    public enum Type {
        ITEM,
        BLOCK
    }

}
