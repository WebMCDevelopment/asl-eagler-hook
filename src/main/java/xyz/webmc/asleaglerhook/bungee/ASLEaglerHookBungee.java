package xyz.webmc.asleaglerhook.bungee;

import xyz.webmc.asleaglerhook.base.ASLEaglerHook;
import xyz.webmc.asleaglerhook.base.EventPriority;
import xyz.webmc.asleaglerhook.base.IASLEaglerHookPlugin;
import xyz.webmc.asleaglerhook.base.asl.EaglerPingEventWrapper;
import xyz.webmc.asleaglerhook.bungee.asl.BungeePlayerImpl;
import xyz.webmc.asleaglerhook.bungee.asl.BungeePreServerListSetEvent;
import xyz.webmc.asleaglerhook.bungee.asl.BungeeProxyImpl;

import ch.andre601.advancedserverlist.api.events.GenericServerListEvent;
import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.api.bungeecord.events.PostServerListSetEvent;
import ch.andre601.advancedserverlist.api.bungeecord.events.PreServerListSetEvent;
import ch.andre601.advancedserverlist.core.compat.papi.PAPIUtil;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.objects.CachedPlayer;
import net.lax1dude.eaglercraft.backend.server.api.bungee.event.EaglercraftMOTDEvent;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

@SuppressWarnings({ "unchecked" })
public final class ASLEaglerHookBungee extends Plugin implements IASLEaglerHookPlugin<Favicon>, Listener {
  private PluginCore<Favicon> plugin;

  @Override
  public final void onEnable() {
    this.getProxy().getPluginManager().registerListener(this, this);
    this.plugin = (PluginCore<Favicon>) this.getProxy().getPluginManager().getPlugin("AdvancedServerList");
  }

  @EventHandler(priority = EventPriority.EAGLER_MOTD_EVENT)
  public final void onEaglerMOTD(final EaglercraftMOTDEvent event) {
    final ProfileEntry entry = ASLEaglerHook.handleEvent(new EaglerPingEventWrapper(this, event.getMOTDConnection()));
    this.getProxy().getPluginManager().callEvent(new PostServerListSetEvent(entry));
  }

  @Override
  public final byte[] getFaviconBytes(final Favicon favicon) {
    return ASLEaglerHook.getDataURIBytes(favicon.getEncoded());
  }

  @Override
  public final Class<Favicon> getFaviconClass() {
    return Favicon.class;
  }

  @Override
  public final String parsePAPIPlaceholders(final String text, final GenericPlayer player) {
    if (this.getProxy().getPluginManager().getPlugin("PAPIProxyBridge") != null) {
      if (PAPIUtil.get().isCompatible()) {
        final String server = PAPIUtil.get().getServer();
        if (server != null && !server.isEmpty()) {
          final ServerInfo serverInfo = this.getProxy().getServerInfo(server);
          if (serverInfo != null && !serverInfo.getPlayers().isEmpty()) {
            final ProxiedPlayer carrier = PAPIUtil.get().getPlayer(serverInfo.getPlayers());
            if (carrier != null) {
              return PAPIUtil.get().parse(text, carrier.getUniqueId(), player.getUUID());
            }
          }
        }
      }
    }
    return text;
  }

  @Override
  public final PluginCore<Favicon> getASL() {
    return this.plugin;
  }

  @Override
  public final GenericPlayer createPlayer(final CachedPlayer player, final int protocol) {
    return new BungeePlayerImpl(player, protocol);
  }

  @Override
  public final GenericServer createServer(final int playersOnline, final int playersMax, final String host) {
    return new BungeeProxyImpl(this.getProxy().getServers(), playersOnline, playersMax, host);
  }

  @Override
  public final GenericServerListEvent callEvent(final ProfileEntry entry) {
    final PreServerListSetEvent event = new BungeePreServerListSetEvent(entry);
    this.getProxy().getPluginManager().callEvent(event);
    return event;
  }

  @Override
  public final boolean isMaintenancePluginLoaded() {
    return this.getProxy().getPluginManager().getPlugin("Maintenance") != null;
  }
}
