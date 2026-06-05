from django.urls import path, include
from rest_framework.routers import DefaultRouter
from rest_framework_simplejwt.views import TokenObtainPairView, TokenRefreshView

from .views import RegisterView, UserViewSet, VehicleViewSet, ReservationViewSet

router = DefaultRouter()
router.register("users", UserViewSet, basename="users")
router.register("vehicles", VehicleViewSet, basename="vehicles")
router.register("reservations", ReservationViewSet, basename="reservations")

urlpatterns = [
    path("auth/register/", RegisterView.as_view()),
    path("auth/login/", TokenObtainPairView.as_view()),
    path("auth/refresh/", TokenRefreshView.as_view()),
    path("", include(router.urls)),
]
