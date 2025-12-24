type RuntimeKey = "short" | "standard" | "long";
type RuntimeValue = 0 | 1 | 2;
type RuntimeMap = Record<RuntimeKey, RuntimeValue>;

export interface RuntimeResponse {
  data: RuntimeMap;
}
