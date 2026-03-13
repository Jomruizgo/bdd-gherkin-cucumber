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
| "As a Customer I want to select a specific seat, reserve it temporarily, add it to my cart, and complete payment so that I receive a valid ticket with QR and email confirmation" | **Se desglosó en 3 Historias Atómicas:**<br><br>**HU-01: Reserva Temporal:** Enfoque en el bloqueo (TTL 15 min), visualización de estados y temporizador.<br>**HU-02: Carrito y Pago:** Manejo de transacciones, validación de expiración y transición a `vendido`.<br>**HU-03: Emisión y Confirmación:** Generación de PDF con QR y registro de email para envío diferido. | 1. **Atomicidad (INVEST):** Se transformó de Épica a historias estimables.<br>2. **Reglas de Negocio:** Se añadió el **TTL de 15 min** y la **validación de expiración** durante el pago.<br>3. **Arquitectura:** Se especificó que el envío de email es **diferido** (asíncrono). |

---

### <a name="exploracion"></a>1.2. Exploración y Descubrimiento

| **HU Original** | **HU Refinada por SKAI (Instrucción)** | **Diferencias Detectadas e Impacto** |
| :--- | :--- | :--- |
| "Como visitante, quiero explorar eventos, lugares y mapas de asientos para poder encontrar eventos y elegir asientos para reservar." | **HU-04: Exploración y Selección de Asientos para Reserva**<br><br>**Como** Visitante (autenticado o no), **quiero** explorar el catálogo de eventos con filtros y mapas interactivos, **para** encontrar eventos de interés y seleccionar asientos específicos para reservarlos. | 1. **Claridad:** Se definió el alcance de "explorar" con **filtros** (fecha, categoría, precio).<br>2. **Restricciones:** Se añadió un límite comercial de **6 asientos** por usuario.<br>3. **Seguridad:** Se aclaró que el login es diferido hasta el checkout. |

---

### <a name="gestion-catalogo"></a>1.3. Gestión de Catálogo por Organizador

| **HU Original** | **HU Refinada por SKAI (Instrucción)** | **Diferencias Detectadas e Impacto** |
| :--- | :--- | :--- |
| "Como organizador, quiero crear eventos y configurar los asientos del lugar para que se puedan vender entradas para mis eventos." | **Se desglosó en 4 Historias de Gestión (HU-05 a HU-08):**<br><br>**HU-05:** Crear Evento (Datos básicos e ID único).<br>**HU-06:** Configurar Asientos (Zonas, precios y mapas visuales).<br>**HU-07:** Editar Configuración (Con reglas de bloqueo si hay ventas).<br>**HU-08:** Guardar y Validar (Integridad de datos y confirmación). | 1. **Testeabilidad:** Se definieron criterios claros sobre qué datos deben persistir y notificaciones de error.<br>2. **Integridad:** Se incluyó el bloqueo de edición para asientos que ya tengan reservas activas.<br>3. **Precisión:** Se detallaron los campos obligatorios (recinto, capacidad). |

---

## <a name="detalle-historias"></a>2. Detalle Completo de Historias Refinadas

### <a name="hu-01"></a>HU-01: Selección y Reserva Temporal de Asiento Específico
**Como** Cliente  
**Quiero** seleccionar un asiento específico disponible en el mapa de un evento y reservarlo temporalmente  
**Para** asegurar que el asiento esté bloqueado a mi nombre mientras decido completar la compra.

*   **Criterios de Aceptación:**
    1. Visualización de estados en tiempo real (disponible, reservado, vendido).
    2. Bloqueo exclusivo con TTL de 15 minutos.
    3. Liberación automática tras expiración.
    4. Manejo de concurrencia: primer intento exitoso bloquea, el resto recibe error.
    5. Temporizador visible con el tiempo restante.

### <a name="hu-02"></a>HU-02: Agregar Asiento Reservado al Carrito y Realizar Pago
**Como** Cliente  
**Quiero** agregar mi asiento reservado al carrito y realizar el pago correspondiente  
**Para** completar la compra y asegurar el acceso al evento.

*   **Criterios de Aceptación:**
    1. Validación de expiración antes de añadir al carrito.
    2. Integración con simulador de pagos.
    3. Transición a estado `vendido` solo si el pago ocurre dentro del TTL.
    4. Manejo de errores en pagos tardíos (reserva ya expirada).

### <a name="hu-03"></a>HU-03: Emisión de Boleto Digital con QR y Confirmación de Compra
**Como** Cliente  
**Quiero** recibir un boleto digital con código QR y confirmación de mi compra  
**Para** poder acceder al evento de forma segura y tener constancia de mi transacción.

*   **Criterios de Aceptación:**
    1. Generación de PDF con QR único tras confirmación de pago.
    2. Asociación unívoca entre cliente, asiento y boleto.
    3. Registro de email para envío diferido (asíncrono).
    4. Contingencia: estado `pendiente` si falla la generación del PDF.

### <a name="hu-04"></a>HU-04: Exploración y Selección de Asientos para Reserva
**Como** Visitante (autenticado o no)  
**Quiero** explorar el catálogo de eventos, detalles de recintos y mapas interactivos  
**Para** encontrar eventos de interés y seleccionar asientos específicos para reservarlos.

*   **Criterios de Aceptación:**
    1. Filtros de búsqueda (fecha, categoría, ubicación, precio).
    2. Mapa interactivo con disponibilidad en tiempo real.
    3. Límite máximo de **6 asientos** por usuario por evento.
    4. Bloqueo inmediato tras selección con cuenta regresiva visible.
    5. Solicitud de login/registro solo al momento de intentar proceder a la compra.

### <a name="hu-05-08"></a>HU-05 a HU-08: Gestión de Catálogo por Organizador
**Como** Organizador  
**Quiero** crear eventos y configurar los asientos del lugar  
**Para** que se puedan vender entradas para mis eventos.

*   **Criterios de Aceptación (Generales):**
    1. Registro de: nombre del evento, fecha, recinto y capacidad máxima.
    2. Definición de zonas con mapas visuales y asignación de precios.
    3. Bloqueo de edición de asientos si ya poseen reservas o ventas activas.
    4. Notificaciones de éxito/error al guardar la configuración e integridad de datos.
