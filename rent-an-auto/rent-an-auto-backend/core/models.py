from django.conf import settings
from django.db import models

class Vehicle(models.Model):
    owner = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE, related_name="vehicles")
    brand = models.CharField(max_length=64)
    model = models.CharField(max_length=64)
    city = models.CharField(max_length=64)
    year = models.PositiveIntegerField()
    price_per_day = models.DecimalField(max_digits=10, decimal_places=2)
    is_active = models.BooleanField(default=True)

    def __str__(self):
        return f"{self.brand} {self.model} ({self.year})"


class Reservation(models.Model):
    vehicle = models.ForeignKey(Vehicle, on_delete=models.CASCADE, related_name="reservations")
    renter = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE, related_name="reservations")
    start_date = models.DateField()
    end_date = models.DateField()
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"{self.vehicle} | {self.start_date}-{self.end_date}"
