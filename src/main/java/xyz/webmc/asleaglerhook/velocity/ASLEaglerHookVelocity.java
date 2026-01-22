package xyz.webmc.asleaglerhook.velocity;

import xyz.webmc.asleaglerhook.base.ASLEaglerHook;
import xyz.webmc.asleaglerhook.base.EventPriority;
import xyz.webmc.asleaglerhook.base.IASLEaglerHookPlugin;
import xyz.webmc.asleaglerhook.base.asl.EaglerPingEventWrapper;
import xyz.webmc.asleaglerhook.velocity.asl.VelocityPlayerImpl;
import xyz.webmc.asleaglerhook.velocity.asl.VelocityPreServerListSetEvent;
import xyz.webmc.asleaglerhook.velocity.asl.VelocityProxyImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import ch.andre601.advancedserverlist.api.events.GenericServerListEvent;
import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.api.velocity.events.PostServerListSetEvent;
import ch.andre601.advancedserverlist.api.velocity.events.PreServerListSetEvent;
import ch.andre601.advancedserverlist.core.compat.papi.PAPIUtil;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.objects.CachedPlayer;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.util.Favicon;
import net.lax1dude.eaglercraft.backend.server.api.velocity.event.EaglercraftMOTDEvent;

@SuppressWarnings({ "unchecked" })
public final class ASLEaglerHookVelocity implements IASLEaglerHookPlugin<Favicon> {
  private final ProxyServer proxy;
  private PluginCore<Favicon> plugin;

  @Inject
  public ASLEaglerHookVelocity(final ProxyServer proxy) {
    this.proxy = proxy;
  }
    
  @Subscribe
  public final void init(ProxyInitializeEvent event) {
    this.plugin = (PluginCore<Favicon>) this.proxy.getPluginManager().getPlugin("advancedserverlist").get().getInstance().get();
  }

  @Subscribe(priority = EventPriority.EAGLER_MOTD_EVENT)
  public final void onEaglerMOTD(final EaglercraftMOTDEvent event) {
    final ProfileEntry entry = ASLEaglerHook.handleEvent(new EaglerPingEventWrapper(this, event.getMOTDConnection()));
    this.proxy.getEventManager().fire(new PostServerListSetEvent(entry));
  }

  @Override
  public final byte[] getFaviconBytes(final Favicon favicon) {
    return ASLEaglerHook.getDataURIBytes(favicon.getBase64Url());
  }

  @Override
  public final Class<Favicon> getFaviconClass() {
    return Favicon.class;
  }

  @Override
  public final String parsePAPIPlaceholders(final String text, final GenericPlayer player) {
    if (this.proxy.getPluginManager().isLoaded("papiproxybridge")) {
      if (PAPIUtil.get().isCompatible()) {
        final String server = PAPIUtil.get().getServer();
        if (server != null && !server.isEmpty()) {
          final RegisteredServer registeredServer = this.proxy.getServer(server).orElse(null);
          if (registeredServer != null && !registeredServer.getPlayersConnected().isEmpty()) {
            final Player carrier = PAPIUtil.get().getPlayer(registeredServer.getPlayersConnected());
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
    return new VelocityPlayerImpl(player, protocol);
  }

  @Override
  public final GenericServer createServer(final int playersOnline, final int playersMax, final String host) {
    final Map<String, RegisteredServer> servers = new HashMap<>();
    for (final RegisteredServer server : this.proxy.getAllServers()) {
      servers.put(server.getServerInfo().getName(), server);
    }
    return new VelocityProxyImpl(servers, playersOnline, playersMax, host);
  }

  @Override
  public final GenericServerListEvent callEvent(final ProfileEntry entry) {
    final PreServerListSetEvent event = new VelocityPreServerListSetEvent(entry);
    try {
      this.proxy.getEventManager().fire(event).get();
    } catch (InterruptedException | ExecutionException e) {
      return null;
    }
    return event;
  }

  @Override
  public final boolean isMaintenancePluginLoaded() {
    return this.proxy.getPluginManager().isLoaded("maintenance");
  }
}
