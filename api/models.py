from django.db import models

# Create your models here.


class Fixture(models.Model):
    home_team = models.CharField(max_length=100)
    home_team_logo = models.ImageField(upload_to='team_logos/', blank=True, null=True)
    away_team = models.CharField(max_length=100)
    away_team_logo = models.ImageField(upload_to='team_logos/', blank=True, null=True)
    match_date = models.DateTimeField()
    venue = models.CharField(max_length=200)

    def __str__(self):
        return f"{self.home_team} vs {self.away_team} on {self.match_day} at {self.match_time}, {self.match_date.strftime('%Y-%m-%d')}"


class Partner(models.Model):
    name = models.CharField(max_length=255)
    logo = models.ImageField(upload_to="partners/")

    def __str__(self):
        return self.name

class News(models.Model):
    team = models.CharField(max_length=255)
    headline = models.CharField(max_length=255)  # Added headline
    video = models.URLField(null=True, blank=True)  # Video as a URL instead of a file
    news = models.TextField()
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return self.headline



class FeaturedPlayer(models.Model):
    name = models.CharField(max_length=100)
    age = models.IntegerField()
    position = models.CharField(max_length=50)
    club = models.CharField(max_length=100)
    nationality = models.CharField(max_length=50)
    image = models.ImageField(upload_to='players/')
    bio = models.TextField()
    goals_scored = models.IntegerField(default=0)
    matches_played = models.IntegerField(default=0)
    is_featured = models.BooleanField(default=False)  # Mark player as featured

    def __str__(self):
        return f"{self.name} - {self.club}"
