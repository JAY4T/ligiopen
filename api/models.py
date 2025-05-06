from django.db import models

# Create your models here.


class Fixture(models.Model):
    home_team = models.CharField(max_length=100)
    home_team_logo = models.ImageField(upload_to='team_logos/', blank=True, null=True)
    away_team = models.CharField(max_length=100)
    away_team_logo = models.ImageField(upload_to='team_logos/', blank=True, null=True)
    match_date = models.DateTimeField()
    venue = models.CharField(max_length=200)

    # Result-tracking fields
    is_completed = models.BooleanField(default=False)
    home_score = models.PositiveIntegerField(blank=True, null=True)
    away_score = models.PositiveIntegerField(blank=True, null=True)

    def __str__(self):
        return f"{self.home_team} vs {self.away_team} at {self.venue} on {self.match_date.strftime('%Y-%m-%d %H:%M')}"

    def has_result(self):
        return self.is_completed and self.home_score is not None and self.away_score is not None


class Partner(models.Model):
    name = models.CharField(max_length=255)
    logo = models.ImageField(upload_to="partners/")

    def __str__(self):
        return self.name

class News(models.Model):
    team = models.CharField(max_length=255)
    headline = models.CharField(max_length=255)  # Added headline
    news_photo = models.ImageField(upload_to='news_photos/', null=True, blank=True)  # New image field for photo
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



# clubs

class Club(models.Model):
    name = models.CharField(max_length=255)
    logo = models.ImageField(upload_to="club_logos/", blank=True, null=True)
    source_link = models.URLField(max_length=500, blank=True, null=True)  # New field added


    def __str__(self):
        return self.name

class Highlight(models.Model):
    title = models.CharField(max_length=255)
    description = models.TextField()
    imageUrl = models.URLField()  # Or use ImageField with MEDIA if you're uploading
    date = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return self.title

class LiveMatch(models.Model):
    stream_url = models.URLField()  # URL for the live stream (e.g., YouTube)
    match_title = models.CharField(max_length=200)
    is_live = models.BooleanField(default=False)  # Indicates if the match is live

    def __str__(self):
        return self.match_title


class Result(models.Model):
    fixture = models.OneToOneField(Fixture, on_delete=models.CASCADE, related_name='result')
    home_team_score = models.PositiveIntegerField()
    away_team_score = models.PositiveIntegerField()
    start_time = models.DateTimeField()
    end_time = models.DateTimeField()

    def get_duration(self):
        return self.end_time - self.start_time

    def __str__(self):
        return f"{self.fixture.home_team} {self.home_team_score} - {self.away_team_score} {self.fixture.away_team}"

class Season(models.Model):
    name = models.CharField(max_length=50)  # e.g. "2024/25"
    start_date = models.DateField()
    end_date = models.DateField()

    def __str__(self):
        return self.name

class PlayerProfile(models.Model):
    name = models.CharField(max_length=100)
    age = models.PositiveIntegerField()
    position = models.CharField(max_length=50)
    image = models.ImageField(upload_to='player_photos/', blank=True, null=True)
    club = models.ForeignKey('Club', on_delete=models.CASCADE, related_name='players')
    #season = models.ForeignKey(Season, on_delete=models.CASCADE, related_name='players')

    def __str__(self):
        return f"{self.name} - {self.position} ({self.club.name})"