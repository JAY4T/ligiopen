# Generated by Django 5.0.4 on 2024-07-29 03:37

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('football', '0045_remove_highlight_url'),
    ]

    operations = [
        migrations.DeleteModel(
            name='Highlight',
        ),
    ]