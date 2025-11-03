package org.ywzj.midi.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.ywzj.midi.all.AllInstruments;
import org.ywzj.midi.blockentity.PianoBlockEntity;
import org.ywzj.midi.blockentity.TimpaniBlockEntity;
import org.ywzj.midi.gui.screen.*;
import org.ywzj.midi.instrument.Instrument;
import org.ywzj.midi.util.ComponentUtils;
import org.ywzj.midi.util.MathUtils;

import java.util.HashMap;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class ScreenManager {

    private static ViolScreen violinScreen;
    private static ViolScreen violaScreen;
    private static ViolScreen celloScreen;
    private static ViolScreen doubleBassScreen;
    private static WoodwindScreen oboeScreen;
    private static WoodwindScreen clarinetScreen;
    private static WoodwindScreen fluteScreen;
    private static WoodwindScreen bassoonScreen;
    private static BrassScreen hornScreen;
    private static BrassScreen trumpetScreen;
    private static BrassScreen tromboneScreen;
    private static BrassScreen tubaScreen;
    private static final HashMap<UUID, ServerMidiScreen> fakePlayerConductorScreens = new HashMap<>();

    public static void openPianoScreen(BlockPos pos, Instrument instrument, PianoBlockEntity pianoBlockEntity) {
        if (!checkDistance(pos, 3)) {
            return;
        }
        if (pianoBlockEntity.clavichordScreen == null) {
            pianoBlockEntity.clavichordScreen = new ClavichordScreen(instrument, pos, ComponentUtils.literal("钢琴"), "c4", "b6");
        }
        Minecraft.getInstance().tell(() -> Minecraft.getInstance().setScreen(pianoBlockEntity.clavichordScreen));
    }

    public static void openSpeakerScreen(BlockPos pos, MusicPlayerScreen musicPlayerScreen) {
        if (!checkDistance(pos, 2)) {
            return;
        }
        Minecraft.getInstance().tell(() -> Minecraft.getInstance().setScreen(musicPlayerScreen));
    }

    public static void openMusicPlayerScreen(MusicPlayerScreen musicPlayerScreen) {
        Minecraft.getInstance().tell(() -> Minecraft.getInstance().setScreen(musicPlayerScreen));
    }
    
    private static boolean checkDistance(BlockPos pos, int distance) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            if (MathUtils.distance(player.getX(), player.getY(), player.getZ(), pos.getX(), pos.getY(), pos.getZ()) > distance) {
                player.sendSystemMessage(ComponentUtils.translatable("info.ywzj_midi.warn_2"));
                return false;
            }
        }
        return true;
    }

}
