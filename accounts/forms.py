# forms.py
from django import forms
from .models import Team

class TeamRegistrationForm(forms.ModelForm):
    class Meta:
        model = Team
        fields = ['team_name', 'coach', 'captain_name', 'captain_email', 'num_players', 'home_ground']
