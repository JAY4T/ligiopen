# Generated by Django 5.0.6 on 2024-05-24 17:56

import django.db.models.deletion
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('football', '0011_rename_matchresult_result'),
    ]

    operations = [
        migrations.RenameField(
            model_name='fixture',
            old_name='location',
            new_name='venue',
        ),
        migrations.RemoveField(
            model_name='fixture',
            name='match_date',
        ),
        migrations.RemoveField(
            model_name='result',
            name='away_goal_details',
        ),
        migrations.RemoveField(
            model_name='result',
            name='away_team_score',
        ),
        migrations.RemoveField(
            model_name='result',
            name='home_goal_details',
        ),
        migrations.RemoveField(
            model_name='result',
            name='home_team_score',
        ),
        migrations.AddField(
            model_name='fixture',
            name='away_score',
            field=models.IntegerField(blank=True, null=True),
        ),
        migrations.AddField(
            model_name='fixture',
            name='date',
            field=models.DateField(default='2024-01-01'),
        ),
        migrations.AddField(
            model_name='fixture',
            name='home_score',
            field=models.IntegerField(blank=True, null=True),
        ),
        migrations.AddField(
            model_name='result',
            name='is_home_team',
            field=models.BooleanField(default=True),
        ),
        migrations.AddField(
            model_name='result',
            name='minute',
            field=models.IntegerField(default=0),
        ),
        migrations.AddField(
            model_name='result',
            name='scorer',
            field=models.CharField(default='Unknown Scorer', max_length=100),
        ),
        migrations.AlterField(
            model_name='result',
            name='fixture',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='goals', to='football.fixture'),
        ),
    ]
