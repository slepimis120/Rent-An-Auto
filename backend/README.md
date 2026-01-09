# 🚗 Backend API – Vehicle Rental System

- upravljanje korisnicima
- kačenje i pretragu vozila
- iznajmljivanje vozila (rezervacije)

Autentifikacija se vrši pomoću **JWT (JSON Web Tokens)**.

## ⚙️ Tehnologije

- Python 3
- Django
- Django REST Framework
- JWT (SimpleJWT)
- SQLite (development)
- Docker & Docker Compose

---

## 🔐 Autentifikacija

### Registracija korisnika

**POST** `/api/auth/register/`

```json
{
  "username": "testuser",
  "password": "test12345",
  "email": "test@test.com"
}
```

---

### Login (JWT)

**POST** `/api/auth/login/`

```json
{
  "username": "testuser",
  "password": "test12345"
}
```

**Response:**

```json
{
  "access": "JWT_ACCESS_TOKEN",
  "refresh": "JWT_REFRESH_TOKEN"
}
```

📌 `access` token se koristi za sve zaštićene API pozive.

---

## 🔑 Autorizacija

Za sve rute (osim registracije i login-a) potrebno je dodati HTTP header:

```
Authorization: Bearer <access_token>
```

---

## 👤 Users API

**Base ruta:** `/api/users/`

Podržane operacije:

- `GET /api/users/` – lista korisnika
- `GET /api/users/{id}/` – detalj korisnika
- `POST /api/users/` – kreiranje korisnika
- `PATCH /api/users/{id}/` – izmena korisnika
- `DELETE /api/users/{id}/` – brisanje korisnika

---

## 🚗 Vehicles API

**Base ruta:** `/api/vehicles/`

### CRUD operacije

- `GET /api/vehicles/`
- `POST /api/vehicles/` – kačenje vozila
- `GET /api/vehicles/{id}/`
- `PATCH /api/vehicles/{id}/`
- `DELETE /api/vehicles/{id}/`

📌 `owner` vozila se automatski postavlja na ulogovanog korisnika.

### Pretraga

```
/api/vehicles/?search=BMW
```

### Filteri

```
/api/vehicles/?city=Beograd&brand=BMW&year=2022
```

### Sortiranje

```
/api/vehicles/?ordering=price_per_day
/api/vehicles/?ordering=-price_per_day
```

---

## 📅 Reservations API

**Base ruta:** `/api/reservations/`

### Operacije

- `GET /api/reservations/`
- `POST /api/reservations/` – kreiranje rezervacije
- `GET /api/reservations/{id}/`
- `PATCH /api/reservations/{id}/`
- `DELETE /api/reservations/{id}/`

**Primer body-ja (POST):**

```json
{
  "vehicle": 1,
  "start_date": "2026-01-10",
  "end_date": "2026-01-15"
}
```

📌 `renter` se automatski postavlja na ulogovanog korisnika  
📌 Zabranjeno je preklapanje rezervacija za isto vozilo

---

## 🛠️ Admin panel

Django admin je dostupan na:

```
/admin/
```

Omogućava:

- pregled korisnika
- pregled vozila
- pregled rezervacija

---

## ▶️ Pokretanje projekta (Docker)

```bash
docker compose up --build
```

---
