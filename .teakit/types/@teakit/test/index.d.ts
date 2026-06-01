export type Awaitable<T> = T | Promise<T>;

import type {
  ArtifactAttachment,
  LoaderId,
  RuntimeCallOptions,
  RuntimeCapabilities,
  RuntimeCapability,
  RuntimeCapabilityMatrix,
  RuntimeCapabilityMatrixEntry,
  RuntimeHealth,
  TeaKitHostOperation,
  TeaKitHostPayload,
  TeaKitHostResult,
} from "./protocol";

export type {
  ArtifactAttachment,
  AttachJsonRequest,
  AttachTextRequest,
  LoaderId,
  RuntimeBridgeOptions,
  RuntimeCallOptions,
  RuntimeCapabilities,
  RuntimeCapability,
  RuntimeCapabilityMatrix,
  RuntimeCapabilityMatrixEntry,
  RuntimeGetRequest,
  RuntimeHealth,
  RuntimePath,
  RuntimePostRequest,
  TeaKitHostOperation,
  TeaKitHostOperations,
  TeaKitHostPayload,
  TeaKitHostResult,
} from "./protocol";

type RequiredKeys<T> = {
  [K in keyof T]-?: Record<never, never> extends Pick<T, K> ? never : K;
}[keyof T];

type HostPayloadArgs<K extends TeaKitHostOperation> =
  RequiredKeys<TeaKitHostPayload<K>> extends never
    ? [payload?: TeaKitHostPayload<K>]
    : [payload: TeaKitHostPayload<K>];

export interface TeaKitTestContext {
  teakit: {
    call<K extends TeaKitHostOperation>(
      operation: K,
      ...args: HostPayloadArgs<K>
    ): Promise<TeaKitHostResult<K>>;
    call<T = unknown, K extends string = string>(
      operation: K extends TeaKitHostOperation ? never : K,
      payload?: unknown,
    ): Promise<T>;
  };
  runtime: RuntimeApi;
  scenario: ScenarioApi;
  commands: CommandApi;
  server: ServerApi;
  registry: RegistryApi;
  logs: LogsApi;
  spy: SpyApi;
  world: WorldApi;
  signs: SignsApi;
  player: PlayerApi;
  client: ClientApi;
  render: RenderApi;
  loot: LootApi;
  entities: EntitiesApi;
  recipes: RecipesApi;
  expectEvent: ExpectEventApi;
  artifacts: ArtifactApi;
}

export interface TeaKitTestInfo {
  name: string;
  slug: string;
  startedAt: number;
  attempt: number;
  retry: number;
  tags: string[];
  timeout?: string | number;
}

export interface RuntimeApi {
  /** Read the TeaKit runtime health endpoint, including readiness and basic environment metadata. */
  health(options?: RuntimeCallOptions): Promise<RuntimeHealth>;
  /** Read advertised runtime capabilities used for scenario feature negotiation. */
  capabilities(options?: RuntimeCallOptions): Promise<RuntimeCapabilities>;
  get<T = unknown>(path: string, options?: RuntimeCallOptions): Promise<T>;
  post<T = unknown>(path: string, body?: unknown, options?: RuntimeCallOptions): Promise<T>;
  delete<T = unknown>(path: string, options?: RuntimeCallOptions): Promise<T>;
  summary(options?: RuntimeCallOptions): Promise<RuntimeSummary>;
  lastError(options?: RuntimeCallOptions): Promise<RuntimeErrorSummary | null>;
  /** Execute multiple runtime operations as one runtime-owned batch when TeaKit advertises `runtime.batch`. */
  batch<T = unknown>(operations: RuntimeBatchOperation[], options?: RuntimeCallOptions): Promise<RuntimeBatchResult<T>>;
  /** Wait inside the TeaKit runtime without routing through a legacy scenario action. */
  wait(durationMs: number, options?: RuntimeCallOptions): Promise<RuntimeWaitResult>;
}

export interface ScenarioApi {
  run(definition: ScenarioDefinition, options?: RuntimeCallOptions): Promise<ScenarioResult>;
}

export interface CommandCallOptions extends RuntimeCallOptions {
  captureOutput?: boolean;
  expectOutputContains?: string[];
  requireSuccess?: boolean;
}

export interface CommandApi {
  run(command: string, options?: CommandCallOptions): Promise<ScenarioResult | ServerCommandResult>;
  assert(command: string, options?: CommandCallOptions): Promise<ScenarioResult | ServerCommandResult>;
}

export interface ServerApi {
  command(command: string, options?: CommandCallOptions): Promise<ServerCommandResult>;
  commands(commands: string[], options?: CommandCallOptions): Promise<ServerCommandResult>;
}

export interface RegistryApi {
  missing(ids: string[], options?: RuntimeCallOptions): Promise<string[]>;
  lookup(ids: string[], options?: RuntimeCallOptions): Promise<RegistryLookupResult>;
}

export interface LogsApi {
  text(query?: LogQuery, options?: RuntimeCallOptions): Promise<string>;
  entries(query?: LogQuery, options?: RuntimeCallOptions): Promise<LogEntry[]>;
}

export interface SpyApi {
  /**
   * Reset one spy, or all spies when no name is provided.
   *
   * Legacy runtimes may implement this through JSON scenario actions. Newer runtimes
   * should expose the `spy.calls` capability and preserve the same behavior.
   */
  reset(name?: string, options?: RuntimeCallOptions): Promise<SpyOperationResult>;
  /**
   * Return a runtime summary for one spy, or all spies when no name is provided.
   */
  report(name?: string, options?: RuntimeCallOptions): Promise<SpyReport>;
  /**
   * Record a synthetic call from the scenario itself.
   *
   * This is useful for correlating helper activity with runtime-owned probes.
   */
  record(name: string, args?: unknown[], options?: RuntimeCallOptions): Promise<SpyOperationResult>;
  /** Assert that a spy has been called at least `minCount` times. */
  assertCalled(name: string, minCount?: number, options?: RuntimeCallOptions): Promise<SpyOperationResult>;
  /** Assert that a spy has been called exactly `count` times. */
  assertCount(name: string, count: number, options?: RuntimeCallOptions): Promise<SpyOperationResult>;
  /** Assert that one spy observed a call before another spy. */
  assertCalledBefore(before: string, after: string, options?: RuntimeCallOptions): Promise<SpyOperationResult>;
  /** Assert the last recorded argument at `index` for a spy. */
  assertLastArg(name: string, index: number, value: unknown, options?: RuntimeCallOptions): Promise<SpyOperationResult>;
  /**
   * Attach a runtime-owned probe such as commands, logs, chat, packets, block changes,
   * entity lifecycle, sounds, particles, screens, screenshots, or client input.
   */
  observe(source: SpySource, options?: RuntimeCallOptions): Promise<SpyHandle>;
  /**
   * Attach a first-party server command probe.
   *
   * This is equivalent to `spy.observe({ kind: SpyKind.Command, ... })`, but gives
   * scenario authors IDE completion for the stable TeaKit probe vocabulary.
   */
  command(name: string, filters?: SpyProbeFilters, options?: RuntimeCallOptions): Promise<SpyHandle>;
  /**
   * Attach a first-party runtime event probe.
   *
   * `target` should be a stable TeaKit event ID, not a loader-specific Java class.
   */
  event(name: string, target: SpyEventTarget, filters?: SpyProbeFilters, options?: RuntimeCallOptions): Promise<SpyHandle>;
  /**
   * Attach a first-party method probe.
   *
   * The Java runtime may implement this with reflection, Mixins, or other hooks,
   * but TypeScript scenarios should use stable TeaKit method target IDs.
   */
  method(
    name: string,
    target: SpyMethodTarget,
    filters?: SpyProbeFilters,
    options?: RuntimeCallOptions,
  ): Promise<SpyHandle>;
  /**
   * Attach a first-party network packet probe.
   *
   * `target` should be a stable packet ID from the TeaKit runtime contract.
   */
  packet(
    name: string,
    target: SpyPacketTarget,
    filters?: SpyProbeFilters,
    options?: RuntimeCallOptions,
  ): Promise<SpyHandle>;
  /** Read structured calls captured by a runtime spy. */
  calls(name: string, query?: SpyCallQuery, options?: RuntimeCallOptions): Promise<SpyCall[]>;
  /**
   * Detach a runtime-owned spy or proxy and release any runtime resources it owns.
   *
   * Prefer this for long-lived probes or interface proxies created inside a test body.
   * TeaKit also clears remaining spies during runtime/test teardown.
   */
  detach(spyOrName: string | SpyHandle, options?: RuntimeCallOptions): Promise<SpyOperationResult>;
  /**
   * Create an interface-backed Java proxy for TeaKit-owned or cooperating mod APIs.
   *
   * The returned JavaScript proxy records method invocations through TeaKit runtime.
   * The runtime owns the Java `Proxy` and call serialization; TypeScript owns the
   * ergonomic shape.
   */
  proxy<T extends object = Record<string, (...args: any[]) => unknown>>(
    name: string,
    interfaceName: SpyProxyTarget,
    proxyOptions?: SpyProxyOptions,
    options?: RuntimeCallOptions,
  ): Promise<SpyProxy<T>>;
}

export type SpyKind = (typeof SpyKind)[keyof typeof SpyKind] | (string & {});

export type SpySide = (typeof SpySide)[keyof typeof SpySide] | (string & {});

export type SpyProbeFilters = Record<string, unknown>;

export type SpyEventTarget = (typeof SpyEventTarget)[keyof typeof SpyEventTarget] | (string & {});

export type SpyMethodTarget = (typeof SpyMethodTarget)[keyof typeof SpyMethodTarget] | (string & {});

export type SpyPacketTarget = (typeof SpyPacketTarget)[keyof typeof SpyPacketTarget] | (string & {});

export type SpyProxyTarget = (typeof SpyProxyTarget)[keyof typeof SpyProxyTarget] | (string & {});

export interface SpySource {
  /** Stable spy name used in reports and assertions. */
  name: string;
  /** Runtime-owned probe type. */
  kind: SpyKind;
  /** Optional target identifier, such as a registry ID, class name, packet ID, or screen ID. */
  target?: string;
  /** Runtime-specific filters. These must not change public behavior across supported versions. */
  filters?: Record<string, unknown>;
}

export interface SpyHandle {
  /** Stable spy name used in reports and assertions. */
  name: string;
  /** Runtime-owned probe type. */
  kind?: SpyKind;
  /** Runtime-specific opaque handle ID. */
  id?: string;
  /** Whether the spy is currently attached. */
  active?: boolean;
  /** Read structured calls for this runtime-owned spy. */
  $calls(query?: SpyCallQuery, options?: RuntimeCallOptions): Promise<SpyCall[]>;
  /** Detach this runtime-owned spy and release its resources. */
  $detach(options?: RuntimeCallOptions): Promise<SpyOperationResult>;
  [key: string]: unknown;
}

export interface SpyCall {
  /** Spy name that recorded this call. */
  name: string;
  /** Monotonic sequence number within the runtime. */
  sequence?: number;
  /** Minecraft tick when the runtime observed the call. */
  tick?: number;
  /** Java thread that observed the call. */
  thread?: string;
  /** Logical side that observed the call. */
  side?: SpySide;
  /** Method, event, or operation name when available. */
  method?: string;
  /** JSON-serializable argument summaries or stable runtime references. */
  args?: unknown[];
  /** JSON-serializable return value or stable runtime reference. */
  returned?: unknown;
  /** Structured thrown error, if the observed call failed. */
  thrown?: RuntimeErrorSummary | null;
  [key: string]: unknown;
}

export interface SpyCallQuery {
  /** Only return calls after this runtime sequence number. */
  sinceSequence?: number;
  /** Only return calls at or after this tick. */
  sinceTick?: number;
  /** Maximum number of calls to return. */
  limit?: number;
  /** Filter by method, event, or operation name. */
  method?: string;
}

export interface SpyReport {
  /** Spy name for a single-spy report. Absent when the runtime returned an aggregate report. */
  name?: string;
  /** Total number of calls included or summarized by the report. */
  count?: number;
  /** Structured calls included directly in the report. */
  calls?: SpyCall[];
  /** Aggregate reports keyed by spy name when requesting all spies. */
  spies?: Record<string, SpyReport>;
  /** Legacy scenario-run shape when a runtime without `/capabilities` served the fallback path. */
  legacyScenario?: ScenarioResult;
  [key: string]: unknown;
}

export interface SpyOperationResult {
  /** Whether the runtime accepted the spy operation or assertion. */
  ok?: boolean;
  /** Spy name affected by the operation when applicable. */
  name?: string;
  /** Observed call count when returned by the runtime. */
  count?: number;
  /** Assertion or operation message when returned by the runtime. */
  message?: string;
  /** Legacy scenario-run shape when a runtime without `/capabilities` served the fallback path. */
  legacyScenario?: ScenarioResult;
  [key: string]: unknown;
}

export interface SpyProxyOptions {
  /** Default value returned when a proxied method has no explicit behavior. */
  defaultReturn?: unknown;
  /** Runtime-defined method behavior map. */
  behavior?: Record<string, unknown>;
  [key: string]: unknown;
}

export type SpyProxy<T extends object> = T & {
  /** Runtime spy handle backing this proxy. */
  readonly $spy: SpyHandle;
  /** Read structured calls for the backing spy. */
  $calls(query?: SpyCallQuery, options?: RuntimeCallOptions): Promise<SpyCall[]>;
  /** Detach the runtime-owned Java proxy and release its resources. */
  $detach(options?: RuntimeCallOptions): Promise<SpyOperationResult>;
};

export interface WorldApi {
  /** Start a named fixture builder for repeatable world setup. */
  fixture(name: string): WorldFixtureBuilder;
  /** Read the block state at a position. */
  block(pos: BlockPos, options?: RuntimeCallOptions): Promise<BlockState>;
  /** Set a single block at a position. */
  setBlock(pos: BlockPos, block: BlockId | BlockStateInput, options?: RuntimeCallOptions): Promise<unknown>;
  /** Fill an inclusive block volume. */
  fill(from: BlockPos, to: BlockPos, block: BlockId | BlockStateInput, options?: RuntimeCallOptions): Promise<unknown>;
  /** Clear an inclusive block volume to air. */
  clear(from: BlockPos, to: BlockPos, options?: RuntimeCallOptions): Promise<unknown>;
  /**
   * Remove runtime-owned world fixtures and markers associated with a scenario namespace.
   *
   * This is intended for fast per-test cleanup without relaunching Minecraft.
   */
  cleanupNamespace(namespace: string, options?: RuntimeCallOptions): Promise<WorldCleanupResult>;
  /** Read the current world time. */
  time(options?: RuntimeCallOptions): Promise<WorldTime>;
  /** Set the current world time. */
  setTime(time: number | WorldTimeInput, options?: RuntimeCallOptions): Promise<unknown>;
  /** Read the current weather state. */
  weather(options?: RuntimeCallOptions): Promise<WorldWeather>;
  /** Set the current weather state. */
  setWeather(weather: WorldWeatherInput, options?: RuntimeCallOptions): Promise<unknown>;
  /** Open a drainable stream of runtime events, optionally scoped to one event name. */
  events(name?: string, filters?: EventFilters): EventStream;
  /** Start a runtime-backed transaction for atomic setup, assertions, rollback, and diagnostics. */
  transaction(name: string): WorldTransactionBuilder;
  /** Inspect a container inventory at a block position. */
  container(pos: BlockPos): WorldContainerProbe;
  /** Ensure the target block has a stable floor and headroom for player movement. */
  ensureWalkable(pos: BlockPos, options?: RuntimeCallOptions): Promise<PathingResult>;
  /** Build a water or fluid pool used by movement scenarios. */
  pool(pos: BlockPos, options: PoolOptions, callOptions?: RuntimeCallOptions): Promise<PathingResult>;
  /** Build a ladder tower fixture for vertical pathing. */
  ladderTower(pos: BlockPos, options: LadderTowerOptions, callOptions?: RuntimeCallOptions): Promise<PathingResult>;
  /** Build a channel between two points for swimming or boat routes. */
  channel(from: BlockPos, to: BlockPos, options: ChannelOptions, callOptions?: RuntimeCallOptions): Promise<PathingResult>;
  /** Build a complete mixed route fixture and return a fluent runtime builder. */
  routeFixture(name: string, start: BlockPos, route: readonly RouteStepInput[]): RouteFixtureBuilder;
  /** Trigger a runtime-owned explosion at a known position. */
  explode(pos: BlockPos, options: ExplosionOptions, callOptions?: RuntimeCallOptions): Promise<PathingResult>;
  /** Inspect a bounded world area for artifact attachments and computed expectations. */
  inspectArea(origin: BlockPos, query: InspectAreaQuery, options?: RuntimeCallOptions): Promise<WorldAreaInspection>;
}

export interface SignsApi {
  /** Place or update a sign at a position with up to four display lines. */
  place(pos: BlockPos, lines: readonly string[], options?: RuntimeCallOptions): Promise<SignPlacementResult>;
}

/** Fluent builder for a named world fixture. */
export interface WorldFixtureBuilder {
  /** Set the fixture origin used by relative operations. */
  origin(pos: BlockPos): WorldFixtureBuilder;
  /** Clear a volume around or above the origin before building the fixture. */
  clearVolume(size: VolumeSize): WorldFixtureBuilder;
  /** Build a flat platform at the fixture origin. */
  platform(options: FixturePlatformOptions): WorldFixtureBuilder;
  /** Place a regular grid of marker or light blocks. */
  grid(options: FixtureGridOptions): WorldFixtureBuilder;
  /** Add a visible label for screenshots and debugging. */
  label(text: string, options?: FixtureLabelOptions): WorldFixtureBuilder;
  /** Request a fixture-local biome override when the runtime supports it. */
  biome(id: BiomeId): WorldFixtureBuilder;
  /** Add a pond or fluid feature. */
  pond(options: FixturePondOptions): WorldFixtureBuilder;
  /** Add a generated or schematic tree feature. */
  tree(options: FixtureTreeOptions): WorldFixtureBuilder;
  /** Add a crop patch fixture feature. */
  cropPatch(options: FixtureCropPatchOptions): WorldFixtureBuilder;
  /** Add a fenced animal pen fixture feature. */
  animalPen(options: FixtureAnimalPenOptions): WorldFixtureBuilder;
  /** Send the fixture definition to the TeaKit runtime and build it in-world. */
  build(options?: RuntimeCallOptions): Promise<WorldFixtureResult>;
}

export interface WorldTransactionBuilder {
  /** Set a block as part of the transaction. */
  setBlock(pos: BlockPos, block: BlockId | BlockStateInput): WorldTransactionBuilder;
  /** Insert an item into a container block as part of the transaction. */
  insertItem(pos: BlockPos, item: ItemId | ItemStackInput, count?: number, options?: Record<string, unknown>): WorldTransactionBuilder;
  /** Give an item to a player as part of the transaction. */
  give(player: PlayerRef | PlayerApi, item: ItemId | ItemStackInput, options?: Record<string, unknown>): WorldTransactionBuilder;
  /** Capture a runtime snapshot with a stable label. */
  snapshot(label: string): WorldTransactionBuilder;
  /** Run an async TypeScript assertion whose structured result is sent to the runtime transaction report. */
  assert(assertion: () => Awaitable<unknown>): WorldTransactionBuilder;
  /** Execute the transaction through TeaKit runtime. */
  run(options?: TransactionRunOptions): Promise<TransactionResult>;
}

export interface WorldContainerProbe {
  /** Position inspected by this container probe. */
  pos: BlockPos;
  /** Read the container inventory. */
  inspect(options?: RuntimeCallOptions): Promise<PlayerInventory>;
  /** Internal inspection hook used by TeaKit inventory matchers. */
  $inspect(options?: RuntimeCallOptions): Promise<PlayerInventory>;
}

export interface RouteFixtureBuilder {
  /** Build the route fixture in-world. */
  build(options?: RuntimeCallOptions): Promise<PathingResult>;
}

export interface PlayerApi {
  self(options?: RuntimeCallOptions): Promise<PlayerRef>;
  position(options?: RuntimeCallOptions): Promise<Vec3>;
  teleport(pos: BlockPos | Vec3, options?: RuntimeCallOptions): Promise<unknown>;
  /** Reset player state such as game mode, health, food, effects, and inventory. */
  reset(state?: PlayerResetState, options?: RuntimeCallOptions): Promise<PlayerResetResult>;
  give(item: ItemId | ItemStackInput, count?: number, options?: RuntimeCallOptions): Promise<unknown>;
  inventory(options?: RuntimeCallOptions): PlayerInventoryProbe;
  useItem(options?: RuntimeCallOptions): Promise<unknown>;
  /** Craft a recipe through the player action facade. */
  craft(recipe: ItemId, craftOptions?: CraftOptions, options?: RuntimeCallOptions): Promise<PlayerActionResult>;
  /** Equip an item in the player's hand or armor slot. */
  equip(item: ItemId | ItemStackInput, options?: RuntimeCallOptions): Promise<PlayerActionResult>;
  /** Mine a block as the player, waiting until the runtime reports completion. */
  mine(pos: BlockPos, mineOptions?: PlayerActionOptions, options?: RuntimeCallOptions): Promise<PlayerActionResult>;
  /** Place an item or block at a target position. */
  place(
    item: ItemId | ItemStackInput,
    pos: BlockPos,
    placeOptions?: PlayerActionOptions,
    options?: RuntimeCallOptions,
  ): Promise<PlayerActionResult>;
  /** Open a block menu, such as a crafting table or container. */
  openBlock(pos: BlockPos, options?: RuntimeCallOptions): Promise<PlayerActionResult>;
  /** Rotate the client or player toward a world target. */
  lookAt(target: BlockPos | Vec3, options?: RuntimeCallOptions): Promise<PlayerActionResult>;
  /** Use an item on an entity reference. */
  useItemOnEntity(entity: EntityRef, item: ItemId | ItemStackInput, options?: RuntimeCallOptions): Promise<PlayerActionResult>;
  /** Change the primary player's game mode. */
  setGameMode(gameMode: "survival" | "creative" | "adventure" | "spectator" | string, options?: RuntimeCallOptions): Promise<PlayerActionResult>;
  /** Read the player's current health. */
  health(options?: RuntimeCallOptions): Promise<number>;
  /** Walk to a target block position. */
  walkTo(pos: BlockPos, options?: PlayerActionOptions): Promise<PlayerActionResult>;
  /** Sprint-jump to a target block position. */
  sprintJumpTo(pos: BlockPos, options?: PlayerActionOptions): Promise<PlayerActionResult>;
  /** Swim to a target block position. */
  swimTo(pos: BlockPos, options?: PlayerActionOptions): Promise<PlayerActionResult>;
  /** Climb to a target block position. */
  climbTo(pos: BlockPos, options?: PlayerActionOptions): Promise<PlayerActionResult>;
  /** Ride a boat to a target block position. */
  rideBoatTo(pos: BlockPos, options?: PlayerActionOptions): Promise<PlayerActionResult>;
}

export interface ClientApi {
  screen(options?: RuntimeCallOptions): Promise<ClientScreen>;
  /** Close any open menu or handled screen and return to the in-game HUD. */
  closeMenus(options?: RuntimeCallOptions): Promise<ClientScreen>;
  /** Open the local player inventory screen. */
  openInventory(options?: RuntimeCallOptions): Promise<ClientScreen>;
  /** Wait until the current client screen matches a stable TeaKit screen ID. */
  waitForScreen(screen: string, options?: RuntimeCallOptions): Promise<ClientScreen>;
  /** Wait for at least `frames` rendered client frames before continuing. */
  waitForFrames(frames?: number, options?: RuntimeCallOptions): Promise<RenderFrameWaitResult>;
  screenshot(name: string, options?: RuntimeCallOptions): Promise<ArtifactAttachment>;
  /** Rotate the client camera toward a world target. */
  lookAt(target: BlockPos | Vec3, options?: RuntimeCallOptions): Promise<unknown>;
  /** Send a client key press, optionally releasing it in the same call. */
  key(key: number, options?: ClientKeyOptions): Promise<ClientScreen>;
  /** Set whether a client key is currently held down. */
  keyState(key: number, held: boolean, options?: ClientKeyOptions): Promise<ClientScreen>;
}

export interface RenderApi {
  /** Create a probe handle for a rendered block at a position. */
  block(pos: BlockPos): RenderBlockProbe;
  /** Read client light information at a world position. */
  lightAt(pos: BlockPos, options?: RuntimeCallOptions): Promise<RenderLightProbe>;
  /** Return visible rendered blocks around a point. */
  visibleBlocks(query: VisibleBlocksQuery, options?: RuntimeCallOptions): Promise<RenderedBlock[]>;
}

export interface LootApi {
  /** Find dropped loot entities near a position. */
  near(pos: BlockPos, query?: LootQuery, options?: RuntimeCallOptions): Promise<LootEntity[]>;
}

export interface EntitiesApi {
  /** Spawn an entity through the TeaKit runtime and return its stable reference. */
  spawn(type: EntityTypeId, pos: BlockPos | Vec3, options?: RuntimeCallOptions): Promise<EntityRef>;
  /** Find the nearest entity of a type around a position. */
  nearest(type: EntityTypeId, pos: BlockPos | Vec3, options?: RuntimeCallOptions): Promise<EntityRef | null>;
}

export interface RecipesApi {
  /** Craft a recipe in the currently open menu. */
  craftInOpenMenu(recipe: ItemId, craftOptions?: CraftOptions, options?: RuntimeCallOptions): Promise<CraftResult>;
  /** Assert that a crafting grid resolves to the expected item result. */
  assertCrafting(
    width: number,
    height: number,
    items: ItemId[],
    result: ItemId,
    recipeOptions?: RecipeAssertionOptions,
    options?: RuntimeCallOptions,
  ): Promise<CraftingRecipeResult>;
  /** Assert that a smithing transform resolves to the expected item result. */
  assertSmithingTransform(
    base: ItemId,
    result: ItemId,
    recipeOptions?: SmithingRecipeAssertionOptions,
    options?: RuntimeCallOptions,
  ): Promise<SmithingRecipeResult>;
}

export interface ExpectEventApi {
  /** Create an assertion builder for a runtime event name. */
  (name: string): EventExpectation;
  /** Open a drainable runtime event stream. */
  stream(name: string, filters?: EventFilters): EventStream;
}

export interface EventExpectation {
  /** Add filters that must match the expected event. */
  where(filters: EventFilters): EventExpectation;
  /** Assert that the event has occurred. */
  toOccur(options?: RuntimeCallOptions): Promise<EventExpectationResult>;
  /** Assert that the event occurs within a timeout such as `"5s"`. */
  toOccurWithin(timeout: string | number, options?: RuntimeCallOptions): Promise<EventExpectationResult>;
}

export interface EventStream {
  /** Runtime event stream name, absent when draining all matching events. */
  name?: string;
  /** Filters applied to the stream. */
  filters?: EventFilters;
  /** Drain matching events from the runtime. */
  drain(query?: EventDrainQuery, options?: RuntimeCallOptions): Promise<RuntimeEvent[]>;
  /** Internal hook used by TeaKit event matchers. */
  $events(query?: EventDrainQuery, options?: RuntimeCallOptions): Promise<RuntimeEvent[]>;
}

export interface ArtifactApi {
  attachJson(name: string, value: unknown): Promise<ArtifactAttachment>;
  attachText(name: string, value: string): Promise<ArtifactAttachment>;
  attachScreenshot(attachment: ArtifactAttachment): Promise<ArtifactAttachment>;
  measure(name: string, durationMs: number): Promise<ArtifactMeasurement>;
  step<T>(name: string, action: () => Awaitable<T>): Promise<T>;
}

export interface ArtifactMeasurement {
  name: string;
  kind: "measurement";
  durationMs: number;
}

export interface ArtifactStep {
  name: string;
  kind: "step";
  status: "passed" | "failed";
  durationMs: number;
  error?: string;
}

export interface RuntimeSummary {
  runtimeApi?: number;
  minecraftVersion?: string;
  loader?: LoaderId;
  uptimeMs?: number;
  readiness?: Record<string, boolean>;
  capabilities?: RuntimeCapabilities;
  [key: string]: unknown;
}

export interface RuntimeErrorSummary {
  code?: string;
  message?: string;
  stack?: string;
  details?: unknown;
  [key: string]: unknown;
}

export interface RuntimeBatchOperation {
  /** Runtime HTTP method used for this batched operation. */
  method: "GET" | "POST" | "DELETE";
  /** Runtime HTTP path beginning with `/`. */
  path: `/${string}`;
  /** JSON payload for `POST` operations. */
  body?: unknown;
  /** Optional stable operation ID used by runtime reports. */
  id?: string;
  [key: string]: unknown;
}

export interface RuntimeBatchResult<T = unknown> {
  /** Whether every operation in the batch completed successfully. */
  ok?: boolean;
  /** Ordered runtime results, or runtime-defined transaction output. */
  results?: T[];
  /** Runtime-defined rollback or failure metadata. */
  details?: unknown;
  [key: string]: unknown;
}

export interface RuntimeWaitResult {
  /** Whether the runtime accepted the wait. */
  ok?: boolean;
  /** Requested wait duration. */
  durationMs?: number;
  /** Actual elapsed wall-clock duration observed by the runtime. */
  elapsedMs?: number;
  [key: string]: unknown;
}

export interface TransactionRunOptions {
  onFailure?: "rollback" | "commit" | "keep";
  captureDiagnostics?: boolean;
  [key: string]: unknown;
}

export interface TransactionResult {
  ok?: boolean;
  failed?: boolean;
  code?: string;
  name?: string;
  /** Stable rollback status alias normalized from runtime-specific fields. */
  rollbackStatus?: "completed" | "failed" | "skipped" | string;
  rollback?: "completed" | "failed" | "skipped" | string;
  /** Operation results mapped to the original transaction builder operations. */
  operations?: TransactionOperationResult[];
  /** Alias for `operations`, kept for runtimes that expose this field name directly. */
  operationResults?: TransactionOperationResult[];
  /** Failed operation results only. */
  operationFailures?: TransactionOperationResult[];
  assertions?: TransactionAssertionResult[];
  /** Failed assertion results only. */
  assertionFailures?: TransactionAssertionResult[];
  diagnostics?: unknown;
  /** Normalized attachment references returned by runtime diagnostics. */
  diagnosticsAttachments?: DiagnosticAttachmentRef[];
  failure?: TransactionFailure;
  [key: string]: unknown;
}

export interface TransactionOperationResult {
  index: number;
  action?: string;
  ok: boolean;
  source?: SourceLocation;
  failure?: TransactionFailure;
  [key: string]: unknown;
}

export interface TransactionAssertionResult {
  index: number;
  ok: boolean;
  /** Source-mapped assertion failure location, or the assertion declaration site when the assertion passed. */
  source?: SourceLocation;
  /** Location where `.assert(...)` was registered in the transaction builder. */
  declarationSource?: SourceLocation;
  failure?: TransactionFailure;
  [key: string]: unknown;
}

export interface TransactionFailure {
  code?: string;
  name?: string;
  message?: string;
  stack?: string;
  source?: string;
  location?: SourceLocation;
  details?: unknown;
  [key: string]: unknown;
}

export interface SourceLocation {
  file: string;
  line: number;
  column: number;
  [key: string]: unknown;
}

export interface DiagnosticAttachmentRef {
  name?: string;
  path?: string;
  mediaType?: string;
  kind?: string;
  [key: string]: unknown;
}

export type RouteStepInput =
  | { kind: "walk" | "jump" | "swim" | "climb" | "boat" | string; to: BlockPos; [key: string]: unknown };

export interface PoolOptions {
  size?: number;
  depth?: number;
  fluid?: FluidId;
  [key: string]: unknown;
}

export interface LadderTowerOptions {
  height: number;
  [key: string]: unknown;
}

export interface ChannelOptions {
  width?: number;
  fluid?: FluidId;
  [key: string]: unknown;
}

export interface ExplosionOptions {
  power?: number;
  source?: EntityTypeId | string;
  [key: string]: unknown;
}

export interface InspectAreaQuery {
  radius?: number;
  from?: BlockPos;
  to?: BlockPos;
  [key: string]: unknown;
}

export interface PathingResult {
  ok?: boolean;
  name?: string;
  [key: string]: unknown;
}

export interface WorldAreaInspection {
  /** Runtime-normalized inspection origin. */
  origin?: TeaKitPosition;
  /** Stable block snapshots found in the inspected area. */
  blocks?: WorldBlockSnapshot[];
  /** Stable entity snapshots found in the inspected area. */
  entities?: EntitySnapshot[];
  /** Raw runtime container map, preserved for compatibility with early runtimes. */
  containers?: Record<string, PlayerInventory> | WorldContainerSnapshot[];
  /** Stable container snapshots found in the inspected area. */
  containerContents?: WorldContainerSnapshot[];
  /** Stable fixture/artifact markers found in the inspected area. */
  fixtureMarkers?: FixtureMarkerSnapshot[];
  /** Compatibility alias for runtimes that return markers under a shorter key. */
  markers?: FixtureMarkerSnapshot[];
  /** Compact area counts suitable for artifact attachment and quick assertions. */
  summary?: WorldAreaSummary;
  [key: string]: unknown;
}

export interface WorldBlockSnapshot extends BlockState {
  /** Runtime-normalized block position. */
  pos?: TeaKitPosition;
  /** Compatibility aliases used by older runtime DTOs. */
  block?: BlockId;
  blockId?: BlockId;
  air?: boolean;
}

export interface WorldContainerSnapshot {
  /** Runtime-normalized container position. */
  pos?: TeaKitPosition;
  /** Runtime-normalized inventory contents. */
  items?: ItemStack[];
  /** Optional container block ID or runtime kind. */
  id?: BlockId | string;
  kind?: string;
  [key: string]: unknown;
}

export interface FixtureMarkerSnapshot {
  /** Runtime-normalized marker position. */
  pos?: TeaKitPosition;
  /** Stable marker label or runtime-assigned name. */
  name?: string;
  label?: string;
  /** Marker category such as `fixture`, `spawn`, `tree`, or `pen`. */
  kind?: string;
  [key: string]: unknown;
}

export interface WorldAreaSummary {
  blocks: number;
  entities: number;
  containers: number;
  fixtureMarkers: number;
  [key: string]: unknown;
}

export interface WorldCleanupResult {
  /** Whether the runtime accepted and completed cleanup. */
  ok?: boolean;
  /** Scenario namespace that was cleaned up. */
  namespace?: string;
  /** Runtime-owned fixture/entity/block count removed, when available. */
  removed?: number;
  [key: string]: unknown;
}

export interface PlayerResetState {
  /** Requested game mode after reset. */
  gameMode?: "survival" | "creative" | "adventure" | "spectator" | string;
  /** Requested health value after reset. */
  health?: number;
  /** Requested food value after reset. */
  food?: number;
  /** Requested saturation value after reset. */
  saturation?: number;
  /** Use `"clear"` to remove active potion/status effects. */
  effects?: "clear" | string[] | Record<string, unknown>;
  /** Use `"clear"` to remove inventory contents. */
  inventory?: "clear" | ItemStackInput[] | Record<string, unknown>;
  [key: string]: unknown;
}

export interface PlayerResetResult {
  /** Whether the runtime accepted and completed the reset. */
  ok?: boolean;
  /** Stable player reference returned by the runtime, when available. */
  player?: PlayerRef;
  [key: string]: unknown;
}

export interface PlayerActionOptions {
  timeout?: string | number;
  [key: string]: unknown;
}

export interface PlayerActionResult {
  /** Whether the runtime accepted and completed the player action. */
  ok?: boolean;
  /** Stable action name reported by the runtime. */
  action?: string;
  [key: string]: unknown;
}

export interface SignPlacementResult {
  /** Whether the runtime accepted and completed sign placement. */
  ok?: boolean;
  /** Position of the placed or updated sign. */
  pos?: BlockPos;
  /** Display lines written to the sign. */
  lines?: string[];
  [key: string]: unknown;
}

export interface RenderFrameWaitResult {
  /** Whether the runtime observed the requested frames. */
  ok?: boolean;
  /** Number of frames requested by the scenario. */
  frames?: number;
  /** Render tick or frame counter after the wait, when available. */
  currentFrame?: number;
  [key: string]: unknown;
}

export interface ClientKeyOptions extends RuntimeCallOptions {
  scancode?: number;
  modifiers?: number;
  release?: boolean;
}

export interface RenderBlockProbe {
  /** Position inspected by this render probe. */
  pos: BlockPos;
  /** Read the current rendered block model/texture state. */
  inspect(options?: RuntimeCallOptions): Promise<RenderBlockInspection>;
  /** Internal inspection hook used by TeaKit render matchers. */
  $inspect(options?: RuntimeCallOptions): Promise<RenderBlockInspection>;
}

export interface RenderBlockInspection {
  id?: BlockId;
  model?: string | null;
  texture?: string | string[] | null;
  missingModel?: boolean;
  missingTexture?: boolean;
  [key: string]: unknown;
}

export interface RenderLightProbe {
  /** Runtime-normalized light sample position. */
  pos?: TeaKitPosition;
  block?: number;
  sky?: number;
  raw?: number;
  level?: number;
  valueOf?: () => number;
  [key: string]: unknown;
}

export interface VisibleBlocksQuery {
  around: BlockPos | Vec3;
  radius: number;
  [key: string]: unknown;
}

export interface RenderedBlock {
  /** Runtime-normalized rendered block position. */
  pos: TeaKitPosition;
  id?: BlockId;
  block?: BlockId;
  blockId?: BlockId;
  model?: string | null;
  texture?: string | string[] | null;
  [key: string]: unknown;
}

export interface LootQuery {
  itemLike?: ItemId;
  radius?: number;
  [key: string]: unknown;
}

export interface LootEntity {
  item?: ItemId;
  itemId?: ItemId;
  count?: number;
  pos?: Vec3;
  [key: string]: unknown;
}

export interface CraftOptions {
  times?: number;
  [key: string]: unknown;
}

export interface CraftResult {
  ok?: boolean;
  recipe?: ItemId;
  crafted?: number;
  [key: string]: unknown;
}

export interface RecipeAssertionOptions {
  resultCount?: number;
  player?: string;
  [key: string]: unknown;
}

export interface SmithingRecipeAssertionOptions extends RecipeAssertionOptions {
  template?: ItemId;
  templateItemId?: ItemId;
  addition?: ItemId;
  additionItemId?: ItemId;
}

export interface CraftingRecipeResult {
  recipeId?: string;
  width?: number;
  height?: number;
  itemIds?: ItemId[];
  result?: ItemStack;
  [key: string]: unknown;
}

export interface SmithingRecipeResult {
  recipeId?: string;
  templateItemId?: ItemId;
  baseItemId?: ItemId;
  additionItemId?: ItemId;
  result?: ItemStack;
  [key: string]: unknown;
}

export type EventFilters = Record<string, unknown>;

export interface EventDrainQuery {
  since?: number | string;
  limit?: number;
  [key: string]: unknown;
}

export interface RuntimeEvent {
  name?: string;
  type?: string;
  tick?: number;
  timestamp?: number | string;
  [key: string]: unknown;
}

export interface EventExpectationResult {
  ok?: boolean;
  event?: RuntimeEvent;
  count?: number;
  [key: string]: unknown;
}

export interface ServerCommandResult {
  ok?: boolean;
  commands?: string[];
  output?: string[];
  [key: string]: unknown;
}

export interface RegistryLookupResult {
  missing?: string[];
  entries?: Record<string, unknown>;
  [key: string]: unknown;
}

export interface LogQuery {
  levelAtLeast?: "TRACE" | "DEBUG" | "INFO" | "WARN" | "ERROR" | string;
  since?: number | string;
  limit?: number;
  contains?: string;
  [key: string]: unknown;
}

export interface LogEntry {
  timestamp?: number | string;
  level?: string;
  logger?: string;
  message: string;
  [key: string]: unknown;
}

export interface TestOptions {
  timeout?: string | number;
  retries?: number;
  tags?: string[];
  readiness?: RuntimeReadiness[];
  capabilities?: RuntimeCapability[];
}

export type SuiteOptions = TestOptions;

export type EachRow = readonly unknown[] | Record<string, unknown> | unknown;

export type MutableTuple<T extends readonly unknown[]> = { -readonly [K in keyof T]: T[K] };

export type EachArgs<Row> = Row extends readonly unknown[] ? MutableTuple<Row> : [Row];

export type EachDescribeBody<Row> = (...args: EachArgs<Row>) => void;

export type RuntimeReadiness =
  | "none"
  | "title"
  | "world"
  | "player"
  | "client-ready"
  | "player-spawned"
  | "integrated-server-ready"
  | "resource-reload-complete"
  | (string & {});

/**
 * Runtime capability constants for `describe.configure()` and per-test `capabilities`.
 * Use these instead of raw protocol strings so scenario requirements stay typed and discoverable.
 */
export declare const Capability: {
  /** TeaKit runtime can report its supported actions, probes, and features. */
  readonly RuntimeCapabilities: "runtime.capabilities";
  /** TeaKit runtime can return a diagnostic summary for the current client/session. */
  readonly RuntimeSummary: "runtime.summary";
  /** TeaKit runtime can return the most recent structured runtime error, if one exists. */
  readonly RuntimeLastError: "runtime.lastError";
  /** TeaKit runtime can return log text and structured log entries. */
  readonly RuntimeLogs: "runtime.logs";
  /** TeaKit runtime can execute multiple runtime operations as one batch. */
  readonly RuntimeBatch: "runtime.batch";
  /** TeaKit runtime can wait without routing through legacy scenario actions. */
  readonly RuntimeTiming: "runtime.timing";
  /** TeaKit runtime can record, query, and assert structured runtime events. */
  readonly RuntimeEvents: "runtime.events";
  /** TeaKit runtime can run world/player transactions with rollback and diagnostics. */
  readonly RuntimeTransactions: "runtime.transactions";
  /** TeaKit runtime can reset spies, record calls, report calls, and assert counts/order. */
  readonly SpyCalls: "spy.calls";
  /** TeaKit runtime can attach first-party probes to game behavior. */
  readonly SpyProbes: "spy.probes";
  /** TeaKit runtime can create Java interface proxies for cooperating APIs. */
  readonly SpyInterfaceProxies: "spy.interfaceProxies";
  /** TeaKit runtime can intercept targeted concrete classes through explicit instrumentation. */
  readonly SpyInstrumentation: "spy.instrumentation";
  /** TeaKit runtime can execute one or more server commands. */
  readonly ServerCommands: "server.commands";
  /** TeaKit runtime can look up registry IDs and report missing entries. */
  readonly RegistryLookup: "registry.lookup";
  /** TeaKit runtime can execute legacy JSON scenario definitions. */
  readonly LegacyJsonScenarios: "legacy-json-scenarios";
  /** TeaKit runtime can read block state at a position. */
  readonly WorldBlock: "world.block";
  /** TeaKit runtime can set a single block. */
  readonly WorldSetBlock: "world.setBlock";
  /** TeaKit runtime can fill a volume. */
  readonly WorldFill: "world.fill";
  /** TeaKit runtime can clear a volume. */
  readonly WorldClear: "world.clear";
  /** TeaKit runtime can clean up runtime-owned world fixtures by namespace. */
  readonly WorldCleanup: "world.cleanup";
  /** TeaKit runtime can build fluent named world fixtures. */
  readonly WorldFixtures: "world.fixtures";
  /** TeaKit runtime can read world time. */
  readonly WorldTime: "world.time";
  /** TeaKit runtime can set world time. */
  readonly WorldSetTime: "world.setTime";
  /** TeaKit runtime can read world weather. */
  readonly WorldWeather: "world.weather";
  /** TeaKit runtime can set world weather. */
  readonly WorldSetWeather: "world.setWeather";
  /** TeaKit runtime can place or update signs. */
  readonly WorldSigns: "world.signs";
  /** TeaKit runtime can inspect nearby dropped loot entities. */
  readonly WorldLoot: "world.loot";
  /** TeaKit runtime can spawn and query entities. */
  readonly WorldEntities: "world.entities";
  /** TeaKit runtime can drive recipe crafting. */
  readonly WorldRecipes: "world.recipes";
  /** TeaKit runtime can build route fixtures and pathing support structures. */
  readonly WorldPathing: "world.pathing";
  /** TeaKit runtime can trigger controlled explosions. */
  readonly WorldExplosions: "world.explosions";
  /** TeaKit runtime can inspect bounded world areas. */
  readonly WorldInspection: "world.inspection";
  /** TeaKit runtime can identify the primary player. */
  readonly PlayerSelf: "player.self";
  /** TeaKit runtime can read the primary player position. */
  readonly PlayerPosition: "player.position";
  /** TeaKit runtime can teleport the primary player. */
  readonly PlayerTeleport: "player.teleport";
  /** TeaKit runtime can reset the primary player's state. */
  readonly PlayerReset: "player.reset";
  /** TeaKit runtime can give items to the primary player. */
  readonly PlayerGive: "player.give";
  /** TeaKit runtime can read the primary player inventory. */
  readonly PlayerInventory: "player.inventory";
  /** TeaKit runtime can make the primary player use the held item. */
  readonly PlayerUseItem: "player.useItem";
  /** TeaKit runtime can perform higher-level player actions such as crafting, mining, and interacting. */
  readonly PlayerActions: "player.actions";
  /** TeaKit runtime can inspect the current client screen. */
  readonly ClientScreen: "client.screen";
  /** TeaKit runtime can close menus and wait for stable screen IDs. */
  readonly ClientScreens: "client.screens";
  /** TeaKit runtime can capture a client screenshot. */
  readonly ClientScreenshot: "client.screenshot";
  /** TeaKit runtime can observe client render state such as rendered frames. */
  readonly ClientRenderProbes: "client.render.probes";
  /** TeaKit runtime can synthesize or direct client input such as camera look targets. */
  readonly ClientInput: "client.input";
};

/**
 * Runtime-owned spy probe kinds.
 *
 * Use these constants instead of raw strings when attaching probes with
 * `spy.observe()` so IDE completion shows the first-party probe vocabulary.
 */
export declare const SpyKind: {
  /** Observe server command execution. */
  readonly Command: "command";
  /** Observe a stable TeaKit runtime event. */
  readonly Event: "event";
  /** Observe a stable TeaKit method target implemented by reflection, Mixins, or instrumentation. */
  readonly Method: "method";
  /** Observe runtime or Minecraft log entries. */
  readonly Log: "log";
  /** Observe chat messages. */
  readonly Chat: "chat";
  /** Observe clientbound or serverbound network packets when TeaKit supports the stream. */
  readonly Packet: "packet";
  /** Observe block changes. */
  readonly Block: "block";
  /** Observe entity lifecycle events. */
  readonly Entity: "entity";
  /** Observe played sounds. */
  readonly Sound: "sound";
  /** Observe spawned particles. */
  readonly Particle: "particle";
  /** Observe active screen changes. */
  readonly Screen: "screen";
  /** Observe screenshot capture events. */
  readonly Screenshot: "screenshot";
  /** Observe synthesized or captured client input. */
  readonly ClientInput: "client-input";
  /** Observe calls made through a TeaKit-managed Java interface proxy. */
  readonly InterfaceProxy: "interface-proxy";
};

/**
 * Stable event target IDs for `spy.event()`.
 *
 * These names are the public TeaKit contract. Loader-specific event classes and
 * Minecraft internals should stay inside the Java runtime.
 */
export declare const SpyEventTarget: {
  /** Server command execution event. */
  readonly ServerCommand: "server.command";
  /** Chat message event. */
  readonly ChatMessage: "chat.message";
  /** Block state change event. */
  readonly BlockChanged: "block.changed";
  /** Entity spawned into the world. */
  readonly EntitySpawned: "entity.spawned";
  /** Entity removed from the world. */
  readonly EntityRemoved: "entity.removed";
  /** Player interacted with an entity or block. */
  readonly PlayerInteract: "player.interact";
  /** Client screen changed. */
  readonly ClientScreenChanged: "client.screen.changed";
};

/**
 * Stable method target IDs for `spy.method()`.
 *
 * TeaKit runtime maps these to the right Java symbols per Minecraft version.
 */
export declare const SpyMethodTarget: {
  /** Primary player position setter or teleport path. */
  readonly PlayerSetPosition: "player.setPosition";
  /** Entity position setter. */
  readonly EntitySetPosition: "entity.setPosition";
  /** World block setter. */
  readonly WorldSetBlock: "world.setBlock";
  /** Client screen opening method. */
  readonly ClientSetScreen: "client.setScreen";
};

/**
 * Stable packet target IDs for `spy.packet()`.
 */
export declare const SpyPacketTarget: {
  /** Any clientbound packet. */
  readonly ClientboundAny: "clientbound.*";
  /** Any serverbound packet. */
  readonly ServerboundAny: "serverbound.*";
  /** Clientbound entity equipment packet. */
  readonly ClientboundSetEquipment: "clientbound.setEquipment";
  /** Clientbound block update packet. */
  readonly ClientboundBlockUpdate: "clientbound.blockUpdate";
  /** Serverbound player action packet. */
  readonly ServerboundPlayerAction: "serverbound.playerAction";
  /** Serverbound use item packet. */
  readonly ServerboundUseItem: "serverbound.useItem";
};

/**
 * Stable allowlist target IDs for Java interface proxies created by `spy.proxy()`.
 *
 * Prefer these constants over raw JVM class names. TeaKit runtime maps each target
 * ID to a concrete allowlisted Java interface per supported Minecraft version.
 */
export declare const SpyProxyTarget: {
  /** General callback shape for scenario-owned callback probes. */
  readonly ScenarioCallback: "teakit.scenarioCallback";
  /** TeaKit runtime lifecycle callback interface. */
  readonly RuntimeLifecycle: "teakit.runtimeLifecycle";
  /** Amber platform hook interface exposed through the cooperating runtime. */
  readonly AmberPlatformHook: "amber.platformHook";
  /** Amber registry callback interface exposed through the cooperating runtime. */
  readonly AmberRegistryCallback: "amber.registryCallback";
};

/**
 * Logical runtime side values used in structured spy call reports.
 */
export declare const SpySide: {
  /** The call was observed on the Minecraft client side. */
  readonly Client: "client";
  /** The call was observed on the Minecraft server side. */
  readonly Server: "server";
  /** The call is side-neutral or applies to shared runtime behavior. */
  readonly Common: "common";
};

/**
 * Runtime readiness constants for `describe.configure()` and per-test `readiness`.
 * Use these to declare required Minecraft/client state before tests execute.
 */
export declare const Readiness: {
  /** No Minecraft readiness requirement. */
  readonly None: "none";
  /** The client may still be on the title screen. */
  readonly Title: "title";
  /** A world is loaded. */
  readonly World: "world";
  /** At least one player is present. */
  readonly Player: "player";
  /** The client runtime is responding and ready for basic interaction. */
  readonly ClientReady: "client-ready";
  /** The primary player has spawned. */
  readonly PlayerSpawned: "player-spawned";
  /** The integrated server is ready, when running singleplayer/client scenarios. */
  readonly IntegratedServerReady: "integrated-server-ready";
  /** Resource reload has completed. */
  readonly ResourceReloadComplete: "resource-reload-complete";
};

export interface TestFunction {
  (name: string, fn: (context: TeaKitTestContext, info: TeaKitTestInfo) => Awaitable<void>, options?: TestOptions): void;
  (name: string, options: TestOptions, fn: (context: TeaKitTestContext, info: TeaKitTestInfo) => Awaitable<void>): void;
  /**
   * Define one test for each row in `rows`.
   *
   * Name templates support printf-style placeholders such as `%s` and object
   * placeholders such as `$id`. The row values are passed to the callback before
   * TeaKit's `(context, info)` arguments.
   */
  each<Row extends readonly unknown[]>(rows: readonly Row[]): EachTestFunction<Row>;
  /**
   * Define one test for each object or scalar row in `rows`.
   *
   * Object rows work well with `$property` placeholders in the test name.
   */
  each<Row>(rows: readonly Row[]): EachTestFunction<Row>;
  skip: SkippableTestFunction;
  only: FocusableTestFunction;
  todo(name: string, options?: TestOptions): void;
}

export interface EachTestFunction<Row> {
  (name: string, fn: (...args: [...EachArgs<Row>, TeaKitTestContext, TeaKitTestInfo]) => Awaitable<void>, options?: TestOptions): void;
  (name: string, fn: (...args: [...EachArgs<Row>, TeaKitTestContext]) => Awaitable<void>, options?: TestOptions): void;
  (name: string, fn: (...args: EachArgs<Row>) => Awaitable<void>, options?: TestOptions): void;
  (name: string, options: TestOptions, fn: (...args: [...EachArgs<Row>, TeaKitTestContext, TeaKitTestInfo]) => Awaitable<void>): void;
  (name: string, options: TestOptions, fn: (...args: [...EachArgs<Row>, TeaKitTestContext]) => Awaitable<void>): void;
  (name: string, options: TestOptions, fn: (...args: EachArgs<Row>) => Awaitable<void>): void;
}

export interface SkippableTestFunction {
  (name: string, fn?: (context: TeaKitTestContext, info: TeaKitTestInfo) => Awaitable<void>, options?: TestOptions): void;
  (name: string, options: TestOptions, fn?: (context: TeaKitTestContext, info: TeaKitTestInfo) => Awaitable<void>): void;
  /** Define skipped table-driven tests. */
  each<Row extends readonly unknown[]>(rows: readonly Row[]): EachTestFunction<Row>;
  /** Define skipped table-driven tests. */
  each<Row>(rows: readonly Row[]): EachTestFunction<Row>;
}

export interface FocusableTestFunction {
  (name: string, fn: (context: TeaKitTestContext, info: TeaKitTestInfo) => Awaitable<void>, options?: TestOptions): void;
  (name: string, options: TestOptions, fn: (context: TeaKitTestContext, info: TeaKitTestInfo) => Awaitable<void>): void;
  /** Define focused table-driven tests. */
  each<Row extends readonly unknown[]>(rows: readonly Row[]): EachTestFunction<Row>;
  /** Define focused table-driven tests. */
  each<Row>(rows: readonly Row[]): EachTestFunction<Row>;
}

export interface DescribeFunction {
  (name: string, fn: () => void): void;
  configure(options: SuiteOptions): void;
  /**
   * Define one suite for each row in `rows`.
   *
   * Name templates support printf-style placeholders such as `%s` and object
   * placeholders such as `$id`. The row values are passed to the suite callback.
   */
  each<Row extends readonly unknown[]>(rows: readonly Row[]): EachDescribeFunction<Row>;
  /**
   * Define one suite for each object or scalar row in `rows`.
   *
   * Object rows work well with `$property` placeholders in the suite name.
   */
  each<Row>(rows: readonly Row[]): EachDescribeFunction<Row>;
  skip: SkippableDescribeFunction;
  only: FocusableDescribeFunction;
}

export interface EachDescribeFunction<Row> {
  (name: string, fn: EachDescribeBody<Row>): void;
}

export interface SkippableDescribeFunction {
  (name: string, fn: () => void): void;
  /** Define skipped table-driven suites. */
  each<Row extends readonly unknown[]>(rows: readonly Row[]): EachDescribeFunction<Row>;
  /** Define skipped table-driven suites. */
  each<Row>(rows: readonly Row[]): EachDescribeFunction<Row>;
}

export interface FocusableDescribeFunction {
  (name: string, fn: () => void): void;
  /** Define focused table-driven suites. */
  each<Row extends readonly unknown[]>(rows: readonly Row[]): EachDescribeFunction<Row>;
  /** Define focused table-driven suites. */
  each<Row>(rows: readonly Row[]): EachDescribeFunction<Row>;
}

export interface Matchers<T> {
  toBe(expected: T): void;
  toEqual(expected: unknown): void;
  toContain(expected: unknown): void;
  toBeDefined(): void;
  toBeUndefined(): void;
  toBeNull(): void;
  toBeTruthy(): void;
  toBeFalsy(): void;
  toBeGreaterThan(expected: number): void;
  toBeGreaterThanOrEqual(expected: number): void;
  toBeLessThan(expected: number): void;
  toBeLessThanOrEqual(expected: number): void;
  toThrow(expected?: string | RegExp | Error | (new (...args: never[]) => Error)): void;
  /** Assert that a runtime summary/capabilities object supports at least this TeaKit runtime API version. */
  toSupportRuntimeApi(expected: number): void;
  /** Assert that a `{ x, y, z }` value is within `distance` blocks of another position. */
  toBeNear(expected: Vec3 | BlockPos, options?: { distance?: number }): void;
  /** Assert that a block, item, entity, or registry-like value has the expected ID. */
  toHaveId(expected: string): void;
  /** Assert that an inventory-like value or lazy inventory/container probe contains the expected item. */
  toContainItem(expected: ItemId, options?: InventoryItemExpectationOptions): InventoryMatcherResult<T>;
  /** Assert that a render probe or inspection has a resolved model. */
  toHaveModel(): Promise<void>;
  /** Assert that a render probe or inspection has resolved texture data. */
  toHaveTexture(): Promise<void>;
  /** Assert that a block/state-like value reports the expected effective tool. */
  toHaveEffectiveTool(expected: string): Promise<void>;
  /** Assert that an entity/reference-like value has the expected entity type. */
  toHaveType(expected: EntityTypeId): Promise<void>;
  /** Assert that an entity/reference-like value currently exists. */
  toExist(): Promise<void>;
  /** Assert that a screen-like value has a matching title. */
  toHaveTitleLike(expected: string | RegExp): Promise<void>;
  /** Assert that a `SpyCall[]`, `SpyReport`, `SpyHandle`, or `SpyProxy` has at least one recorded call. */
  toHaveBeenCalled(): SpyMatcherResult<T>;
  /** Assert that a `SpyCall[]`, `SpyReport`, `SpyHandle`, or `SpyProxy` has exactly `expected` recorded calls. */
  toHaveBeenCalledTimes(expected: number): SpyMatcherResult<T>;
  /** Assert that a `SpyCall[]`, `SpyReport`, `SpyHandle`, or `SpyProxy` includes a call with exactly these arguments. */
  toHaveBeenCalledWith(...expectedArgs: unknown[]): SpyMatcherResult<T>;
  /** Assert that an event array or event stream contains the expected partial events in order. */
  toContainOrdered(expected: unknown[]): T extends EventStream ? Promise<void> : void;
  /** Assert that a transaction or structured runtime result failed with matching partial metadata. */
  toHaveFailedWith(expected: Record<string, unknown>): void;
  /** Poll a function, or await a promise/value once, until it equals `expected`. */
  toEventuallyEqual(expected: unknown, options?: EventuallyOptions): Promise<void>;
  /** Poll a function, or await a promise/value once, until it resolves to a present value. */
  toEventuallyExist(options?: EventuallyOptions): Promise<void>;
  /** Poll a function, or await a promise/value once, until it is less than `expected`. */
  toEventuallyBeLessThan(expected: number, options?: EventuallyOptions): Promise<void>;
  /** Poll a function, or await a promise/value once, until it looks like a dead/removed entity. */
  toEventuallyBeDead(options?: EventuallyOptions): Promise<void>;
  not: Matchers<T>;
  resolves: Matchers<Awaited<T>>;
  rejects: Matchers<unknown>;
}

export interface EventuallyOptions {
  timeout?: string | number;
  interval?: string | number;
}

export type SpyMatcherResult<T> = T extends SpyHandle | SpyProxy<any> ? Promise<void> : void;

export interface AsymmetricMatcher<T = unknown> {
  readonly __teakitAsymmetricMatcher: string;
  readonly expected?: T;
}

export declare const describe: DescribeFunction;
export declare const test: TestFunction;
export declare const it: TestFunction;
export declare function beforeAll(fn: (context: TeaKitTestContext, info: TeaKitTestInfo) => Awaitable<void>): void;
export declare function beforeEach(fn: (context: TeaKitTestContext, info: TeaKitTestInfo) => Awaitable<void>): void;
export declare function afterEach(fn: (context: TeaKitTestContext, info: TeaKitTestInfo) => Awaitable<void>): void;
export declare function afterAll(fn: (context: TeaKitTestContext, info: TeaKitTestInfo) => Awaitable<void>): void;
export declare function expect<T>(actual: T): Matchers<T>;
export declare namespace expect {
  /** Match any value except `null` and `undefined`. */
  function anything(): AsymmetricMatcher;
  /** Match values assignable to a JavaScript constructor such as `String`, `Number`, `Array`, or a class. */
  function any(constructor: StringConstructor): AsymmetricMatcher<string>;
  function any(constructor: NumberConstructor): AsymmetricMatcher<number>;
  function any(constructor: BooleanConstructor): AsymmetricMatcher<boolean>;
  function any(constructor: ArrayConstructor): AsymmetricMatcher<unknown[]>;
  function any(constructor: ObjectConstructor): AsymmetricMatcher<Record<string, unknown>>;
  function any<T>(constructor: new (...args: any[]) => T): AsymmetricMatcher<T>;
  /** Match objects that include the provided subset, recursively honoring other asymmetric matchers. */
  function objectContaining<T extends Record<string, unknown>>(expected: T): AsymmetricMatcher<T>;
  /** Match arrays that include every provided item, recursively honoring other asymmetric matchers. */
  function arrayContaining<T>(expected: T[]): AsymmetricMatcher<T[]>;
  /** Match strings that contain the provided substring. */
  function stringContaining(expected: string): AsymmetricMatcher<string>;
}
/** Create a JSON-serializable block position with fluent coordinate helpers. */
export declare function pos(x: number, y: number, z: number): TeaKitPosition;

export type BlockId = `${string}:${string}`;
export type ItemId = `${string}:${string}`;
export type EntityTypeId = `${string}:${string}`;
export type BiomeId = `${string}:${string}`;
export type FluidId = `${string}:${string}`;
export interface BlockPos { x: number; y: number; z: number; }
export interface Vec3 { x: number; y: number; z: number; }
export interface TeaKitPosition extends BlockPos {
  /** Return a new position offset from this one. */
  offset(dx?: number, dy?: number, dz?: number): TeaKitPosition;
  /** Return a new position above this one. */
  above(dy?: number): TeaKitPosition;
  /** Return a new position below this one. */
  below(dy?: number): TeaKitPosition;
  /** Return the center point of this block position. */
  center(): Vec3;
  /** Return whether another block position has the same coordinates. */
  equals(other: BlockPos): boolean;
}
export interface EntitySnapshot {
  id: string;
  type: string;
  uuid?: string;
  entityType?: EntityTypeId;
  pos?: TeaKitPosition;
  position?: Vec3;
  dimension?: string;
  exists?: boolean;
  alive?: boolean;
  dead?: boolean;
  removed?: boolean;
  vehicle?: EntityRef | null;
}
export interface EntityVehicleProbe extends PromiseLike<EntityRef | null> {
  (): Promise<EntityRef | null>;
  catch<TResult = never>(
    onrejected?: ((reason: any) => TResult | PromiseLike<TResult>) | null,
  ): Promise<EntityRef | null | TResult>;
  finally(onfinally?: (() => void) | null): Promise<EntityRef | null>;
}
export interface EntityRef {
  id: string;
  type: string;
  /** Read the current runtime state for this entity. */
  inspect(options?: RuntimeCallOptions): Promise<EntitySnapshot>;
  /** Kill or remove this entity through the runtime. */
  kill(options?: RuntimeCallOptions): Promise<EntityActionResult>;
  /** Move this entity to a world position. */
  moveTo(pos: BlockPos | Vec3, options?: RuntimeCallOptions): Promise<EntityActionResult>;
  /** Push this entity into a vehicle or mount-like target. */
  pushInto(target: EntityRef, options?: RuntimeCallOptions): Promise<EntityActionResult>;
  /** Return a pollable probe for the entity's current vehicle, if any. */
  vehicle(options?: RuntimeCallOptions): EntityVehicleProbe;
}
export interface PlayerRef { name: string; uuid?: string; }

export interface EntityActionResult {
  ok?: boolean;
  action?: string;
  entity?: EntityRef | EntitySnapshot;
  target?: EntityRef | EntitySnapshot;
  pos?: Vec3;
  position?: Vec3;
  dimension?: string;
}

export interface VolumeSize {
  /** Width of the fixture volume on the X axis. */
  dx: number;
  /** Height of the fixture volume on the Y axis. */
  dy: number;
  /** Depth of the fixture volume on the Z axis. */
  dz: number;
}

export interface FixturePlatformOptions {
  /** Block used for the platform surface. */
  block: BlockId | BlockStateInput;
  /** Square platform size in blocks. */
  size: number;
  [key: string]: unknown;
}

export interface FixtureGridOptions {
  /** Block used at each grid point. */
  block: BlockId | BlockStateInput;
  /** Distance in blocks between grid points. */
  spacing: number;
  /** Optional radius around the fixture origin. */
  radius?: number;
  [key: string]: unknown;
}

export interface FixtureLabelOptions {
  /** Optional position for the label. */
  at?: BlockPos | RelativeColumnPos;
  /** Optional block or rendering style requested from the runtime. */
  style?: string;
  [key: string]: unknown;
}

export interface FixturePondOptions {
  /** Relative or absolute pond anchor. */
  at: BlockPos | RelativeColumnPos;
  /** Square pond size in blocks. */
  size: number;
  /** Pond depth in blocks. */
  depth?: number;
  /** Fluid block used to fill the pond. */
  fluid?: BlockId;
  [key: string]: unknown;
}

export interface FixtureTreeOptions {
  /** Relative or absolute tree anchor. */
  at: BlockPos | RelativeColumnPos;
  /** Runtime-defined tree type, such as `oak` or `minecraft:oak`. */
  type: string;
  [key: string]: unknown;
}

export interface FixtureCropPatchOptions {
  /** Relative or absolute patch anchor. */
  at: BlockPos | RelativeColumnPos;
  /** Crop block to place. */
  crop: BlockId;
  /** Whether the crop should be placed at mature age. */
  mature?: boolean;
  [key: string]: unknown;
}

export interface FixtureAnimalPenOptions {
  /** Relative or absolute animal pen anchor. */
  at: BlockPos | RelativeColumnPos;
  /** Entity types to spawn inside the pen. */
  animals: EntityTypeId[];
  [key: string]: unknown;
}

export interface RelativeColumnPos {
  /** Relative X coordinate from the fixture origin. */
  x: number;
  /** Relative Z coordinate from the fixture origin. */
  z: number;
}

export interface WorldFixtureResult {
  /** Runtime-accepted fixture name. */
  name?: string;
  /** Runtime-accepted fixture origin. */
  origin?: TeaKitPosition;
  /** Optional suggested spawn or camera point for the fixture. */
  spawn?: TeaKitPosition;
  /** Stable result for a pond feature, when requested or returned by the runtime. */
  pond?: FixturePondResult;
  /** Stable result for a tree feature, when requested or returned by the runtime. */
  tree?: FixtureTreeResult;
  /** Stable result for a crop patch feature, when requested or returned by the runtime. */
  cropPatch?: FixtureCropPatchResult;
  /** Stable result for an animal pen feature, when requested or returned by the runtime. */
  animalPen?: FixtureAnimalPenResult;
  /** Convenience alias for `animalPen`. */
  pen?: FixtureAnimalPenResult;
  [key: string]: unknown;
}

export interface FixturePondResult {
  center?: TeaKitPosition;
  surface?: TeaKitPosition;
  fluid?: BlockId;
  [key: string]: unknown;
}

export interface FixtureTreeResult {
  trunk: TeaKitPosition;
  canopy?: TeaKitPosition;
  type?: string;
  [key: string]: unknown;
}

export interface FixtureCropPatchResult {
  center?: TeaKitPosition;
  crop?: BlockId;
  mature?: boolean;
  [key: string]: unknown;
}

export interface FixtureAnimalPenResult {
  center: TeaKitPosition;
  animals?: EntityRef[];
  [key: string]: unknown;
}

export interface ScenarioDefinition {
  name?: string;
  player?: string;
  setup?: ScenarioStep[];
  steps: ScenarioStep[];
  cleanup?: ScenarioStep[];
  [key: string]: unknown;
}

export type ScenarioStep =
  | ActivateWidgetStep
  | AssertSpyCalledStep
  | AssertSpyCalledBeforeStep
  | AssertSpyCountStep
  | AssertSpyLastArgStep
  | BreakBlockStep
  | ClearNearbyEntitiesStep
  | ClickMenuButtonStep
  | ClickMenuSlotStep
  | CloseMenuStep
  | CommandStep
  | DamageNearestEntityStep
  | DropMainHandItemStep
  | ForceFishingBiteStep
  | InteractNearestEntityStep
  | KeyStateStep
  | LeaveWorldStep
  | LookAtStep
  | OpenInventoryStep
  | ProbePlayerStep
  | RandomTickBlockStep
  | RunScenarioStep
  | ScrollMouseStep
  | SetUseHeldStep
  | SpawnEntityStep
  | SpawnItemStep
  | SpyResetStep
  | UseBlockStep
  | UseBlockServerStep
  | UseItemStep
  | WaitForBlockStep
  | WaitForEntityCountStep
  | WaitForInventoryItemStep
  | WaitForNoScreenStep
  | WaitForScreenStep
  | WaitMsStep;

export interface StepFilters {
  versions?: VersionSelector;
  loaders?: LoaderSelector;
}

export interface VersionSelector {
  include?: string | string[];
  exclude?: string | string[];
  min?: string;
  max?: string;
}

export interface LoaderSelector {
  include?: LoaderId | LoaderId[];
  exclude?: LoaderId | LoaderId[];
}

export type HandId = "main_hand" | "off_hand" | string;
export type DirectionId = "up" | "down" | "north" | "south" | "east" | "west" | string;
export type ClickTypeId = "PICKUP" | "QUICK_MOVE" | "SWAP" | "CLONE" | "THROW" | "QUICK_CRAFT" | "PICKUP_ALL" | string;

export interface ScenarioStepBase extends StepFilters {
  action: string;
  comment?: string;
}

export interface ActivateWidgetStep extends ScenarioStepBase {
  action: "activate_widget";
  label: string;
  waitAfterMs?: number;
}

export interface AssertSpyCalledStep extends ScenarioStepBase {
  action: "assert_spy_called";
  name: string;
}

export interface AssertSpyCalledBeforeStep extends ScenarioStepBase {
  action: "assert_spy_called_before";
  before: string;
  after: string;
}

export interface AssertSpyCountStep extends ScenarioStepBase {
  action: "assert_spy_count";
  name: string;
  count: number;
}

export interface AssertSpyLastArgStep extends ScenarioStepBase {
  action: "assert_spy_last_arg";
  name: string;
  index: number;
  value: unknown;
}

export interface BreakBlockStep extends ScenarioStepBase, BlockPos {
  action: "break_block";
  timeoutMs?: number;
}

export interface ClearNearbyEntitiesStep extends ScenarioStepBase {
  action: "clear_nearby_entities";
  radius: number;
  entityType?: EntityTypeId;
  timeoutMs?: number;
}

export interface ClickMenuButtonStep extends ScenarioStepBase {
  action: "click_menu_button";
  button: number;
}

export interface ClickMenuSlotStep extends ScenarioStepBase {
  action: "click_menu_slot";
  slot: number;
  button?: number;
  clickType?: ClickTypeId;
}

export interface CloseMenuStep extends ScenarioStepBase {
  action: "close_menu";
}

export interface CommandStep extends ScenarioStepBase {
  action: "command" | "assert_command";
  command: string;
  waitAfterMs?: number;
}

export interface DamageNearestEntityStep extends ScenarioStepBase {
  action: "damage_nearest_entity";
  radius: number;
  amount: number;
  entityType?: EntityTypeId;
}

export interface DropMainHandItemStep extends ScenarioStepBase {
  action: "drop_main_hand_item";
  count?: number;
}

export interface ForceFishingBiteStep extends ScenarioStepBase {
  action: "force_fishing_bite";
}

export interface InteractNearestEntityStep extends ScenarioStepBase {
  action: "interact_nearest_entity";
  radius: number;
  entityType?: EntityTypeId;
  hand?: HandId;
  targetX?: number;
  targetY?: number;
  targetZ?: number;
  waitAfterMs?: number;
}

export interface KeyStateStep extends ScenarioStepBase {
  action: "key_state";
  key: number;
  pressed: boolean;
}

export interface LeaveWorldStep extends ScenarioStepBase {
  action: "leave_world";
  waitAfterMs?: number;
}

export interface LookAtStep extends ScenarioStepBase, Vec3 {
  action: "look_at";
}

export interface OpenInventoryStep extends ScenarioStepBase {
  action: "open_inventory";
}

export interface ProbePlayerStep extends ScenarioStepBase {
  action: "probe_player";
  radius?: number;
}

export interface RandomTickBlockStep extends ScenarioStepBase, BlockPos {
  action: "random_tick_block";
}

export interface RunScenarioStep extends ScenarioStepBase {
  action: "run_scenario";
  scenario: string;
}

export interface ScrollMouseStep extends ScenarioStepBase {
  action: "scroll_mouse";
  verticalAmount: number;
  x?: number;
  y?: number;
}

export interface SetUseHeldStep extends ScenarioStepBase {
  action: "set_use_held";
  held: boolean;
}

export interface SpawnEntityStep extends ScenarioStepBase {
  action: "spawn_entity";
  entityType: EntityTypeId;
}

export interface SpawnItemStep extends ScenarioStepBase {
  action: "spawn_item";
  itemId: ItemId;
}

export interface SpyResetStep extends ScenarioStepBase {
  action: "spy_reset";
  name?: string;
}

export interface UseBlockStep extends ScenarioStepBase, BlockPos {
  action: "use_block";
  direction?: DirectionId;
  hand?: HandId;
}

export interface UseBlockServerStep extends ScenarioStepBase, BlockPos {
  action: "use_block_server";
  direction?: DirectionId;
  hand?: HandId;
}

export interface UseItemStep extends ScenarioStepBase {
  action: "use_item";
}

export interface WaitForBlockStep extends ScenarioStepBase, BlockPos {
  action: "wait_for_block";
  blockId?: BlockId;
  block?: BlockId | BlockStateInput;
  air?: boolean;
  timeoutMs?: number;
}

export interface WaitForEntityCountStep extends ScenarioStepBase {
  action: "wait_for_entity_count";
  radius: number;
  entityType?: EntityTypeId;
  count: number;
  timeoutMs?: number;
  readyForShearing?: boolean;
  sheared?: boolean;
}

export interface WaitForInventoryItemStep extends ScenarioStepBase {
  action: "wait_for_inventory_item";
  itemId?: ItemId;
  count?: number;
  displayNameContains?: string;
  timeoutMs?: number;
}

export interface WaitForNoScreenStep extends ScenarioStepBase {
  action: "wait_for_no_screen";
  timeoutMs?: number;
}

export interface WaitForScreenStep extends ScenarioStepBase {
  action: "wait_for_screen";
  screenClass: string;
  timeoutMs?: number;
}

export interface WaitMsStep extends ScenarioStepBase {
  action: "wait_ms";
  durationMs: number;
}

export interface ScenarioResult {
  name?: string;
  player?: string;
  durationMs?: number;
  setup?: ScenarioStepResult[];
  steps?: ScenarioStepResult[];
  cleanup?: ScenarioStepResult[];
  [key: string]: unknown;
}

export interface ScenarioStepResult {
  index?: number;
  action?: string;
  comment?: string;
  result?: unknown;
  [key: string]: unknown;
}

export interface BlockState {
  id: BlockId;
  properties?: Record<string, string | number | boolean>;
  [key: string]: unknown;
}

export type BlockStateInput = BlockState | { id: BlockId; properties?: Record<string, string | number | boolean> };

export interface ItemStack {
  id: ItemId;
  count: number;
  slot?: number;
  equipmentSlot?: string;
  components?: Record<string, unknown>;
  metadata?: Record<string, unknown>;
  nbt?: Record<string, unknown>;
  [key: string]: unknown;
}

export type ItemStackInput = ItemId | Partial<ItemStack> & { id: ItemId };

export interface PlayerInventory {
  items: ItemStack[];
  selectedSlot?: number;
  selectedItem?: ItemStack;
  equipment?: Record<string, ItemStack>;
  [key: string]: unknown;
}

export type PlayerInventoryProbe = Promise<PlayerInventory> & {
  /** Read a stable inventory snapshot for artifact attachment or later comparison. */
  snapshot(options?: RuntimeCallOptions): Promise<PlayerInventorySnapshot>;
};

export interface PlayerInventorySnapshot extends PlayerInventory {
  capturedAt?: number | string;
}

export interface InventoryItemExpectationOptions {
  /** Minimum matching stack count required. Defaults to 1. */
  count?: number;
  /** Require the matching stack to be in this inventory slot. */
  slot?: number;
  /** Require the matching stack to be the selected/held item. */
  selected?: boolean;
  /** Require the matching stack to be in this equipment slot, such as `mainhand`, `offhand`, `head`, or `feet`. */
  equipmentSlot?: string;
  /** Alias for `equipmentSlot`. */
  equipment?: string;
  /** Require matching item components as a partial object. */
  components?: Record<string, unknown>;
  /** Require matching item metadata as a partial object across metadata/components/NBT-like fields. */
  metadata?: Record<string, unknown>;
  /** Require matching NBT/tag data as a partial object. */
  nbt?: Record<string, unknown>;
}

export type InventoryMatcherResult<T> = T extends WorldContainerProbe | PlayerInventoryProbe ? Promise<void> : void;

export interface WorldTime {
  dayTime: number;
  gameTime?: number;
  [key: string]: unknown;
}

export type WorldTimeInput = number | { dayTime: number };
export type WorldWeatherInput = "clear" | "rain" | "thunder" | { type: "clear" | "rain" | "thunder"; durationTicks?: number };

export interface WorldWeather {
  type: "clear" | "rain" | "thunder" | string;
  raining?: boolean;
  thundering?: boolean;
  [key: string]: unknown;
}

export interface ClientScreen {
  id?: string;
  title?: string;
  widgets?: unknown[];
  [key: string]: unknown;
}
