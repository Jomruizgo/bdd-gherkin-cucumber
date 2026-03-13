# Informe de Refinamiento de Historias de Usuario

Este informe presenta el análisis comparativo entre las Historias de Usuario (HU) originales y su versión refinada mediante la herramienta **SKAI**, aplicando los principios **INVEST** y el contexto del proyecto **Ticketing Platform MVP**.

---

## 📌 Índice de Navegación

1.  [**Comparativa de Refinamiento (Antes vs Después)**](#comparativa-refinamiento)
    *   [Flujo de Compra E2E (HU-01 a HU-03)](#flujo-compra)
    *   [Exploración y Descubrimiento (HU-04)](#exploracion)
    *   [Gestión de Catálogo (HU-05 a HU-08)](#gestion-catalogo)

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
