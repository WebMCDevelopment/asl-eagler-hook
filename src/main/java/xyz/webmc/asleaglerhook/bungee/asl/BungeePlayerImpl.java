package xyz.webmc.asleaglerhook.bungee.asl;

import xyz.webmc.asleaglerhook.base.asl.EaglerASLPlayer;

import java.util.UUID;

import ch.andre601.advancedserverlist.api.bungeecord.objects.BungeePlayer;
import ch.andre601.advancedserverlist.core.objects.CachedPlayer;

public final class BungeePlayerImpl implements BungeePlayer, EaglerASLPlayer {
  private final CachedPlayer player;
  private final int protocol;

  public BungeePlayerImpl(final CachedPlayer player, final int protocol) {
    this.player = player;
    this.protocol = protocol;
  }

  @Override
  public final String getName() {
    return this.player.name();
  }

  @Override
  public final int getProtocol() {
    return this.protocol;
  }

  @Override
  public final UUID getUUID() {
    return this.player.uuid();
  }
}