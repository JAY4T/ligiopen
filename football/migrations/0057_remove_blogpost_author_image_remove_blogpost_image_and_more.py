# Generated by Django 5.0.4 on 2024-07-30 09:56

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('football', '0056_alter_blogpost_day'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='blogpost',
            name='author_image',
        ),
        migrations.RemoveField(
            model_name='blogpost',
            name='image',
        ),
        migrations.AddField(
            model_name='blogpost',
            name='photo',
            field=models.ImageField(default='photos/staff/default.jpg', upload_to='photos/staff/'),
        ),
    ]
