# language: es
@snippet-demo
Característica: Generación automática de plantillas Java desde Gherkin
  Como expositor de BDD
  Quiero mostrar cómo Cucumber sugiere step definitions faltantes
  Para explicar la transición desde Gherkin hacia código Java

  Escenario: Mostrar snippets Java de pasos no implementados
    Dado que existe una condición demostrativa sin implementación
    Cuando ejecuto la demo de generación de snippets
    Entonces Cucumber debería sugerir la plantilla Java correspondiente

