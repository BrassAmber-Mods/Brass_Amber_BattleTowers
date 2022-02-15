package com.BrassAmber.ba_bt.block.tileentity;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.init.BTTileEntityTypes;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.Level;

public class StoneChestTileEntity extends GolemChestTileEntity {

	private Double SpawnersDestroyed = 0D;

	public StoneChestTileEntity() {
		super(BTTileEntityTypes.LAND_CHEST);
	}

	/**
	 * Single chest inventory name
	 */
	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("container.ba_bt.land_chest");
	}

	public void spawnerDestroyed() {
		this.SpawnersDestroyed = this.SpawnersDestroyed + 1D;
		if (this.SpawnersDestroyed == 2) {
			setNoLockKey();
		}
		BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, this.SpawnersDestroyed);
	}
}