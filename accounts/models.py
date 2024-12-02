from django.db import models
from django.contrib.auth.models import User


class Profile(models.Model):
    USER_TYPES = [
        ('coach', 'Coach'),
        ('admin', 'Admin'),
        ('player', 'Player'),
    ]
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    user_type = models.CharField(max_length=10, choices=USER_TYPES)

    def __str__(self):
        return f"{self.user.username} ({self.user_type})"



# accounts/models.py

class Team(models.Model):
    team_name = models.CharField(max_length=100)
    coach = models.CharField(max_length=100)
    captain_name = models.CharField(max_length=100)
    captain_email = models.EmailField()
    num_players = models.IntegerField()
    home_ground = models.CharField(max_length=100)

    def __str__(self):
        return self.team_name

