ApplicationContext (AnnotationConfigApplicationContext)
 └─ loads ProjectConfig (@Configuration)
    ├─ @ComponentScan("org.example.beans") → finds Person, Vehicle components
    ├─ @ComponentScan("org.example.implementations") → finds FullTimeEmployee, PartTimeEmployee
    ├─ @ComponentScan("org.example.services") → finds EmployeeService
    ├─ @Bean vehicle1() @Primary  → creates Vehicle bean [name="Volkswagen Golf"]
    ├─ @Bean vehicle2()          → creates Vehicle bean [name="Audi 8"]
    ├─ @Bean vehicle3()          → creates Vehicle bean [name="BMW 3"]
    ├─ @Bean person()            → creates Person bean "person" with:
    │      Vehicle = vehicle3 bean injected (via method call)
    │      Spouse  = person2 bean injected (via method call person2())
    ├─ @Bean person2()           → creates Person bean "person2" with:
    │      Vehicle = vehicle2 bean injected
    ├─ @Bean fullTimeEmployee() @Qualifier("fullTimeEmployee")
    │                         → creates FullTimeEmployee bean (implements Employee)
    ├─ @Bean partTimeEmployee() @Qualifier("partTimeEmployee")
    │                         → creates PartTimeEmployee bean (implements Employee)
    ├─ @Bean employeeService(fullTimeEmployee) → creates EmployeeService bean,
    │      injecting Employee = fullTimeEmployee bean (via @Qualifier)
    └─ (Beans from scanning):
       ├─ Person bean (from @Component, if not already defined by @Bean) – has:
       │      @Autowired Vehicle → injects primary Vehicle (vehicle1):contentReference[oaicite:39]{index=39}
       │      @Autowired @Lazy Person spouse → injects spouse proxy (circular):contentReference[oaicite:40]{index=40}
       ├─ Vehicle bean (from @Component) – not used because explicit beans exist
       ├─ FullTimeEmployee bean (@Component) – (duplicate of fullTimeEmployee bean, potentially)
       ├─ PartTimeEmployee bean (@Component) – (duplicate of partTimeEmployee bean)
       └─ EmployeeService bean (@Component) – has constructor:
              EmployeeService(@Qualifier("fullTimeEmployee") Employee emp)
              → Spring injects the FullTimeEmployee bean here:contentReference[oaicite:41]{index=41}


ApplicationContext
   |
   |-- reads @Configuration
   |-- scans @Component
   |-- builds BeanDefinitions
   |
   |-- creates Vehicle beans
   |-- creates Person beans
   |-- resolves circular dependency (@Lazy)
   |-- creates Employee beans
   |-- injects qualified Employee into Service
   |
   v
Fully wired application graph
