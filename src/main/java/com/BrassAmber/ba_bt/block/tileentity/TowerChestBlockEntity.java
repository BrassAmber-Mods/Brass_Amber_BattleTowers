package com.BrassAmber.ba_bt.block.tileentity;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.init.BTBlockEntityTypes;
import com.BrassAmber.ba_bt.init.BTTileEntityTypes;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.Level;

public class TowerChestBlockEntity extends GolemChestBlockEntity {

	private Double SpawnersDestroyed = 0D;

	public TowerChestBlockEntity() {
		super(BTBlockEntityTypes.LAND_CHEST);
	}

	/**
	 * Single chest inventory name
	 * @return
	 */
	@Override
	protected Component getDefaultName() {
		return new TranslatableComponent("container.ba_bt.land_chest");
	}

	public void spawnerDestroyed() {
		this.SpawnersDestroyed = this.SpawnersDestroyed + 1D;
		if (this.SpawnersDestroyed == 2) {
			setNoLockKey();
		}
		BrassAmberBattleTowers.LOGGER.log(Level.DEBUG, this.SpawnersDestroyed);
	}
}