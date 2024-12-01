from django.db import models
from django.contrib.auth.models import User
# Create your models here.
class Profile(models.Model):
    USER_TYPES = [
        ('club', 'Club'),
        ('fan', 'Fan'),
        ('player', 'Player'),
    ]
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    user_type = models.CharField(max_length=10, choices=USER_TYPES)

    def __str__(self):
        return f"{self.user.username} - {self.user_type}"