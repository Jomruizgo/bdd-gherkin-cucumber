# Matriz de Casos de Prueba - IA vs Refinamiento Humano

Este documento contiene los escenarios de prueba generados por la IA (**SKAI**) en lenguaje Gherkin, junto con la validación y ajustes técnicos realizados por el equipo de QA basándose en la arquitectura del proyecto (**Ticketing Platform MVP**). Hubo casos en los cuáles el refinamiento de las HU se baso en dividir sus responsabilidades, para ello se tomo solo la primera HU dividida para generarle sus test_cases respectivos.

---

## 📌 Índice de Navegación

1.  [**HU-01: Reserva Temporal**](#hu-01)
2.  [**HU-04: Exploración y Selección**](#hu-04)
3.  [**HU-05: Crear Evento (Admin)**](#hu-05)

---

## <a name="hu-01"></a>1. HU-01: Selección y Reserva Temporal de Asiento

### Escenarios Gherkin y Matriz de Ajustes (HU-01)

| ID | Escenario (Gherkin / IA) | Ajuste del Probador (QA) | ¿Por qué se ajustó? |
| :--- | :--- | :--- | :--- |
| **TC-01** | Bloqueo de asiento exitoso y visualización de temporizador. | Bloqueo con validación en `bc_inventory` y evento Kafka `reservation-created`. | La IA ignoró la arquitectura orientada a eventos necesaria para el servicio de órdenes. |
| **TC-02** | Reserva expira después del TTL. | Limpieza de lock en **Redis** y emisión de evento `reservation-expired`. | El sistema usa Redis para bloqueo distribuido; la IA solo veía el cambio visual. |
| **TC-03** | Concurrencia de reserva simultánea. | Validación de **Optimistic Concurrency** (campo `version`) y HTTP 409 Conflict. | Se debe asegurar que la DB rechace técnicamente la segunda petición concurrente. |
| **TC-04** | El cliente B ve el asiento como reservado. | Sincronización de estado mediante eventos de Kafka y actualización de UI. | Crítico para la consistencia eventual entre microservicios de catálogo e inventario. |
| **TC-05** | Pago completado antes de los 15 min. | Detención del worker de expiración en Redis vía evento `payment-succeeded`. | Evita condiciones de carrera donde el asiento se libera justo después de ser pagado. |
| **TC-06** | Cliente B intenta reservar asiento C de A. | Validación del estado desde el **Distributed Lock** activo en Redis. | El API debe ser la fuente de verdad, verificando el lock antes de consultar la DB. |
| **TC-07** | Transiciones: Disponible -> Reservado -> Vendido. | Aserciones exactas en BD para tipos `Available`, `Reserved`, `Sold`. | Los estados deben ser estrictos para mantener la integridad referencial del negocio. |
| **TC-08** | Pago segundos después de la expiración. | Re-validación de reserva en `Payment Service` contra el TTL de Redis. | Previene el cobro indebido de reservas que ya han sido liberadas por el sistema. |
| **TC-09** | Límite de X asientos por usuario. | Validación de regla de negocio en el entry-point del microservicio `Ordering`. | La restricción debe validarse antes de generar cualquier lock de infraestructura. |
| **TC-10** | Sincronización multi-dispositivo. | Uso de **Timestamp UTC** del servidor almacenado en Redis para el temporizador. | Evita que el usuario manipule el reloj local para ganar tiempo de reserva de forma fraudulenta. |
| **TC-11** | Temporizador llega a 00:00. | Disparo de callback de expiración en Redis y liberación atómica de stock. | Asegura que la liberación sea automática y no dependa del refresco del cliente. |
| **TC-12** | Cancelación manual de la reserva. | Emisión de evento `reservation-cancelled` para liberación inmediata en inventario. | La liberación proactiva mejora la disponibilidad de inventario para otros usuarios. |
| **TC-13** | Recuperación ante reinicio de App. | Validación de persistencia de sesión en **Auth Context** y estado en Redis. | La experiencia de usuario debe ser fluida ante micro-cortes o recargas de página. |
| **TC-14** | Intento de reservar asiento vendido. | Filtro de integridad en microservicio `Catalog` y validación en `Inventory`. | Previene peticiones innecesarias a la capa de reserva para stock ya liquidado. |
| **TC-15** | Asignación a los primeros en llegar. | Validación del throughput de Kafka y ordenamiento por timestamp de llegada. | En alta demanda, el orden de los mensajes en el log de Kafka define la prioridad. |
| **TC-16** | Integridad ante caída del sistema. | Configuración de **Replication Factor** y asincronía en Kafka (Acks=all). | Garantiza que ningún cambio de estado se pierda si un nodo del clúster falla. |
| **TC-17** | Cierre de sesión y re-inicio. | Recuperación del `order_id` desde el JWT persistido y sesión activa en Redis. | La persistencia de la intención de compra es clave para la conversión de ventas. |
| **TC-18** | Reintentos rápidos (Ráfaga). | Implementación de **Idempotency Key** en el Header de la petición HTTP. | Evita que fallos de red generen múltiples reservas para el mismo usuario y asiento. |
| **TC-19** | Zona horaria distinta. | Estandarización a formato **ISO 8601 UTC** en toda la comunicación API. | Previene errores de cálculo en el TTL cuando el cliente y el servidor difieren de zona. |
| **TC-20** | Identificador manipulado. | Validación de esquema JSON y respuesta estructurada bajo **RFC 7807**. | Seguridad básica contra manipulación manual de IDs en las peticiones de reserva. |

---

## <a name="hu-04"></a>2. HU-04: Exploración y Selección de Asientos

### Escenarios Gherkin y Matriz de Ajustes (HU-04)

| ID | Escenario (Gherkin / IA) | Ajuste del Probador (QA) | ¿Por qué se ajustó? |
| :--- | :--- | :--- | :--- |
| **TC-01** | Filtros por fecha, tipo y ubicación. | Validación de caché de consulta (Redis) en el `Catalog Service`. | Optimiza el rendimiento del catálogo ante ráfagas de búsqueda de usuarios. |
| **TC-02** | Mapa visual de asientos y estados. | Implementación de **Lazy Loading** desde el API de `Inventory`. | Cargar miles de asientos para un estadio de golpe colapsa el navegador; requiere carga diferida. |
| **TC-03** | Selección de asiento disponible. | Validación técnica de transición de estado en la respuesta del API Gateway. | Asegura que el cliente reciba confirmación inmediata de que el proceso inició. |
| **TC-04** | Selección de asiento reservado. | Feedback visual basado en el evento `seat-status-updated` vía WebSockets. | Permite que el usuario vea cambios en tiempo real sin recargar el mapa. |
| **TC-05** | Selección de asiento vendido. | Bloqueo de interacción en el componente `SeatButton` basado en metadatos. | Mejora la UX previniendo clics en elementos que ya no son accionables. |
| **TC-06** | Dos visitantes, misma reserva. | Implementación de **Distributed Lock** en Redis con resolución de conflictos. | Evita el "Double Booking" a nivel de infraestructura, no solo de lógica de App. |
| **TC-07** | Bloqueo por 15 minutos. | Creación de llave con TTL en Redis vinculada al `SessionId` del usuario. | La lógica de tiempo debe ser centralizada en el backend para ser inviolable. |
| **TC-08** | Liberación tras 15 minutos. | Validación del worker asíncrono que procesa la expiración y actualiza el mapa. | El proceso de "limpieza" debe ser transparente y reactivo para el resto de usuarios. |
| **TC-09** | Alta concurrencia en evento popular. | Pruebas de carga validando el particionamiento de tópicos en Kafka. | Garantiza que el sistema escale horizontalmente sin perder consistencia de datos. |
| **TC-10** | Regreso al mapa, asiento reservado. | Validación de persistencia de estado persistido en la DB de `Inventory`. | La "memoria" del estado debe ser persistente ante navegaciones del usuario. |
| **TC-11** | Selección de varios asientos. | Validación de **Atomic Transaction** para reservas múltiples (Todo o Nada). | Evita que un usuario reserve 2 asientos pero el sistema solo bloquee 1 por error parcial. |
| **TC-12** | Asociación a usuario autenticado. | Extracción y validación del `UserId` desde el **JWT Claims**. | Seguridad: Garantiza que la reserva pertenezca legítimamente a quien la solicita. |
| **TC-13** | Invitado (No autenticado). | Generación de **Anonymous Token** temporal vinculado a la sesión del navegador. | Permite flujo de reserva sin fricción inicial, asegurando trazabilidad técnica. |
| **TC-14** | Más de 10 asientos (Límite). | Validación de regla de negocio en el backend (Ordering API). | No basta el límite en UI; el API debe rechazar payloads que excedan el máximo. |
| **TC-15** | Combinación de estados en selección. | Filtrado de payload en el backend para procesar solo los IDs marcados como `Available`. | Previene ataques donde se envían IDs de asientos ya vendidos junto a uno disponible. |
| **TC-16** | Filtros restrictivos (Sin resultados). | Validación de respuesta HTTP 200 con array vacío y metadatos de búsqueda. | El sistema debe responder correctamente indicando "sin resultados" sin lanzar errores. |
| **TC-17** | Búsqueda > 100 caracteres. | Implementación de **FluentValidation** en el DTO de búsqueda. | Evita ataques de denegación de servicio por procesamiento de strings masivos. |
| **TC-18** | Formato de fecha incorrecto en filtros. | Validación de tipos de datos en la capa de **Middleware** del API. | Garantiza que el microservicio no intente procesar fechas inválidas en la DB. |
| **TC-19** | Error técnico de integridad. | Respuesta bajo estándar **RFC 7807 (Problem Details)**. | Provee detalles técnicos estructurados para facilitar el debugging en producción. |
| **TC-20** | Acceso anónimo al catálogo. | Configuración de políticas de **CORS** y permisos `AllowAnonymous` en Gateway. | Permite que el catálogo sea indexable y accesible sin barreras de autenticación. |

---

## <a name="hu-05"></a>3. HU-05: Gestión de Catálogo por Organizador

### Escenarios Gherkin y Matriz de Ajustes (HU-05)

| ID | Escenario (Gherkin / IA) | Ajuste del Probador (QA) | ¿Por qué se ajustó? |
| :--- | :--- | :--- | :--- |
| **TC-01** | Creación exitosa con ID único. | Generación de **UUID v4** y persistencia en esquema `bc_catalog`. | El ID debe ser un identificador universal para evitar colisiones en microservicios. |
| **TC-02** | Campos obligatorios vacíos. | Validación de esquema mediante **Data Annotations** en el modelo C#. | La validación en el servidor es la última línea de defensa para la integridad de datos. |
| **TC-03** | Nombre excede 256 caracteres. | Restricción de columna `VARCHAR(256)` en PostgreSQL. | Alinea la capacidad física de la DB con las reglas de negocio del software. |
| **TC-04** | Caracteres especiales no permitidos. | Implementación de **Sanitización de Inputs** para prevenir inyección de scripts. | Seguridad: Evita que el administrador inserte código malicioso en el nombre del evento. |
| **TC-05** | Fecha pasada o inválida. | Validación lógica comparando contra `DateTime.UtcNow` en el servicio. | Evita la creación de eventos "muertos" que ensucian el catálogo público. |
| **TC-06** | Recinto inexistente. | Validación de **Integridad Referencial** contra la tabla de `Venues`. | Garantiza que todo evento ocurra en un lugar físico válido registrado en el sistema. |
| **TC-07** | Conflicto de ID (Duplicidad). | Manejo de excepción de **Unique Constraint Violation** de PostgreSQL. | La DB debe ser el árbitro final para evitar duplicidad técnica de registros. |
| **TC-08** | Valor mínimo en cada campo. | Validación de límites inferiores (Boundary Value Analysis) en BD. | Asegura que el sistema acepte los valores más pequeños permitidos por negocio. |
| **TC-09** | Cancelar operación (Descartar). | Validación de no-persistencia (No se genera llamada al comando POST). | Confirmación de que el flujo de UI no dispara efectos secundarios en el backend. |
| **TC-10** | Cliente intenta crear evento (Permisos). | Validación de Policy `AdminOnly` mediante **JWT Role Claims**. | No basta con ocultar el botón en el UI; el API debe rechazar el token del cliente. |
| **TC-11** | Falla técnica (Pérdida conexión). | Implementación de **Atomic Transaction Scope** en el Catalog Service. | Evita que queden registros parciales o "huérfanos" ante fallos de red o base de datos. |
| **TC-12** | Creación simultánea por 2 Admins. | Uso de secuencias thread-safe o UUIDs generados en la capa de aplicación. | Garantiza la unicidad de eventos incluso bajo escalamiento horizontal del API Admin. |
| **TC-13** | Correo de organizador inválido. | Validación mediante **Expresiones Regulares (Regex)** estándar. | Asegura que las notificaciones técnicas tengan un destinatario válido para el negocio. |
| **TC-14** | Verificación en base de datos. | Aserción de mapeo entre objeto de negocio y esquema Físico (SQL). | El probador confirma que no hay pérdida de precisión en tipos de datos persistidos. |

---

## 4. Notas Técnicas de QA
- **Técnicas ISTQB aplicadas:** Partición de Equivalencia, Valores Límite, Transición de Estados y Pruebas de Error.
- **Arquitectura:** Se validó contra .NET 8, Kafka, Redis y PostgreSQL.
- **Recomendación:** Implementar tests automatizados usando **Serenity BDD + RestAssured** para las capas de API.
