package xyz.webmc.asleaglerhook.bungee.asl;

import java.util.Map;

import ch.andre601.advancedserverlist.api.bungeecord.objects.BungeeProxy;
import net.md_5.bungee.api.config.ServerInfo;

public final class BungeeProxyImpl implements BungeeProxy {
  private final Map<String, ServerInfo> servers;
  private final int playersOnline;
  private final int playersMax;
  private final String host;

  public BungeeProxyImpl(final Map<String, ServerInfo> servers, final int playersOnline, final int playersMax, final String host) {
    this.servers = servers;
    this.playersOnline = playersOnline;
    this.playersMax = playersMax;
    this.host = host;
  }

  @Override
  public final Map<String, ServerInfo> getServers() {
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