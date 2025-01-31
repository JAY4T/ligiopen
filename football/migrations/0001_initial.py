# Generated by Django 5.0.1 on 2024-10-25 19:13

import django.db.models.deletion
import django.utils.timezone
import football.models
from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Blog',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('headline', models.CharField(max_length=200)),
                ('description', models.TextField()),
                ('image', models.ImageField(blank=True, null=True, upload_to=football.models.upload_to)),
            ],
        ),
        migrations.CreateModel(
            name='BlogPost',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('title', models.CharField(max_length=200)),
                ('author', models.CharField(max_length=100)),
                ('date_published', models.DateField()),
                ('day', models.CharField(blank=True, max_length=20, null=True)),
                ('content', models.TextField()),
                ('image', models.ImageField(blank=True, null=True, upload_to=football.models.upload_to)),
                ('author_image', models.ImageField(blank=True, null=True, upload_to=football.models.upload_to)),
            ],
        ),
        migrations.CreateModel(
            name='Club',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=100)),
                ('location', models.CharField(blank=True, max_length=100, null=True)),
                ('established_date', models.DateField(default=django.utils.timezone.now)),
                ('stadium', models.CharField(blank=True, max_length=100, null=True)),
                ('photo', models.ImageField(blank=True, default='defaults/club_placeholder.png', null=True, upload_to=football.models.upload_to)),
                ('coach_photo', models.ImageField(blank=True, default='defaults/coach_placeholder.png', null=True, upload_to=football.models.upload_to)),
                ('coach_name', models.CharField(blank=True, max_length=100, null=True)),
            ],
        ),
        migrations.CreateModel(
            name='ClubMatch',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=100)),
                ('logo', models.ImageField(upload_to=football.models.upload_to)),
            ],
        ),
        migrations.CreateModel(
            name='Feedback',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('rating', models.IntegerField(choices=[(1, '1 Star'), (2, '2 Stars'), (3, '3 Stars'), (4, '4 Stars'), (5, '5 Stars')], default=5)),
                ('feedback_text', models.TextField()),
                ('created_at', models.DateTimeField(auto_now_add=True)),
            ],
        ),
        migrations.CreateModel(
            name='Fixture',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('date', models.DateField()),
                ('time', models.TimeField(blank=True, null=True)),
                ('competition', models.CharField(max_length=255)),
                ('venue', models.CharField(max_length=255)),
                ('team_a_name', models.CharField(max_length=255)),
                ('team_a_logo', models.ImageField(blank=True, null=True, upload_to='team_logos/')),
                ('team_b_name', models.CharField(max_length=255)),
                ('team_b_logo', models.ImageField(blank=True, null=True, upload_to='team_logos/')),
            ],
        ),
        migrations.CreateModel(
            name='FixtureTeam',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('competition', models.CharField(max_length=100)),
                ('date', models.DateTimeField()),
                ('opponent', models.CharField(max_length=100)),
                ('venue', models.CharField(max_length=100)),
            ],
        ),
        migrations.CreateModel(
            name='Stadium',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=100)),
            ],
        ),
        migrations.CreateModel(
            name='Team',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=100)),
                ('logo', models.ImageField(blank=True, null=True, upload_to=football.models.upload_to)),
                ('coach', models.CharField(blank=True, max_length=100, null=True)),
                ('home_ground', models.CharField(blank=True, max_length=100, null=True)),
                ('lineup', models.TextField(blank=True, null=True)),
                ('substitutes', models.TextField(blank=True, null=True)),
                ('booked', models.CharField(blank=True, max_length=255, null=True)),
                ('goalscorers', models.CharField(blank=True, max_length=255, null=True)),
            ],
        ),
        migrations.CreateModel(
            name='TeamIndividual',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=100)),
                ('logo', models.ImageField(blank=True, null=True, upload_to='team_logos/')),
            ],
        ),
        migrations.CreateModel(
            name='FixtureResult',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('score', models.CharField(default=0, max_length=20)),
                ('time_taken', models.TimeField(blank=True, null=True)),
                ('result_photo', models.ImageField(blank=True, null=True, upload_to='result_photos/')),
                ('fixture', models.OneToOneField(blank=True, null=True, on_delete=django.db.models.deletion.CASCADE, related_name='result', to='football.fixture')),
            ],
        ),
        migrations.CreateModel(
            name='Player',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=100)),
                ('position', models.CharField(blank=True, max_length=100, null=True)),
                ('height_cm', models.PositiveIntegerField(blank=True, null=True)),
                ('weight_kg', models.PositiveIntegerField(blank=True, null=True)),
                ('date_of_birth', models.DateField(default=django.utils.timezone.now)),
                ('nationality', models.CharField(blank=True, max_length=50, null=True)),
                ('team', models.CharField(blank=True, max_length=100, null=True)),
                ('photo', models.ImageField(blank=True, default='defaults/player_placeholder.png', null=True, upload_to=football.models.upload_to)),
                ('apps', models.IntegerField()),
                ('mins', models.IntegerField()),
                ('goals', models.IntegerField()),
                ('assists', models.IntegerField()),
                ('yellow_cards', models.IntegerField(default=0)),
                ('red_cards', models.IntegerField(default=0)),
                ('motm', models.IntegerField()),
                ('club', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='players', to='football.club')),
            ],
        ),
        migrations.CreateModel(
            name='ResultTeam',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('home_score', models.IntegerField()),
                ('away_score', models.IntegerField()),
                ('home_goal_scorers', models.TextField()),
                ('away_goal_scorers', models.TextField()),
                ('fixture_team', models.OneToOneField(on_delete=django.db.models.deletion.CASCADE, to='football.fixtureteam')),
            ],
        ),
        migrations.CreateModel(
            name='Matchday',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('date_time', models.DateTimeField()),
                ('home_team_logo', models.ImageField(blank=True, null=True, upload_to=football.models.upload_to)),
                ('away_team_logo', models.ImageField(blank=True, null=True, upload_to=football.models.upload_to)),
                ('summary', models.TextField(blank=True, null=True)),
                ('first_half_summary', models.TextField(blank=True, null=True)),
                ('second_half_summary', models.TextField(blank=True, null=True)),
                ('referee_name', models.CharField(blank=True, max_length=100, null=True)),
                ('author_name', models.CharField(blank=True, max_length=100, null=True)),
                ('away_team', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='away_matches', to='football.club')),
                ('home_team', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='home_matches', to='football.club')),
                ('stadium', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='football.stadium')),
            ],
        ),
        migrations.CreateModel(
            name='Staff',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=100)),
                ('role', models.CharField(choices=[('Head Coach', 'Head Coach'), ('Assistant Coach', 'Assistant Coach'), ('Director', 'Diretor'), ('Project Manager', 'Project Manager'), ('Community Engager', 'Community Engager'), ('Backend Developer', 'Backend Developer'), ('Frontend Developer', 'Frontend Developer')], max_length=100)),
                ('staff_type', models.CharField(choices=[('Coaching Staff', 'Coaching Staff'), ('Other Staff', 'Other Staff'), ('Club Match Staff', 'Club Match Staff'), ('ligiopen staff', 'ligiopen staff')], max_length=100)),
                ('nationality', models.CharField(blank=True, max_length=100, null=True)),
                ('experience', models.CharField(blank=True, max_length=50, null=True)),
                ('image', models.ImageField(default='defaults/staff_placeholder.png', upload_to='staff_images/')),
                ('club', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='staff', to='football.club')),
            ],
        ),
        migrations.AddField(
            model_name='fixtureteam',
            name='team',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='football.teamindividual'),
        ),
    ]
