# accounts/models.py
from django.db import models
from django.contrib.auth.models import User

class Profile(models.Model):
    USER_TYPE_CHOICES = [
        ('Admin', 'Admin'),
        ('Coach', 'Coach'),
        ('Player', 'Player'),
    ]
    
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    user_type = models.CharField(max_length=10, choices=USER_TYPE_CHOICES)

    def __str__(self):
        return self.user.username
