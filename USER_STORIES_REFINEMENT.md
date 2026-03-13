# Informe de Refinamiento de Historias de Usuario

Este informe presenta el análisis comparativo entre las Historias de Usuario (HU) originales y su versión refinada mediante la herramienta **SKAI**, aplicando los principios **INVEST** y el contexto del proyecto **Ticketing Platform MVP**.

---

## 📌 Índice de Navegación

1.  [**Comparativa de Refinamiento (Antes vs Después)**](#comparativa-refinamiento)
    *   [Flujo de Compra E2E (HU-01 a HU-03)](#flujo-compra)
    *   [Exploración y Descubrimiento (HU-04)](#exploracion)
    *   [Gestión de Catálogo (HU-05 a HU-08)](#gestion-catalogo)
2.  [**Detalle Completo de Historias Refinadas**](#detalle-historias)
    *   [HU-01: Selección y Reserva](#hu-01)
    *   [HU-02: Carrito y Pago](#hu-02)
    *   [HU-03: Emisión y Confirmación](#hu-03)
    *   [HU-04: Exploración y Selección](#hu-04)
    *   [HU-05 a HU-08: Gestión de Catálogo](#hu-05-08)

---

## <a name="comparativa-refinamiento"></a>1. Comparativa de Refinamiento (Antes vs Después)

### <a name="flujo-compra"></a>1.1. Flujo de Compra E2E (Épica)

| **HU Original** | **HU Refinada por SKAI (Instrucción)** | **Diferencias Detectadas e Impacto** |
| :--- | :--- | :--- |
| "As a Customer I want to select a specific seat, reserve it temporarily, add it to my cart, and complete payment so that I receive a valid ticket with QR and email confirmation" | **Se desglosó en 3 Historias Atómicas:**<br><br>**Historia de Usuario 1: Selección y Reserva Temporal de Asiento**<br>Como Cliente, quiero seleccionar un asiento específico disponible en el mapa de un evento y reservarlo temporalmente, para asegurar que el asiento esté bloqueado a mi nombre mientras decido completar la compra.<br><br>**Historia de Usuario 2: Agregar Asiento Reservado al Carrito y Proceso de Pago**<br>Como Cliente, quiero agregar mi asiento reservado al carrito y realizar el pago correspondiente, para completar la compra y asegurar el acceso al evento.<br><br>**Historia de Usuario 3: Generación de Boleto Digital y Confirmación**<br>Como Cliente, quiero recibir un boleto digital con código QR y confirmación de mi compra, para poder acceder al evento de forma segura y tener constancia de mi transacción. | 1. **Atomicidad (INVEST):** Se transformó de Épica a historias estimables.<br>2. **Reglas de Negocio:** Se añadió el **TTL de 15 min**, validación de expiración y manejo de concurrencia.<br>3. **Arquitectura:** Se especificó que el envío de email es **diferido** y la orden queda `pendiente` en caso de error. |

---

### <a name="exploracion"></a>1.2. Exploración y Descubrimiento

| **HU Original** | **HU Refinada por SKAI (Instrucción)** | **Diferencias Detectadas e Impacto** |
| :--- | :--- | :--- |
| "Como visitante, quiero explorar eventos, lugares y mapas de asientos para poder encontrar eventos y elegir asientos para reservar." | **HU-04: Exploración y Selección de Asientos para Reserva**<br><br>**Como** Visitante (autenticado o no), **quiero** explorar el catálogo de eventos con filtros y mapas interactivos, **para** encontrar eventos de interés y seleccionar asientos específicos para reservarlos. | 1. **Claridad:** Se definió el alcance de "explorar" con **filtros** (fecha, categoría, precio).<br>2. **Seguridad:** Se aclaró que el login es diferido hasta el checkout. |

---

### <a name="gestion-catalogo"></a>1.3. Gestión de Catálogo por Organizador

| **HU Original** | **HU Refinada por SKAI (Instrucción)** | **Diferencias Detectadas e Impacto** |
| :--- | :--- | :--- |
| "Como organizador, quiero crear eventos y configurar los asientos del lugar para que se puedan vender entradas para mis eventos." | **Título: Creación de eventos y configuración de asientos**<br><br>Como organizador, quiero crear eventos y configurar los asientos del lugar para que se puedan vender entradas para mis eventos. Esto implica definir el evento con datos básicos (nombre, fecha, recinto, tipo), asignar mapas visuales de asientos con zonas, precios y disponibilidad, y garantizar que la configuración cumpla con reglas de negocio como la unicidad de asientos y la integridad de datos. | 1. **Testeabilidad:** Se definieron criterios claros sobre qué datos deben persistir y notificaciones de error.<br>2. **Integridad:** Se incluyó el bloqueo de edición para asientos que ya tengan reservas activas.<br>3. **Precisión:** Se detallaron los campos obligatorios (recinto, capacidad). |

---

## <a name="detalle-historias"></a>2. Detalle Completo de Historias Refinadas

### <a name="hu-01"></a>Historia de Usuario 1: Selección y Reserva Temporal de Asiento
**Título:** Selección y Reserva Temporal de Asiento Específico

**Como** Cliente  
**Quiero** seleccionar un asiento específico disponible en el mapa de un evento y reservarlo temporalmente  
**Para** asegurar que el asiento esté bloqueado a mi nombre mientras decido completar la compra.

*   **Criterios de Aceptación:**
    1. El cliente puede visualizar el mapa de asientos de un evento con estados actualizados (disponible, reservado, vendido).
    2. Al seleccionar un asiento disponible, el sistema lo reserva exclusivamente para el cliente durante 15 minutos (TTL).
    3. Si el cliente no completa el proceso de compra en ese tiempo, el asiento se libera automáticamente y vuelve a estar disponible.
    4. Si dos clientes intentan seleccionar el mismo asiento simultáneamente, solo el primero en confirmar la selección lo reserva; el segundo recibe un mensaje de no disponibilidad.
    5. El sistema muestra un temporizador con el tiempo restante de la reserva.

### <a name="hu-02"></a>Historia de Usuario 2: Agregar Asiento Reservado al Carrito y Proceso de Pago
**Título:** Agregar Asiento Reservado al Carrito y Realizar Pago

**Como** Cliente  
**Quiero** agregar mi asiento reservado al carrito y realizar el pago correspondiente  
**Para** completar la compra y asegurar el acceso al evento.

*   **Criterios de Aceptación:**
    1. El cliente puede agregar uno o varios asientos reservados a su carrito.
    2. El sistema no permite agregar asientos cuyo tiempo de reserva ha expirado.
    3. El cliente puede ingresar los datos necesarios para el pago y confirmar la transacción.
    4. Si el pago se realiza dentro del TTL, el asiento pasa a estado vendido.
    5. Si el pago se procesa después de la expiración de la reserva, el sistema muestra un mensaje de error y no finaliza la compra.
    6. En caso de fallo en el pago, el asiento permanece reservado hasta que expire el TTL.

### <a name="hu-03"></a>Historia de Usuario 3: Generación de Boleto Digital y Confirmación
**Título:** Emisión de Boleto Digital con QR y Confirmación de Compra

**Como** Cliente  
**Quiero** recibir un boleto digital con código QR y confirmación de mi compra  
**Para** poder acceder al evento de forma segura y tener constancia de mi transacción.

*   **Criterios de Aceptación:**
    1. Una vez confirmado el pago, el sistema genera un boleto digital en PDF con un código QR único.
    2. El boleto se asocia únicamente al cliente y al asiento comprado.
    3. El sistema almacena el correo electrónico del cliente para el envío posterior de la confirmación (el envío se realiza en un proceso diferido, no en tiempo real).
    4. Si ocurre un error en la generación del boleto digital, la orden queda en estado pendiente y se notifica a soporte técnico.
    5. El cliente puede visualizar un mensaje de confirmación en pantalla y descargar el boleto desde su perfil.

### <a name="hu-04"></a>HU-04: Exploración y Selección de Asientos para Reserva
**Como** Visitante (autenticado o no)  
**Quiero** explorar el catálogo de eventos, detalles de recintos y mapas interactivos  
**Para** encontrar eventos de interés y seleccionar asientos específicos para reservarlos.

*   **Criterios de Aceptación:**
    1. Filtros de búsqueda (fecha, categoría, ubicación, precio).
    2. Mapa interactivo con disponibilidad en tiempo real.
    3. Bloqueo inmediato tras selección con cuenta regresiva visible.

### <a name="hu-05-08"></a>HU-05 a HU-08: Gestión de Catálogo por Organizador

#### Historia de Usuario 5: Creación de Evento
**Título:** Crear Evento
**Como** Organizador  
**Quiero** crear un nuevo evento con sus datos básicos  
**Para** que esté disponible en el catálogo y se puedan configurar sus entradas.

*   **Criterios de Aceptación:**
    1. El organizador puede ingresar el nombre, fecha, tipo, recinto y capacidad máxima del evento.
    2. El sistema genera un ID único (UUID v4) para cada evento.
    3. Validación de campos obligatorios y formato de fecha (no anterior a la actual).
    4. Notificación de éxito o error tras el intento de creación.

#### Historia de Usuario 6: Configuración Inicial de Asientos
**Título:** Configurar Asientos
**Como** Organizador  
**Quiero** configurar los asientos del evento con mapas visuales, zonas y precios  
**Para** definir la oferta comercial del evento.

*   **Criterios de Aceptación:**
    1. Definición de zonas con mapas visuales y asignación de precios por zona.
    2. Los asientos pasan a estado `Disponible` inmediatamente tras la configuración.
    3. Validación de unicidad de asientos (no duplicados en el mismo recinto/evento).

#### Historia de Usuario 7: Edición y Gestión de Asientos
**Título:** Editar Configuración de Asientos
**Como** Organizador  
**Quiero** modificar la configuración de asientos después de la creación  
**Para** ajustar precios o zonas según la demanda del mercado.

*   **Criterios de Aceptación:**
    1. El organizador puede editar zonas, precios y disponibilidad.
    2. **Restricción:** No se pueden eliminar o modificar asientos que ya tengan reservas activas o ventas confirmadas.
    3. Registro de auditoría de los cambios realizados.

#### Historia de Usuario 8: Visualización y Persistencia de Catálogo
**Título:** Visualizar y Guardar Configuración
**Como** Organizador  
**Quiero** previsualizar el mapa de asientos y persistir los cambios de forma segura  
**Para** garantizar la integridad de la información presentada al público.

*   **Criterios de Aceptación:**
    1. Visualización del mapa real con zonas, precios y estados (Disponible/Reservado/Vendido).
    2. Verificación de integridad de datos antes de guardar (consistencia entre capacidad y asientos mapeados).
    3. Notificación de confirmación tras guardado exitoso o detalle de errores de validación.

---

## 4. Notas Técnicas de QA
