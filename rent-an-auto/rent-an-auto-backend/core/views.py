from django.contrib.auth.models import User
from rest_framework import viewsets, generics, permissions
from django_filters.rest_framework import DjangoFilterBackend
from rest_framework.filters import SearchFilter, OrderingFilter

from .models import Vehicle, Reservation
from .serializers import RegisterSerializer, UserSerializer, VehicleSerializer, ReservationSerializer

class RegisterView(generics.CreateAPIView):
    permission_classes = [permissions.AllowAny]
    serializer_class = RegisterSerializer


class UserViewSet(viewsets.ModelViewSet):
    queryset = User.objects.all()
    serializer_class = UserSerializer


class VehicleViewSet(viewsets.ModelViewSet):
    queryset = Vehicle.objects.all()
    serializer_class = VehicleSerializer

    filter_backends = [DjangoFilterBackend, SearchFilter, OrderingFilter]
    filterset_fields = ["city", "brand", "year", "is_active"]
    search_fields = ["brand", "model", "city"]
    ordering_fields = ["price_per_day", "year", "id"]

    def perform_create(self, serializer):
        serializer.save(owner=self.request.user)


class ReservationViewSet(viewsets.ModelViewSet):
    queryset = Reservation.objects.all()
    serializer_class = ReservationSerializer

    def perform_create(self, serializer):
        serializer.save(renter=self.request.user)
