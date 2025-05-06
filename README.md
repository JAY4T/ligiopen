# ⚽ LigiOpen Version2

LigiOpen is a full-stack web app built with Django REST Framework and ReactJS for managing live scores, fixtures, player stats, and news.

---

## 🌟 Features

- Live Scores  
- Fixtures & Results  
- Player & Team Profiles  
- Stats Table  

---

## 🚀 Tech Stack

**Backend**: Django, DRF, PostgreSQL  
**Frontend**: ReactJS, Axios, Bootstrap  

---

## 🛠️ Setup

## Backend (Django)

## Clone the repository:

git clone https://github.com/JAY4T/ligiopenV2.git

cd ligiopenweb/backend

Create a virtual environment:


python -m venv env

Activate the environment:

# macOS/Linux:


source env/bin/activate
# Windows:


env\Scripts\activate


Install dependencies:


pip install -r requirements.txt

## Configure PostgreSQL in settings.py, then run the following commands:


python manage.py makemigrations

python manage.py migrate

python manage.py createsuperuser

Start the backend server:


python manage.py runserver

API available at: http://127.0.0.1:8000/api/

## Frontend (React)

Navigate to the frontend directory:


cd ../frontend

## Install dependencies:


- npm install

## Start the frontend server:


- npm start

- App available at: http://localhost:3000

## 🌐 Deployment

- Backend: Use ngrok to expose the API(We are currently using it (free trial)

- Frontend: Deploy on Netlify or Vercel

- Enable CORS in settings.py:

- Add 'corsheaders' to INSTALLED_APPS and set:


- CORS_ALLOW_ALL_ORIGINS = True

⚠️ Note: Frontend code is still under development.

## 🤝 Contributing

-  Fork the repo

- Create a new branch
  
-  Make changes

- Submit a Pull Request

## 📌 Conventional Commit Types

- Please see the rules   https://www.conventionalcommits.org/en/v1.0.0/

## Example of a commit

- git commit -m "feat: add a new feature"





## 🪪 License


MIT License © 2025 LigiOpen Team

## 📌 Commit Types and Contribution
| Type  | Purpose | Contributor(s) |
|-------|---------|----------------|
| feat  | Add new feature |  |
| fix   | Fix a bug or issue |  |
| docs  | Update documentation |  |
| chore | Routine tasks |  |
