package xyz.webmc.asleaglerhook.velocity.asl;

import java.util.Map;

import ch.andre601.advancedserverlist.api.velocity.objects.VelocityProxy;
import com.velocitypowered.api.proxy.server.RegisteredServer;

public final class VelocityProxyImpl implements VelocityProxy {
  private final Map<String, RegisteredServer> servers;
  private final int playersOnline;
  private final int playersMax;
  private final String host;

  public VelocityProxyImpl(final Map<String, RegisteredServer> servers, final int playersOnline, final int playersMax, final String host) {
    this.servers = servers;
    this.playersOnline = playersOnline;
    this.playersMax = playersMax;
    this.host = host;
  }

  @Override
  public final Map<String, RegisteredServer> getServers() {
    return this.servers;
  }
  
  @Override
  public final int getPlayersOnline() {
    return this.playersOnline;
  }
  
  @Override
  public final int getPlayersMax() {
    return this.playersMax;
  }
  
  @Override
  public final String getHost() {
    return this.host;
  }
}