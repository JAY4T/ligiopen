# Generated by Django 5.0.4 on 2024-05-06 21:31

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('football', '0003_team_venue_fixture'),
    ]

    operations = [
        migrations.CreateModel(
            name='Club',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=100)),
                ('logo', models.ImageField(blank=True, null=True, upload_to='club_logos/')),
            ],
        ),
        migrations.RemoveField(
            model_name='player',
            name='image',
        ),
        migrations.AddField(
            model_name='player',
            name='photo',
            field=models.ImageField(blank=True, null=True, upload_to='player_photos/'),
        ),
        migrations.AlterField(
            model_name='player',
            name='name',
            field=models.CharField(max_length=100),
        ),
        migrations.AlterField(
            model_name='player',
            name='position',
            field=models.CharField(max_length=100),
        ),
    ]
