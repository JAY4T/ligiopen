from django.db import models
from django.utils import timezone

def upload_to(instance, filename):
    """Generate dynamic upload paths based on instance and file type."""
    upload_paths = {
        Club: 'club_photos/',
        Player: 'players/',
        Staff: 'staff/',
        Team: 'team_logos/',
        FixtureResult: 'team_logos/',
        Matchday: 'match_logos/',
        Blog: 'blog_images/',
        BlogPost: 'blogpost_images/',
        ClubMatch: 'club_logos/',
    }
    return upload_paths.get(type(instance), '') + filename

class Club(models.Model):
    name = models.CharField(max_length=100)
    location = models.CharField(max_length=100, blank=True, null=True)
    established_date = models.DateField(default=timezone.now)
    stadium = models.CharField(max_length=100, blank=True, null=True)
    photo = models.ImageField(upload_to=upload_to, blank=True, null=True, default='defaults/club_placeholder.png')
    coach_photo = models.ImageField(upload_to=upload_to, blank=True, null=True, default='defaults/coach_placeholder.png')

    coach_name = models.CharField(max_length=100, blank=True, null=True)

    def __str__(self):
        return self.name

class Player(models.Model):
    name = models.CharField(max_length=100)
    position = models.CharField(max_length=100, blank=True, null=True)
    height_cm = models.PositiveIntegerField(blank=True, null=True)
    weight_kg = models.PositiveIntegerField(blank=True, null=True)
    date_of_birth = models.DateField(default=timezone.now)
    nationality = models.CharField(max_length=50, blank=True, null=True)
    club = models.ForeignKey(Club, on_delete=models.CASCADE, related_name='players')
    team = models.CharField(max_length=100, blank=True, null=True)
    photo = models.ImageField(upload_to=upload_to, blank=True, null=True, default='defaults/player_placeholder.png')

    apps = models.IntegerField()
    mins = models.IntegerField()
    goals = models.IntegerField()
    assists = models.IntegerField()
    yellow_cards = models.IntegerField(default=0)
    red_cards = models.IntegerField(default=0)
    motm = models.IntegerField()

   

    def __str__(self):
        return self.name



class Staff(models.Model):
    STAFF_ROLES = [
        ('Coaching Staff', 'Coaching Staff'),
        ('Other Staff', 'Other Staff'),
        ('Club Match Staff', 'Club Match Staff'),
        ('ligiopen staff', 'ligiopen staff')
        
    ]

    ROLE_CHOICES = [
        ('Head Coach', 'Head Coach'),
        ('Assistant Coach', 'Assistant Coach'),
        ('Director', 'Diretor'),
        ('Project Manager', 'Project Manager'),
        ('Community Engager', 'Community Engager'),
        ('Backend Developer','Backend Developer'),
        ('Frontend Developer','Frontend Developer')
    ]
    name = models.CharField(max_length=100)
    role = models.CharField(max_length=100, choices=ROLE_CHOICES)
    staff_type = models.CharField(max_length=100, choices=STAFF_ROLES)
    nationality = models.CharField(max_length=100, blank=True, null=True)
    experience = models.CharField(max_length=50, blank=True, null=True)
    club = models.ForeignKey(Club, related_name='staff', on_delete=models.CASCADE)
    image = models.ImageField(upload_to='staff_images/', default='defaults/staff_placeholder.png')



    def __str__(self):
        return f"{self.name} ({self.staff_type})"

class Team(models.Model):
    name = models.CharField(max_length=100)
    logo = models.ImageField(upload_to=upload_to, blank=True, null=True)
    coach = models.CharField(max_length=100, blank=True, null=True)
    home_ground = models.CharField(max_length=100, blank=True, null=True)
    lineup = models.TextField(blank=True, null=True)
    substitutes = models.TextField(blank=True, null=True)
    booked = models.CharField(max_length=255, blank=True, null=True)
    goalscorers = models.CharField(max_length=255, blank=True, null=True)

    def __str__(self):
        return self.name

# class Result(models.Model):
#     score = models.CharField(max_length=100, blank=True, null=True)
#     minute = models.IntegerField(default=0)
#     is_home_team = models.BooleanField(default=True)
#     fixture = models.ForeignKey('Fixture', related_name='results', on_delete=models.CASCADE)
#     photo = models.ImageField(upload_to=upload_to, blank=True, null=True)

#     def __str__(self):
#         return f"{self.score} - {self.minute} min"


class Fixture(models.Model):
    date = models.DateField()
    time = models.TimeField(blank=True, null=True)
    competition = models.CharField(max_length=255)
    venue = models.CharField(max_length=255)
    team_a_name = models.CharField(max_length=255)
    team_a_logo = models.ImageField(upload_to='team_logos/', blank=True, null=True)
    team_b_name = models.CharField(max_length=255)
    team_b_logo = models.ImageField(upload_to='team_logos/', blank=True, null=True)

    def __str__(self):
        return f"{self.team_a_name} vs {self.team_b_name} on {self.date}"


class FixtureResult(models.Model):
    fixture = models.OneToOneField(Fixture, related_name='result', on_delete=models.CASCADE, blank=True, null=True)  # Allow null initially
    score = models.CharField(max_length=20, default=0)
    time_taken = models.TimeField(null=True, blank=True)  # Time taken for the result
    result_photo = models.ImageField(upload_to='result_photos/', blank=True, null=True)

    def __str__(self):
        if self.fixture:  
         return f"Result: {self.fixture.team_a_name} vs {self.fixture.team_b_name} on {self.fixture.date} - Score: {self.score or 'No score'}"
        return "Result with no associated fixture"


class Stadium(models.Model):
    name = models.CharField(max_length=100)

    def __str__(self):
        return self.name

class Matchday(models.Model):
    date_time = models.DateTimeField()
    stadium = models.ForeignKey(Stadium, on_delete=models.CASCADE)
    home_team = models.ForeignKey(Club, related_name='home_matches', on_delete=models.CASCADE)
    away_team = models.ForeignKey(Club, related_name='away_matches', on_delete=models.CASCADE)
    home_team_logo = models.ImageField(upload_to=upload_to, blank=True, null=True)
    away_team_logo = models.ImageField(upload_to=upload_to, blank=True, null=True)
    summary = models.TextField(blank=True, null=True)
    first_half_summary = models.TextField(blank=True, null=True)
    second_half_summary = models.TextField(blank=True, null=True)
    referee_name = models.CharField(max_length=100, blank=True, null=True)
    author_name = models.CharField(max_length=100, blank=True, null=True)

    def __str__(self):
        return f"{self.home_team.name} vs {self.away_team.name} on {self.date_time.strftime('%B %d, %Y')}"

class Blog(models.Model):
    headline = models.CharField(max_length=200)
    description = models.TextField()
    image = models.ImageField(upload_to=upload_to, blank=True, null=True)

    def __str__(self):
        return self.headline


class BlogPost(models.Model):
    title = models.CharField(max_length=200)
    author = models.CharField(max_length=100)
    date_published = models.DateField()
    day = models.CharField(max_length=20, blank=True, null=True)
    content = models.TextField()
    image = models.ImageField(upload_to=upload_to, blank=True, null=True)
    author_image = models.ImageField(upload_to=upload_to, blank=True, null=True)

    def __str__(self):
        return self.title
        
        

class ClubMatch(models.Model):
    name = models.CharField(max_length=100)
    logo = models.ImageField(upload_to=upload_to)

    def __str__(self):
        return self.name




class Feedback(models.Model):
    RATING_CHOICES = [
        (1, '1 Star'),
        (2, '2 Stars'),
        (3, '3 Stars'),
        (4, '4 Stars'),
        (5, '5 Stars'),
    ]
    rating = models.IntegerField(choices=RATING_CHOICES, default=5)  # Set a default value, e.g., 3 stars

    feedback_text = models.TextField()
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f'{self.rating} - {self.feedback_text[:50]}'


class TeamIndividual(models.Model):
    name = models.CharField(max_length=100)
    logo = models.ImageField(upload_to='team_logos/', blank=True, null=True)

    def __str__(self):
        return self.name


class FixtureTeam(models.Model):
    competition = models.CharField(max_length=100)
    date = models.DateTimeField()
    opponent = models.CharField(max_length=100)
    venue = models.CharField(max_length=100)
    team = models.ForeignKey(TeamIndividual, on_delete=models.CASCADE)  

    def __str__(self):
        return f'{self.team.name} vs {self.opponent}'


class ResultTeam(models.Model):
    fixture_team = models.OneToOneField(FixtureTeam, on_delete=models.CASCADE)
    home_score = models.IntegerField()
    away_score = models.IntegerField()
    home_goal_scorers = models.TextField() 
    away_goal_scorers = models.TextField()  
    def __str__(self):
        return f'{self.fixture_team.team.name} {self.home_score} - {self.away_score} {self.fixture_team.opponent}'
