package xyz.webmc.asleaglerhook.velocity.asl;

import xyz.webmc.asleaglerhook.base.asl.EaglerASLPlayer;

import java.util.UUID;

import ch.andre601.advancedserverlist.api.velocity.objects.VelocityPlayer;
import ch.andre601.advancedserverlist.core.objects.CachedPlayer;
import com.velocitypowered.api.network.ProtocolVersion;

public final class VelocityPlayerImpl implements VelocityPlayer, EaglerASLPlayer {
  private final CachedPlayer player;
  private final int protocol;

  public VelocityPlayerImpl(final CachedPlayer player, final int protocol) {
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

  @Override
  public final String getVersion() {
    return ProtocolVersion.getProtocolVersion(this.protocol).getVersionIntroducedIn();
  }
}