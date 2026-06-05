import os
import sys
from pathlib import Path

# ✅ 1) Dodaj root projekta u sys.path (da Python vidi "config" i "core")
BASE_DIR = Path(__file__).resolve().parent.parent  # /app
sys.path.append(str(BASE_DIR))

# ✅ 2) Poveži Django settings (kod tebe je folder "config")
os.environ.setdefault("DJANGO_SETTINGS_MODULE", "config.settings")

# ✅ 3) Podigni Django
import django
django.setup()

# ✅ 4) Tek sada Django importi
from django.contrib.auth.models import User
from core.models import Vehicle, Reservation
from datetime import date, timedelta
import random

# --- CLEAN ---
Reservation.objects.all().delete()
Vehicle.objects.all().delete()
User.objects.filter(username__startswith="testuser").delete()

print("DB cleaned.")

# --- USERS ---
users = []
for i in range(1, 4):
    u = User.objects.create_user(
        username=f"testuser{i}",
        password="test12345",
        email=f"testuser{i}@example.com",
    )
    users.append(u)

print("Users created.")

# --- VEHICLES ---
v = Vehicle.objects.create(
    owner=users[0],
    brand="BMW",
    model="X5",
    city="Beograd",
    year=2022,
    price_per_day=100,
    is_active=True
)

# --- RESERVATION ---
Reservation.objects.create(
    vehicle=v,
    renter=users[1],
    start_date=date.today(),
    end_date=date.today() + timedelta(days=3),
)

print("DONE.")
