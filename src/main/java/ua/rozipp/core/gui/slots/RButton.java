package ua.rozipp.core.gui.slots;

import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import ua.rozipp.core.gui.RGuiSlot;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author Redempt
 */
public class RButton extends RGuiSlot {

	private BiConsumer<InventoryClickEvent, RButton> biConsumer;
	private Consumer<InventoryClickEvent> consumer;
	private Runnable runnable;

	@Override
	public RGuiSlot clone() {
		return new RButton()
				.setBiConsumer(biConsumer)
				.setConsumer(consumer)
				.setRunnable(runnable)
				.setItemStack(getItemStack());
	}

	@Override
	public void onClick(InventoryClickEvent e) {
		if (biConsumer != null)
			biConsumer.accept(e, this);
		else if (consumer != null)
			consumer.accept(e);
		else if (runnable != null)
			runnable.run();
		else return;
		e.setCancelled(true);
		e.setResult(Event.Result.DENY);
	}

	public RButton setBiConsumer(BiConsumer<InventoryClickEvent, RButton> biConsumer) {
		this.biConsumer = biConsumer;
		return this;
	}

	public RButton setConsumer(Consumer<InventoryClickEvent> consumer) {
		this.consumer = consumer;
		return this;
	}

	public RButton setRunnable(Runnable runnable) {
		this.runnable = runnable;
		return this;
	}
}
