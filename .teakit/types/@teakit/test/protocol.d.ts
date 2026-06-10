export interface TeaKitHostOperations {
  "runtime.health": {
    payload: RuntimeBridgeOptions;
    result: RuntimeHealth;
  };
  "runtime.capabilities": {
    payload: RuntimeBridgeOptions;
    result: RuntimeCapabilities;
  };
  "runtime.get": {
    payload: RuntimeGetRequest;
    result: unknown;
  };
  "runtime.post": {
    payload: RuntimePostRequest;
    result: unknown;
  };
  "runtime.delete": {
    payload: RuntimeDeleteRequest;
    result: unknown;
  };
  "runtime.raw": {
    payload: RuntimeRawRequest;
    result: RuntimeRawResponse;
  };
  "artifacts.attachJson": {
    payload: AttachJsonRequest;
    result: ArtifactAttachment;
  };
  "artifacts.attachText": {
    payload: AttachTextRequest;
    result: ArtifactAttachment;
  };
}

export type TeaKitHostOperation = keyof TeaKitHostOperations;
export type TeaKitHostPayload<K extends TeaKitHostOperation> = TeaKitHostOperations[K]["payload"];
export type TeaKitHostResult<K extends TeaKitHostOperation> = TeaKitHostOperations[K]["result"];

export interface RuntimeCallOptions {
  timeoutMs?: number;
}

export interface RuntimeBridgeOptions {
  timeoutSeconds?: number;
}

export interface RuntimeGetRequest extends RuntimeBridgeOptions {
  path: RuntimePath;
}

export interface RuntimePostRequest extends RuntimeBridgeOptions {
  path: RuntimePath;
  body?: unknown;
}

export interface RuntimeDeleteRequest extends RuntimeBridgeOptions {
  path: RuntimePath;
}

export interface RuntimeRawRequest extends RuntimeBridgeOptions {
  method?: string;
  path: RuntimePath;
  body?: string;
  includeToken?: boolean;
  token?: string;
}

export interface RuntimeRawResponse {
  status: number;
  ok: boolean;
  body: string;
  json: unknown;
}

export type RuntimePath = `/${string}`;

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
  [key: string]: unknown;
}

export interface RuntimeCapabilities {
  available?: boolean;
  runtimeApi?: number;
  teakitRuntimeApi?: number;
  minecraftVersion?: string;
  loader?: LoaderId;
  actions?: RuntimeCapability[];
  probes?: RuntimeCapability[];
  features?: RuntimeCapability[];
  reason?: string;
  [key: string]: unknown;
}

export type RuntimeCapability =
  | "runtime.capabilities"
  | "runtime.summary"
  | "runtime.lastError"
  | "runtime.logs"
  | "runtime.batch"
  | "runtime.timing"
  | "runtime.events"
  | "runtime.transactions"
  | "spy.calls"
  | "spy.probes"
  | "spy.interfaceProxies"
  | "spy.instrumentation"
  | "server.commands"
  | "registry.lookup"
  | "legacy-json-scenarios"
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
  | "player.actions"
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