package arctic.ac.listener.bukkit;

import arctic.ac.Arctic;
import arctic.ac.data.PlayerData;
import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

public class JoinLeaveListener implements Listener, PluginMessageListener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Arctic.INSTANCE.getDataManager().add(event.getPlayer());


        final PlayerData data = Arctic.INSTANCE.getDataManager().getPlayerData(event.getPlayer());
        addChannel(event.getPlayer(), "MC|BRAND");

        data.getInteractData().setTicksSinceJoin(0);

        if (event.getPlayer().hasPermission("alerts.see") && event.getPlayer().hasPermission("alerts.command")) {
            if (event.getPlayer().hasMetadata("ALERTS_ON_NORMAL") || event.getPlayer().hasMetadata("ALERTS_ON_VERBOSE"))
                return;

            event.getPlayer().setMetadata("ALERTS_ON_NORMAL", new FixedMetadataValue(Arctic.INSTANCE, true));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final PlayerData data = Arctic.INSTANCE.getDataManager().getPlayerData(event.getPlayer());

        if (Arctic.INSTANCE.isCitizensPresent() && data.getInteractData().getEntityANPC() != null) {
            data.getInteractionData().getEntityANPC().destroy();
        }
        Arctic.INSTANCE.getDataManager().remove(event.getPlayer());
    }

    @EventHandler
    public void onBow(EntityShootBowEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER) {
            final PlayerData data = Arctic.INSTANCE.getDataManager().getPlayerData((Player) event.getEntity());
            if (data == null) return;

            Arctic.INSTANCE.getDataThread().execute(() -> data.getInteractData().onBow());

        }
    }

    @EventHandler
    public void onEDBE(EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER) {
            final PlayerData data = Arctic.INSTANCE.getDataManager().getPlayerData((Player) event.getEntity());
            if (data == null) return;

            Arctic.INSTANCE.getDataThread().execute(() -> data.getInteractData().onEDBE());
            data.getInteractData().onEDBE();
        } else if (event.getEntity() instanceof Item) {
            Item item = (Item) event.getEntity();

            if (item.getItemStack().getType() == Material.INK_SACK
                    && item.getItemStack().getData().getData() == DyeColor.RED.getDyeData())
                event.setCancelled(true);

        }
    }

    @EventHandler
    public void onTeleport(final PlayerTeleportEvent event) {
        final PlayerData data = Arctic.INSTANCE.getDataManager().getPlayerData(event.getPlayer());
        if (data == null) return;

        data.getInteractData().handleEventTeleport(event);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        try {
            PlayerData data = Arctic.INSTANCE.getDataManager().getPlayerData(player);
            if (data == null) return;
            data.getNetworkProcessor().setClientBrand(new String(message, "UTF-8").substring(1));
            data.getNetworkProcessor().setClientVersion(PacketEvents.get().getPlayerUtils().getClientVersion(player));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void addChannel(Player player, String channel) {
        try {
            player.getClass().getMethod("addChannel", String.class).invoke(player, channel);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {

        if (!(event.getItem().getItemStack().getData().getData() == (short) DyeColor.RED.getData())) return;
        if (event.getItem().getItemStack().getItemMeta().getDisplayName() == null) return;
        if (!(event.getItem().getItemStack().getItemMeta().getDisplayName().contains("Blood"))) return;

        event.setCancelled(true);
    }
}
