# Generated by Django 5.0.6 on 2024-07-09 16:08

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('football', '0035_remove_fixturematch_opponent'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='resultmatch',
            name='fixture',
        ),
        migrations.DeleteModel(
            name='FixtureMatch',
        ),
        migrations.DeleteModel(
            name='ResultMatch',
        ),
    ]