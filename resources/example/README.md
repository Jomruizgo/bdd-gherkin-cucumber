# Ejemplo práctico de BDD con Gherkin y Cucumber

Este proyecto muestra **dos ejemplos simples y prácticos** para explicar **BDD** usando **Gherkin** como lenguaje de especificación y **Cucumber** como mecanismo de ejecución.

El foco no está solo en automatizar, sino en mostrar cómo una **Historia de Usuario (HU)** se convierte en:

1. comportamiento esperado,
2. escenario legible para negocio en Gherkin,
3. step definitions que conectan ese lenguaje con el sistema,
4. y una validación ejecutable con Cucumber sobre Java/Gradle.

## 1. Objetivo pedagógico

No busca cubrir todo TicketRush. Busca mostrar el camino completo de **BDD aplicado**:

**HU → comportamiento → Gherkin → Cucumber → validación ejecutable**.

La automatización aparece aquí como consecuencia del enfoque BDD, no como el fin principal del repositorio.

Se incluyen dos ejemplos:

- **UI**: el comprador entra a la página principal de compra, selecciona un evento disponible, escribe su correo y hace clic en comprar.
- **Backend**: un cliente backend envía una solicitud válida de reserva y recibe `202 Accepted`.

## 2. Historias de Usuario base

### HU-UI-01 — Iniciar una compra desde la UI

**Como** comprador  
**quiero** entrar a la página principal de compra, seleccionar un evento disponible y registrar mi correo  
**para** iniciar la compra de un ticket.

### HU-BE-01 — Aceptar una reserva válida en backend

**Como** consumidor del API del producer  
**quiero** enviar una solicitud válida de reserva  
**para** que el sistema la acepte de forma asíncrona.

## 3. Cómo se expresan en BDD con Gherkin

### HU-UI-01 → comportamiento → Gherkin

Se descompone en comportamiento observable:

- existe al menos un evento disponible,
- el usuario entra a la página principal,
- selecciona un evento,
- escribe correo,
- pulsa comprar,
- el sistema muestra el siguiente paso del flujo.

Eso se expresa en [src/test/resources/features/ui/ui_purchase.feature](src/test/resources/features/ui/ui_purchase.feature), donde Cucumber puede ejecutar el escenario tal como fue descrito.

### HU-BE-01 → comportamiento → Gherkin

Se descompone en:

- existe un evento con ticket disponible,
- se envía un request válido a `POST /api/tickets/reserve`,
- la API responde `202 Accepted`,
- el cuerpo contiene el `ticketId`.

Eso se expresa en [src/test/resources/features/api/api_reservation.feature](src/test/resources/features/api/api_reservation.feature), manteniendo visible el comportamiento esperado antes de entrar al detalle técnico.

## 4. Estructura

- `src/test/resources/features/ui`: escenarios BDD de UI escritos en Gherkin
- `src/test/resources/features/api`: escenarios BDD de backend escritos en Gherkin
- `src/test/java/.../ui`: steps y hooks que conectan Gherkin con la UI
- `src/test/java/.../ui/port`: contrato que usa la capa BDD para automatizar navegador
- `src/test/java/.../ui/adapter`: implementación concreta con Selenium
- `src/test/java/.../api`: steps que conectan Gherkin con llamadas backend usando RestAssured
- `src/test/java/.../support`: apoyo para preparar datos de prueba en TicketRush
- `src/test/java/.../runners`: suites/runners de Cucumber por tipo de prueba

## 5. Versión del sistema bajo prueba

Este repositorio BDD está alineado explícitamente con la versión del sistema bajo prueba que fue publicada en `main` de TicketRush para esta presentación.

- **Repo objetivo:** `Jomruizgo/ticketing_project_week1`
- **Rama publicada:** `main`
- **Commit exacto:** `e4ee9dcf75a277d9589746217c349c9b652c7f0c`

La intención es que los escenarios descritos y ejecutados aquí se expliquen siempre en referencia a esa versión exacta del sistema.

## 6. Requisitos

Antes de correr estos ejemplos debe estar levantado TicketRush:

- Frontend: `http://localhost:3000`
- CRUD: `http://localhost:8002`
- Producer: `http://localhost:8001`

## 7. Comandos

Estos comandos ejecutan los escenarios BDD con Cucumber:

### Ejecutar solo backend

```bash
gradle apiTest
```

### Ejecutar solo UI

```bash
gradle uiTest
```

### Ejecutar todo

```bash
gradle test
```

### Demostrar generación automática de plantillas Java desde Gherkin

```bash
gradle snippetDemo
```

Esta tarea ejecuta una feature de demostración con pasos **intencionalmente no implementados**.
Así, Cucumber imprime en consola los **snippets Java sugeridos** para crear los step definitions a partir del escenario Gherkin.
La tarea está aislada y no afecta `uiTest`, `apiTest` ni `test`.

## 8. URLs configurables

Puedes sobrescribir por propiedades del sistema:

```bash
gradle uiTest -Dui.base.url=http://localhost:3000 -Dcrud.base.url=http://localhost:8002 -Dproducer.base.url=http://localhost:8001
```

## 9. Observación importante

El ejemplo UI está pensado para ser **simple y demostrativo**.  
No representa todavía una suite robusta de regresión.  
La intención es enseñar claramente cómo una HU sencilla se transforma en comportamiento especificado con Gherkin y ejecutado con Cucumber.

## 10. Demostración de snippets automáticos de Cucumber

En este proyecto hay dos formas distintas de mostrar la relación entre **BDD, Gherkin y Cucumber**:

1. **Escenarios implementados**: muestran el flujo completo desde la HU hasta la ejecución del comportamiento.
2. **Demo de snippets**: muestra cómo Cucumber sugiere automáticamente la plantilla Java cuando encuentra pasos sin implementación.

La demo está en:

- `src/test/resources/features/snippets/snippet_generation_demo.feature`
- `src/test/java/co/sofka/ticketrush/bdd/runners/SnippetGenerationDemo.java`

Importante:

- Esto **no reemplaza** la ayuda del plugin del IDE.
- El plugin del IDE sirve para navegación, autocompletado y, en algunos casos, asistencia para crear steps.
- La demo agregada aquí muestra el mecanismo propio de **Cucumber en ejecución**, que imprime los métodos Java sugeridos para los pasos faltantes.

En otras palabras: este repositorio busca enseñar **BDD con Gherkin y Cucumber**; la automatización existe para volver ese comportamiento verificable.

## 11. Patrón de abstracción usado en UI

Los steps de UI no invocan Selenium directamente.

Se dejó esta estructura:

- `BrowserAutomation`: contrato que usa la capa BDD.
- `SeleniumBrowserAutomationAdapter`: adapter que traduce ese contrato a Selenium + Chrome.

Así, la automatización queda desacoplada del framework concreto y Selenium permanece encapsulado como detalle de infraestructura.
