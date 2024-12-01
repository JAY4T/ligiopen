# LigiOpen

LigiOpen is a community initiative supporting small sports clubs in organizing leagues, managing revenue and resourcing equipment and facilities to improve the income of youth footballers and their families and create entrepreneurship opportunities for more young people during match days.

# Accessing LigiOpen(Windows)

Installing LigiOpen on windows machine

git clone https://github.com/JAY4T/ligiopen.git

Create python environment:

python -m venv venv

Activate:

.\venv\Scripts\activate

pip install django and requirements.txt

Database:

MySQL database

Have xampp installed as your web server

# Installing(Linux)

1. Install Git from your package manager of choice:

```bash
   $ sudo apt install git
```

or

```bash
$ yum install git
```

2. Install XAMPP:
   Download from https://www.apachefriends.org/download.html

   Open the download directory in terminal

   Change permissions of the installer

   ```bash
   $ chmod 755 xampp-linux-8.1.2-installer.run
   ```

   Run the installer

   ```bash
   $ sudo ./xampp-linux-8.1.2-installer.run
   ```

3. Clone the repository to your working environment,prefferably to a new folder:

   ```bash
   $ git clone https://github.com/JAY4T/ligiopen.git
   ```

4. Navigate to the folder(e.g ligiopen)
   ```bash
   $ cd ligiopen
   ```
5. Create a virtual environment in that directory:

   ```bash
   $ python3 -m venv venv
   ```

   This will create the virtual environment in a folder 'venv' within your working directory

6. Activate the virtual environment:

   ```bash
   $ source venv/bin/activate
   ```

   You will now see the name of youre virtual environment prepended to your username on terminal
   e.g

   ```bash
   (venv) root@root
   ```

   To deactivate the virtual environment, just type 'deactivate' and execute:

   ```bash
   $ deactivate
   ```

7. Install dependencies/requirements specified in the requirements.txt

   ```bash
   $ pip install -r requirements.txt

   ```

8. Try running the development server:

```bash
   $ python manage.py runserver
```

If your terminal shows this, then you're up and running and dont need any more steps.

```bash
System check identified 1 issue (0 silenced).
November 22, 2024 - 14:41:45
Django version 5.0.1, using settings 'ligiopen.settings'
Starting development server at http://127.0.0.1:8000/
Quit the server with CONTROL-C.
```

### Widget Tweaks Error

_No module named 'widget_tweaks'_
If the last message on your terminal is something like "No module named widget_tweaks", this
is a missing dependency which can be installed with the following:

```bash
$ pip install django-widget-tweaks
```

Press

```bash
Ctrl + C
```

    and rerun your server to confirm that the error is resolved

### MYSQL Error

    If the last message on your terminal is something like "Can't connect to local MySQL server through socket '/var/run/mysqld/mysqld.sock' (2)", we will need a workaround because XAMPP has a different installation directory(/opt/lampp):

    1.  Navigate to the root of the project(ligiopen) or whatever name your directory is:

        ```bash
        $ cd ligiopen
        ```

    2.  Create a .env file. You can create it with your code editor or with terminal:

        ```bash
        $  touch .env
        ```

    3.  Create your variables here. Create one named DATABASE_HOST:
        ```bash
        DATABASE_HOST = '/opt/lampp/var/mysql/mysql.sock'
        ```

    4.  Halt and rerun your server.

        The server should now run successfully as shown above.

# Contributing

# Support

# License

# Keeping in touch

```

```
