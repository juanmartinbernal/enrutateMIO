# EnrútateMIO

Es una aplicación dirigida para la comunidad caleña usuario del sistema de transporte masivo MIO de manera gratuita.

Con esta aplicación los usuarios podrán conocer: 

* Las rutas de cada estación o parada.
* Recorrido de paradas que tiene de cada una de las rutas que se encuentren dentro del sistema.
* Podrán ver noticias relacionados con el sistema MIO (Tweets @METROCALI).
* Podrás conocer los puntos de recarga de tu tarjeta, (mapa o listado).
* También tendrás la posibilidad de buscar estaciones o paradas, por el nombre o una dirección. 
- Presionando sobre el mapa, podrás marcar origen/destino y calcular la ruta a pie con Google Directions.

---

## Estado de la migración (2026)

Este proyecto fue modernizado completamente desde una base 100% Java (Gradle 3.3 / AGP 2.3.2, ~2017,
sin arquitectura formal, Apache HttpClient, ActiveAndroid/SQLite manual) a una arquitectura moderna:

- **Lenguaje**: Kotlin 100%.
- **UI**: Jetpack Compose + Material 3 (reemplaza Views XML, SlidingMenu, ListViewAnimations).
- **Arquitectura**: MVI unidireccional (`core:common`'s `BaseMviViewModel`: State/Intent/Effect).
- **Multimódulo** por capas:
  - `app` — Application, MainActivity, NavHost, wiring de Koin.
  - `core:common` — MVI base, `Result`, dispatchers, utilidades geo (Haversine).
  - `core:model` — modelos de dominio puros (sin Android).
  - `core:network` — Retrofit + Moshi + OkHttp (Directions API, Twitter API v2, backend propio).
  - `core:database` — Room (reemplaza `AdminSQLiteOpenHelper`/SQLite manual).
  - `core:datastore` — Preferences DataStore (reemplaza `SharedPreferences`/`GlobalData`).
  - `core:designsystem` — tema Compose, colores/tipografía migrados 1:1 de la app legacy.
  - `core:ui` — composables compartidos (loading/error/empty, badges de ruta, buscador).
  - `domain` — casos de uso + interfaces de repositorio (Kotlin puro).
  - `data` — implementación de repositorios, seeding de Room desde datos reales exportados
    del `test.db` legacy, parsing de `puntos_recarga.json`.
  - `feature:routes`, `feature:feeders`, `feature:recharge`, `feature:map`, `feature:news` —
    una pantalla Compose+MVI por funcionalidad.
- **Inyección de dependencias**: Koin (reemplaza instanciación manual `new`).
- **Red**: Retrofit + Moshi (reemplaza Apache HttpClient legacy/`RequestHttp`/`JSONParser`).
  - El backend propio de planificación de viajes (`tuyo.herokuapp.com`, inactivo) se reemplazó
    por **Google Directions API** para calcular rutas punto a punto.
  - El login OAuth 1.0a de Twitter4j se reemplazó por **Twitter API v2 con Bearer token**
    (app-only), ya que la app solo necesita leer el timeline público de @METROCALI.
- **Persistencia**: Room (GTFS-like: estaciones y rutas reales exportadas del `test.db` legacy,
  58 estaciones / 241 filas de rutas) + Preferences DataStore.
- **minSdk 24 / compileSdk-targetSdk 35**, Gradle 8.9, AGP 8.7.3, Kotlin 2.0.21.
- El código legacy original se conserva en `legacy/` solo como referencia histórica y **no
  forma parte del build** (no está incluido en `settings.gradle.kts`).

### Pendiente / decisiones a validar por el equipo

1. **Secretos**: `GOOGLE_MAPS_API_KEY` y `TWITTER_BEARER_TOKEN` deben configurarse en
   `local.properties` (no se commitean). Ver sección "Configuración" abajo.
2. **Foursquare** (sitios cercanos) quedó fuera del alcance de esta migración por decisión
   del equipo; puede reintroducirse como nuevo `feature:nearby-places` si se desea.
3. **Backend propio de noticias** (`enrutatemio.com/enrutate/noticias.php`) y el antiguo
   planificador de viajes (`tuyo.herokuapp.com`) probablemente ya no estén activos (databan
   de ~2015-2017); `NewsRepositoryImpl`/`DirectionsRepositoryImpl` están preparados para
   fallar de forma aislada si una fuente no responde.
4. Falta feature de "integración" del sistema y widget de home screen (no migrados).

## Configuración

Crea/edita `local.properties` en la raíz del proyecto con:

```properties
sdk.dir=/ruta/a/tu/Android/Sdk
GOOGLE_MAPS_API_KEY=tu_api_key_de_google_maps
TWITTER_BEARER_TOKEN=tu_bearer_token_de_twitter_api_v2
ENRUTATE_BACKEND_BASE_URL=https://tu-backend-actualizado.com/
```

Luego compila con `./gradlew :app:assembleDebug`.

