from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('football', '0077_alter_fixtureresult_score'),
    ]

    operations = [
        migrations.DeleteModel(
            name='Highlight',
        ),
    ]
