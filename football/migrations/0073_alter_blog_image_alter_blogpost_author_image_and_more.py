# Generated by Django 5.0.1 on 2024-08-20 11:21

import django.db.models.deletion
import football.models
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('football', '0072_alter_player_rating'),
    ]

    operations = [
        migrations.AlterField(
            model_name='blog',
            name='image',
            field=models.ImageField(blank=True, null=True, upload_to=football.models.upload_to),
        ),
        migrations.AlterField(
            model_name='blogpost',
            name='author_image',
            field=models.ImageField(blank=True, null=True, upload_to=football.models.upload_to),
        ),
        migrations.AlterField(
            model_name='blogpost',
            name='image',
            field=models.ImageField(blank=True, null=True, upload_to=football.models.upload_to),
        ),
        migrations.AlterField(
            model_name='club',
            name='coach_photo',
            field=models.ImageField(blank=True, null=True, upload_to=football.models.upload_to),
        ),
        migrations.AlterField(
            model_name='club',
            name='location',
            field=models.CharField(blank=True, max_length=100, null=True),
        ),
        migrations.AlterField(
            model_name='club',
            name='photo',
            field=models.ImageField(blank=True, null=True, upload_to=football.models.upload_to),
        ),
        migrations.AlterField(
            model_name='club',
            name='stadium',
            field=models.CharField(blank=True, max_length=100, null=True),
        ),
        migrations.AlterField(
            model_name='clubmatch',
            name='logo',
            field=models.ImageField(upload_to=football.models.upload_to),
        ),
        migrations.AlterField(
            model_name='fixture',
            name='club',
            field=models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.CASCADE, related_name='fixtures', to='football.club'),
        ),
        migrations.AlterField(
            model_name='fixture',
            name='team_a_logo',
            field=models.ImageField(blank=True, null=True, upload_to=football.models.upload_to),
        ),
        migrations.AlterField(
            model_name='fixture',
            name='team_a_name',
            field=models.CharField(blank=True, max_length=255, null=True),
        ),
        migrations.AlterField(
            model_name='fixture',
            name='team_b_logo',
            field=models.ImageField(blank=True, null=True, upload_to=football.models.upload_to),
        ),
        migrations.AlterField(
            model_name='fixture',
            name='team_b_name',
            field=models.CharField(blank=True, max_length=255, null=True),
        ),
        migrations.AlterField(
            model_name='fixture',
            name='time',
            field=models.TimeField(blank=True, null=True),
        ),
        migrations.AlterField(
            model_name='match',
            name='away_team_logo',
            field=models.ImageField(blank=True, null=True, upload_to=football.models.upload_to),
        ),
        migrations.AlterField(
            model_name='match',
            name='home_team_logo',
            field=models.ImageField(blank=True, null=True, upload_to=football.models.upload_to),
        ),
        migrations.AlterField(
            model_name='player',
            name='apps',
            field=models.IntegerField(default=0),
        ),
        migrations.AlterField(
            model_name='player',
            name='assists',
            field=models.IntegerField(default=0),
        ),
        migrations.AlterField(
            model_name='player',
            name='goals',
            field=models.IntegerField(default=0),
        ),
        migrations.AlterField(
            model_name='player',
            name='mins',
            field=models.IntegerField(default=0),
        ),
        migrations.AlterField(
            model_name='player',
            name='motm',
            field=models.IntegerField(default=0),
        ),
        migrations.AlterField(
            model_name='player',
            name='nationality',
            field=models.CharField(blank=True, max_length=50, null=True),
        ),
        migrations.AlterField(
            model_name='player',
            name='photo',
            field=models.ImageField(blank=True, null=True, upload_to=football.models.upload_to),
        ),
        migrations.AlterField(
            model_name='player',
            name='position',
            field=models.CharField(blank=True, max_length=100, null=True),
        ),
        migrations.AlterField(
            model_name='player',
            name='red_cards',
            field=models.IntegerField(default=0),
        ),
        migrations.AlterField(
            model_name='player',
            name='yellow_cards',
            field=models.IntegerField(default=0),
        ),
        migrations.AlterField(
            model_name='result',
            name='fixture',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='results', to='football.fixture'),
        ),
        migrations.AlterField(
            model_name='result',
            name='photo',
            field=models.ImageField(blank=True, null=True, upload_to=football.models.upload_to),
        ),
        migrations.AlterField(
            model_name='result',
            name='score',
            field=models.CharField(blank=True, max_length=100, null=True),
        ),
        migrations.AlterField(
            model_name='staff',
            name='nationality',
            field=models.CharField(blank=True, max_length=100, null=True),
        ),
        migrations.AlterField(
            model_name='staff',
            name='photo',
            field=models.ImageField(blank=True, null=True, upload_to=football.models.upload_to),
        ),
        migrations.AlterField(
            model_name='team',
            name='logo',
            field=models.ImageField(blank=True, null=True, upload_to=football.models.upload_to),
        ),
    ]
