# Generated by Django 5.0.4 on 2024-07-25 14:52

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('football', '0044_rename_title_blog_headline'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='highlight',
            name='url',
        ),
    ]
