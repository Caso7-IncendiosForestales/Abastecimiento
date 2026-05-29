# MS-08 · Gestión de Puntos de Abastecimiento

> **CONAF — Coordinación de Emergencias por Incendios Forestales**  
> Microservicio encargado de gestionar la geolocalización y niveles 
> de agua de los puntos de reabastecimiento en terreno, para redirigir 
> camiones aljibe a puntos de carga óptimos.

---

## Contexto

El Coordinador de CONAF necesita gestionar la geolocalización y 
los niveles de agua de los puntos de reabastecimiento en terreno, 
para redirigir camiones aljibe a puntos de carga óptimos y con 
disponibilidad de recursos hídricos.

---

## Stack Tecnológico

| Lenguaje | Java 21 |
| Framework | Spring Boot 3.4.5 |
| Base de datos | PostgreSQL (NeonTech) |
| ORM | Spring Data JPA / Hibernate |
| Validaciones | Bean Validation (@Min) |
| Puerto | 8088 |

---

## Entidades

- **PuntoAbastecimiento** — geolocalización y estado del punto de agua
- **InventarioAgua** — volumen disponible y registro de carga de camiones aljibe

---

## Endpoint Base
http://localhost:8088/api/v1/abastecimiento

---

## Roadmap de Endpoints

### 1. Crear punto de abastecimiento
**POST** `/api/v1/abastecimiento/puntos`

```json
{
  "nombre": "Punto Agua Pencahue",
  "latitud": -35.3833,
  "longitud": -71.8167,
  "direccionReferencia": "Camino a Pencahue km 5",
  "region": "Maule",
  "estado": "DISPONIBLE"
}
```

Respuesta exitosa `201 Created`:
```json
{
  "id": 1,
  "nombre": "Punto Agua Pencahue",
  "latitud": -35.3833,
  "longitud": -71.8167,
  "direccionReferencia": "Camino a Pencahue km 5",
  "region": "Maule",
  "estado": "DISPONIBLE"
}
```

---

### 2. Obtener todos los puntos
**GET** `/api/v1/abastecimiento/puntos`

Respuesta exitosa `200 OK`:
```json
[
  {
    "id": 1,
    "nombre": "Punto Agua Pencahue",
    "region": "Maule",
    "estado": "DISPONIBLE",
    "latitud": -35.3833,
    "longitud": -71.8167
  }
]
```

---

### 3. Obtener punto por ID
**GET** `/api/v1/abastecimiento/puntos/{id}`

Ejemplo: `GET /api/v1/abastecimiento/puntos/1`

Respuesta exitosa `200 OK`:
```json
{
  "id": 1,
  "nombre": "Punto Agua Pencahue",
  "region": "Maule",
  "estado": "DISPONIBLE"
}
```

---

### 4. Obtener puntos disponibles
**GET** `/api/v1/abastecimiento/puntos/disponibles`

Respuesta exitosa `200 OK`:
```json
[
  {
    "id": 1,
    "nombre": "Punto Agua Pencahue",
    "estado": "DISPONIBLE",
    "latitud": -35.3833,
    "longitud": -71.8167
  }
]
```

---

### 5. Registrar inventario de agua
**POST** `/api/v1/abastecimiento/inventario`

```json
{
  "puntoAbastecimientoId": 1,
  "volumenDisponibleLitros": 4000,
  "capacidadMaximaLitros": 5000,
  "camionAljibeId": 7
}
```

Respuesta exitosa `201 Created`:
```json
{
  "id": 1,
  "puntoAbastecimiento": { "id": 1, "nombre": "Punto Agua Pencahue" },
  "volumenDisponibleLitros": 4000,
  "capacidadMaximaLitros": 5000,
  "camionAljibeId": 7,
  "ultimaActualizacion": "2026-05-28T10:30:00",
  "logCarga": "[2026-05-28T10:30:00] Camión aljibe ID=7 inició proceso de carga en punto 'Punto Agua Pencahue'. Volumen disponible: 4000 L"
}
```

---

### 6. Actualizar volumen disponible
**PUT** `/api/v1/abastecimiento/inventario/{id}/volumen?nuevoVolumen=3000`

Ejemplo: `PUT /api/v1/abastecimiento/inventario/1/volumen?nuevoVolumen=3000`

Respuesta exitosa `200 OK`:
```json
{
  "id": 1,
  "volumenDisponibleLitros": 3000,
  "ultimaActualizacion": "2026-05-28T11:00:00"
}
```

> ! Si `nuevoVolumen = 0`, el punto cambia automáticamente 
> su estado a `SIN_AGUA`.

---

### 7. Validación — volumen negativo rechazado
**POST** `/api/v1/abastecimiento/inventario` con volumen negativo:

```json
{
  "puntoAbastecimientoId": 1,
  "volumenDisponibleLitros": -100,
  "capacidadMaximaLitros": 5000
}
```

Error `400 Bad Request`:
```json
{
  "timestamp": "2026-05-28T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "El volumen no puede ser negativo"
}
```

---