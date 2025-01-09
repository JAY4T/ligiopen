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



class TeamRegistration(models.Model):
    team_name = models.CharField(max_length=255)
    coach_name = models.CharField(max_length=255)
    coach_phone = models.CharField(max_length=15)
    coach_email = models.EmailField()
    manager_name = models.CharField(max_length=255)
    manager_phone = models.CharField(max_length=15)
    manager_email = models.EmailField()
    team_category = models.CharField(max_length=50, choices=[('men', 'Men'), ('women', 'Women'), ('youth', 'Youth')])
    num_players = models.PositiveIntegerField()
    home_ground = models.CharField(max_length=255)
    training_ground_needs = models.TextField()
    equipment_needed = models.TextField()
    registration_date = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return self.team_name