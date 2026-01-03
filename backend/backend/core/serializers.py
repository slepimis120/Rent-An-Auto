from django.contrib.auth.models import User
from rest_framework import serializers
from .models import Vehicle, Reservation

class RegisterSerializer(serializers.ModelSerializer):
    password = serializers.CharField(write_only=True, min_length=8)

    class Meta:
        model = User
        fields = ["id", "username", "email", "password", "first_name", "last_name"]

    def create(self, validated_data):
        password = validated_data.pop("password")
        user = User(**validated_data)
        user.set_password(password)
        user.save()
        return user


class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ["id", "username", "email", "first_name", "last_name"]


class VehicleSerializer(serializers.ModelSerializer):
    owner_id = serializers.ReadOnlyField(source="owner.id")

    class Meta:
        model = Vehicle
        fields = "__all__"
        read_only_fields = ["owner"]


class ReservationSerializer(serializers.ModelSerializer):
    renter_id = serializers.ReadOnlyField(source="renter.id")

    class Meta:
        model = Reservation
        fields = "__all__"
        read_only_fields = ["renter", "created_at"]

    def validate(self, attrs):
        start = attrs["start_date"]
        end = attrs["end_date"]
        vehicle = attrs["vehicle"]

        if start > end:
            raise serializers.ValidationError("start_date ne može biti posle end_date.")

        # zabrani preklapanje (najbitnije pravilo)
        overlap = Reservation.objects.filter(
            vehicle=vehicle,
            start_date__lte=end,
            end_date__gte=start,
        ).exists()

        if overlap:
            raise serializers.ValidationError("Vozilo je već rezervisano u tom periodu.")

        return attrs
