# Informe de Refinamiento de Historias de Usuario

Este informe presenta el análisis comparativo entre las Historias de Usuario (HU) originales y su versión refinada mediante la herramienta **SKAI**, aplicando los principios **INVEST** y el contexto del proyecto **Ticketing Platform MVP**.

---

## 📌 Índice de Navegación

1.  **Comparativa de Refinamiento (Antes vs Después)**
    *   Flujo de Compra E2E (HU-01 a HU-03)
    *   Exploración y Descubrimiento (HU-04)
    *   Gestión de Catálogo (HU-05 a HU-08)
2.  **Complemento documental de historias refinadas y casos de prueba**

---

## 1. Comparativa de Refinamiento (Antes vs Después)

### 1.1. Flujo de Compra E2E (Épica)

| **HU Original** | **HU Refinada por SKAI (Instrucción)** | **Diferencias Detectadas e Impacto** |
| :--- | :--- | :--- |
| "Como cliente, quiero seleccionar un asiento específico, reservarlo temporalmente, agregarlo a mi carrito y completar el pago para recibir un boleto válido con código QR y confirmación por correo electrónico" | **Se desglosó en 3 Historias Atómicas:**<br><br>**Historia de Usuario 1: Selección y Reserva Temporal de Asiento**<br>Como Cliente, quiero seleccionar un asiento específico disponible en el mapa de un evento y reservarlo temporalmente, para asegurar que el asiento esté bloqueado a mi nombre mientras decido completar la compra.<br><br>**Historia de Usuario 2: Agregar Asiento Reservado al Carrito y Proceso de Pago**<br>Como Cliente, quiero agregar mi asiento reservado al carrito y realizar el pago correspondiente, para completar la compra y asegurar el acceso al evento.<br><br>**Historia de Usuario 3: Generación de Boleto Digital y Confirmación**<br>Como Cliente, quiero recibir un boleto digital con código QR y confirmación de mi compra, para poder acceder al evento de forma segura y tener constancia de mi transacción. | 1. **Atomicidad (INVEST):** Se transformó de Épica a historias estimables.<br>2. **Reglas de Negocio:** Se añadió el **TTL de 15 min**, validación de expiración y manejo de concurrencia.<br>3. **Arquitectura:** Se especificó que el envío de email es **diferido** y la orden queda `pendiente` en caso de error. |

---

### 1.2. Exploración y Descubrimiento

| **HU Original** | **HU Refinada por SKAI (Instrucción)** | **Diferencias Detectadas e Impacto** |
| :--- | :--- | :--- |
| "Como visitante, quiero explorar eventos, lugares y mapas de asientos para poder encontrar eventos y elegir asientos para reservar." | **HU-04: Exploración y Selección de Asientos para Reserva**<br><br>**Como** Visitante (autenticado o no), **quiero** explorar el catálogo de eventos con filtros y mapas interactivos, **para** encontrar eventos de interés y seleccionar asientos específicos para reservarlos. | 1. **Claridad:** Se definió el alcance de "explorar" con **filtros** (fecha, categoría, precio).<br>2. **Seguridad:** Se aclaró que el login es diferido hasta el checkout. |

---

### 1.3. Gestión de Catálogo por Organizador

| **HU Original** | **HU Refinada por SKAI (Instrucción)** | **Diferencias Detectadas e Impacto** |
| :--- | :--- | :--- |
| "Como organizador, quiero crear eventos y configurar los asientos del lugar para que se puedan vender entradas para mis eventos." | **Título: Creación de eventos y configuración de asientos**<br><br>Como organizador, quiero crear eventos y configurar los asientos del lugar para que se puedan vender entradas para mis eventos. Esto implica definir el evento con datos básicos (nombre, fecha, recinto, tipo), asignar mapas visuales de asientos con zonas, precios y disponibilidad, y garantizar que la configuración cumpla con reglas de negocio como la unicidad de asientos y la integridad de datos. | 1. **Testeabilidad:** Se definieron criterios claros sobre qué datos deben persistir y notificaciones de error.<br>2. **Integridad:** Se incluyó el bloqueo de edición para asientos que ya tengan reservas activas.<br>3. **Precisión:** Se detallaron los campos obligatorios (recinto, capacidad). |

---

## 2. Complemento documental de historias refinadas y casos de prueba

Esta sección reúne el desarrollo narrativo de las historias refinadas y algunos casos de prueba representativos del proyecto.

### 2.1. Desarrollo narrativo de historias refinadas

#### Historia de Usuario 1: Selección y Reserva Temporal de Asiento

**Título:** Selección y Reserva Temporal de Asiento Específico

**Como** Cliente  
**Quiero** seleccionar un asiento específico disponible en el mapa de un evento y reservarlo temporalmente  
**Para** asegurar que el asiento esté bloqueado a mi nombre mientras decido completar la compra.

**Enfoque funcional identificado en el insumo:**
- Visualización del mapa de asientos y sus estados.
- Reserva temporal del asiento seleccionado.
- Inicio de temporizador de retención para proteger la disponibilidad.

#### Historia de Usuario 2: Exploración y selección de asientos para reserva en eventos

**Título:** Exploración y selección de asientos para reserva en eventos

**Descripción:**  
Como visitante (usuario no autenticado o registrado), quiero explorar el catálogo de eventos, visualizar detalles de los recintos y acceder a mapas interactivos de asientos para poder encontrar eventos de interés y seleccionar asientos específicos para reservarlos de manera temporal.

**Criterios de aceptación consolidados a partir del insumo recibido:**
1. El usuario puede visualizar un catálogo de eventos con opciones de filtrado por fecha, categoría, ubicación y rango de precios.
2. Al seleccionar un evento, el usuario visualiza información relevante del evento y los detalles del recinto.
3. El usuario puede acceder a un mapa interactivo de asientos para identificar disponibilidad y apoyar la selección previa a la reserva.

#### Historia de Usuario 3: Creación de eventos y configuración de asientos

**Título:** Creación de eventos y configuración de asientos

**Descripción:**  
Como organizador, quiero crear eventos y configurar los asientos del lugar para que se puedan vender entradas para mis eventos. Esto implica definir el evento con datos básicos (nombre, fecha, recinto, tipo), asignar mapas visuales de asientos con zonas, precios y disponibilidad, y garantizar que la configuración cumpla con reglas de negocio como la unicidad de asientos y la integridad de datos.

**Criterios funcionales identificados en el insumo:**
1. El organizador debe poder registrar los datos básicos del evento.
2. El organizador debe poder definir zonas, asientos, precios y disponibilidad inicial.
3. La configuración debe respetar reglas de integridad y unicidad para evitar inconsistencias en el catálogo.

### 2.2. Casos de prueba representativos derivados del refinamiento

Los mensajes compartidos incluían listados amplios de casos de prueba. A continuación se documentan los casos representativos que sí quedaron claramente identificables en el insumo recibido, redactados en formato homogéneo para este informe.

#### HU-01 / Reserva temporal

**Caso de prueba 1: Bloqueo de asiento exitoso y visualización de temporizador**

```gherkin
Dado que un cliente accede al catálogo de eventos y selecciona un asiento disponible
Cuando solicita reservar el asiento
Entonces el asiento aparece en estado reservado para ese cliente
Y se inicia un temporizador de 15 minutos visible para el cliente
```

#### HU-04 / Exploración y descubrimiento

**Caso de prueba 1: Visualización de eventos con filtros básicos**

```gherkin
Dado que el visitante accede al catálogo de eventos
Cuando aplica filtros por fecha, tipo de evento y ubicación
Entonces se muestran únicamente los eventos que cumplen los criterios seleccionados
```

#### HU-05 / Gestión de catálogo por organizador

**Caso de prueba 1: Creación exitosa de evento con datos válidos**

```gherkin
Dado que un administrador inicia sesión en la plataforma
Y accede a la opción para crear un nuevo evento
Cuando registra la información requerida con datos válidos
Entonces el sistema crea el evento correctamente
Y le asigna un identificador único
```


