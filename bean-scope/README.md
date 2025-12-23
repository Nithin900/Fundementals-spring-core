# Bean Scope Project

Spring Core practice module focused on how bean scopes and initialization strategies affect runtime behavior.

## What This Demonstrates
- Prototype vs singleton scope (`VehicleServices` is prototype; `Vehicle` and `Person` are singletons).
- Eager vs lazy initialization (`Vehicle` is created eagerly, `Person` is marked `@Lazy`).
- Constructor injection of dependencies (`Vehicle` depends on `VehicleServices`; `Person` depends on `Vehicle`).
- Multiple implementations of an interface (`Tyres` has `BridgeStoneTyres` and `MichelinTyres`).

## Bean Scope Concepts
- **Singleton**: one instance per container. Good for stateless services; reused on every lookup (`Vehicle`, `Person` here).
- **Prototype**: new instance on every lookup (`VehicleServices`). The container creates but does not manage lifecycle beyond creation.
- **Prototype injected into singleton**: the prototype is resolved once during singleton creation unless you request fresh instances via `ObjectProvider` or method injection.
- **Lazy vs eager**: singleton beans are eager by default; adding `@Lazy` defers creation until first access (`Person`).

### Object Creation and Dependency Flow
```
App startup
  |
  |-- create Vehicle (singleton, eager)
  |      |
  |      |-- create VehicleServices (prototype) for constructor injection
  |
  |-- Person (singleton, lazy) not created yet

Main execution
  |-- getBean(VehicleServices) -> new prototype instance (1)
  |-- getBean(VehicleServices) -> new prototype instance (2)
  |-- getBean(Vehicle)         -> returns existing singleton
  |-- getBean(Person)          -> creates lazy singleton now, using existing Vehicle
```

### Lifecycle of Beans (as used here)
```
Container refresh
  |
  |-- instantiate singletons (eager): Vehicle
  |       |
  |       +-- inject prototype VehicleServices (created now)
  |
  |-- register lazy singleton Person (not instantiated yet)
  |
Runtime lookups
  |
  |-- prototype bean requests -> new VehicleServices each time
  |-- first Person request    -> instantiate Person, inject existing Vehicle
  |-- subsequent Person requests -> same Person instance reused
```

## How It Works
- `Main` boots `AnnotationConfigApplicationContext` with `AppConfig`.
- Two calls to `context.getBean(VehicleServices.class)` show prototype scope via differing hash codes.
- Fetching `Vehicle` triggers eager creation; `Person` creation is deferred until first access because of `@Lazy`.
- Console logs from constructors make lifecycle events visible.

## Run the Demo
```bash
cd bean-scope
mvn -q clean package
# If you have the exec plugin available:
mvn -q exec:java -Dexec.mainClass="org.example.Main"
```
If `exec:java` is not configured, add the standard `exec-maven-plugin` or run the compiled classes with your preferred launcher.

## Recommended Improvements / Advanced Practice
1. Fix the component scan package typo (`org.example.implementation` -> `org.example.implementations`) so tyre beans are discovered.
2. Inject `Tyres` into `VehicleServices` via constructor injection and mark one implementation `@Primary` (or use `@Qualifier`) to make the example fully wired.
3. When injecting prototypes into singletons, consider using `ObjectProvider<VehicleServices>` to obtain a fresh instance per use instead of one-at-startup injection.
4. Add simple assertions or JUnit tests that validate scope differences and lazy loading behavior.
5. Add lightweight logging (`java.util.logging` or SLF4J) instead of `System.out` for clearer output control.

## Expected Output (abridged)
- `VehicleServices object is created` printed for each prototype instance (one during `Vehicle` construction, plus each explicit `getBean` call).
- `Vehicle bean is created by spring` printed eagerly at startup.
- `Person bean is created by spring.` printed only when the lazy `Person` bean is first retrieved.
- Hash codes in the console showing whether `VehicleServices` instances are identical (singleton) or different (prototype).
