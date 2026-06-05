from django.contrib import admin
from .models import Vehicle, Reservation

@admin.register(Vehicle)
class VehicleAdmin(admin.ModelAdmin):
    list_display = ("id", "brand", "model", "year", "city", "price_per_day", "owner", "is_active")
    list_filter = ("city", "brand", "is_active", "year")
    search_fields = ("brand", "model", "city", "owner__username", "owner__email")
    ordering = ("-id",)

@admin.register(Reservation)
class ReservationAdmin(admin.ModelAdmin):
    list_display = ("id", "vehicle", "renter", "start_date", "end_date", "created_at")
    list_filter = ("start_date", "end_date")
    search_fields = ("vehicle__brand", "vehicle__model", "renter__username", "renter__email")
    ordering = ("-id",)
