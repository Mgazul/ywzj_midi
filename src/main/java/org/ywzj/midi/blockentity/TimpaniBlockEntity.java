package org.ywzj.midi.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.ywzj.midi.util.MidiUtils;

public class TimpaniBlockEntity extends NoteBlockEntity {

    public TimpaniBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(pWorldPosition, pBlockState);
        this.note = MidiUtils.notationToNote("c3");
    }

}
