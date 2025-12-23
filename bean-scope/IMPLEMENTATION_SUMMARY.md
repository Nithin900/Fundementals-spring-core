# Implementation Summary: Bean Scope Demo

## Core Scope Behavior
- **Prototype bean**: `VehicleServices` is annotated with `@Scope(BeanDefinition.SCOPE_PROTOTYPE)`. Every `getBean` call creates a new instance and prints `VehicleServices object is created`.
- **Singleton beans**: `Vehicle` and `Person` rely on default singleton scope.
- **Lazy singleton**: `Person` is annotated with `@Lazy`, so it initializes only when first requested.
- **Eager creation via injection**: Because `Vehicle` constructor-injects `VehicleServices`, Spring creates one prototype instance while building the `Vehicle` singleton. Additional calls to `getBean(VehicleServices.class)` create separate instances.

## Application Flow (Main.java)
1. Start `AnnotationConfigApplicationContext` with `AppConfig`.
2. Retrieve `VehicleServices` twice to compare hash codes and illustrate prototype scope.
3. Retrieve `Vehicle` (eager) and `Person` (lazy) to show lifecycle differences.
4. Print whether the two `VehicleServices` references are the same instance.

## Components
- `AppConfig`: `@Configuration` with component scanning. Note: `basePackages` currently uses `org.example.implementation` (typo) plus `basePackageClasses` for `Vehicle` and `Person`.
- `Vehicle`: `@Component("vehicleBean")` with constructor-injected `VehicleServices` and `@Value` for `name`.
- `Person`: `@Component("personBean")` and `@Lazy`, constructor-injects `Vehicle`.
- `VehicleServices`: `@Component`, prototype scope, placeholder `moveVehicle()` method.
- `Tyres` + `BridgeStoneTyres` + `MichelinTyres`: interface and two implementations to illustrate multiple beans of the same type.

## Gaps and Suggested Enhancements
1. **Component scanning**: Correct the typo so the `Tyres` implementations are actually discovered (`org.example.implementations`).
2. **Real dependency wiring**: Inject `Tyres` into `VehicleServices` via constructor and mark one implementation `@Primary` or use `@Qualifier` to demonstrate disambiguation.
3. **Prototype-with-singleton pattern**: Use `ObjectProvider<VehicleServices>` (or method injection) inside `Vehicle` if you want a fresh prototype per use instead of the startup-created instance.
4. **Lifecycle transparency**: Add logging around bean creation and retrieval to make lazy vs eager timing clearer.
5. **Tests**: Add JUnit tests that assert prototype beans are different, singleton beans are identical, and lazy beans defer instantiation until first access.

## Build & Run
```bash
cd bean-scope
mvn -q clean package
# optional if exec plugin is available
mvn -q exec:java -Dexec.mainClass="org.example.Main"
```

## What to Observe
- Constructor log lines fire each time a prototype instance is created.
- Hash codes printed for `VehicleServices` show differing values (prototype) unless scope is switched to singleton.
- `Person` log appears only upon first retrieval because of `@Lazy`.
