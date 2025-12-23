# Quick Reference: Bean Scope Demo

## Commands
```bash
cd bean-scope
mvn -q clean package
mvn -q exec:java -Dexec.mainClass="org.example.Main"   # if exec plugin is present
```

## Key Annotations & Scopes
- `@Scope(BeanDefinition.SCOPE_PROTOTYPE)` — new instance per lookup (`VehicleServices`).
- Default scope is singleton — one instance per container (`Vehicle`, `Person`).
- `@Lazy` — defer creation until first access (`Person`).
- `@ComponentScan` — ensures beans are discovered; fix typo to `org.example.implementations` to pick up tyre beans.

## Verification Steps
- Watch console logs:
  - `VehicleServices object is created` appears for each prototype instance (expect multiple).
  - `Vehicle bean is created by spring` appears at startup (eager).
  - `Person bean is created by spring.` appears only when the lazy bean is first requested.
- Hash codes printed for `VehicleServices` should differ when scope is prototype; they will match if you switch to singleton.

## Advanced Tips
- Prefer constructor injection for `Tyres` in `VehicleServices` and use `@Primary`/`@Qualifier` when multiple implementations exist.
- When singletons need fresh prototypes per call, inject `ObjectProvider<VehicleServices>` instead of a direct field.
- Replace `System.out` with a logger and add lightweight tests to guard scope behavior.
