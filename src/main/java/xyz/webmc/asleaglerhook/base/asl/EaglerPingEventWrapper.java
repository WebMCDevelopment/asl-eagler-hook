package xyz.webmc.asleaglerhook.base.asl;

import xyz.webmc.asleaglerhook.base.ASLEaglerHook;
import xyz.webmc.asleaglerhook.base.IASLEaglerHookPlugin;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import ch.andre601.advancedserverlist.api.events.GenericServerListEvent;
import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.interfaces.events.GenericEventWrapper;
import ch.andre601.advancedserverlist.core.objects.CachedPlayer;
import ch.andre601.advancedserverlist.core.parsing.ComponentParser;
import ch.andre601.advancedserverlist.core.profiles.replacer.StringReplacer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.lax1dude.eaglercraft.backend.server.api.query.IMOTDConnection;

@SuppressWarnings({ "rawtypes", "unchecked" })
public final class EaglerPingEventWrapper implements GenericEventWrapper<Object, GenericPlayer> {
  private final IASLEaglerHookPlugin plugin;
  private final IMOTDConnection motd;
  private boolean changed = false;

  public EaglerPingEventWrapper(final IASLEaglerHookPlugin plugin, final IMOTDConnection motd) {
    this.plugin = plugin;
    this.motd = motd;
  }

  @Override
  public final GenericServerListEvent callEvent(final ProfileEntry entry) {
    return this.plugin.callEvent(entry);
  }

  @Override
  public final void maxPlayers(final int maxPlayers) {
    this.motd.setPlayerMax(maxPlayers);
    this.changed = true;
  }

  @Override
  public final void onlinePlayers(final int onlinePlayers) {
    this.motd.setPlayerTotal(onlinePlayers);
    this.changed = true;
  }

  @Override
  public final void motd(final Component component) {
    final List<String> arr = new ArrayList<>();
    arr.add(LegacyComponentSerializer.legacySection().serialize(component));
    this.motd.setServerMOTD(arr);
    this.changed = true;
  }

  @Override
  public final void hidePlayers() {
    this.maxPlayers(-1);
    this.onlinePlayers(0);
  }

  @Override
  public final void playerCount(final String name) {

  }

  @Override
  public final void players(final List<String> lines, final GenericPlayer player, final GenericServer server) {
    final List<String> players = new ArrayList<>();

    for (int i = 0; i < lines.size(); i++) {
      players.add(ComponentParser.text(lines.get(i))
          .modifyText(text -> StringReplacer.replace(text, player, server))
          .modifyText(text -> this.parsePAPIPlaceholders(text, player))
          .applyMMTags(plugin())
          .toString());
    }

    if (!players.isEmpty()) {
      this.motd.setPlayerList(players);
      this.changed = true;
    }
  }

  @Override
  public final void playersHidden() {
    this.motd.setPlayerList(List.of());
    this.changed = true;
  }

  @Override
  public final void favicon(final Object favicon) {
    this.favicon(ASLEaglerHook.convertFavicon(this.plugin.getFaviconBytes(this.plugin.getFaviconClass().cast(favicon))));
  }

  @Override
  public final void defaultFavicon() {
    this.favicon(this.motd.getDefaultServerIcon());
  }

  private final void favicon(final byte[] favicon) {
    this.motd.setServerIcon(favicon);
    this.changed = true;
  }

  @Override
  public final void updateEvent() {
    if (this.changed) {
      this.motd.sendToUser();
      this.motd.disconnect();
    }
  }

  @Override
  public final boolean isInvalidProtocol() {
    return false;
  }

  @Override
  public final boolean isMaintenanceModeActive() {
    return this.plugin.isMaintenancePluginLoaded() && ASLEaglerHook.isMaintenance();
  }

  @Override
  public final int protocolVersion() {
    return 47;
  }

  @Override
  public final int maxPlayers() {
    return this.motd.getPlayerMax();
  }

  @Override
  public final int onlinePlayers() {
    return this.motd.getPlayerTotal();
  }

  @Override
  public final String playerIP() {
    final SocketAddress addr = this.motd.getSocketAddress();
    if (addr instanceof InetSocketAddress inet) {
      final InetAddress host = inet.getAddress();
      if (host != null) {
        return host.getHostAddress();
      } else {
        return inet.getHostString();
      }
    } else {
      return addr.toString();
    }
  }

  @Override
  public final String parsePAPIPlaceholders(final String text, final GenericPlayer player) {
    return this.plugin.parsePAPIPlaceholders(text, player);
  }

  @Override
  public final String virtualHost() {
    return this.motd.getWebSocketHost();
  }

  @Override
  public final PluginCore plugin() {
    return this.plugin.getASL();
  }

  @Override
  public final GenericPlayer createPlayer(final CachedPlayer player, final int protocol) {
    return this.plugin.createPlayer(player, protocol);
  }

  @Override
  public final GenericServer createServer(final int playersOnline, final int playersMax, final String host) {
    return this.plugin.createServer(playersOnline, playersMax, host);
  }
}
