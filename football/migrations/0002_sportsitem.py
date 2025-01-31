# Generated by Django 5.0.1 on 2024-12-01 14:34

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('football', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='SportsItem',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=255)),
                ('description', models.TextField()),
                ('price', models.DecimalField(decimal_places=2, max_digits=10)),
                ('stock', models.IntegerField(default=0)),
                ('image', models.ImageField(upload_to='sports_items/')),
            ],
        ),
    ]
