# Generated by Django 5.0.4 on 2024-08-05 17:35

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('football', '0064_clubmatch_blogpost_author_image'),
    ]

    operations = [
        migrations.AlterField(
            model_name='highlight',
            name='image',
            field=models.ImageField(blank=True, null=True, upload_to='highlights/'),
        ),
    ]