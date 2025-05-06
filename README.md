
# ⚽ LigiOpen Version2

**LigiOpen** is a full-stack web app using **Django REST Framework** and **ReactJS** for **live scores**, **fixtures**, **player stats**, and **news updates**.

---

## 🌟 Features
- 🔴 **Live Scores**
- 🏆 **Fixtures & Results**
- 👥 **Player & Team Profiles**
- 📝 **Club Registration**
- 📊 **Stats Table**

---

## 🚀 Tech Stack
- **Backend**: Django, DRF, PostgreSQL
- **Frontend**: ReactJS, Axios, Bootstrap

---

## 🛠️ Setup

### Backend (Django)
1. Clone the repo and navigate to the backend folder:
   ```bash
   git clone https://github.com/JAY4T/ligiopenV2.git
   cd ligiopenweb/backend
Create and activate virtual environment:


python -m venv env
source env/bin/activate  # Linux/macOS
env\Scripts\activate  # Windows
Install dependencies:


pip install -r requirements.txt
Configure PostgreSQL in settings.py and run migrations:


python manage.py makemigrations
python manage.py migrate
python manage.py createsuperuser
Start the backend server:


python manage.py runserver
API available at http://127.0.0.1:8000/api/

Frontend (React)
Navigate to the frontend folder:


cd ../frontend
Install dependencies and start the server:


npm install
npm start
Frontend available at http://localhost:3000

🌐 Deployment
Backend: Use ngrok to expose the API.

Frontend: Deploy on Netlify or Vercel.

Ensure CORS is enabled in settings.py:


INSTALLED_APPS += ['corsheaders']
CORS_ALLOW_ALL_ORIGINS = True


🗂️ Project Structure
bash

ligiopenweb/
├── backend/          # Django backend
│   ├── manage.py
│   └── api/
└── frontend/         # React frontend
    ├── package.json
    └── src/
🤝 Contributing
Fork → Create a branch → Make changes → Submit PR

🪪 License
MIT License.
