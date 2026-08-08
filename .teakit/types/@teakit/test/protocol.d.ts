export interface RuntimeCallOptions {
  timeoutMs?: number;
}

export interface RuntimeHealth {
  ok?: boolean;
  modId?: string;
  version?: string;
  runtimeApi?: number;
  minecraftVersion?: string;
  loader?: LoaderId;
  readiness?: Record<string, boolean>;
  status?: {
    worldLoaded?: boolean;
    playerCount?: number;
    integratedServerReady?: boolean;
    singleplayer?: boolean;
    resourceReloadComplete?: boolean;
    [key: string]: unknown;
  };
  loaderError?: RuntimeLoaderError;
  [key: string]: unknown;
}

export interface RuntimeLoaderError {
  active: boolean;
  source: "client-screen" | "launch-log" | (string & {});
  loader?: LoaderId;
  severity?: "error" | "warning" | (string & {});
  screenClass?: string;
  title?: string;
  message?: string;
  matchedLogLines?: string[];
  [key: string]: unknown;
}

export interface RuntimeCapabilities {
  available?: boolean;
  runtimeApi?: number;
  minecraftVersion?: string;
  loader?: LoaderId;
  capabilities?: RuntimeCapability[];
  endpoints?: RuntimeEndpointMetadata[];
  reason?: string;
  [key: string]: unknown;
}

export interface RuntimeEndpointMetadata {
  method: string;
  path: string;
  family?: string;
}

export type RuntimeCapability =
  | "runtime.capabilities"
  | "runtime.summary"
  | "runtime.lastError"
  | "runtime.logs"
  | "runtime.timing"
  | "runtime.events"
  | "runtime.transactions"
  | "gametest.list"
  | "gametest.run"
  | "gametest.verify"
  | "spy.calls"
  | "spy.probes"
  | "spy.interfaceProxies"
  | "spy.instrumentation"
  | "server.commands"
  | "registry.lookup"
  | "world.block"
  | "world.setBlock"
  | "world.fill"
  | "world.clear"
  | "world.cleanup"
  | "world.fixtures"
  | "world.time"
  | "world.setTime"
  | "world.weather"
  | "world.setWeather"
  | "world.signs"
  | "world.loot"
  | "world.entities"
  | "world.recipes"
  | "world.pathing"
  | "world.explosions"
  | "world.inspection"
  | "player.self"
  | "player.position"
  | "player.teleport"
  | "player.reset"
  | "player.give"
  | "player.inventory"
  | "player.useItem"
  | "player.interactions"
  | "player.driver"
  | "client.screen"
  | "client.screens"
  | "client.screenshot"
  | "client.render.probes"
  | "client.input"
  | (string & {});

export type LoaderId = "fabric" | "forge" | "neoforge" | (string & {});

export interface RuntimeCapabilityMatrix {
  schemaVersion: number;
  runtimeApi: number;
  capabilities: Record<RuntimeCapability, RuntimeCapabilityMatrixEntry>;
}

export interface RuntimeCapabilityMatrixEntry {
  category: "runtime" | "spy" | "server" | "registry" | "legacy" | "world" | "player" | "client" | string;
  sdk: string[];
  endpoints: string[];
  status?: "active" | "legacy" | "future" | string;
  [key: string]: unknown;
}

export interface AttachJsonRequest {
  name: string;
  value: unknown;
}

export interface AttachTextRequest {
  name: string;
  value: string;
}

export interface ArtifactAttachment {
  name?: string;
  path: string;
  mediaType?: string;
  [key: string]: unknown;
}
